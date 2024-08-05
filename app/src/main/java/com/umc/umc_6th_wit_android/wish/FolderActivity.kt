package com.umc.umc_6th_wit_android.wish

import android.app.Activity
import android.content.Intent
import android.graphics.Rect
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.ViewTreeObserver
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.umc.umc_6th_wit_android.R
import com.umc.umc_6th_wit_android.databinding.ActivityFolderBinding

class FolderActivity : AppCompatActivity() {

    lateinit var binding: ActivityFolderBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFolderBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()

        binding.folderNameEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (s.isNullOrEmpty()) {
                    binding.createFolderButton.isEnabled = false
                    binding.createFolderButton.setBackgroundResource(R.drawable.button_folder_background_disabled)
                    binding.createFolderButton.setTextColor(ContextCompat.getColor(this@FolderActivity, R.color.folderGray))
                } else {
                    binding.createFolderButton.isEnabled = true
                    binding.createFolderButton.setBackgroundResource(R.drawable.button_folder_background_enabled)
                    binding.createFolderButton.setTextColor(ContextCompat.getColor(this@FolderActivity, R.color.folderBlack))
                }
            }

            override fun afterTextChanged(s: Editable?) {}
        })

        binding.createFolderButton.setOnClickListener {
            val folderName = binding.folderNameEditText.text.toString()
            if (folderName.isNotEmpty()) {
                val resultIntent = Intent()
                resultIntent.putExtra("folderName", folderName)
                setResult(Activity.RESULT_OK, resultIntent)
                finish()
            }
        }

        binding.folderBack.setOnClickListener{
            finish()
        }

        // 키보드가 나타나거나 사라질 때 호출되는 리스너 설정
        binding.root.viewTreeObserver.addOnGlobalLayoutListener {
            val rect = Rect()
            binding.root.getWindowVisibleDisplayFrame(rect)
            val screenHeight = binding.root.rootView.height
            val keypadHeight = screenHeight - rect.bottom
            if (keypadHeight > screenHeight) { // 키보드가 열렸을 때
                binding.createFolderButton.translationY = -keypadHeight.toFloat()
            } else { // 키보드가 닫혔을 때
                binding.createFolderButton.translationY = 0f
            }
        }
    }
}
