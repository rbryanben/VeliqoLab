package com.wapazock.veliqolab.utils.interfaces

import com.wapazock.veliqolab.utils.errors.ServerError

interface AddressManagerInterface {
    fun onAddressUpdateResult(wasSuccessful: Boolean, error: ServerError?)
    fun onDeleteAddressResult(wasSuccessful: Boolean,error: ServerError?)
}