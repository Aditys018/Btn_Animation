package com.aditys.btn_animation

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.os.Parcel
import android.os.Parcelable
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.animation.doOnEnd
import com.aditys.btn_animation.R

class CustomToggleButton @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private var isOn = false
    private var sliderPosition = 0f
    private val paint = Paint(Paint.ANTI_ALIAS_FLAG)

    private var onColor = Color.GREEN
    private var offColor = Color.GRAY
    private var sliderColor = Color.WHITE
    private var textColor = Color.WHITE

    val isToggleOn: Boolean
        get() = isOn

    init {
        context.theme.obtainStyledAttributes(attrs, R.styleable.CustomToggleButton, 0, 0).apply {
            try {
                onColor = getColor(R.styleable.CustomToggleButton_onColor, Color.GREEN)
                offColor = getColor(R.styleable.CustomToggleButton_offColor, Color.GRAY)
                sliderColor = getColor(R.styleable.CustomToggleButton_sliderColor, Color.WHITE)
                textColor = getColor(R.styleable.CustomToggleButton_textColor, Color.WHITE)
            } finally {
                recycle()
            }
        }
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        val width = width.toFloat()
        val height = height.toFloat()


        paint.color = if (isOn) onColor else offColor
        canvas.drawRoundRect(0f, 0f, width, height, height / 2, height / 2, paint)


        if (isOn && onColor == Color.BLACK) {
            paint.color = Color.WHITE
        } else if (!isOn && offColor == Color.BLACK) {
            paint.color = Color.WHITE
        } else {
            paint.color = textColor
        }


        val text = if (isOn) "ON" else "OFF"
        paint.textSize = height / 3f
        val textWidth = paint.measureText(text)


        val textX = (width - textWidth) / 2
        val textY = (height + paint.textSize) / 2 - 10

        canvas.drawText(text, textX, textY, paint)


        val sliderRadius = height / 2 - 10
        val sliderX = if (sliderPosition > 0) sliderPosition else if (isOn) width - height / 2 else height / 2
        paint.color = sliderColor
        canvas.drawCircle(sliderX, height / 2, sliderRadius, paint)


        val themeText = if (isOn) "Dark Theme" else "Light Theme"
        paint.color = textColor
        paint.textSize = height / 5f
        val themeTextWidth = paint.measureText(themeText)


        val themeTextX = (width - themeTextWidth) / 2


        val themeTextY = height + paint.textSize + 30

        canvas.drawText(themeText, themeTextX, themeTextY, paint)
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        if (event.action == MotionEvent.ACTION_UP) {
            isOn = !isOn
            animateSlider()


            if (isOn) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            }
        }
        return true
    }

    private fun animateSlider() {
        val start = if (isOn) height / 2f else width - height / 2f
        val end = if (isOn) width - height / 2f else height / 2f

        val animator = ValueAnimator.ofFloat(start, end)
        animator.duration = 300
        animator.addUpdateListener {
            sliderPosition = it.animatedValue as Float
            invalidate()
        }
        animator.doOnEnd { sliderPosition = 0f }
        animator.start()
    }

    override fun onSaveInstanceState(): Parcelable? {
        val superState = super.onSaveInstanceState()
        return SavedState(superState, isOn)
    }

    override fun onRestoreInstanceState(state: Parcelable?) {
        if (state is SavedState) {
            super.onRestoreInstanceState(state.superState)
            isOn = state.isOn


            if (isOn) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            }
        } else {
            super.onRestoreInstanceState(state)
        }
    }

    private class SavedState : BaseSavedState {
        var isOn: Boolean = false

        constructor(superState: Parcelable?, isOn: Boolean) : super(superState) {
            this.isOn = isOn
        }

        private constructor(parcel: Parcel) : super(parcel) {
            isOn = parcel.readInt() == 1
        }

        override fun writeToParcel(out: Parcel, flags: Int) {
            super.writeToParcel(out, flags)
            out.writeInt(if (isOn) 1 else 0)
        }

        companion object {
            @JvmField
            val CREATOR: Parcelable.Creator<SavedState> = object : Parcelable.Creator<SavedState> {
                override fun createFromParcel(parcel: Parcel): SavedState {
                    return SavedState(parcel)
                }

                override fun newArray(size: Int): Array<SavedState?> {
                    return arrayOfNulls(size)
                }
            }
        }
    }
}
