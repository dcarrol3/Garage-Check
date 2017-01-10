#include <stdio.h>
#include <stdlib.h>
#include <wiringPi.h>
#include <curl/curl.h>
#include <string.h>
#include "link.h"
#include "switchhandler.h"

// Door open(1) or closed (0).
int doorState = CLOSE;
int triggerFlag = 0;

void initDoorSensor(){
    pinMode(PIN, INPUT);

    if(digitalRead(PIN) == LOW){
        doorState = CLOSE;
    }
    else{
        doorState = OPEN;
    }

    wiringPiISR(PIN, INT_EDGE_BOTH, stateChange);
}

// Interrupt
void stateChange(){
    triggerFlag = 1;
}

int checkState(){
    if(digitalRead(PIN) == LOW){
        doorState = CLOSE;
    }
    else{
        doorState = OPEN;
    }
}

int getState(){
    return doorState;
}

char * getStateString(){
    char * str = malloc(32 * sizeof(char));
    if(doorState == OPEN){
        strcpy(str, OPEN_STR);
    }
    else{
        strcpy(str, CLOSED_STR);
    }
    return str;
}

int sendState(){
    int success = 0;
    char status[32];
    char * state = getStateString();
    CURL *curl;
    CURLcode res;
    curl = curl_easy_init();
    strcpy(status, "status=");
    strcat(status, state);

    if(curl) {
        printf("Sending data: %s\n", state);
        // URL to send post request
        curl_easy_setopt(curl, CURLOPT_URL, URL);
        // Set post data
        curl_easy_setopt(curl, CURLOPT_POSTFIELDS, status);
        // Send request
        res = curl_easy_perform(curl);
        /// Error check
        if (res != CURLE_OK) {
            fprintf(stderr, "curl_easy_perform() failed: %s\n",
                    curl_easy_strerror(res));
        }
        // Cleanup
        curl_easy_cleanup(curl);
    }

    free(state);

    return success;
}
