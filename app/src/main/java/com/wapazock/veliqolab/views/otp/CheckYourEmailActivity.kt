package com.wapazock.veliqolab.views.otp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import com.wapazock.veliqolab.R
import com.wapazock.veliqolab.custom.CustomButton

class CheckYourEmailActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_check_your_email)

        // Handle Close
        this.findViewById<ImageView>(R.id.activityCheckYourEmailCancelImageView).setOnClickListener {
            finish()
        }

        // Open Gmail On Click
        val button : CustomButton = this.findViewById(R.id.activityCheckYourEmailContinueButton)
        button.enable()
        button.setOnClickListener {
            val intent = Intent(baseContext,VerifyRegistrationEmailActivity::class.java)
            startActivity(intent)
        }
    }
}