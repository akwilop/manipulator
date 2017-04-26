/**
  ******************************************************************************
  * File Name          : main.h
  * Description        : This file contains the common defines of the application
  ******************************************************************************
  *
  * Copyright (c) 2016 STMicroelectronics International N.V. 
  * All rights reserved.
  *
  * Redistribution and use in source and binary forms, with or without 
  * modification, are permitted, provided that the following conditions are met:
  *
  * 1. Redistribution of source code must retain the above copyright notice, 
  *    this list of conditions and the following disclaimer.
  * 2. Redistributions in binary form must reproduce the above copyright notice,
  *    this list of conditions and the following disclaimer in the documentation
  *    and/or other materials provided with the distribution.
  * 3. Neither the name of STMicroelectronics nor the names of other 
  *    contributors to this software may be used to endorse or promote products 
  *    derived from this software without specific written permission.
  * 4. This software, including modifications and/or derivative works of this 
  *    software, must execute solely and exclusively on microcontroller or
  *    microprocessor devices manufactured by or for STMicroelectronics.
  * 5. Redistribution and use of this software other than as permitted under 
  *    this license is void and will automatically terminate your rights under 
  *    this license. 
  *
  * THIS SOFTWARE IS PROVIDED BY STMICROELECTRONICS AND CONTRIBUTORS "AS IS" 
  * AND ANY EXPRESS, IMPLIED OR STATUTORY WARRANTIES, INCLUDING, BUT NOT 
  * LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY, FITNESS FOR A 
  * PARTICULAR PURPOSE AND NON-INFRINGEMENT OF THIRD PARTY INTELLECTUAL PROPERTY
  * RIGHTS ARE DISCLAIMED TO THE FULLEST EXTENT PERMITTED BY LAW. IN NO EVENT 
  * SHALL STMICROELECTRONICS OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT,
  * INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
  * LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, 
  * OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF 
  * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING 
  * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE,
  * EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
  *
  ******************************************************************************
  */
/* Define to prevent recursive inclusion -------------------------------------*/
#ifndef __MAIN_H
#define __MAIN_H
  /* Includes ------------------------------------------------------------------*/

/* USER CODE BEGIN Includes */

/* USER CODE END Includes */

/* Private define ------------------------------------------------------------*/

