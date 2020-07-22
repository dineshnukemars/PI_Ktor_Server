package com.sky.pi.com.sky.pi

import com.pi4j.io.gpio.*

import java.util.*

interface PiAccess {
    fun create() {
        println("no implementation")
    }

    fun setPinDigitalOut(pinNumber: Int, pinState: PinState) {
        println("no implementation")
    }

    fun setPwm(pin: Pin, dutyCycle: Double, loopTime: Long, duration: Long) {
        println("no implementation")
    }

    fun destroy() {
        println("no implementation")
    }
}


class PiAccessImpl : PiAccess {

    val provisionedPinList: TreeSet<Int> = TreeSet()

    override fun create() {
        GpioFactory.setDefaultProvider(RaspiGpioProvider(RaspiPinNumberingScheme.BROADCOM_PIN_NUMBERING))
    }

    override fun setPinDigitalOut(pinNumber: Int, pinState: PinState) {

        val controller = GpioFactory.getInstance()

        val pinDigitalOutput: GpioPinDigitalOutput =
            if (provisionedPinList.add(pinNumber))
                controller.provisionDigitalOutputPin(bcmPinMap[pinNumber])
            else
                controller.getProvisionedPin(bcmPinMap[pinNumber]) as GpioPinDigitalOutput

        pinDigitalOutput.state = pinState
    }

    override fun setPwm(pin: Pin, dutyCycle: Double, loopTime: Long, duration: Long) {
        PwmThread.getInstance().startPwm(
            gpioController = GpioFactory.getInstance(),
            pin = pin,
            dutyCycle = dutyCycle,
            loopTime = loopTime,
            duration = duration
        )
    }

    override fun destroy() {
        val gpioController = GpioFactory.getInstance()
        val arrayOfGpioPins = gpioController.provisionedPins.toTypedArray()
        if (arrayOfGpioPins.isNotEmpty()) gpioController.unprovisionPin(*arrayOfGpioPins)

        provisionedPinList.clear()
        gpioController.shutdown()
    }
}

class PwmThread(val isRunning: Boolean = false) {

    companion object {

        fun getInstance(): PwmThread {
            return PwmThread() // todo
        }
    }

    fun startPwm(
        gpioController: GpioController,
        pin: Pin,
        dutyCycle: Double,
        loopTime: Long,
        duration: Long
    ) {
        // loop in coroutine
    }
}


val bcmPinMap = mapOf<Int, Pin>(
    2 to RaspiBcmPin.GPIO_02,
    3 to RaspiBcmPin.GPIO_03,
    4 to RaspiBcmPin.GPIO_04,
    5 to RaspiBcmPin.GPIO_05,
    6 to RaspiBcmPin.GPIO_06,
    7 to RaspiBcmPin.GPIO_07,
    8 to RaspiBcmPin.GPIO_08,
    9 to RaspiBcmPin.GPIO_09,
    10 to RaspiBcmPin.GPIO_10,
    11 to RaspiBcmPin.GPIO_11,
    12 to RaspiBcmPin.GPIO_12,
    13 to RaspiBcmPin.GPIO_13,
    14 to RaspiBcmPin.GPIO_14,
    15 to RaspiBcmPin.GPIO_15,
    16 to RaspiBcmPin.GPIO_16,
    17 to RaspiBcmPin.GPIO_17,
    18 to RaspiBcmPin.GPIO_18,
    19 to RaspiBcmPin.GPIO_19,
    20 to RaspiBcmPin.GPIO_20,
    21 to RaspiBcmPin.GPIO_21,
    22 to RaspiBcmPin.GPIO_22,
    23 to RaspiBcmPin.GPIO_23,
    24 to RaspiBcmPin.GPIO_24,
    25 to RaspiBcmPin.GPIO_25,
    26 to RaspiBcmPin.GPIO_26,
    27 to RaspiBcmPin.GPIO_27,
    28 to RaspiBcmPin.GPIO_28,
    29 to RaspiBcmPin.GPIO_29,
    30 to RaspiBcmPin.GPIO_30,
    31 to RaspiBcmPin.GPIO_31
)

