package com.wapazock.veliqolab

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.wapazock.veliqolab.models.AuthUser
import com.wapazock.veliqolab.repository.AuthRepository
import com.wapazock.veliqolab.utils.errors.ServerError
import com.wapazock.veliqolab.utils.interfaces.GetAuthTokenInterface

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }
}