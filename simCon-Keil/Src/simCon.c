#include "simCon.h"
#include "simCon_elements.h"
#include "stdbool.h"
#include "usbd_def.h"
#include "usbd_customhid.h"
#include "math.h"
#include "stm32f407xx.h"

/**DEFINES**/

#define SIMCON_AXIS_FREQ			1
#define SIMCON_OTHER_FREQ			20
#define SIMCON_FLASH_START 		0x08020000

/**MACROS**/

#define $ axis[ID]

/**DECLARATIONS**/

extern USBD_HandleTypeDef hUsbDeviceFS;
extern ADC_HandleTypeDef hadc1;
static uint32_t adcData[8];
static axis_struct axis[8];
static uint8_t inReport[22];
static uint8_t outReport[5];
static uint32_t buttons;
static uint8_t idDiagnostic = 0;

void simCon_axesToReport(void);
void simCon_diagnosticToReport(uint8_t);
void simCon_writeFiveBytes(uint8_t, uint16_t, uint16_t, uint16_t, uint16_t);
void simCon_button(GPIO_TypeDef *, uint16_t, simCon_BUTTON);
void simCon_readButtons(void);
void simCon_loadDefaults(void);
void simCon_flashSave(void);
void simCon_flashRead(void);

void simCon_init() {
	HAL_ADC_Start_DMA(&hadc1, adcData, 8);
	inReport[0] = 0x01;
	simCon_flashRead();
	inReport[SIMCON_HAT_VALUES] = 0x08;
}

/**Funkcja obliczajaca wartosci logiczne osi na podstawie odczytow z przetwornika ADC**/
void simCon_calculateAxes() {
	uint16_t temp;
	for(uint8_t ID = 0; ID < 8; ID++) {
		$.val = ($.filt * $.val + adcData[ID]) / ($.filt + 1);
		if($.enable) {
			if($.val <= $.min) {
				if($.invert) $.logic = 1023;
				else $.logic = 0;
			}
			else if ($.val >= $.max) {
					if($.invert) $.logic = 0;
					else $.logic = 1023;
				}
			else {
				if($.mod) {
					if($.val < $.cent) {
						if($.invert) temp = 1023 - (512 * ($.val - $.min) / ($.cent - $.min));
						else temp = 512 * ($.val - $.min) / ($.cent - $.min);
					}
					else if($.val > $.cent) {
						if($.invert) temp = 1023 - (512 + (512 * ($.val - $.cent) / ($.max - $.cent)));
						else temp = 512 + (512 * ($.val - $.cent) / ($.max - $.cent));
					}
					else temp = 512;
				} else {
					if($.invert) {
						temp = 1023 - (1023 * ($.val - $.min) / ($.max - $.min));
					} else {
						temp = 1023 * ($.val - $.min) / ($.max - $.min);
					}
				}
				if(abs($.logic - temp) > 1) $.logic = ($.logic + temp) / 2;
			}
		} else $.logic = 512;
	}
}

/**Funkcja rozdzielajaca 10cio bitowe wartosci osi na 8bitowe pola raportu**/
void simCon_writeFiveBytes(uint8_t START, uint16_t VAL_1, uint16_t VAL_2, uint16_t VAL_3, uint16_t VAL_4) {
	inReport[START    ] = (uint8_t)(VAL_1 & 0xFF);
	inReport[START + 1] = (uint8_t)(VAL_1 >> 8 | ((VAL_2 & 0x3F) << 2));
	inReport[START + 2] = (uint8_t)(VAL_2 >> 6 | ((VAL_3 & 0x0F) << 4));
	inReport[START + 3] = (uint8_t)(VAL_3 >> 4 | ((VAL_4 & 0x03) << 6));
	inReport[START + 4] = (uint8_t)(VAL_4 >> 2);
}

/**Funkcja uzupelniajaca raport wartosciami osi**/
void simCon_axesToReport() {
	simCon_writeFiveBytes(SIMCON_HID_VALUES, axis[X].logic, axis[Y].logic, axis[Z].logic, axis[RX].logic);
	simCon_writeFiveBytes(SIMCON_HID_VALUES + 5, axis[RY].logic, axis[RZ].logic, axis[SL].logic, axis[DI].logic);
}

