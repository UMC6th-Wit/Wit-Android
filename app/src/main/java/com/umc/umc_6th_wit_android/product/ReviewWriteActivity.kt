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
import com.umc.umc_6th_wit_android.data.remote.product.ContentResponse
import com.umc.umc_6th_wit_android.data.remote.product.ProductService
import com.umc.umc_6th_wit_android.data.remote.product.RatingResponse
import com.umc.umc_6th_wit_android.data.remote.product.ReviewCreationResult
import com.umc.umc_6th_wit_android.databinding.ActivityReviewWriteBinding
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody


class ReviewWriteActivity : AppCompatActivity(), ReviewCreationView {

    lateinit var binding: ActivityReviewWriteBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityReviewWriteBinding.inflate(layoutInflater)
        setContentView(binding.root)

            val editText: EditText = findViewById(R.id.editTextText)
            val rating: AppCompatRatingBar = findViewById(R.id.review_rate_btn)
            val charCount: TextView = findViewById(R.id.charCount)
            val photo: ImageView = findViewById(R.id.review_input_photo_iv)
            val createReviewBtn: AppCompatButton = findViewById(R.id.review_create_btn)
            val id: Int = intent.getIntExtra("id", 1)

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

            binding.reviewCreateBtn.setOnClickListener {
                Log.d("CREATEBTN", id.toString())
                val intent = Intent(this@ReviewWriteActivity, ReviewOnlyActivity::class.java)
                intent.putExtra("id", id)
                startActivity(intent)
            }

            photo.setOnClickListener{
                if(rating.rating > 0.0 && charCount.text.toString().toInt() > 0){
                    val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                    imagePickerLauncher.launch(intent)
                }
            }

            createReviewBtn.setOnClickListener{
                val productService = ProductService(this@ReviewWriteActivity)
                productService.setReviewCreationView(this)
                Log.d("rating", rating.rating.toString())
                Log.d("editText", editText.text.toString())
                val rating = RatingResponse(
                    rating.rating.toString()
                )
                val content = ContentResponse(
                    editText.text.toString()
                )
                val emptyMultipartList: List<MultipartBody.Part> = emptyList()
                rating.rating?.let { it1 -> productService.createReview(1, it1.toDouble(), RequestBody.create("text/plain".toMediaTypeOrNull(), editText.text.toString()), emptyMultipartList) }
            }
        }

    // 갤러리에서 이미지 선택 후 결과 처리
    private val imagePickerLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            result.data?.let { data ->
                val clipData = data.clipData
                if (clipData != null) {
                    // 사용자가 여러 이미지를 선택한 경우
                    for (i in 0 until clipData.itemCount) {
                        val imageUri = clipData.getItemAt(i).uri
                        // 각 이미지의 URI를 처리
                        handleSelectedImage(imageUri)
                    }
                } else {
                    // 사용자가 단일 이미지를 선택한 경우
                    data.data?.let { uri ->
                        handleSelectedImage(uri)
                    }
                }
            }
        }
    }

    // 이미지 URI를 처리하는 함수 예시
    private fun handleSelectedImage(uri: Uri) {
        // 선택된 이미지 URI를 처리하는 코드 (예: 이미지뷰에 표시하거나 서버에 업로드)
        Log.d("ImagePicker", "Selected image URI: $uri")
    }

    override fun onPostReviewCreationSuccess(code: String, result: ReviewCreationResult) {
        if(code.equals("201")){
            finish()
        }
    }

    override fun onPostReviewCreationFailure(code: String, message: String) {
        TODO("Not yet implemented")
    }
}

