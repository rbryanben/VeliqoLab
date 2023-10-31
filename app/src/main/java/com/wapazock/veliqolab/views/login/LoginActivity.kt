package com.wapazock.veliqolab.views.login

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.core.widget.doOnTextChanged
import com.wapazock.veliqolab.R
import com.wapazock.veliqolab.custom.CustomButton
import com.wapazock.veliqolab.custom.CustomEmailEditText
import com.wapazock.veliqolab.custom.CustomPasswordEditText
import com.wapazock.veliqolab.models.AuthUser
import com.wapazock.veliqolab.repository.AuthRepository
import com.wapazock.veliqolab.utils.errors.ServerError
import com.wapazock.veliqolab.utils.interfaces.GetAuthTokenInterface
import com.wapazock.veliqolab.utils.notifications.ClassicNotifications
import com.wapazock.veliqolab.utils.validators.AuthUserValidator

class login : AppCompatActivity() , GetAuthTokenInterface {

    // View Variables
    private lateinit var closeImageView : ImageView
    private lateinit var emailEditText: CustomEmailEditText
    private lateinit var passwordEditText: CustomPasswordEditText
    private lateinit var forgotPasswordTextView: TextView
    private lateinit var signInButton: CustomButton

    // Class Variables
    private val authUser: AuthUser = AuthUser();

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        // Assign Variables
        closeImageView = findViewById(R.id.activityLoginCancelImageView)
        emailEditText = findViewById(R.id.activityLoginCustomEmailEditText)
        passwordEditText = findViewById(R.id.activityLoginCustomPasswordEditText)
        forgotPasswordTextView = findViewById(R.id.activityLoginForgotButtonTextView)
        signInButton = findViewById(R.id.activityLoginButton)

        // Bind email and password inputs to watch for changes
        bindEmailInput()
        bindPasswordInput()
        // Bind signin butotn
        bindSignInButton()
    }

    // Listens for button click
    private fun bindSignInButton() {
        signInButton.setOnClickListener {
            //If Auth is valid then sign in
            if (AuthUserValidator.isValid(authUser)){
                // Set loading
                signInButton.setLoading(true)
                // Attempt to login , the response is received in this class since it implements GetAuthTokenInterface
                AuthRepository.getAuthToken(authUser,this);
            }
        }
    }

    // Listens for changes to the password edit text and assigns value to authUser
    private fun bindPasswordInput() {
        passwordEditText.getEditText().doOnTextChanged { text, start, before, count ->
            authUser.setPassword(text.toString())
            validateAuthUser()
        }
    }

    // Listens for changes to the email edit text and assigns value to authUser
    private fun bindEmailInput() {
        emailEditText.getEditText().doOnTextChanged { text, start, before, count ->
            authUser.setEmail(text.toString())
            validateAuthUser()
        }
    }

    // Called everytime when any of the text fields changes to check if input is valid and then enable
    // or disable sign in button
    private fun validateAuthUser(){
        if (AuthUserValidator.isValid(this.authUser)){
            signInButton.enable()
        }
        else {
            signInButton.disable()
        }
    }

    // When token is received from auth request
    override fun onAuthTokenResponse(token: String?, wasSuccessful: Boolean, error: ServerError?) {
        // Run on ui thread
        this.runOnUiThread( object:Runnable {
            override fun run() {
                // Stop Loading
                signInButton.setLoading(false)

                // Valid Credentials - set the global auth token header
                if (wasSuccessful){
                    ClassicNotifications.alertNotification(baseContext,"Auth Successful")
                    return
                }

                // Server Unreachable
                if (error == ServerError.SERVER_UNREACHABLE) {
                    ClassicNotifications.alertNotification(baseContext,"Server Unreachable")
                }

                // Invalid Credentials
                if (error == ServerError.INVALID_CREDENTIALS){
                    ClassicNotifications.alertNotification(baseContext,"Invalid Email or Password")
                }
            }
        })






    }
}