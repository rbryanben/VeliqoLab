package com.wapazock.veliqolab.views.otp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import com.wapazock.veliqolab.R
import com.wapazock.veliqolab.custom.CustomButton
import com.wapazock.veliqolab.utils.notifications.ClassicNotifications

class CheckYourEmailActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_check_your_email)

        // Open Gmail On Click
        val button : CustomButton = this.findViewById(R.id.activityCheckYourEmailOpenEmailButton)
        button.enable()
        button.setOnClickListener {
            val intent = Intent(Intent.ACTION_MAIN)
            intent.addCategory(Intent.CATEGORY_APP_EMAIL)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
        }
    }
}