/**Funkcja uzupelniajaca raport danymi do kalibracji**/
void simCon_diagnosticToReport(uint8_t ID) {
	inReport[15] = ID;
	if($.enable) inReport[15] |= (1UL << 3);
	else inReport[15] &=~ (1UL << 3);
	if($.invert) inReport[15] |= (1UL << 4);
	else inReport[15] &=~ (1UL << 4);
	if($.mod) inReport[15] |= (1UL << 5);
	else inReport[15] &=~ (1UL << 5);
	simCon_writeFiveBytes(16, adcData[ID], $.min, $.cent, $.max);
	inReport[21] = $.filt;
}

/**Funkcja uzupelniajaca raport stanami przyciskow**/
void simCon_otherToReport() {
	inReport[SIMCON_BUTTON_VALUES]	   = (buttons & 0x000000FF);
	inReport[SIMCON_BUTTON_VALUES + 1] = (buttons & 0x0000FF00) >> 8;
	inReport[SIMCON_BUTTON_VALUES + 2] = (buttons & 0x00FF0000) >> 16;
	inReport[SIMCON_BUTTON_VALUES + 3] = (buttons & 0xFF000000) >> 24;
}

/**Funkcja odczytujaca stan przyciskow i wstawiajaca je do raportu**/
void simCon_readButtons() {
	simCon_button(GPIOB, GPIO_PIN_14, SIMCON_BUTTON_1);
	simCon_button(GPIOB, GPIO_PIN_15, SIMCON_BUTTON_2);
}

/**Funkcja zawierajaca funkcje wywolywane w przerwaniu od SysTick**/
void simCon_sysTickHandler() {
	static uint8_t tick = 0;
	
	simCon_calculateAxes();
	if((tick % SIMCON_OTHER_FREQ) == 0) {
		simCon_readButtons();
		simCon_otherToReport();
	}
	if((tick % SIMCON_AXIS_FREQ) == 0) {
		simCon_axesToReport();
		simCon_diagnosticToReport(idDiagnostic);
		USBD_CUSTOM_HID_SendReport(&hUsbDeviceFS, (uint8_t *)&inReport, 22);
	}
	
	tick++;
	if(tick >= 250) tick = 0;
}

/**Funkcja wiazaca pin mikrokontrolera z przyciskiem logicznym urzadzenia**/
void simCon_button(GPIO_TypeDef * PORT, uint16_t PIN, simCon_BUTTON BUTTON) {
	if(HAL_GPIO_ReadPin(PORT, PIN)) buttons |= (1UL << BUTTON);
	else buttons &=~ (1UL << BUTTON);
}

/**Funkcja sluzaca do obslugi ramki pochodzacej od hosta zawierajacej instrukcje dla urzadzenia**/
void simCon_readFromHost() {
	USBD_CUSTOM_HID_HandleTypeDef * USBHandler = (USBD_CUSTOM_HID_HandleTypeDef *)hUsbDeviceFS.pClassData;
	for(uint8_t i = 0; i < 2; i++) outReport[i] = USBHandler->Report_buf[i];
	uint8_t ID, CODE;
	uint16_t VAL;
	ID = outReport[0] & 0x07;
	CODE = (outReport[0] & 0x38) >> 3;
	VAL = ((outReport[0] & 0xC0) << 2) | outReport[1];
	
	switch(CODE) {
		case 0:
			$.min = VAL;
			break;
		case 1:
			$.cent = VAL;
			break;
		case 2:
			$.max = VAL;
			break;
		case 3:
			$.filt = VAL;
			break;
		case 7: {
			switch(VAL) {
				case 0:
					$.enable ^= 1UL;
					break;
				case 1:
					$.invert ^= 1UL;
					break;
				case 2:
					$.mod ^= 1UL;
					break;
				case 3:
					idDiagnostic = ID;
					break;
				case 4:
					simCon_flashSave();
					break;
				case 5:
					simCon_loadDefaults();
					break;
			}
			break;
		}
	}
}

