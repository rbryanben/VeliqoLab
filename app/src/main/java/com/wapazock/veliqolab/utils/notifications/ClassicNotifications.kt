package com.wapazock.veliqolab.utils.notifications

import android.content.Context
import android.widget.Toast

class ClassicNotifications {
    companion object {

        // Alert Notification
        fun alertNotification(context: Context, text: String){
            Toast.makeText(context,text,Toast.LENGTH_SHORT).show()
        }
    }
}