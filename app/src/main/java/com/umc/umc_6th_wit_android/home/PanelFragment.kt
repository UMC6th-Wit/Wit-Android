package com.umc.umc_6th_wit_android.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.umc.umc_6th_wit_android.databinding.FragmentPanelBinding

class PanelFragment(val imgRes : Int, val detail : String, val title : String) : Fragment() {
    lateinit var binding : FragmentPanelBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentPanelBinding.inflate(inflater,container,false)

        binding.homePanelBgIv.setImageResource(imgRes)//인자값으로 받은 imgRes으로 bannerImageIv의 src값이 변경됨
        binding.homePanelDetailTv.text = detail
        binding.homePanelTitleTv.text = title

        return binding.root
    }
}