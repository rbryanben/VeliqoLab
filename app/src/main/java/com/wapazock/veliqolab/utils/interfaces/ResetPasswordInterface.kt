package com.wapazock.veliqolab.utils.interfaces

import com.wapazock.veliqolab.utils.errors.ServerError

interface ResetPasswordInterface {
    fun onResetPasswordRequestResult(wasSuccessful: Boolean, error: ServerError?)
    fun onChangePasswordRequestResult(wasSuccessful: Boolean,error: ServerError?)
}