#define HAT_U_Pin GPIO_PIN_2
#define HAT_U_GPIO_Port GPIOE
#define HAT_D_Pin GPIO_PIN_3
#define HAT_D_GPIO_Port GPIOE
#define HAT_R_Pin GPIO_PIN_4
#define HAT_R_GPIO_Port GPIOE
#define HAT_L_Pin GPIO_PIN_5
#define HAT_L_GPIO_Port GPIOE
#define AXIS_X_Pin GPIO_PIN_0
#define AXIS_X_GPIO_Port GPIOA
#define AXIS_Y_Pin GPIO_PIN_1
#define AXIS_Y_GPIO_Port GPIOA
#define AXIS_Z_Pin GPIO_PIN_2
#define AXIS_Z_GPIO_Port GPIOA
#define ROT_X_Pin GPIO_PIN_3
#define ROT_X_GPIO_Port GPIOA
#define ROT_Y_Pin GPIO_PIN_4
#define ROT_Y_GPIO_Port GPIOA
#define ROT_Z_Pin GPIO_PIN_5
#define ROT_Z_GPIO_Port GPIOA
#define SLIDER_Pin GPIO_PIN_6
#define SLIDER_GPIO_Port GPIOA
#define DIAL_Pin GPIO_PIN_7
#define DIAL_GPIO_Port GPIOA
#define BUTTON_1_Pin GPIO_PIN_4
#define BUTTON_1_GPIO_Port GPIOC
#define BUTTON_2_Pin GPIO_PIN_5
#define BUTTON_2_GPIO_Port GPIOC
#define Y_FEED_PWM_1_Pin GPIO_PIN_0
#define Y_FEED_PWM_1_GPIO_Port GPIOB
#define Y_FEED_PWM_2_Pin GPIO_PIN_1
#define Y_FEED_PWM_2_GPIO_Port GPIOB
#define BUTTON_3_Pin GPIO_PIN_2
#define BUTTON_3_GPIO_Port GPIOB
#define BUTTON_4_Pin GPIO_PIN_7
#define BUTTON_4_GPIO_Port GPIOE
#define BUTTON_5_Pin GPIO_PIN_8
#define BUTTON_5_GPIO_Port GPIOE
#define BUTTON_6_Pin GPIO_PIN_9
#define BUTTON_6_GPIO_Port GPIOE
#define BUTTON_7_Pin GPIO_PIN_10
#define BUTTON_7_GPIO_Port GPIOE
#define BUTTON_8_Pin GPIO_PIN_11
#define BUTTON_8_GPIO_Port GPIOE
#define BUTTON_9_Pin GPIO_PIN_12
#define BUTTON_9_GPIO_Port GPIOE
#define BUTTON_10_Pin GPIO_PIN_13
#define BUTTON_10_GPIO_Port GPIOE
#define BUTTON_11_Pin GPIO_PIN_14
#define BUTTON_11_GPIO_Port GPIOE
#define BUTTON_12_Pin GPIO_PIN_15
#define BUTTON_12_GPIO_Port GPIOE
#define BUTTON_13_Pin GPIO_PIN_12
#define BUTTON_13_GPIO_Port GPIOB
#define BUTTON_14_Pin GPIO_PIN_13
#define BUTTON_14_GPIO_Port GPIOB
#define BUTTON_15_Pin GPIO_PIN_14
#define BUTTON_15_GPIO_Port GPIOB
#define BUTTON_16_Pin GPIO_PIN_15
#define BUTTON_16_GPIO_Port GPIOB
#define BUTTON_17_Pin GPIO_PIN_8
#define BUTTON_17_GPIO_Port GPIOD
#define BUTTON_18_Pin GPIO_PIN_9
#define BUTTON_18_GPIO_Port GPIOD
#define BUTTON_19_Pin GPIO_PIN_10
#define BUTTON_19_GPIO_Port GPIOD
#define BUTTON_20_Pin GPIO_PIN_11
#define BUTTON_20_GPIO_Port GPIOD
#define Z_FEED_PWM_1_Pin GPIO_PIN_12
#define Z_FEED_PWM_1_GPIO_Port GPIOD
#define Z_FEED_PWM_2_Pin GPIO_PIN_13
#define Z_FEED_PWM_2_GPIO_Port GPIOD
#define SLIDER_FEED_PWM_1_Pin GPIO_PIN_14
#define SLIDER_FEED_PWM_1_GPIO_Port GPIOD
#define SLIDER_FEED_PWM_2_Pin GPIO_PIN_15
#define SLIDER_FEED_PWM_2_GPIO_Port GPIOD
#define X_FEED_PWM_1_Pin GPIO_PIN_6
#define X_FEED_PWM_1_GPIO_Port GPIOC
#define X_FEED_PWM_2_Pin GPIO_PIN_7
#define X_FEED_PWM_2_GPIO_Port GPIOC
#define BUTTON_21_Pin GPIO_PIN_8
#define BUTTON_21_GPIO_Port GPIOC
#define BUTTON_22_Pin GPIO_PIN_9
#define BUTTON_22_GPIO_Port GPIOC
#define BUTTON_23_Pin GPIO_PIN_8
#define BUTTON_23_GPIO_Port GPIOA
#define BUTTON_24_Pin GPIO_PIN_9
#define BUTTON_24_GPIO_Port GPIOA
#define BUTTON_25_Pin GPIO_PIN_10
#define BUTTON_25_GPIO_Port GPIOA
#define USB_DM_Pin GPIO_PIN_11
#define USB_DM_GPIO_Port GPIOA
#define USB_DP_Pin GPIO_PIN_12
#define USB_DP_GPIO_Port GPIOA
#define SWDIO_Pin GPIO_PIN_13
#define SWDIO_GPIO_Port GPIOA
#define SWCLK_Pin GPIO_PIN_14
#define SWCLK_GPIO_Port GPIOA
#define BUTTON_26_Pin GPIO_PIN_0
#define BUTTON_26_GPIO_Port GPIOD
#define BUTTON_27_Pin GPIO_PIN_1
#define BUTTON_27_GPIO_Port GPIOD
#define BUTTON_28_Pin GPIO_PIN_2
#define BUTTON_28_GPIO_Port GPIOD
#define BUTTON_29_Pin GPIO_PIN_3
#define BUTTON_29_GPIO_Port GPIOD
#define BUTTON_30_Pin GPIO_PIN_4
#define BUTTON_30_GPIO_Port GPIOD
#define BUTTON_31_Pin GPIO_PIN_5
#define BUTTON_31_GPIO_Port GPIOD
#define BUTTON_32_Pin GPIO_PIN_6
#define BUTTON_32_GPIO_Port GPIOD
#define SLIDER_FEED_EN_Pin GPIO_PIN_8
#define SLIDER_FEED_EN_GPIO_Port GPIOB
#define Z_FEED_EN_Pin GPIO_PIN_9
#define Z_FEED_EN_GPIO_Port GPIOB
#define Y_FEED_EN_Pin GPIO_PIN_0
#define Y_FEED_EN_GPIO_Port GPIOE
#define X_FEED_EN_Pin GPIO_PIN_1
#define X_FEED_EN_GPIO_Port GPIOE
/* USER CODE BEGIN Private defines */

/* USER CODE END Private defines */

/**
  * @}
  */ 

/**
  * @}
*/ 

#endif /* __MAIN_H */
/************************ (C) COPYRIGHT STMicroelectronics *****END OF FILE****/
