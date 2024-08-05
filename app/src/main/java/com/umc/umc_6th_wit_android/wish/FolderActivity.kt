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

// FolderActivity 클래스 정의: 새로운 폴더를 생성하는 Activity
class FolderActivity : AppCompatActivity() {

    // View Binding 객체를 지연 초기화
    lateinit var binding: ActivityFolderBinding

    // 액티비티 생성 시 호출되는 메서드
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // View Binding 초기화
        binding = ActivityFolderBinding.inflate(layoutInflater)
        setContentView(binding.root)
        // 액션 바 숨기기
        supportActionBar?.hide()

        // EditText에 텍스트 변경 리스너 추가
        binding.folderNameEditText.addTextChangedListener(object : TextWatcher {
            // 텍스트 변경 전 호출되는 메서드 (사용하지 않음)
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            // 텍스트 변경 시 호출되는 메서드
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                // 텍스트가 비어있으면 버튼 비활성화 및 스타일 변경
                if (s.isNullOrEmpty()) {
                    binding.createFolderButton.isEnabled = false
                    binding.createFolderButton.setBackgroundResource(R.drawable.button_folder_background_disabled)
                    binding.createFolderButton.setTextColor(ContextCompat.getColor(this@FolderActivity, R.color.folderGray))
                } else {
                    // 텍스트가 있으면 버튼 활성화 및 스타일 변경
                    binding.createFolderButton.isEnabled = true
                    binding.createFolderButton.setBackgroundResource(R.drawable.button_folder_background_enabled)
                    binding.createFolderButton.setTextColor(ContextCompat.getColor(this@FolderActivity, R.color.folderBlack))
                }
            }

            // 텍스트 변경 후 호출되는 메서드 (사용하지 않음)
            override fun afterTextChanged(s: Editable?) {}
        })

        // 폴더 생성 버튼 클릭 리스너 설정
        binding.createFolderButton.setOnClickListener {
            // EditText에서 폴더 이름 가져오기
            val folderName = binding.folderNameEditText.text.toString()
            if (folderName.isNotEmpty()) {
                // 폴더 이름이 비어있지 않으면 결과를 설정하고 액티비티 종료
                val resultIntent = Intent()
                resultIntent.putExtra("folderName", folderName)
                setResult(Activity.RESULT_OK, resultIntent)
                finish()
            }
        }

        // 뒤로 가기 버튼 클릭 리스너 설정
        binding.folderBack.setOnClickListener {
            // 액티비티 종료
            finish()
        }

        // 키보드가 나타나거나 사라질 때 호출되는 리스너 설정
        binding.root.viewTreeObserver.addOnGlobalLayoutListener {
            val rect = Rect()
            // 화면의 가시 영역을 rect 객체에 저장
            binding.root.getWindowVisibleDisplayFrame(rect)
            val screenHeight = binding.root.rootView.height
            val keypadHeight = screenHeight - rect.bottom
            // 키보드가 열렸을 때
            if (keypadHeight > screenHeight * 0.15) { // 키보드가 열렸을 때 (화면의 15% 이상 차지할 때)
                binding.createFolderButton.translationY = -keypadHeight.toFloat()
            } else { // 키보드가 닫혔을 때
                binding.createFolderButton.translationY = 0f
            }
        }
    }
}
