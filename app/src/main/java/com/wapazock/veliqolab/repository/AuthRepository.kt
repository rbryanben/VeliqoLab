package com.wapazock.veliqolab.repository

import RegisterUserData
import com.wapazock.veliqolab.models.AuthUser
import com.wapazock.veliqolab.models.ResetPasswordData
import com.wapazock.veliqolab.utils.errors.ServerError
import com.wapazock.veliqolab.utils.http.http
import com.wapazock.veliqolab.utils.interfaces.GetAuthTokenInterface
import com.wapazock.veliqolab.utils.interfaces.RegisterUserInterface
import com.wapazock.veliqolab.utils.interfaces.RequestNewOTPInterface
import com.wapazock.veliqolab.utils.interfaces.ResetPasswordInterface
import com.wapazock.veliqolab.utils.interfaces.VerifyEmailInterface
import okhttp3.Call
import okhttp3.Callback
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.Request
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.Response
import org.json.JSONObject
import java.io.IOException


/*
    Singleton Repository for Authentication events
    Methods:
        getAuthToken -> Obtains an authentication token from the server
 */
class AuthRepository {

    companion object {
        // HTTP Client
        private var client = http.getClient();

        val mediaType = "application/json; charset=utf-8".toMediaType()

        /*
         Obtains an authentication token from the server
        */
        public fun getAuthToken(user: AuthUser, getAuthTokenInterface: GetAuthTokenInterface) {
            // Request Body
            val requestBody = user.asJSONObject().toString().toRequestBody(mediaType)

            // Request
            val request = Request.Builder()
                .post(requestBody)
                .url(http.BASE_URL + "/auth/login")
                .build()

            // Enqueue request
            client.newCall(request).enqueue(object: Callback{
                override fun onFailure(call: Call, e: IOException) {
                   getAuthTokenInterface.onAuthTokenResponse(null,false,ServerError.SERVER_UNREACHABLE)
                }

                override fun onResponse(call: Call, response: Response) {
                    when (response.code){
                        // Invalid Credentials
                        404 -> {
                            getAuthTokenInterface.onAuthTokenResponse(null,false,ServerError.INVALID_CREDENTIALS)
                            return
                        }
                        422 -> {
                            getAuthTokenInterface.onAuthTokenResponse(null,false,ServerError.INVALID_CREDENTIALS)
                            return
                        }
                        200 -> {
                            var token: String = ""

                            // Extract the token
                            val responseAsString = response.body?.string().toString()
                            val jsonObject: JSONObject = JSONObject(responseAsString)
                            token = (jsonObject.get("data") as JSONObject).get("accessToken").toString()

                            // Return
                            getAuthTokenInterface.onAuthTokenResponse(token, true, null)
                            return
                        }
                    }
                }
            })

        }

        /*
         Register a new user
         */
        public fun registerUser(registerUserData: RegisterUserData,registerUserInterface: RegisterUserInterface){
            // Request Body
            val requestBody: RequestBody = registerUserData.toJSONObject().toString().toRequestBody(
                mediaType)

            // Request
            val request: Request = Request.Builder()
                .url(http.BASE_URL + "/auth/signup")
                .post(requestBody)
                .build()

            // Enqueue the request
            client.newCall(request).enqueue(object : Callback {
                override fun onFailure(call: Call, e: IOException) {
                    registerUserInterface.onRegistrationResult(null,false,ServerError.SERVER_UNREACHABLE)
                }

                override fun onResponse(call: Call, response: Response) {
                    when (response.code){
                        200 -> {
                            registerUserInterface.onRegistrationResult(registerUserData.getEmail(),true,null)
                        }
                        else -> {
                            registerUserInterface.onRegistrationResult(registerUserData.getEmail(),false,ServerError.SOMETHING_WENT_WRONG)
                        }
                    }
                }
            })
        }

        /*
         Verify email with OTP
         */
        public fun verifyEmailWithOTP(otpPin: String, onVerifyEmailInterface: VerifyEmailInterface){
            // Request
            val request = Request.Builder()
                .get()
                .url(http.BASE_URL+"/auth/effector/verify-email/$otpPin")
                .build()

            // Enqueue Request
            client.newCall(request).enqueue(object : Callback{
                override fun onFailure(call: Call, e: IOException) {
                    onVerifyEmailInterface.onVerifyEmailWithOTPResult(false,ServerError.SERVER_UNREACHABLE)
                }

                override fun onResponse(call: Call, response: Response) {
                    val code = response.code
                    // 200 response
                    if (code == 200){
                        onVerifyEmailInterface.onVerifyEmailWithOTPResult(true,null)
                        return
                    }

                    // 401 - Invalid OTP
                    if (code == 401 && response.body.toString().contains("Not Found")){
                        onVerifyEmailInterface.onVerifyEmailWithOTPResult(false,ServerError.INVALID_OTP)
                        return
                    }

                    // 401 - Expired OTP
                    if (code == 401){
                        onVerifyEmailInterface.onVerifyEmailWithOTPResult(false,ServerError.INVALID_OTP)
                        return
                    }

                    onVerifyEmailInterface.onVerifyEmailWithOTPResult(false,ServerError.SOMETHING_WENT_WRONG)
                }
            })
        }

        /*
        Request new OTP
         */
        public fun requestNewOTP(email: String,requestNewOTPInterface: RequestNewOTPInterface ){
            // Request
            val request: Request = Request.Builder()
                .get()
                .url("${http.BASE_URL}/auth/email-request/email-verification/${email}")
                .build()

            // Enqueue the request
            client.newCall(request).enqueue(object: Callback {
                override fun onFailure(call: Call, e: IOException) {
                    requestNewOTPInterface.onNewOTPRequestResult(false,ServerError.SERVER_UNREACHABLE)
                }

                override fun onResponse(call: Call, response: Response) {
                    // 200 and success
                    if (response.body?.byteString().toString().contains("Please Check")){
                        requestNewOTPInterface.onNewOTPRequestResult(true,null)
                        return
                    }

                    // Everything else something went wrong
                    requestNewOTPInterface.onNewOTPRequestResult(false,ServerError.SOMETHING_WENT_WRONG)
                }
            })
        }


        /*
        Reset password request
         */
        public fun resetPasswordRequest(email: String,requestPasswordInterface: ResetPasswordInterface){
            //JSON object
            val body = JSONObject().apply {
                put("email",email)
                put("redirectionUrl","https://challenge.veliqo.com/change-password")
            }

            // Request
            val request = Request.Builder()
                .post(body.toString().toRequestBody(mediaType))
                .url("${http.BASE_URL}/auth/email-request/forgot-password")
                .build()

            // Enqueue
            client.newCall(request).enqueue(object: Callback {
                override fun onFailure(call: Call, e: IOException) {
                    requestPasswordInterface.onResetPasswordRequestResult(false,ServerError.SERVER_UNREACHABLE)
                }

                override fun onResponse(call: Call, response: Response) {
                    // 200 - success
                    if (response.body?.byteString().toString().contains("Please Check")){
                        requestPasswordInterface.onResetPasswordRequestResult(true,null)
                        return
                    }

                    requestPasswordInterface.onResetPasswordRequestResult(false,ServerError.INVALID_EMAIL)
                }
            })

        }

        /*
        Change password request
         */
        fun changePasswordRequest(resetPasswordData: ResetPasswordData, resetPasswordInterface: ResetPasswordInterface) {
            // Request
            val request = Request.Builder()
                .post(resetPasswordData.toJSONObject().toString().toRequestBody(mediaType))
                .url("${http.BASE_URL}/auth/effector/reset-password")
                .build()

            // Enqueue
            client.newCall(request).enqueue(object : Callback {
                override fun onFailure(call: Call, e: IOException) {
                    resetPasswordInterface.onChangePasswordRequestResult(false,ServerError.SERVER_UNREACHABLE)
                }

                override fun onResponse(call: Call, response: Response) {
                    // 200
                    if (response.code == 200){
                        resetPasswordInterface.onChangePasswordRequestResult(true,null)
                        return
                    }

                    // 401 - Invalid OTP
                    if (response.code == 401){
                        resetPasswordInterface.onChangePasswordRequestResult(false,ServerError.INVALID_OTP)
                        return
                    }
                }
            })
        }
    }
}