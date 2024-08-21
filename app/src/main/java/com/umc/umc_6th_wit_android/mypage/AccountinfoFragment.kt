package com.umc.umc_6th_wit_android.mypage

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.ImageView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.bumptech.glide.Glide
import com.umc.umc_6th_wit_android.MainActivity
import com.umc.umc_6th_wit_android.R
import com.umc.umc_6th_wit_android.databinding.FragmentAccountinfoBinding
import com.umc.umc_6th_wit_android.login.TokenManager
import com.umc.umc_6th_wit_android.network.TokenRetrofitManager
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.Calendar

class AccountinfoFragment : Fragment() {

    private lateinit var binding: FragmentAccountinfoBinding
    private lateinit var tokenManager: TokenManager
    private var cachedUserInfo: UserResult? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAccountinfoBinding.inflate(inflater, container, false)
        (activity as? MainActivity)?.setBottomNavigationViewVisibility(false)

        // 초기에는 TextView들을 보이지 않게 설정
        binding.accountinfoNameEt.visibility = View.INVISIBLE
        binding.accountinfoNicknameEt.visibility = View.INVISIBLE
        binding.accountinfoBirthTv.visibility = View.INVISIBLE

        // 생년월일 수정 결과 수신
        parentFragmentManager.setFragmentResultListener("birthdateUpdate", viewLifecycleOwner) { _, bundle ->
            val updatedBirthdate = bundle.getString("updatedBirthdate")
            if (updatedBirthdate != null) {
                // 생년월일을 캐시에 반영
                cachedUserInfo?.birth = updatedBirthdate
                binding.accountinfoBirthTv.text = formatBirthdate(updatedBirthdate)
                Log.d("AccountinfoFramgnet","생년월일 수정 결과 수신,  updatedBirthdate: $updatedBirthdate")
                // 서버로 다시 updateUserInfo 호출하여 저장
                updateUserInfo()
            }
        }

        // TokenManager 초기화
        tokenManager = TokenManager(requireContext().getSharedPreferences("auth_prefs", Context.MODE_PRIVATE))

        // 유저 정보 조회
        fetchUserInfo()

        // UI 초기화
        setupUI()

