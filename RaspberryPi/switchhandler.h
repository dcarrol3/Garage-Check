#ifndef switchhandler_h_
#define switchhandler_h_

#define PIN 25
#define OPEN 1
#define CLOSE 0
#define OPEN_STR "open"
#define CLOSED_STR "closed"

extern int triggerFlag;

// Setup garage door sensor for use
void initDoorSensor();
// Garage door state change handler
void stateChange();
int getState();
int sendState();
int checkState();
#endif
