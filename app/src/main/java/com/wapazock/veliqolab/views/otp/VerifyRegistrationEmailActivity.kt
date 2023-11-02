package com.wapazock.veliqolab.views.otp

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View.GONE
import android.view.View.VISIBLE
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.doOnTextChanged
import com.wapazock.veliqolab.R
import com.wapazock.veliqolab.custom.CustomButton
import com.wapazock.veliqolab.repository.AuthRepository
import com.wapazock.veliqolab.utils.errors.ServerError
import com.wapazock.veliqolab.utils.interfaces.RequestNewOTPInterface
import com.wapazock.veliqolab.utils.interfaces.VerifyEmailInterface
import com.wapazock.veliqolab.utils.notifications.ClassicNotifications
import com.wapazock.veliqolab.utils.notifications.NotificationTypes
import com.wapazock.veliqolab.utils.validators.OTPValidator
import com.wapazock.veliqolab.views.login.loginActivity
import com.wapazock.veliqolab.views.register.register

class VerifyRegistrationEmailActivity : AppCompatActivity(), VerifyEmailInterface, RequestNewOTPInterface {

    // View Variables
    private lateinit var otpEditText : EditText
    private lateinit var verifyButton : CustomButton
    private lateinit var cancelButton : ImageView
    private lateinit var requestNewOTP : TextView

    // Variables
    private var otpPin: String = ""
    private var email: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_verify_registration_email)

        // Assign View Variables
        otpEditText =  this.findViewById(R.id.activityVerifyRegistrationEmailOTPEditText)
        verifyButton = this.findViewById(R.id.activityVerifyRegistrationEmailOTPCustomButton)
        cancelButton = this.findViewById(R.id.activityVerifyRegistrationEmailOTPCancelImageView)
        requestNewOTP = this.findViewById(R.id.activityVerifyEmailOTPNewOTPTextView)

        // Get email from shared prefs
        getEmailFromSharedPrefs()
        // Bind Cancel
        bindCancel()
        // Bind OTP input
        bindOTPInput()
        // Bind Verify Button
        bindVerifyButton()
    }

    // Retrieves and set the email to verify from shared prefs
    private fun getEmailFromSharedPrefs() {
        // Write the email to shared preferences
        val sharedPreferences: SharedPreferences = getSharedPreferences("registrations", Context.MODE_PRIVATE)
        email = sharedPreferences.getString("email",null)
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

    // Result when new OTP is requested
    override fun onNewOTPRequestResult(wasSuccessful: Boolean, error: ServerError?) {
        // Run on UI thread
        this.runOnUiThread {
            // Enable request for new OTP
            setRequestingForNewOTPState(true)

            // If successful show notification
            if (wasSuccessful)   {
                //clear text
                otpEditText.text.clear()
                
                ClassicNotifications.alertNotification(baseContext,"Successfully requested new OTP",NotificationTypes.SUCCESS)
                return@runOnUiThread
            }

            // Network Error
            if (error == ServerError.SERVER_UNREACHABLE){
                ClassicNotifications.alertNotification(baseContext,"Please check your internet connection",NotificationTypes.NETWORK_ERROR)
                return@runOnUiThread
            }

            // Something went wrong
            ClassicNotifications.alertNotification(baseContext,"Failed to request new OTP",NotificationTypes.SOMETHING_WENT_WRONG)
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

            // Failed OTP Enable request for a new OTP
            enableNewOTPRequestLink()
            setRequestingForNewOTPState(true)

            // Invalid OTP
            if (errorCode == ServerError.INVALID_OTP){
                ClassicNotifications.alertNotification(baseContext,"You entered an invalid OTP",NotificationTypes.INVALID_INPUT)
                return@runOnUiThread
            }

            // Expired OTP
            if (errorCode == ServerError.EXPIRED_OTP){
                ClassicNotifications.alertNotification(baseContext,"You entered an expired OPT",NotificationTypes.INVALID_INPUT)
                return@runOnUiThread
            }

            // Something Went Terribly Wrong
            ClassicNotifications.alertNotification(baseContext,"Something Went Wrong",NotificationTypes.SOMETHING_WENT_WRONG)
        }
    }

    // Enable request for new OTP
    private fun enableNewOTPRequestLink(){
        requestNewOTP.visibility = VISIBLE
    }

    // Requesting for new OTP state
    private fun setRequestingForNewOTPState(state: Boolean){
        if (!state){
            requestNewOTP.text = "Requesting new OTP"
            requestNewOTP.alpha = 0.2f

            // Do nothing when new OTP is requested
            requestNewOTP.setOnClickListener {}
            return
        }

        // Request for a new otp
        requestNewOTP.text = "Request new OTP"
        requestNewOTP.alpha = 0.5f
        requestNewOTP.setOnClickListener {
            // On requesting a new OTP set the state to false
            setRequestingForNewOTPState(false)
            // If email is null then go back to register activity
            if (email == null){
                val intent = Intent(baseContext,register::class.java)
                baseContext.startActivity(intent)
                finish()
            }
            // Request new OTP
            AuthRepository.requestNewOTP(email!!,this)
        }
    }
}