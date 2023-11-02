package com.wapazock.veliqolab.views.deep_linking

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.wapazock.veliqolab.R
import com.wapazock.veliqolab.views.otp.VerifyRegistrationEmailActivity
import android.net.Uri

class DeepLinkingActivity : AppCompatActivity() {

    // Variables
    private final val TAG: String = "DeepLinkingActivity"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_deep_linking)

        // Otp Activity
        val intent = Intent(this, VerifyRegistrationEmailActivity::class.java)
        this.startActivity(intent)
        finish()
    }

}