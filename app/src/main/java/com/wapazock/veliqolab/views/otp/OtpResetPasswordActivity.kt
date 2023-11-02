package com.wapazock.veliqolab.views.otp

import android.content.Intent
import android.widget.EditText
import com.wapazock.veliqolab.custom.CustomButton
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.core.widget.doOnTextChanged
import com.wapazock.veliqolab.R
import com.wapazock.veliqolab.models.ResetPasswordData
import com.wapazock.veliqolab.repository.AuthRepository
import com.wapazock.veliqolab.utils.errors.ServerError
import com.wapazock.veliqolab.utils.interfaces.ResetPasswordInterface
import com.wapazock.veliqolab.utils.notifications.ClassicNotifications
import com.wapazock.veliqolab.utils.notifications.NotificationTypes
import com.wapazock.veliqolab.views.login.loginActivity
import com.wapazock.veliqolab.views.reset_password.ResetPasswordActivity

class OtpResetPasswordActivity : AppCompatActivity(), ResetPasswordInterface {

    // View Variables
    private lateinit var cancelButton: ImageView
    private lateinit var otp: EditText
    private lateinit var newPassword: EditText
    private lateinit var requestNewOTP : TextView
    private lateinit var changePasswordButton: CustomButton

    // Variables
    private var resetPasswordData = ResetPasswordData()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_otp_reset_password)

        // Set view variables
        cancelButton = this.findViewById(R.id.activityResetPasswordOTPCancelImageView)
        otp = this.findViewById(R.id.activityResetPasswordOTPOTPEditText)
        newPassword = this.findViewById(R.id.activityResetPasswordOTPNewPasswordTextView)
        changePasswordButton = this.findViewById(R.id.activityResetPasswordOTPChangePasswordButton)
        requestNewOTP = this.findViewById(R.id.activityResetChangePasswordNewOTPTextView)

        // Show any toast
        val toast = intent.getStringExtra("toast")
        if (toast != null){
            ClassicNotifications.alertNotification(this,toast,NotificationTypes.SUCCESS)
        }

        // bind cancel button
        bindCancelButton()
        // bind button
        bindChangePasswordButton()
        // bind OTP
        bindOTP()
        // bind email
        bindPassword()
        // bind request for new OTP
        bindRequestNewOTP()
    }

    // Close the activity
    private fun bindCancelButton() {
        cancelButton.setOnClickListener {
            finish()
        }
    }


    // Request new OTP if clicked
    private fun bindRequestNewOTP() {
        requestNewOTP.setOnClickListener {
            val intent = Intent(this,ResetPasswordActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    // Watches button on click and requests for a password change if reset data is valid
    private fun bindChangePasswordButton() {
        changePasswordButton.setOnClickListener {
           //validate
            if (validateResetUserData()){
                //set loading
                changePasswordButton.setLoading(true)
                //send the request
                AuthRepository.changePasswordRequest(resetPasswordData,this)
            }
        }
    }

    // Watches password text changes
    private fun bindPassword() {
        newPassword.doOnTextChanged { text, start, before, count ->
            resetPasswordData.setNewPassword(text.toString())
            validateResetUserData()
        }
    }

    // Watches OTP text changes and sets to reset password data
    private fun bindOTP() {
        otp.doOnTextChanged { text, start, before, count ->
            resetPasswordData.setOtp(text.toString())
            validateResetUserData()
        }
    }


    // If reset user data is valid then enable change password button
    private fun validateResetUserData(): Boolean {
        if (resetPasswordData.getNewPassword().length >= 5
            && resetPasswordData.getOtp().length == 6){
            // enable the button
            changePasswordButton.enable()
            return true
        }
        changePasswordButton.disable()
        return false
    }

    // Not in use
    override fun onResetPasswordRequestResult(wasSuccessful: Boolean, error: ServerError?) {
        TODO("Not yet implemented")
    }

    // Result on password change
    override fun onChangePasswordRequestResult(wasSuccessful: Boolean, error: ServerError?) {
        runOnUiThread{
            //stop loading
            changePasswordButton.setLoading(false)

            //if was successful then redirect to login
            if (wasSuccessful){
                val intent = Intent(baseContext,loginActivity::class.java)
                intent.putExtra("toast","Password successfully changed.")
                startActivity(intent)
                finish()
            }

            //invalid otp
            if (error == ServerError.INVALID_OTP){
                ClassicNotifications.alertNotification(baseContext,"You entered an invalid OTP.",NotificationTypes.INVALID_INPUT)
                return@runOnUiThread
            }

            //network error
            if (error == ServerError.SERVER_UNREACHABLE){
                ClassicNotifications.alertNotification(baseContext,"Please check your internet connection.",NotificationTypes.NETWORK_ERROR)
                return@runOnUiThread
            }

            ClassicNotifications.alertNotification(baseContext,"Something went terribly wrong.",NotificationTypes.SOMETHING_WENT_WRONG)
        }
    }
}