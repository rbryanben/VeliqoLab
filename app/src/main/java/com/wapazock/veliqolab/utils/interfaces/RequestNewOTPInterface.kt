package com.wapazock.veliqolab.utils.interfaces

import com.wapazock.veliqolab.utils.errors.ServerError

interface RequestNewOTPInterface {
    fun onNewOTPRequestResult(wasSuccessful: Boolean, error: ServerError?)
}