package com.umc.umc_6th_wit_android.product

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import androidx.appcompat.widget.AppCompatRatingBar
import com.umc.umc_6th_wit_android.R
import com.umc.umc_6th_wit_android.mypage.CropActivity

class ReviewWriteActivity : AppCompatActivity() {

        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            setContentView(R.layout.activity_review_write)

            val editText: EditText = findViewById(R.id.editTextText)
            val rating: AppCompatRatingBar = findViewById(R.id.review_rate_btn)
            val charCount: TextView = findViewById(R.id.charCount)
            val photo: ImageView = findViewById(R.id.review_input_photo_iv)
            val createReviewBtn: AppCompatButton = findViewById(R.id.review_create_btn)

            rating.setOnRatingBarChangeListener { ratingBar, rating, fromUser ->
                if (fromUser) {
                    if(charCount.text.toString().toInt() > 0){
                        //photo 버튼 활성화 표현 작성해야함
                        createReviewBtn.visibility = View.VISIBLE
                    } else {
                        //photo 버튼 비활성화 표현 작성해야함
                        createReviewBtn.visibility = View.GONE
                    }
                }
            }

            //val maxLength = 1000  // 최대 글자 수 설정
            editText.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                    // 텍스트 변경 되기 전 호출
                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    // 텍스트 변경될 때마다 호출
                    val currentLength = s?.length ?: 0
                    charCount.text = "$currentLength"
                }

                override fun afterTextChanged(s: Editable?) {
                    // 텍스트 변경 후에 호출
                    if(rating.rating > 0.0 && charCount.text.toString().toInt() > 0){
                        //photo 버튼 활성화 표현 작성해야함
                        createReviewBtn.visibility = View.VISIBLE
                    } else {
                        //photo 버튼 비활성화 표현 작성해야함
                        createReviewBtn.visibility = View.GONE
                    }
                }
            })

            createReviewBtn.setOnClickListener {
                val intent = Intent(this@ReviewWriteActivity, ReviewOnlyActivity::class.java)
                intent.putExtra("id", 1)
                startActivity(intent)
            }

            photo.setOnClickListener{
                if(rating.rating > 0.0 && charCount.text.toString().toInt() > 0){
                    val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                    imagePickerLauncher.launch(intent)
                }
            }
        }

    // 갤러리에서 이미지 선택 후 결과 처리
    private val imagePickerLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            result.data?.data?.let { uri ->

            }
        }
    }
}

