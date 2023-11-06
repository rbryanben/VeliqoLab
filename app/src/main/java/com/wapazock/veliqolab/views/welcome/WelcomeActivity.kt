package com.wapazock.veliqolab.views.welcome

import android.app.Activity
import android.content.Intent
import android.content.res.Configuration
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatDelegate
import com.wapazock.veliqolab.R
import com.wapazock.veliqolab.views.login.loginActivity
import com.wapazock.veliqolab.views.register.register
import com.wapazock.veliqolab.views.reset_password.ResetPasswordActivity

class WelcomeActivity : AppCompatActivity() {

    // View variables
    final lateinit var signInLink : TextView
    final lateinit var signUpLink : TextView
    final lateinit var resetPassword: TextView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_welcome)

        // Force dark mode
        forceDarkMode(this)

        // Assign view variables
        signInLink = this.findViewById(R.id.activityWelcomeSignInTextView)
        signUpLink = this.findViewById(R.id.activityWelcomeSignUpTextView)
        resetPassword = this.findViewById(R.id.activityWelcomeResetPasswordTextView)

        // Bind the links to their activities
        signInLink.setOnClickListener {
            val intent = Intent(this,loginActivity::class.java)
            startActivity(intent)
        }

        signUpLink.setOnClickListener {
            val intent = Intent(this, register::class.java)
            startActivity(intent)
        }

        resetPassword.setOnClickListener {
            val intent = Intent(this,ResetPasswordActivity::class.java)
            startActivity(intent)
        }
    }

    fun forceDarkMode(activity: Activity) {
        val uiMode: Int = activity.resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
    }
}