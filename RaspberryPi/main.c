#include <stdio.h>
#include <wiringPi.h>
#include <signal.h>
#include <unistd.h>
#include <pthread.h>
#include "switchhandler.h"

pthread_t pid;

// Update thread
void * updateHandler(){
    while(1) {
        triggerFlag = 1;
        sleep(10 * 60); // 10 Minutes
    }
}

int main(){

    wiringPiSetup();
    initDoorSensor();

    // Create thread that handles updating
    pthread_create(&pid, NULL, &updateHandler, NULL);

    // Keep program alive
    while(1){
        // Poll for interrupt trigger
        if(triggerFlag){
            delay(100);
            checkState(); // Check state after change
            sendState();
            triggerFlag = 0;
        }
        delay(5);
    }

    return 0;
}