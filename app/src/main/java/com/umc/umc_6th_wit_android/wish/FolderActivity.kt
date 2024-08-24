package com.umc.umc_6th_wit_android.wish

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Rect
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import android.util.Log
import android.view.View
import android.view.ViewTreeObserver
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.content.ContextCompat
import com.umc.umc_6th_wit_android.R
import com.umc.umc_6th_wit_android.databinding.ActivityFolderBinding
import com.umc.umc_6th_wit_android.login.TokenManager

// FolderActivity 클래스 정의: 새로운 폴더를 생성하는 Activity
class FolderActivity : AppCompatActivity(), FolderView {

    // View Binding 객체를 지연 초기화
    lateinit var binding: ActivityFolderBinding
    private lateinit var tokenManager: TokenManager

    // 액티비티 생성 시 호출되는 메서드
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // TokenManager 초기화
        tokenManager = TokenManager(getSharedPreferences("auth_prefs", Context.MODE_PRIVATE))
        // View Binding 초기화
        binding = ActivityFolderBinding.inflate(layoutInflater)
        setContentView(binding.root)
        // 액션 바 숨기기
        supportActionBar?.hide()

        // Intent로부터 데이터 받기
        val selectedItemsList = intent.getSerializableExtra("selected_items") as? ArrayList<WishItem>
        var selectedItemsIdsList: List<Int> = listOf()

        // selectedItemsList가 null이 아닌지 확인한 후 작업 수행
        selectedItemsList?.let {
            // WishItem들의 product_id를 추출하여 리스트에 저장
            selectedItemsIdsList = it.map { item -> item.product_id }
        } ?: run {
            // 리스트가 비어있는 경우 처리할 로직
        }
        Log.d("selectedItemsList", selectedItemsList.toString())
        Log.d("selectedItemsIdsList", selectedItemsIdsList.toString())

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
                val wishService = WishService()
                wishService.setFolderView(this)
                val request = WishListCreateRequest(
                    product_ids = selectedItemsIdsList,
                    folder_name = binding.folderNameEditText.text.toString()
                )
                val accessToken = tokenManager.getAccessToken()
                if (accessToken != null) {
                    wishService.postWishListCreate(accessToken, request)
                }
                // 폴더 이름이 비어있지 않으면 결과를 설정하고 액티비티 종료
                setResult(Activity.RESULT_OK)
                finish()
            }
        }

        // 뒤로 가기 버튼 클릭 리스너 설정
        binding.folderBack.setOnClickListener {
            // 액티비티 종료
            finish()
        }
    }


    //위시리스트 폴더 생성 성공
    override fun onPostWishListCreateSuccess(code: String, result: WishBoardItemResult) {
        Log.d("FOLDER-RESPONSE", result.folders.toString())
    }

    //위시리스트 폴더 생성 실패
    override fun onPostWishListCreateFailure(code: String, message: String) {
        Log.d("FOLDER-RESPONSE", message)
    }
}
