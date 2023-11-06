package com.wapazock.veliqolab.repository
import com.wapazock.veliqolab.models.Address
import com.wapazock.veliqolab.models.UserData
import com.wapazock.veliqolab.utils.errors.ServerError
import com.wapazock.veliqolab.utils.http.http
import com.wapazock.veliqolab.utils.interfaces.GetUserDataInterface
import okhttp3.Call
import okhttp3.Callback
import okhttp3.Request
import okhttp3.Response
import org.json.JSONObject
import java.io.IOException

class UserDataRepository {

    // Companion
    companion object {
        // instance
        private var instance : UserDataRepository? = null

        // Get instance
        public fun getInstance(): UserDataRepository {
            if (instance == null){
                instance = UserDataRepository()
            }
            return instance as UserDataRepository
        }
    }

    // client
    private var client = http.getClient()

    // Gets the user data from the server
    public fun getUserData(getUserDataInterface: GetUserDataInterface){
        // Check token
        if (http.token == null){
            getUserDataInterface.onGetUserDataRequestResult(null,false,ServerError.INVALID_AUTH_TOKEN)
            return
        }

        // Request
        val request = Request.Builder()
            .get()
            .url("${http.BASE_URL}/auth/me")
            .header("Authorization","Bearer ${http.token}")
            .build()

        // Enqueue
        client.newCall(request).enqueue(object : Callback{
            override fun onFailure(call: Call, e: IOException) {
                getUserDataInterface.onGetUserDataRequestResult(null,false,ServerError.SERVER_UNREACHABLE)
            }

            override fun onResponse(call: Call, response: Response) {
                // 200 -> Extract the user data
                if (response.code == 200){
                    // Response object
                    val responseBodyAsString = response.body?.string();
                    val responseJSONObject = JSONObject(responseBodyAsString)

                    // Construct the UserData from JSONObject
                    val userData = UserData(responseJSONObject)

                    // Callback
                    getUserDataInterface.onGetUserDataRequestResult(userData,true,null)

                    // Get Address List
                    val addressesJSONArray = responseJSONObject.getJSONArray("addresses")
                    //Array to store Address objects
                    val addresses = ArrayList<Address>()
                    //Iterate addresses array list constructing Address objects and adding to list
                    for (i:Int in 0 until addressesJSONArray.length() ) {
                        addresses.add(
                            Address(addressesJSONArray.get(i) as JSONObject)
                        )
                    }
                    // Callback for addresses
                    getUserDataInterface.onGetUserDataRequestResultAddresses(addresses)
                    return
                }

                // Invalid token
                getUserDataInterface.onGetUserDataRequestResult(null,false,ServerError.INVALID_AUTH_TOKEN)

            }
        })
    }
}