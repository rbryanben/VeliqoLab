package com.wapazock.veliqolab.repository

import com.wapazock.veliqolab.models.Address
import com.wapazock.veliqolab.utils.errors.ServerError
import com.wapazock.veliqolab.utils.http.http
import com.wapazock.veliqolab.utils.interfaces.AddressManagerInterface
import okhttp3.Call
import okhttp3.Callback
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.Response
import java.io.IOException

class AddressesRepository {
    // Companion 
    companion object {
        // instance 
        private var instance : AddressesRepository? = null

        
        // get instance
        public fun getInstance(): AddressesRepository? {
            if (instance == null){
                instance = AddressesRepository()
            }
            return instance
        }
    }

    // Variables
    private val client: OkHttpClient = http.getClient()
    val mediaType = "application/json; charset=utf-8".toMediaType()

    // Update address
    public fun updateAddressRequest(address: Address,addressManagerInterface: AddressManagerInterface){
        // Request
        val request = Request.Builder()
            .url("${http.BASE_URL}/addresses/${address.id}")
            .header("Authorization","Bearer ${http.token}")
            .put(address.toJSON().toString().toRequestBody(mediaType))
            .build()

        // Enqueue the request
        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                addressManagerInterface.onAddressUpdateResult(false,ServerError.SERVER_UNREACHABLE)
            }

            override fun onResponse(call: Call, response: Response) {
                // 403 - Forbidden
                if (response.code == 403){
                    addressManagerInterface.onAddressUpdateResult(false,ServerError.INVALID_AUTH_TOKEN)
                    return
                }

                // Bad Request
                if (response.code == 400){
                    addressManagerInterface.onAddressUpdateResult(false,ServerError.SOMETHING_WENT_WRONG)
                    return
                }

                // 200 has changed
                if (response.code == 200){
                    addressManagerInterface.onAddressUpdateResult(true,null)
                    return
                }

                addressManagerInterface.onAddressUpdateResult(false,ServerError.SOMETHING_WENT_WRONG)
            }
        })

    }

    // Deletes an address
    fun deleteAddress(address: Address,onAddressManagerInterface: AddressManagerInterface) {
        // Request
        val request = Request.Builder()
            .url("${http.BASE_URL}/addresses/${address.id}")
            .header("Authorization","Bearer ${http.token}")
            .delete()
            .build()

        // Enqueue request
        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                onAddressManagerInterface.onDeleteAddressResult(false,ServerError.SERVER_UNREACHABLE)
            }

            override fun onResponse(call: Call, response: Response) {
                if (response.code == 200){
                    onAddressManagerInterface.onDeleteAddressResult(true,null)
                    return
                }

                if (response.code == 403){
                    onAddressManagerInterface.onDeleteAddressResult(false,ServerError.INVALID_AUTH_TOKEN)
                    return
                }

                onAddressManagerInterface.onDeleteAddressResult(false,ServerError.SOMETHING_WENT_WRONG)
            }
        })

    }

}