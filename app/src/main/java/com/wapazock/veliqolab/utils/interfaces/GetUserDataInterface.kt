package com.wapazock.veliqolab.utils.interfaces
import com.wapazock.veliqolab.models.Address
import com.wapazock.veliqolab.models.UserData
import com.wapazock.veliqolab.utils.errors.ServerError

interface GetUserDataInterface {
    fun onGetUserDataRequestResult(userData: UserData?, wasSuccessful: Boolean, error: ServerError?)
    fun onGetUserDataRequestResultAddresses(addresses: ArrayList<Address>)
}