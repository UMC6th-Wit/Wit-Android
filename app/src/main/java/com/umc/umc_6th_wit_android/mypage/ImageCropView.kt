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
                    imageView.setImageBitmap(scaleBitmapToFitView(resource))
                    invalidate()
                }

                override fun onLoadCleared(placeholder: android.graphics.drawable.Drawable?) {
                }
            })
    }

    private fun scaleBitmapToFitView(bitmap: Bitmap): Bitmap {
        val viewWidth = imageView.width.toFloat()
        val viewHeight = imageView.height.toFloat()

        val bitmapWidth = bitmap.width.toFloat()
        val bitmapHeight = bitmap.height.toFloat()

        // 비율 계산: ImageView의 크기에 맞추어 이미지를 확대
        val widthRatio = viewWidth / bitmapWidth
        val heightRatio = viewHeight / bitmapHeight

        // 이미지가 작을 경우 강제로 ImageView의 크기에 맞추어 확대
        val scale = maxOf(widthRatio, heightRatio) // 더 큰 비율로 확대하여 이미지가 꽉 차게 함

        // 확대된 크기 계산
        val scaledWidth = (bitmapWidth * scale).toInt()
        val scaledHeight = (bitmapHeight * scale).toInt()

        // 로그 출력: 크기 확인
        Log.d("ImageCropView", "Scaled Bitmap Size: width=$scaledWidth, height=$scaledHeight")

        // 중앙에 이미지 배치 (확대된 이미지)
        val output = Bitmap.createBitmap(scaledWidth, scaledHeight, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(output)
        val paint = Paint().apply { isAntiAlias = true }

        // 배경 초기화
        canvas.drawARGB(255, 0, 0, 0) // 검은색으로 초기화
        canvas.drawBitmap(bitmap, null, RectF(0f, 0f, scaledWidth.toFloat(), scaledHeight.toFloat()), paint)

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

                    // 가로 길이를 기준으로 1:1 비율 유지
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

        // 1:1 비율을 유지하기 위해 높이도 동일하게 조정
        val newHeight = newWidth // 정사각형 비율 유지

        if (newWidth in minWidth..imageView.width.toFloat() && newHeight in minHeight..imageView.height.toFloat()) {
            cropRect.left = newLeft
            cropRect.top = cropRect.bottom - newHeight // 1:1 비율 유지
            invalidate()
        }

        updateLastTouch(touchX, touchY)
    }

    private fun resizeRight(change: Float, touchX: Float? = null, touchY: Float? = null) {
        val newRight = cropRect.right + change
        val newWidth = cropRect.width() + change

        // 1:1 비율을 유지하기 위해 높이도 동일하게 조정
        val newHeight = newWidth // 정사각형 비율 유지

        if (newWidth in minWidth..imageView.width.toFloat() && newHeight in minHeight..imageView.height.toFloat()) {
            cropRect.right = newRight
            cropRect.bottom = cropRect.top + newHeight // 1:1 비율 유지
            invalidate()
        }

        updateLastTouch(touchX, touchY)
    }

    private fun resizeTop(change: Float, touchX: Float? = null, touchY: Float? = null) {
        val newTop = cropRect.top + change
        val newHeight = cropRect.height() - change

        // 1:1 비율을 유지하기 위해 너비도 동일하게 조정
        val newWidth = newHeight // 정사각형 비율 유지

        if (newHeight in minHeight..imageView.height.toFloat() && newWidth in minWidth..imageView.width.toFloat()) {
            cropRect.top = newTop
            cropRect.left = cropRect.right - newWidth // 1:1 비율 유지
            invalidate()
        }

        updateLastTouch(touchX, touchY)
    }

    private fun resizeBottom(change: Float, touchX: Float? = null, touchY: Float? = null) {
        val newBottom = cropRect.bottom + change
        val newHeight = cropRect.height() + change

        // 1:1 비율을 유지하기 위해 너비도 동일하게 조정
        val newWidth = newHeight // 정사각형 비율 유지

        if (newHeight in minHeight..imageView.height.toFloat() && newWidth in minWidth..imageView.width.toFloat()) {
            cropRect.bottom = newBottom
            cropRect.right = cropRect.left + newWidth // 1:1 비율 유지
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
            // 이미지의 실제 크기와 ImageView에서의 크기 차이를 계산
            val scaleX = it.width.toFloat() / imageView.width.toFloat()
            val scaleY = it.height.toFloat() / imageView.height.toFloat()

            // 사용된 스케일 값을 적용
            val scale = minOf(scaleX, scaleY)

            // 이미지가 ImageView에서 중앙에 맞춰 보여지는 부분을 반영한 크롭 영역 계산
            val displayedImageWidth = (imageView.width * scaleX).toInt()
            val displayedImageHeight = (imageView.height * scaleY).toInt()

            // 크롭 영역 설정 (중앙 정렬된 정사각형 영역)
            val cropSize = minOf(displayedImageWidth, displayedImageHeight).toInt()
            val left = ((it.width - cropSize) / 2).coerceAtLeast(0)
            val top = ((it.height - cropSize) / 2).coerceAtLeast(0)

            if (cropSize > 0) {
                // 크롭된 이미지를 생성 (정사각형으로 설정)
                val croppedBitmap = Bitmap.createBitmap(it, left, top, cropSize, cropSize)

                // 100dp를 픽셀로 변환
                val widthInPx = 100.dpToPx(context)
                val heightInPx = 100.dpToPx(context)

                // 크롭된 이미지를 100dp x 100dp로 리사이즈
                val resizedBitmap = Bitmap.createScaledBitmap(croppedBitmap, widthInPx, heightInPx, true)

                // 로그 출력: 리사이즈된 이미지 크기 확인
                Log.d("ImageCropView", "Resized Bitmap Size: width=${resizedBitmap.width}, height=${resizedBitmap.height}")

                return resizedBitmap
            } else {
                Log.e("ImageCropView", "Invalid crop size")
                return null
            }
        }
        return null
    }




}