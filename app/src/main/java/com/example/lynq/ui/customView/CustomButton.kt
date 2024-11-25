package com.example.lynq.ui.customView

import android.content.Context
import android.graphics.Canvas
import android.graphics.drawable.Drawable
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import android.view.Gravity
import android.widget.Toast
import androidx.appcompat.widget.AppCompatButton
import androidx.core.content.ContextCompat
import com.example.lynq.R

class CustomButton : AppCompatButton {
        constructor(context: Context) : super(context)
        constructor(context: Context, attrs: AttributeSet) : super(context, attrs)

        private var txtColor: Int = 0
        private var enabledBackground: Drawable
        private var disabledBackground: Drawable
        init {
            txtColor = ContextCompat.getColor(context, android.R.color.background_light)
            enabledBackground = ContextCompat.getDrawable(context, R.drawable.bg_button) as Drawable
            disabledBackground = ContextCompat.getDrawable(context, R.drawable.bg_button_disable) as Drawable
            addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    if (s != null && s.length < 8) {
                        Toast.makeText(context, "Password harus terdiri dari minimal 8 karakter", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun afterTextChanged(s: Editable?) {
                    if (s != null && s.length < 8) {
                        Toast.makeText(context, "Password harus terdiri dari minimal 8 karakter", Toast.LENGTH_SHORT).show()
                    }
                }
            })

        }


        override fun onDraw(canvas: Canvas) {
            super.onDraw(canvas)
            background = if(isEnabled) enabledBackground else disabledBackground
            setTextColor(txtColor)
            textSize = 12f
            gravity = Gravity.CENTER

        }
    }