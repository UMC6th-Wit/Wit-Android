package com.umc.umc_6th_wit_android.mypage


import android.content.Context
import android.content.Intent
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.util.Log

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.view.ContextThemeWrapper
import androidx.fragment.app.Fragment
import com.umc.umc_6th_wit_android.R
import com.umc.umc_6th_wit_android.databinding.FragmentMypageBinding
import androidx.fragment.app.activityViewModels
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.engine.GlideException
import com.umc.umc_6th_wit_android.MainActivity
import com.umc.umc_6th_wit_android.login.LoginActivity

import com.umc.umc_6th_wit_android.login.TokenManager
import com.umc.umc_6th_wit_android.network.TokenRetrofitManager
import retrofit2.Callback
import retrofit2.Call
import retrofit2.Response


class MypageFragment : Fragment() {
    lateinit var binding: FragmentMypageBinding
    private val sharedViewModel: SharedViewModel by activityViewModels()
    private lateinit var tokenManager: TokenManager


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val contextThemeWrapper = ContextThemeWrapper(getActivity(), R.style.LoginTheme);
        val localInflater = inflater.cloneInContext(contextThemeWrapper);

        binding = FragmentMypageBinding.inflate(localInflater, container, false)
        binding.loadingImage.visibility = View.VISIBLE
        (activity as? MainActivity)?.setBottomNavigationViewVisibility(true) // main_bnv 보이기

        // TokenManager 초기화
        tokenManager =
            TokenManager(requireContext().getSharedPreferences("auth_prefs", Context.MODE_PRIVATE))

        // 유저 정보 조회
        fetchUserInfo()

        // 프로필 이미지 불러오기
        fetchUserProfileImage()

        // 프로필 이미지 변경 시 이를 반영
        parentFragmentManager.setFragmentResultListener(
            "profileImageUpdated",
            viewLifecycleOwner
        ) { requestKey, bundle ->
            val updatedProfileImageUrl = bundle.getString("updatedProfileImageUrl")
            if (updatedProfileImageUrl != null) {
                loadUserProfileImage(updatedProfileImageUrl)
            }
        }

        // 프로필 이미지 변경 시 이를 반영
        parentFragmentManager.setFragmentResultListener(
            "profileImageUpdated",
            viewLifecycleOwner
        ) { requestKey, bundle ->
            val updatedProfileImageUrl = bundle.getString("updatedProfileImageUrl")
            if (updatedProfileImageUrl != null) {
                loadUserProfileImage(updatedProfileImageUrl)
            }
        }


        parentFragmentManager.setFragmentResultListener(
            "updateNickname",
            viewLifecycleOwner
        ) { requestKey, bundle ->
            val nickname = bundle.getString("nickname")
            if (nickname != null) {
                binding.mypageNickname.text = nickname
            }
        }

        //오버스크롤 제한
        binding.nestedScrollView.overScrollMode = View.OVER_SCROLL_NEVER

        binding.mypageAccountinfoMore.setOnClickListener {
            // AccountinfoFragment로 전환
            val accountinfoFragment = AccountinfoFragment()
            replaceFragment(accountinfoFragment)
        }

        binding.mypageDeleteAccountLayout.setOnClickListener {
            // DleteAccountFragment로 전환
            val deleteAccountFragment = DeleteAccountFragment()
            replaceFragment(deleteAccountFragment)
        }

        binding.mypageLogout.setOnClickListener {
            logoutUser()
        }

        return binding.root
    }


    private fun replaceFragment(fragment: Fragment) {
        // 네비게이션 바 숨김
        (activity as? MainActivity)?.setBottomNavigationViewVisibility(false)
        parentFragmentManager.beginTransaction()
            .add(R.id.main_frm, fragment)
            .addToBackStack(null)  // 백스택에 추가하여 뒤로 가기 버튼으로 돌아올 수 있게 함
            .commit()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // 네비게이션 바 다시 표시
        (activity as? MainActivity)?.binding?.mainBnv?.visibility = View.VISIBLE


        // 뒤로가기 버튼 처리 search->home
        requireActivity().onBackPressedDispatcher.addCallback(
            viewLifecycleOwner,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    // MainActivity의 selectHomeFragment() 호출
                    (activity as? MainActivity)?.selectHomeFragment()
                }
            })
    }