        return binding.root
    }

    // 유저 정보 조회 API 호출
    private fun fetchUserInfo() {
        Log.d("AccountinfoFragment", "fetchUserInfo 실행")

        // TokenManager에서 저장된 액세스 토큰을 가져옴
        val accessToken = tokenManager.getAccessToken()
        Log.d("Accountinfo", "accessToken: $accessToken")

        if (accessToken != null) {
            // Retrofit 인스턴스 생성 및 UserService 호출
            val retrofit = TokenRetrofitManager(requireContext())
            val userService = retrofit.create(UserService::class.java)

            val call = userService.getUserInfo("Bearer $accessToken")
            call.enqueue(object : Callback<UserInfoResponse> {
                override fun onResponse(call: Call<UserInfoResponse>, response: Response<UserInfoResponse>) {
                    if (response.isSuccessful && response.body()?.isSuccess == true) {
                        // 유저 정보 조회 성공
                        Log.d("AccountinfoFragment", "유저 정보 조회 성공")
                        val userInfo = response.body()?.result
                        userInfo?.let {
                            cachedUserInfo = it  // 유저 정보 캐싱
                            bindUserInfoToUI(it)
                        }

                    } else {
                        Log.d("AccountinfoFragment", "Error: ${response.body()?.message}")
                    }
                }

                override fun onFailure(call: Call<UserInfoResponse>, t: Throwable) {
                    Log.d("AccountinfoFragment", "API 호출 실패: ${t.message}")
                }
            })
        } else {
            Log.d("AccountinfoFragment", "액세스 토큰이 없습니다.")
        }
    }

    // 유저 정보를 UI에 반영하는 메서드
    private fun bindUserInfoToUI(userResult: UserResult?) {
        Log.d("AccountinfoFragment", "bindUserInfoToUI 실행")
        userResult?.let {
            binding.accountinfoNameEt.setText(it.username)
            binding.accountinfoNicknameEt.setText(it.usernickname)

            // 성별 설정
            if (it.gender == "male") {
                binding.accountinfoMaleRb.isChecked = true
            } else if (it.gender == "female") {
                binding.accountinfoFemaleRb.isChecked = true
            }

            // 생년월일 설정
            val birthdate = formatBirthdate(it.birth)
            binding.accountinfoBirthTv.text = birthdate

            // 데이터 로드가 완료되면 View를 보이게 설정
            binding.accountinfoNameEt.visibility = View.VISIBLE
            binding.accountinfoNicknameEt.visibility = View.VISIBLE
            binding.accountinfoBirthTv.visibility = View.VISIBLE
        }
    }

    // 생년월일 변환 함수
    private fun formatBirthdate(birth: String): String {
        val year = birth.substring(0, 4).padStart(4, '0')
        val month = birth.substring(4, 6).padStart(2, '0')
        val day = birth.substring(6, 8).padStart(2, '0')
        return "$year.$month.$day"
    }

    // UI 관련 초기화 로직
    private fun setupUI() {
        // 이름 수정
        binding.accountinfoNameEt.setOnEditorActionListener { v, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_DONE || (event != null && event.keyCode == KeyEvent.KEYCODE_ENTER && event.action == KeyEvent.ACTION_DOWN)) {
                updateUserInfo()
                hideKeyboardAndClearFocus(binding.accountinfoNameEt)
                true
            } else {
                false
            }
        }

        // 닉네임 수정
        binding.accountinfoNicknameEt.setOnEditorActionListener { v, actionId, event ->
            showKeyboardAndRequestFocus(binding.accountinfoNameEt)
            if (actionId == EditorInfo.IME_ACTION_DONE || (event != null && event.keyCode == KeyEvent.KEYCODE_ENTER && event.action == KeyEvent.ACTION_DOWN)) {
                updateUserInfo()
                hideKeyboardAndClearFocus(binding.accountinfoNicknameEt)
                true
            } else {
                false
            }
        }

        // 성별 수정
        binding.accountinfoRg.setOnCheckedChangeListener { _, _ ->
            updateUserInfo()
        }

        // 뒤로 가기 버튼 클릭 리스너 설정
        binding.accountinfoBackIv.setOnClickListener {
            navigateToMypage()
        }

        // 생년월일 수정 버튼
        binding.accountinfoBirthLinear.setOnClickListener {
            (activity as? MainActivity)?.setBottomNavigationViewVisibility(false)
            val birthdateFragment = BirthdateFragment()
            parentFragmentManager.beginTransaction()
                .replace(R.id.main_frm, birthdateFragment)
                .addToBackStack(null)
                .commit()
        }

        // 프로필 사진 변경 버튼
        binding.accountinfoBox.setOnClickListener {
            openGallery()
        }
        binding.accountinfoProfilIv.scaleType = ImageView.ScaleType.CENTER_INSIDE
    }

    // 유저 정보 수정 API 호출
    private fun updateUserInfo() {
        val accessToken = tokenManager.getAccessToken()

        cachedUserInfo?.let { userInfo ->
            if (accessToken != null) {
                val userInfoUpdateRequest = UserInfoUpdateRequest(
                    userInfo.username,
                    userInfo.usernickname,
                    userInfo.gender,
                    calculateAge(userInfo.birth.substring(0, 4).toInt(), userInfo.birth.substring(4, 6).toInt(), userInfo.birth.substring(6, 8).toInt()),
                    userInfo.birth
                )

                val retrofit = TokenRetrofitManager(requireContext())
                val userService = retrofit.create(UserService::class.java)

                val call = userService.updateUserInfo("Bearer $accessToken", userInfoUpdateRequest)
                call.enqueue(object : Callback<UserInfoResponse> {
                    override fun onResponse(call: Call<UserInfoResponse>, response: Response<UserInfoResponse>) {
                        if (response.isSuccessful && response.body()?.isSuccess == true) {
                            Log.d("AccountinfoFragment", "유저 정보 수정 성공")
                        } else {
                            Log.d("AccountinfoFragment", "유저 정보 수정 실패: ${response.body()?.message}")
                        }
                    }

                    override fun onFailure(call: Call<UserInfoResponse>, t: Throwable) {
                        Log.d("AccountinfoFragment", "유저 정보 수정 API 호출 실패: ${t.message}")
                    }
                })
            } else {
                Log.d("AccountinfoFragment", "액세스 토큰이 없습니다.")
            }
        }
    }

    private fun openGallery() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        imagePickerLauncher.launch(intent)
    }

    private fun navigateToCropActivity(uri: Uri) {
        val intent = Intent(requireContext(), CropActivity::class.java).apply {
            putExtra("imageUri", uri)
        }
        cropActivityLauncher.launch(intent)
    }

    private fun hideKeyboardAndClearFocus(view: View) {
        val imm = activity?.getSystemService(InputMethodManager::class.java)
        imm?.hideSoftInputFromWindow(view.windowToken, 0)
        view.clearFocus()
    }

    private fun showKeyboardAndRequestFocus(view: View) {
        // 키보드 표시
        val imm = activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT)
    }


    private fun navigateToMypage() {
        parentFragmentManager.popBackStack()
        (activity as? MainActivity)?.setBottomNavigationViewVisibility(true) // main_bnv 보이기

    }

    // 만 나이 계산 함수
    fun calculateAge(birthYear: Int, birthMonth: Int, birthDay: Int): Int {
        val calendar = Calendar.getInstance()
        val currentYear = calendar.get(Calendar.YEAR)
        val currentMonth = calendar.get(Calendar.MONTH) + 1 // Calendar.MONTH는 0부터 시작하므로 +1 필요
        val currentDay = calendar.get(Calendar.DAY_OF_MONTH)

        var age = currentYear - birthYear

        // 생일이 지나지 않았으면 나이에서 1을 뺌
        if (currentMonth < birthMonth || (currentMonth == birthMonth && currentDay < birthDay)) {
            age--
        }

        return age
    }

    // 갤러리에서 이미지 선택 후 결과 처리
    private val imagePickerLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            result.data?.data?.let { uri ->
                navigateToCropActivity(uri)
            }
        }
    }

    // 크롭된 이미지 결과 처리
    private val cropActivityLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            result.data?.data?.let { uri ->
                binding.accountinfoProfilIv.setImageURI(uri)
            }
        }
    }


    // 유저 프로필 이미지 불러오기
    private fun loadUserProfileImage(imageUrl: String) {
        Glide.with(this)
            .load(imageUrl)  // 서버에서 받아온 이미지 URL
            .placeholder(R.drawable.mypage_profil) // 로딩 중에 보여줄 플레이스홀더 이미지
            .error(R.drawable.mypage_profil) // 에러 발생 시 보여줄 이미지
            .into(binding.accountinfoProfilIv) // 이미지를 로드할 ImageView
    }

}