/**Funkcja sluzaca do ustawienia domyslnych wartosci parametrow osi**/
void simCon_loadDefaults() {
	for(uint8_t ID = 0; ID < 2; ID++) {
		$.enable = true;
		$.invert = false;
		$.mod = 1;
		$.max = 1022;
		$.cent = 512;
		$.min = 1;
		$.filt = 7;
	}
	for(uint8_t ID = 2; ID < 8; ID++) {
		$.enable = false;
		$.invert = false;
		$.mod = 0;
		$.max = 1022;
		$.cent = 512;
		$.min = 1;
		$.filt = 15;
	}
	axis[RX].cent = 660;
}

/**Funkcja sluzaca do zachowania parametrow osi w pamieci nieulotnej flash mikrokontrolera**/
void simCon_flashSave() {
	HAL_FLASH_Unlock();
	__HAL_FLASH_CLEAR_FLAG(FLASH_FLAG_EOP | FLASH_FLAG_OPERR | FLASH_FLAG_WRPERR | FLASH_FLAG_PGAERR | FLASH_FLAG_PGSERR);
	FLASH_Erase_Sector(FLASH_SECTOR_5, VOLTAGE_RANGE_3);
	for(int i = 0; i < 48; i++) {
		if(i < 8) {
			HAL_FLASH_Program(FLASH_TYPEPROGRAM_WORD, SIMCON_FLASH_START + 32 * i, *(uint32_t*)&axis[i].enable);
		}
		if(i >= 8 && i < 16) {
			HAL_FLASH_Program(FLASH_TYPEPROGRAM_WORD, SIMCON_FLASH_START + 32 * i, *(uint32_t*)&axis[i - 8].invert);
		}
		if(i >= 16 && i < 24) {
			HAL_FLASH_Program(FLASH_TYPEPROGRAM_WORD, SIMCON_FLASH_START + 32 * i, *(uint32_t*)&axis[i - 16].filt);
		}		
		if(i >= 24 && i < 32) {
			HAL_FLASH_Program(FLASH_TYPEPROGRAM_WORD, SIMCON_FLASH_START + 32 * i, *(uint32_t*)&axis[i - 24].min);
		}		
		if(i >= 32 && i < 40) {
			HAL_FLASH_Program(FLASH_TYPEPROGRAM_WORD, SIMCON_FLASH_START + 32 * i, *(uint32_t*)&axis[i - 32].cent);
		}
		if(i >= 40) {
			HAL_FLASH_Program(FLASH_TYPEPROGRAM_WORD, SIMCON_FLASH_START + 32 * i, *(uint32_t*)&axis[i - 40].max);
		}
	}	
	HAL_FLASH_Lock();
}

/**Funkcja sluzaca do odczytu parametrow osi z pamieci nieulotnej flash mikrokontrolera**/
void simCon_flashRead() {
	for(uint8_t i = 0; i < 48; i++) {
		if(i < 8) {
			axis[i].enable = (*(bool*)(SIMCON_FLASH_START + (i * 32))) & 0x000000FF;
		}	 
		if(i >= 8 && i < 16) {
			axis[i - 8].invert = (*(bool*)(SIMCON_FLASH_START + (i * 32))) & 0x000000FF;
		}
		if(i >= 16 && i < 24) {
			axis[i - 16].filt = *(uint16_t*)(SIMCON_FLASH_START + (i * 32));
		} 		
		if(i >= 24 && i < 32) {
			axis[i - 24].min = *(uint16_t*)(SIMCON_FLASH_START + (i * 32));
		} 
		if(i >= 32 && i < 40) {
			axis[i - 32].cent = *(uint16_t*)(SIMCON_FLASH_START + (i * 32));
		}
		if(i >= 40) {
			axis[i - 40].max = *(uint16_t*)(SIMCON_FLASH_START + (i * 32));
		}
	}
}

