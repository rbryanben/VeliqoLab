package com.wapazock.veliqolab.custom

import android.content.Context
import android.content.res.TypedArray
import android.text.InputType
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.EditText
import android.widget.FrameLayout
import android.widget.TextView
import com.wapazock.veliqolab.R


class CustomEmailEditText : FrameLayout {

    // Variables
    private lateinit var editText: EditText
    private lateinit var headerTextView: TextView
    private lateinit var animationFloatUp: Animation
    private lateinit var animationFloatDown: Animation
    private var animationApplied : Boolean = false

    // Constructors
    constructor(context: Context) : super(context){
        init(context,null);
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs){
        init(context,attrs);
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ){
        init(context, attrs);
    }

    constructor(
        context: Context,
        attrs: AttributeSet?,
        defStyleAttr: Int,
        defStyleRes: Int
    ) : super(context, attrs, defStyleAttr, defStyleRes){
        init(context,attrs);
    }

    // Animate the header to float to the top
    fun animateHeader(){
        // check if the animation has not been applied
        if (this.animationApplied){
            return
        }

        // Start translation animation
        animationFloatUp.fillAfter = true
        headerTextView.startAnimation(animationFloatUp)

        headerTextView.setTextColor(resources.getColor(R.color.colorEditTextFocus))

        // Set animation applied to true
        animationApplied = true
    }

    // Returns the EditTextComponent for watching
    fun getEditText(): EditText {
        return editText
    }


    // Remove the applied animation
    fun removeAnimation(){

        // check if the animation is applied
        if (this.animationApplied == false){
            return
        }

        // Start  Translation Animation
        animationFloatDown.fillAfter = true
        headerTextView.startAnimation(animationFloatDown)

        headerTextView.setTextColor(resources.getColor(R.color.colorEditText))

        this.animationApplied = false
    }

    // Initialization method
    private fun init(context: Context,attrs: AttributeSet?) {

        // Inflate a view
        LayoutInflater.from(context).inflate(R.layout.custom_email_edit_text,this,true)

        // Set variables
        headerTextView = rootView.findViewById(R.id.customEmailEditTextHintTextView)
        editText = rootView.findViewById(R.id.customEmailEditTextEditText)
        animationFloatUp = AnimationUtils.loadAnimation(context,R.anim.float_up)
        animationFloatDown = AnimationUtils.loadAnimation(context,R.anim.float_down)

        // Get and Set Attributes
        val attributesArray : TypedArray = context.obtainStyledAttributes(attrs,R.styleable.CustomEditText)
        val hint: String = attributesArray.getString(R.styleable.CustomEditText_CustomEditTextHint).toString()

        // Set hint
        this.headerTextView.setText(hint)

        // Listen for focus and change focus
        editText.onFocusChangeListener = OnFocusChangeListener { v, hasFocus ->
            if (hasFocus) {
                this.animateHeader()
            } else {
                if (this.editText.text.isEmpty()){
                    this.removeAnimation()
                }
            }
        }
    }



}