package com.umc.umc_6th_wit_android.product

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.umc.umc_6th_wit_android.R

class ReviewWriteActivity : AppCompatActivity() {

        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            setContentView(R.layout.activity_review_write)

            val editText: EditText = findViewById(R.id.editTextText)
            val charCount: TextView = findViewById(R.id.charCount)

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
                }
            })
        }
}

