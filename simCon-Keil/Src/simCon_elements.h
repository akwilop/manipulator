#ifndef SIM_CON_ELEMENTS
#define SIM_CON_ELEMENTS

#include "stm32f4xx_hal.h"
#include "stdbool.h"

#define X			0
#define Y			1
#define Z			2
#define RX		3
#define RY		4
#define RZ		5
#define SL		6
#define DI		7

#define SIMCON_HID_VALUES 			0
#define SIMCON_BUTTON_VALUES 		10
#define SIMCON_HAT_VALUES				14

typedef struct _axis_struct {
	bool enable;
	bool invert;
	bool mod;
	uint8_t filt;
	uint16_t logic;
	uint16_t min;
	uint16_t cent;
	uint16_t max;
	double val;
}axis_struct;

typedef enum _simCon_BUTTON{
	SIMCON_BUTTON_1,
	SIMCON_BUTTON_2,
	SIMCON_BUTTON_3,
	SIMCON_BUTTON_4,
	SIMCON_BUTTON_5,
	SIMCON_BUTTON_6,
	SIMCON_BUTTON_7,
	SIMCON_BUTTON_8,
	SIMCON_BUTTON_9,
	SIMCON_BUTTON_10,
	SIMCON_BUTTON_11,
	SIMCON_BUTTON_12,
	SIMCON_BUTTON_13,
	SIMCON_BUTTON_14,
	SIMCON_BUTTON_15,
	SIMCON_BUTTON_16,
	SIMCON_BUTTON_17,
	SIMCON_BUTTON_18,
	SIMCON_BUTTON_19,
	SIMCON_BUTTON_20,
	SIMCON_BUTTON_21,
	SIMCON_BUTTON_22,
	SIMCON_BUTTON_23,
	SIMCON_BUTTON_24,
	SIMCON_BUTTON_25,
	SIMCON_BUTTON_26,
	SIMCON_BUTTON_27,
	SIMCON_BUTTON_28,
	SIMCON_BUTTON_29,
	SIMCON_BUTTON_30,
	SIMCON_BUTTON_31,
	SIMCON_BUTTON_32
}simCon_BUTTON;

#endif
