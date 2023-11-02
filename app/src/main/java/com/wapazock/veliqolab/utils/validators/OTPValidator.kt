package com.wapazock.veliqolab.utils.validators

class OTPValidator {
    companion object {
        //Validates an OTP pin
        fun isValid(otpPin: String): Boolean {
            if (otpPin.length == 6){
                return true
            }
            return false
        }
    }
}