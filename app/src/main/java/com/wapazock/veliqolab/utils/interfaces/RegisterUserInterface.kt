package com.wapazock.veliqolab.utils.interfaces

import com.wapazock.veliqolab.utils.errors.ServerError

interface RegisterUserInterface {
    fun onRegistrationResult(email : String?, wasSuccessful : Boolean,error : ServerError?)
}