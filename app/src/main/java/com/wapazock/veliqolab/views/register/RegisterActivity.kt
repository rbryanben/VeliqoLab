package com.wapazock.veliqolab.views.register

import RegisterUserData
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.CheckBox
import android.widget.EditText
import androidx.core.widget.doOnTextChanged
import com.wapazock.veliqolab.R
import com.wapazock.veliqolab.custom.CustomButton
import com.wapazock.veliqolab.repository.AuthRepository
import com.wapazock.veliqolab.utils.errors.ServerError
import com.wapazock.veliqolab.utils.interfaces.RegisterUserInterface
import com.wapazock.veliqolab.utils.notifications.ClassicNotifications
import com.wapazock.veliqolab.utils.validators.RegistrationUserDataValidator
import com.wapazock.veliqolab.views.otp.CheckYourEmailActivity


class register : AppCompatActivity(), RegisterUserInterface {

    // View variables
    private lateinit var firstNameEditText: EditText
    private lateinit var lastNameEditText: EditText
    private lateinit var emailEditText: EditText
    private lateinit var phoneNumber: EditText
    private lateinit var passwordEditText: EditText
    private lateinit var checkBox: CheckBox
    private lateinit var signUpCustomButton: CustomButton

    // Variables
    private var registrationUserData : RegisterUserData = RegisterUserData();

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        // Set View Variables
        firstNameEditText = findViewById(R.id.activityRegisterFirstNameEditText)
        lastNameEditText = findViewById(R.id.activityRegisterLastNameEditText)
        emailEditText = findViewById(R.id.activityRegisterEmailEditText)
        phoneNumber = findViewById(R.id.activityRegisterPhoneNumberEditText)
        passwordEditText = findViewById(R.id.activityRegisterPasswordEditText)
        checkBox = findViewById(R.id.activityRegisterTermsCheckBox)
        signUpCustomButton = findViewById(R.id.activityRegisterSignUpCustomButton)

        // Bind data to RegistrationUserData object
        bindInputs()
        // Bind sign up button
        bindSignUpButton()
    }

    // Listens on signup click
    private fun bindSignUpButton() {
        signUpCustomButton.setOnClickListener {
            //If valid register
            if (RegistrationUserDataValidator.isValid(registrationUserData) && checkBox.isChecked){
                signUpCustomButton.setLoading(true)
                AuthRepository.registerUser(registrationUserData,this)
            }
        }
    }

    // Bind inputs to data object
    private fun bindInputs() {
        // First Name
        firstNameEditText.doOnTextChanged { text, start, before, count ->
            registrationUserData.setFirstName(text.toString())
            validateRegistrationData()
        }

        //Last Name
        lastNameEditText.doOnTextChanged { text, start, before, count ->
            registrationUserData.setLastName(text.toString())
            validateRegistrationData()
        }

        // Email
        emailEditText.doOnTextChanged { text, start, before, count ->
            registrationUserData.setEmail(text.toString())
            validateRegistrationData()
        }

        // Phone
        phoneNumber.doOnTextChanged { text, start, before, count ->
            registrationUserData.setMobile(text.toString())
            validateRegistrationData()
        }

        // Password
        passwordEditText.doOnTextChanged { text, start, before, count ->
            registrationUserData.setPassword(text.toString())
            validateRegistrationData()
        }

        // Check Box
        checkBox.setOnCheckedChangeListener { _, isChecked ->
            validateRegistrationData()
        }
    }

    // Validates Registration Data
    private fun validateRegistrationData() {
        //If valid enable register button
        if (RegistrationUserDataValidator.isValid(registrationUserData) && checkBox.isChecked){
            signUpCustomButton.enable()
            return
        }

        signUpCustomButton.disable()
    }

    // On Registration Request Result
    override fun onRegistrationResult(email: String?, wasSuccessful: Boolean, error: ServerError?) {
        this.runOnUiThread(object : Runnable {
            override fun run() {
                // Stop Loading
                signUpCustomButton.setLoading(false)

                // If successful then finish
                if (wasSuccessful){
                    // Write the email to shared preferences
                    val sharedPreferences: SharedPreferences = getSharedPreferences("registrations", Context.MODE_PRIVATE)
                    with (sharedPreferences.edit()){
                        putString("email",registrationUserData.getEmail())
                        apply()
                    }

                    val intent : Intent = Intent(baseContext,CheckYourEmailActivity::class.java);
                    startActivity(intent)
                    return
                }

                // Server Not Reachable
                if (error == ServerError.SERVER_UNREACHABLE){
                    ClassicNotifications.alertNotification(baseContext,"Server Unreachable")
                    return
                }

                // Email Already Exists
                if (error == ServerError.EMAIL_ALREADY_EXISTS){
                    ClassicNotifications.alertNotification(baseContext,"Email Already Exists")
                    return
                }

                // Something Else Went Wrong
                ClassicNotifications.alertNotification(baseContext,"Something Went Wrong")
            }
        })

    }
}