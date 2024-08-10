package com.umc.umc_6th_wit_android.mypage

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.RectF
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.widget.ImageView
import androidx.constraintlayout.widget.ConstraintLayout
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.umc.umc_6th_wit_android.R

class ImageCropView(context: Context, attrs: AttributeSet) : ConstraintLayout(context, attrs) {
    private var bitmap: Bitmap? = null
    private var cropRect: RectF = RectF(100f, 100f, 400f, 400f)
    private var lastTouchX: Float = 0f
    private var lastTouchY: Float = 0f
    private val borderSize = 50f
    private val paint = Paint().apply {
        color = context.getColor(R.color.white)
        style = Paint.Style.STROKE
        strokeWidth = 2.dpToPx(context).toFloat()
    }
    private val imageView: ImageView
    private val minHeight = 100.dpToPx(context).toFloat()
    private val minWidth = 100.dpToPx(context).toFloat()

    fun Int.dpToPx(context: Context): Int {
        val density = context.resources.displayMetrics.density
        return (this * density).toInt()
    }

    init {
        imageView = ImageView(context).apply {
            layoutParams = LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT)
            scaleType = ImageView.ScaleType.FIT_CENTER // 이미지 비율 유지
        }
        addView(imageView)

        // 초기 크롭 영역 설정
        post {
            val width = imageView.width.toFloat()
            val height = imageView.height.toFloat()
            val size = minOf(width, height) // 최대 크기로 설정

            cropRect = RectF(
                (width - size) / 2,
                (height - size) / 2,
                (width + size) / 2,
                (height + size) / 2
            )
            invalidate()
        }
    }


    fun loadImageFromUrl(url: String) {
        Glide.with(context)
            .asBitmap()
            .load(url)
            .into(object : CustomTarget<Bitmap>() {
                override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
                    bitmap = resource
                    imageView.setImageBitmap(scaleBitmapToCenterInside(resource))
                    invalidate()
                }

                override fun onLoadCleared(placeholder: android.graphics.drawable.Drawable?) {
                }
            })
    }

    private fun scaleBitmapToCenterInside(bitmap: Bitmap): Bitmap {
        val viewWidth = imageView.width.toFloat()
        val viewHeight = imageView.height.toFloat()

        val bitmapWidth = bitmap.width.toFloat()
        val bitmapHeight = bitmap.height.toFloat()

        // 비율 계산
        val widthRatio = viewWidth / bitmapWidth
        val heightRatio = viewHeight / bitmapHeight
        val scale = minOf(widthRatio, heightRatio)

        // 새로운 크기 계산
        val scaledWidth = (bitmapWidth * scale).toInt()
        val scaledHeight = (bitmapHeight * scale).toInt()

        // 중앙에 이미지 배치
        val left = (viewWidth - scaledWidth) / 2
        val top = (viewHeight - scaledHeight) / 2

        // 새로운 비트맵 생성
        val output =
            Bitmap.createBitmap(viewWidth.toInt(), viewHeight.toInt(), Bitmap.Config.ARGB_8888)
        val canvas = Canvas(output)
        val paint = Paint().apply {
            isAntiAlias = true
        }

        // 배경 초기화
        canvas.drawARGB(255, 0, 0, 0) // 완전 검은색으로 초기화
        canvas.drawBitmap(
            bitmap,
            null,
            RectF(left, top, left + scaledWidth, top + scaledHeight),
            paint
        )

        return output
    }


    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
    }

    override fun dispatchDraw(canvas: Canvas) {
        super.dispatchDraw(canvas)
        canvas.drawRect(cropRect, paint)
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        event?.let {
            val touchX = event.x
            val touchY = event.y

            when (it.action) {
                MotionEvent.ACTION_DOWN -> {
                    lastTouchX = touchX
                    lastTouchY = touchY
                    if (cropRect.contains(touchX, touchY)) {
                        return true
                    }
                }

                MotionEvent.ACTION_MOVE -> {
                    val offsetX = touchX - lastTouchX
                    val offsetY = touchY - lastTouchY

                    // 가로 길이를 기준으로 1:1 비율 조절
                    if (lastTouchX in cropRect.left..cropRect.left + borderSize) {
                        resizeLeft(offsetX, touchX, touchY)
                        return true
                    } else if (lastTouchX in cropRect.right - borderSize..cropRect.right) {
                        resizeRight(offsetX, touchX, touchY)
                        return true
                    } else if (lastTouchY in cropRect.top..cropRect.top + borderSize) {
                        resizeTop(offsetY, touchX, touchY)
                        return true
                    } else if (lastTouchY in cropRect.bottom - borderSize..cropRect.bottom) {
                        resizeBottom(offsetY, touchX, touchY)
                        return true
                    }

                    // 드래그로 이동
                    if (cropRect.contains(lastTouchX, lastTouchY)) {
                        moveRect(offsetX, offsetY, touchX, touchY)
                        return true
                    }
                }

                MotionEvent.ACTION_UP -> {
                    return true
                }
            }
        }
        return super.onTouchEvent(event)
    }

    private fun resizeLeft(change: Float, touchX: Float? = null, touchY: Float? = null) {
        val newLeft = cropRect.left + change
        val newWidth = cropRect.width() - change
        val newHeight = newWidth // 1:1 비율 유지

        if (newWidth in minWidth..imageView.width.toFloat() && newHeight in minHeight..imageView.height.toFloat()) {
            cropRect.left = newLeft
            cropRect.top = cropRect.bottom - newHeight // 비율 유지
            invalidate()
        }

        updateLastTouch(touchX, touchY)
    }

    private fun resizeRight(change: Float, touchX: Float? = null, touchY: Float? = null) {
        val newRight = cropRect.right + change
        val newWidth = cropRect.width() + change
        val newHeight = newWidth // 1:1 비율 유지

        if (newWidth in minWidth..imageView.width.toFloat() && newHeight in minHeight..imageView.height.toFloat()) {
            cropRect.right = newRight
            cropRect.bottom = cropRect.top + newHeight // 비율 유지
            invalidate()
        }

        updateLastTouch(touchX, touchY)
    }

    private fun resizeTop(change: Float, touchX: Float? = null, touchY: Float? = null) {
        val newTop = cropRect.top + change
        val newHeight = cropRect.height() - change
        val newWidth = newHeight // 1:1 비율 유지

        if (newHeight in minHeight..imageView.height.toFloat() && newWidth in minWidth..imageView.width.toFloat()) {
            cropRect.top = newTop
            cropRect.left = cropRect.right - newWidth // 비율 유지
            invalidate()
        }

        updateLastTouch(touchX, touchY)
    }

    private fun resizeBottom(change: Float, touchX: Float? = null, touchY: Float? = null) {
        val newBottom = cropRect.bottom + change
        val newHeight = cropRect.height() + change
        val newWidth = newHeight // 1:1 비율 유지

        if (newHeight in minHeight..imageView.height.toFloat() && newWidth in minWidth..imageView.width.toFloat()) {
            cropRect.bottom = newBottom
            cropRect.right = cropRect.left + newWidth // 비율 유지
            invalidate()
        }

        updateLastTouch(touchX, touchY)
    }

    private fun moveRect(offsetX: Float, offsetY: Float, touchX: Float, touchY: Float) {
        val newLeft = cropRect.left + offsetX
        val newTop = cropRect.top + offsetY
        val newRight = cropRect.right + offsetX
        val newBottom = cropRect.bottom + offsetY

        // 이미지 경계를 벗어나지 않도록 조절
        val adjustedLeft = newLeft.coerceAtLeast(0f).coerceAtMost(imageView.width - cropRect.width())
        val adjustedTop = newTop.coerceAtLeast(0f).coerceAtMost(imageView.height - cropRect.height())

        cropRect.offsetTo(adjustedLeft, adjustedTop)
        invalidate()

        updateLastTouch(touchX, touchY)
    }

    private fun updateLastTouch(touchX: Float?, touchY: Float?) {
        if (touchX != null && touchY != null) {
            lastTouchX = touchX
            lastTouchY = touchY
        }
    }



    fun getCroppedBitmap(): Bitmap? {
        bitmap?.let {
            val scaleX = it.width.toFloat() / imageView.width.toFloat()
            val scaleY = it.height.toFloat() / imageView.height.toFloat()

            try {
                // 크롭 영역을 원본 이미지의 비율로 변환
                val left = (cropRect.left * scaleX).toInt()
                val top = (cropRect.top * scaleY).toInt()
                val right = (cropRect.right * scaleX).toInt()
                val bottom = (cropRect.bottom * scaleY).toInt()

                // 정사각형 비율 유지 확인
                val size = Math.min(right - left, bottom - top)

                return Bitmap.createBitmap(it, left, top, size, size)
            } catch (e: Exception) {
                Log.e("confirm getCroppedBitmap Error", "${e.message}")
                return null
            }
        }
        return null
    }



}