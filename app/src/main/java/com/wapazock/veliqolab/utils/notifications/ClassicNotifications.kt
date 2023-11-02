package com.wapazock.veliqolab.utils.notifications

import android.content.Context
import android.content.res.Resources
import android.view.Gravity
import android.view.LayoutInflater
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat.getSystemService
import com.wapazock.veliqolab.R

// Type of notification
enum class NotificationTypes {
    NETWORK_ERROR,
    SUCCESS,
    INVALID_INPUT,
    SOMETHING_WENT_WRONG
}


class ClassicNotifications {
    companion object {

        // Notification Customizable
        fun alertNotification(context: Context,text: String,type: NotificationTypes?){
            // Toast
            val toast = Toast(context)

            // Layout
            val layoutInflater = LayoutInflater.from(context) as LayoutInflater
            val layout = layoutInflater.inflate(R.layout.custom_toast, null)

            // Icon
            val icon : ImageView = layout.findViewById(R.id.customToastIconImageView)
            val textView: TextView = layout.findViewById(R.id.customToastTextView)
            val closeImageView: ImageView = layout.findViewById(R.id.customToastCancelImageView)

            // Bind close
            closeImageView.setOnClickListener{
                toast.cancel()
            }

            // Set Text
            textView.text = text
            // Change icon based on type
            when(type){
                // Success
                NotificationTypes.SUCCESS -> {
                    icon.setImageDrawable(context.getDrawable(R.drawable.baseline_check_24))
                }
                // Network Error
                NotificationTypes.NETWORK_ERROR ->{
                    icon.setImageDrawable(context.getDrawable(R.drawable.baseline_signal_wifi_bad_24))
                }
                else -> {
                    icon.setImageDrawable(context.getDrawable(R.drawable.warning_sign))
                }
            }

            // Configure toast
            toast.view = layout
            toast.duration = Toast.LENGTH_LONG
            toast.setGravity(Gravity.TOP or Gravity.FILL_HORIZONTAL,0,60)
            toast.show()
        }
    }
}
