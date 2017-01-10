# Makefile for garage door
# Doug Carroll

FILENAME = main
TARGET = Garage

CC = gcc
CFLAGS = -lwiringPi -std=gnu99 -lrt -lcurl -pthread

all: $(TARGET)

$(TARGET): $(FILENAME).o switchhandler.o
	$(CC) -o $@ $< switchhandler.o $(CFLAGS)

$(FILENAME).o: $(FILENAME).c
	$(CC) $(CFLAGS) -c -o $@ $<

switchhandler.o: switchhandler.c switchhandler.h link.h
	$(CC) $(CFLAGS) -c -o $@ $<

clean:
	$(RM) $(TARGET) *.o *.~
