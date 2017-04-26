#ifndef SIM_CON
#define SIM_CON

void simCon_init(void);
void simCon_calculateAxes(void);
void simCon_sysTickHandler(void);
void simCon_readFromHost(void);

#endif
