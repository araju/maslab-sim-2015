#ifndef ENCODER_H
#define ENCODER_H

#if EDISON

#include <stdint.h>
#include <pthread.h>
#include "mraa.hpp"

struct encoderISRArgs;

class Encoder {
    mraa::Gpio *phaseAp;
    mraa::Gpio *phaseBp;
    pthread_t threadHandle;

    volatile int32_t count;
    volatile uint8_t aState;
    volatile uint8_t bState;
    struct encoderISRArgs *isrArgsA;
    struct encoderISRArgs *isrArgsB;

    void updateTick(uint8_t oldPhase, uint8_t newPhase);
  public:
    Encoder(uint8_t phaseApin, uint8_t phaseBpin);
    ~Encoder();
    int32_t getCount() const;
    void edgeISR(mraa::Gpio *pin);
};

#endif

#endif
