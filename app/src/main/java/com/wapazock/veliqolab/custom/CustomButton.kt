package com.wapazock.veliqolab.custom

import android.content.Context
import android.content.res.TypedArray
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import android.widget.ProgressBar
import android.widget.RelativeLayout
import android.widget.TextView
import com.wapazock.veliqolab.R

class CustomButton : FrameLayout {

    // Variabls
    private lateinit var buttonRelativeLayout: RelativeLayout
    private lateinit var buttonTextView: TextView
    private lateinit var progressBar: ProgressBar

    // Constuctors
    constructor(context: Context) : super(context){
        init(context,null)
    }
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs){
        init(context,attrs)
    }
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ){
        init(context,attrs)
    }

    constructor(
        context: Context,
        attrs: AttributeSet?,
        defStyleAttr: Int,
        defStyleRes: Int
    ) : super(context, attrs, defStyleAttr, defStyleRes){
        init(context,attrs)
    }

    // Enable Button
    public fun enable(){
        buttonRelativeLayout.setBackgroundResource(R.drawable.background_custom_button_enabled)
    }

    // Disable Button
    public fun disable(){
        buttonRelativeLayout.setBackgroundResource(R.drawable.background_custom_button_disabled)
    }

    // Set Loading
    public fun setLoading(state : Boolean){
        if (state) {
            disable()
            progressBar.visibility = VISIBLE
            return
        }

        progressBar.visibility = GONE
        enable()
    }

    // Init method
    private fun init(context: Context, attr: AttributeSet?){
        // Inflate a layout
        LayoutInflater.from(context).inflate(R.layout.custom_button,this,true);

        // Assign variables
        buttonRelativeLayout = rootView.findViewById(R.id.customButtonRelativeLayout)
        buttonTextView = rootView.findViewById(R.id.customButtonTextView)
        progressBar = rootView.findViewById(R.id.customButtonProgressBar)


        // Get and Set Attributes
        val attributesArray : TypedArray = context.obtainStyledAttributes(attr,R.styleable.CustomButton)
        val text: String = attributesArray.getString(R.styleable.CustomButton_CustomButtonText).toString()
        buttonTextView.text = text
    }


}