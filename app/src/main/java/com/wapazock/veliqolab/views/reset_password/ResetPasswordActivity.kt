package com.wapazock.veliqolab.views.reset_password

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.EditText
import android.widget.ImageView
import androidx.core.content.edit
import androidx.core.widget.addTextChangedListener
import androidx.core.widget.doOnTextChanged
import com.wapazock.veliqolab.R
import com.wapazock.veliqolab.custom.CustomButton
import com.wapazock.veliqolab.models.ResetPasswordData
import com.wapazock.veliqolab.repository.AuthRepository
import com.wapazock.veliqolab.utils.errors.ServerError
import com.wapazock.veliqolab.utils.interfaces.ResetPasswordInterface
import com.wapazock.veliqolab.utils.notifications.ClassicNotifications
import com.wapazock.veliqolab.utils.notifications.NotificationTypes
import com.wapazock.veliqolab.utils.validators.EmailValidator
import com.wapazock.veliqolab.views.otp.OtpResetPasswordActivity
import okhttp3.internal.notify

class ResetPasswordActivity : AppCompatActivity(), ResetPasswordInterface {

    // View Variables
    private lateinit var emailEditText: EditText
    private lateinit var sendOTPButton: CustomButton
    private lateinit var cancelButton: ImageView

    // Variables
    private var email: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reset_password)

        // Set view variables
        emailEditText = this.findViewById(R.id.activityResetPasswordEmailEditText)
        sendOTPButton = this.findViewById(R.id.activityResetPasswordCustomButton)
        cancelButton = this.findViewById(R.id.activityResetPasswordCancelImageView)

        //Bind cancel button
        bindCancelButton()
        //Bind email
        bindEmailEditText()
        //Bind reset password click
        bindResetButton()
    }

    // Cancel button finish
    private fun bindCancelButton() {
        cancelButton.setOnClickListener {
            finish()
        }
    }


    // Watched button click - if email is valid then request password reset
    private fun bindResetButton() {
        sendOTPButton.setOnClickListener {
            if (EmailValidator.isValid(email)){
                //Set loading
                sendOTPButton.setLoading(true)
                //Request password rest
                AuthRepository.resetPasswordRequest(email,this)
            }
        }
    }

    // Watches for changes on tying email
    private fun bindEmailEditText() {
        emailEditText.doOnTextChanged { text, start, before, count ->
            email = text.toString()
            validateData()
        }
    }

    // Validate if the reset password data is correct
    private fun validateData() {
        if (EmailValidator.isValid(email)){
            sendOTPButton.enable()
            return
        }
        sendOTPButton.disable()
    }


    // On Reset Password result
    override fun onResetPasswordRequestResult(wasSuccessful: Boolean, error: ServerError?) {
        runOnUiThread {
            sendOTPButton.setLoading(false)

            // If was successful start verify otp intent
            if (wasSuccessful) {
                val intent = Intent(baseContext, OtpResetPasswordActivity::class.java)
                intent.putExtra("toast","Successfully sent OTP")
                // Set reset password shared prefs
                this.getSharedPreferences("reset_password", Context.MODE_PRIVATE).edit {
                    putString("email",email)
                    apply()
                }
                // Start OTP intent
                startActivity(intent)
                return@runOnUiThread
            }

            // Network Error
            if (error == ServerError.SERVER_UNREACHABLE){
                ClassicNotifications.alertNotification(baseContext,"Check your internet connection",NotificationTypes.NETWORK_ERROR)
                return@runOnUiThread
            }

            // Invalid Email
            if (error == ServerError.INVALID_EMAIL){
                ClassicNotifications.alertNotification(baseContext,"You entered an invalid email",NotificationTypes.INVALID_INPUT)
                return@runOnUiThread
            }
        }
    }

    // Not in use
    override fun onChangePasswordRequestResult(wasSuccessful: Boolean, error: ServerError?) {
        TODO("Not yet implemented")
    }
}