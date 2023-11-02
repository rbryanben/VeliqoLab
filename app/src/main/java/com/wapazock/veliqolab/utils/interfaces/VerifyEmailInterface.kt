package com.wapazock.veliqolab.utils.interfaces

import com.wapazock.veliqolab.utils.errors.ServerError

interface VerifyEmailInterface {
    // When OTP request results
    fun onVerifyEmailWithOTPResult(wasSuccessful: Boolean, errorCode: ServerError?)
}