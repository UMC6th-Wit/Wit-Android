package com.umc.umc_6th_wit_android.mypage

import android.content.Context
import android.os.Bundle
import android.view.ContextThemeWrapper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
            val transaction: FragmentTransaction = parentFragmentManager.beginTransaction()
            transaction.add(R.id.main_frm, accountinfoFragment)
            transaction.addToBackStack(null)  // 백스택에 추가하여 뒤로 가기 버튼으로 돌아올 수 있게 함
            transaction.commit()
        }

        binding.mypageDeleteAccountLayout.setOnClickListener {
            // DleteAccountFragment로 전환
            val deleteAccountFragment = DeleteAccountFragment()
            val transaction: FragmentTransaction = parentFragmentManager.beginTransaction()
            transaction.add(R.id.main_frm, deleteAccountFragment)
            transaction.addToBackStack(null)  // 백스택에 추가하여 뒤로 가기 버튼으로 돌아올 수 있게 함
            transaction.commit()
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // 네비게이션 바 다시 표시
        (activity as? MainActivity)?.binding?.mainBnv?.visibility = View.VISIBLE
    }
}