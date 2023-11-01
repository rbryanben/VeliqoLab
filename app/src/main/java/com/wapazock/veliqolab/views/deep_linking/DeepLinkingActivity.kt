package com.wapazock.veliqolab.views.deep_linking

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.wapazock.veliqolab.R
import com.wapazock.veliqolab.views.otp.OtpRegisterActivity
import kotlin.math.log

class DeepLinkingActivity : AppCompatActivity() {

    // Variables
    private final val TAG: String = "DeepLinkingActivity"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_deep_linking)

        // Get uri
        val data: Uri? = intent?.data

        // Get parameters
        val action = data?.getQueryParameter("action")
        val email = data?.getQueryParameter("email")

        // Launch intended intent
        if (action.equals("registration-verify-otp")){
            val intent = Intent(this,OtpRegisterActivity::class.java)
            intent.putExtra("email",email)
            this.startActivity(intent)
            finish()
        }
    }
}