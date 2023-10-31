package com.wapazock.veliqolab.utils.interfaces

import com.wapazock.veliqolab.utils.errors.ServerError

/*
    Callback Interface for obtaining an authentication token
 */
interface GetAuthTokenInterface {
    fun onAuthTokenResponse(token: String?, wasSuccessful: Boolean, error: ServerError?)
}