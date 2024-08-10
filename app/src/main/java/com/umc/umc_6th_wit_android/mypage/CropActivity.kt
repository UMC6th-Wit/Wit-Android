package com.umc.umc_6th_wit_android.mypage

import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.umc.umc_6th_wit_android.databinding.ActivityCropBinding
import java.io.File
import java.io.FileOutputStream

class CropActivity : AppCompatActivity() {
    private lateinit var binding: ActivityCropBinding
    private lateinit var imageCropView: ImageCropView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCropBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // ImageCropView 설정
        imageCropView = binding.cropImageView


        // 인텐트로 전달된 이미지 URI 받기
        val imageUri = intent.getParcelableExtra<Uri>("imageUri")
        imageUri?.let {
            imageCropView.loadImageFromUrl(it.toString())
        }

        // 크롭 버튼 클릭 리스너
        binding.cropButton.setOnClickListener {
            val croppedBitmap = imageCropView.getCroppedBitmap()
            if (croppedBitmap != null) {

                // 원형 비트맵을 URI로 저장
                val resultUri = saveBitmapToUri(croppedBitmap)
                setResult(RESULT_OK, Intent().apply { data = resultUri })
                finish()
            } else {
                Toast.makeText(this, "이미지를 크롭할 수 없습니다.", Toast.LENGTH_SHORT).show()
            }
        }

    }

    private fun saveBitmapToUri(bitmap: Bitmap): Uri {
        val tempFile = File.createTempFile("cropped", ".png", cacheDir)
        val out = FileOutputStream(tempFile)
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, out)
        out.flush()
        out.close()
        return Uri.fromFile(tempFile)
    }

}
