package com.wapazock.veliqolab.views.otp

import android.app.Dialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.core.widget.doOnTextChanged
import com.wapazock.veliqolab.R
import com.wapazock.veliqolab.custom.CustomButton
import com.wapazock.veliqolab.repository.AuthRepository
import com.wapazock.veliqolab.utils.errors.ServerError
import com.wapazock.veliqolab.utils.interfaces.VerifyEmailInterface
import com.wapazock.veliqolab.utils.notifications.ClassicNotifications
import com.wapazock.veliqolab.utils.validators.OTPValidator
import com.wapazock.veliqolab.views.login.loginActivity

class VerifyRegistrationEmailActivity : AppCompatActivity(), VerifyEmailInterface {

    // View Variables
    private lateinit var otpEditText : EditText
    private lateinit var verifyButton : CustomButton
    private lateinit var cancelButton : ImageView
    private lateinit var requestNewOTP : TextView

    // Variables
    private var otpPin: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_verify_registration_email)

        // Assign View Variables
        otpEditText =  this.findViewById(R.id.activityVerifyRegistrationEmailOTPEditText)
        verifyButton = this.findViewById(R.id.activityVerifyRegistrationEmailOTPCustomButton)
        cancelButton = this.findViewById(R.id.activityVerifyRegistrationEmailOTPCancelImageView)
        requestNewOTP = this.findViewById(R.id.activityVerifyEmailOTPNewOTPTextView)


        // Bind Cancel
        bindCancel()
        // Bind OTP input
        bindOTPInput()
        // Bind Verify Button
        bindVerifyButton()
        // Bind Request OTP
        bindRequestNewOTP()
    }

    // Bind the request for a new otp - will show loading
    private fun bindRequestNewOTP() {
        requestNewOTP.setOnClickListener{
            // Start Loading Dialog

        }
    }

    // Bind verify button -  onclick if otp is valid submit the email
    private fun bindVerifyButton() {
        verifyButton.setOnClickListener {
            if (OTPValidator.isValid(otpPin)){
                //start loading
                verifyButton.setLoading(true)
                //submit the otp
                AuthRepository.verifyEmailWithOTP(otpPin,this)
            }
        }
    }

    // Bind input to otpPin and validate
    private fun bindOTPInput() {
        otpEditText.doOnTextChanged { text, start, before, count ->
            otpPin = text.toString()
            validateOTP()
        }
    }

    // Validates OTP - if valid enable verify button
    private fun validateOTP() {
        if (OTPValidator.isValid(otpPin)){
            verifyButton.enable()
            return
        }
        verifyButton.disable()
    }

    // Closes the activity on click
    private fun bindCancel() {
        cancelButton.setOnClickListener {
            this.finish()
        }
    }

    // On OTP Response
    override fun onVerifyEmailWithOTPResult(wasSuccessful: Boolean, errorCode: ServerError?) {
        this.runOnUiThread {
            // Stop Loading
            verifyButton.setLoading(false)

            // Successful - Redirect to login
            if (wasSuccessful){
                val intent = Intent(baseContext, loginActivity::class.java)
                // Pass in a message to show on login
                intent.putExtra("toast","Verification Successful")
                // Start Activity
                startActivity(intent)
                finish()
                return@runOnUiThread
            }

            // Invalid OTP
            if (errorCode == ServerError.INVALID_OTP){
                ClassicNotifications.alertNotification(baseContext,"Invalid OTP")
                return@runOnUiThread
            }

            // Expired OTP
            if (errorCode == ServerError.EXPIRED_OTP){
                ClassicNotifications.alertNotification(baseContext,"Expired OPT")
                return@runOnUiThread
            }

            // Something Went Terribly Wrong
            ClassicNotifications.alertNotification(baseContext,"Something Went Wrong")
        }
    }
}