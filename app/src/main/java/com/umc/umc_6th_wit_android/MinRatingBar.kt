package com.umc.umc_6th_wit_android

import android.content.Context
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatRatingBar

class MinRatingBar @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : AppCompatRatingBar(context, attrs, defStyleAttr) {

    private var minRating: Float = 0f

    init {
        context.theme.obtainStyledAttributes(
            attrs,
            R.styleable.MinRatingBar,
            0, 0).apply {

            try {
                minRating = getFloat(R.styleable.MinRatingBar_minRating, 0f)
            } finally {
                recycle()
            }
        }

        setOnRatingBarChangeListener { _, rating, _ ->
            if (rating < minRating) {
                this.rating = minRating
            }
        }
    }

    fun setMinRating(minRating: Float) {
        this.minRating = minRating
    }
}