//    override fun onStart() {
//        super.onStart()
//        (activity as? MainActivity)?.setBottomNavigationViewVisibility(true) // main_bnv 보이기
//    }

    private fun logoutUser() {
        val accessToken = tokenManager.getAccessToken()

        if (accessToken != null) {
            val retrofit = TokenRetrofitManager(requireContext())
            val userService = retrofit.create(UserService::class.java)

            val call = userService.logout("Bearer $accessToken")
            call.enqueue(object : Callback<LogoutResponse> {
                override fun onResponse(
                    call: Call<LogoutResponse>,
                    response: Response<LogoutResponse>
                ) {
                    if (response.isSuccessful && response.body()?.isSuccess == true) {
                        Log.d("MypageFragment", "로그아웃 성공")
                        handleLogoutSuccess()
                    } else {
                        Log.d("MypageFragment", "로그아웃 실패: ${response.body()?.message}")
                        handleLogoutFailure()
                    }
                }

                override fun onFailure(call: Call<LogoutResponse>, t: Throwable) {
                    Log.d("MypageFragment", "로그아웃 API 호출 실패: ${t.message}")
                    handleLogoutFailure()
                }
            })
        } else {
            Log.d("MypageFragment", "액세스 토큰이 없습니다.")
            handleLogoutFailure()
        }
    }

    private fun handleLogoutSuccess() {
        // 토큰과 사용자 정보 지우기
        tokenManager.clearTokens()

        // 로그인 화면으로 이동
        val intent = Intent(requireContext(), LoginActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
    }

    private fun handleLogoutFailure() {
        // 실패 시 처리 (로그아웃 실패 메시지 표시 등)
        Log.d("MypageFragment", "로그아웃에 실패했습니다.")
    }

    private fun fetchUserInfo() {
        val accessToken = tokenManager.getAccessToken()

        if (accessToken != null) {
            val retrofit = TokenRetrofitManager(requireContext())
            val userService = retrofit.create(UserService::class.java)

            val call = userService.getUserInfo("Bearer $accessToken")
            call.enqueue(object : Callback<UserInfoResponse> {
                override fun onResponse(
                    call: Call<UserInfoResponse>,
                    response: Response<UserInfoResponse>
                ) {
                    if (response.isSuccessful && response.body()?.isSuccess == true) {
                        val userInfo = response.body()?.result
                        userInfo?.let {
                            binding.mypageNickname.text = it.usernickname

                            Log.d("MypageFragment", "유저 정보 조회 성공")
                            Log.d(
                                "MypageFragment",
                                "mypageFragment fetchInfo()에서 nickname: ${userInfo.usernickname}"
                            )


                        }
                    } else {
                        Log.d("MypageFragment", "유저 정보 조회 실패: ${response.body()?.message}")
                    }
                }

                override fun onFailure(call: Call<UserInfoResponse>, t: Throwable) {
                    Log.d("MypageFragment", "유저 정보 조회 API 호출 실패: ${t.message}")
                }
            })
        } else {
            Log.d("MypageFragment", "액세스 토큰이 없습니다.")
        }
    }

    private fun fetchUserProfileImage() {
        val accessToken = tokenManager.getAccessToken()

        if (accessToken != null) {
            val retrofit = TokenRetrofitManager(requireContext())
            val userService = retrofit.create(UserService::class.java)

            val call = userService.getUserProfileImage("Bearer $accessToken")
            call.enqueue(object : Callback<UserProfileResponse> {
                override fun onResponse(
                    call: Call<UserProfileResponse>,
                    response: Response<UserProfileResponse>
                ) {
                    if (response.isSuccessful && response.body()?.isSuccess == true) {
                        val imageUrl = response.body()?.result
                        imageUrl?.let {
                            // 프로필 이미지를 로드
                            Log.d("MypageFragment", "프로필 이미지 로드 loadUserProfileImage(it)")
                            loadUserProfileImage(it)
                        }
                    } else {
                        Log.d("MypageFragment", "프로필 이미지 로드 실패: ${response.body()?.message}")
                    }
                }

                override fun onFailure(call: Call<UserProfileResponse>, t: Throwable) {
                    Log.d("MypageFragment", "프로필 이미지 로드 API 호출 실패: ${t.message}")
                }
            })
        } else {
            Log.d("MypageFragment", "액세스 토큰이 없습니다.")
        }
    }

    private fun loadUserProfileImage(imageUrl: String) {
        Log.d("MypageFragment", "Loading image from URL: $imageUrl")
        Glide.with(this)
            .load(imageUrl)
            .skipMemoryCache(true)  // 메모리 캐시 건너뛰기
            .diskCacheStrategy(DiskCacheStrategy.NONE)
            .placeholder(R.drawable.mypage_profil) // 로딩 중에 보여줄 플레이스홀더 이미지
            .error(R.drawable.mypage_profil) // 에러 발생 시 보여줄 이미지
            .listener(object : com.bumptech.glide.request.RequestListener<Drawable> {
                override fun onLoadFailed(
                    e: GlideException?,
                    model: Any?,
                    target: com.bumptech.glide.request.target.Target<Drawable>?,
                    isFirstResource: Boolean
                ): Boolean {
                    // 이미지 로드 실패 시에도 로딩 이미지 숨김
                    binding.loadingImage.visibility = View.GONE
                    return false
                }

                override fun onResourceReady(
                    resource: Drawable?,
                    model: Any?,
                    target: com.bumptech.glide.request.target.Target<Drawable>?,
                    dataSource: DataSource?,
                    isFirstResource: Boolean
                ): Boolean {
                    // 이미지가 성공적으로 로드되었을 때 로딩 이미지 숨김
                    binding.loadingImage.visibility = View.GONE
                    return false
                }
            })
            .into(binding.mypageProfilIv)

    }


}