package com.umc.umc_6th_wit_android.mypage

import android.content.Context
import android.os.Bundle
import android.view.ContextThemeWrapper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.umc.umc_6th_wit_android.R
import com.umc.umc_6th_wit_android.databinding.FragmentMypageBinding
import androidx.fragment.app.activityViewModels
import com.umc.umc_6th_wit_android.MainActivity
import com.umc.umc_6th_wit_android.databinding.FragmentAccountinfoBinding

class MypageFragment : Fragment() {
    lateinit var binding: FragmentMypageBinding
    private val sharedViewModel: SharedViewModel by activityViewModels()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        (activity as? MainActivity)?.setBottomNavigationViewVisibility(true) // main_bnv 보이기
//        val contextThemeWrapper = ContextThemeWrapper(getActivity(), R.style.LoginTheme);
//        val localInflater = inflater.cloneInContext(contextThemeWrapper);
        binding = FragmentMypageBinding.inflate(inflater, container, false)

        //오버스크롤 제한
        binding.nestedScrollView.overScrollMode = View.OVER_SCROLL_NEVER

        // 닉네임 관찰하여 UI에 반영
        sharedViewModel.nickname.observe(viewLifecycleOwner) { nickname ->
            binding.mypageNickname.text = nickname
        }

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
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                // MainActivity의 selectHomeFragment() 호출
                (activity as? MainActivity)?.selectHomeFragment()
            }
        })
    }
}