package com.example.lynq.ui.customView

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.ColorStateList
import android.graphics.drawable.Drawable
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import androidx.appcompat.widget.AppCompatEditText
import androidx.core.content.ContextCompat
import com.example.lynq.R

class CustomPasswordText @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
) : AppCompatEditText(context, attrs), View.OnTouchListener {

    private var isPasswordVisible = false
    private var eyeIcon = ContextCompat.getDrawable(context, R.drawable.ic_eye_off)

    init {
        backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(context, android.R.color.darker_gray))
        compoundDrawablePadding = 32
        setCompoundDrawablesWithIntrinsicBounds(eyeIcon, null, null, null)
        setOnTouchListener(this)
        addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                if(s.length<8 && s.isNotEmpty()){
                    error="Password minimal 8 karakter"
                    backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(context, android.R.color.holo_red_dark))
                } else {
                    error = null

                    backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(context, android.R.color.darker_gray))
                }

            }
            override fun afterTextChanged(s: Editable) {}
        })
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private fun updateDrawable() {
        val drawable: Drawable? = if (isPasswordVisible) {
            context.getDrawable(R.drawable.ic_eye_on)

        } else {
            context.getDrawable(R.drawable.ic_eye_off)
        }
        setCompoundDrawablesWithIntrinsicBounds(drawable, null, null, null)
    }

    override fun onTouch(v: View?, event: MotionEvent): Boolean {
        if (compoundDrawables[0] != null) { // [0] adalah drawable di kiri (start)
            val drawableStart = compoundDrawables[0]

            val isEyeIconClicked: Boolean = if (layoutDirection == View.LAYOUT_DIRECTION_RTL) {
                event.x > (width - paddingEnd - drawableStart.intrinsicWidth).toFloat()
            } else {
                event.x < (drawableStart.intrinsicWidth + paddingStart).toFloat()
            }
            if (isEyeIconClicked) {
                when (event.action) {
                    MotionEvent.ACTION_UP -> {
                        isPasswordVisible = !isPasswordVisible
                        updatePasswordVisibility()


                        return true
                    }
                }
            }
        }
        return false
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private fun updatePasswordVisibility() {
        inputType = if (isPasswordVisible) {
            android.text.InputType.TYPE_CLASS_TEXT or android.text.InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
        } else {
            android.text.InputType.TYPE_CLASS_TEXT or android.text.InputType.TYPE_TEXT_VARIATION_PASSWORD
        }
        eyeIcon = if (isPasswordVisible) {
            ContextCompat.getDrawable(context, R.drawable.ic_eye_on)
        } else {
            ContextCompat.getDrawable(context, R.drawable.ic_eye_off)
        }

        setCompoundDrawablesWithIntrinsicBounds(eyeIcon, null, null, null)
        setSelection(length())
    }


}
