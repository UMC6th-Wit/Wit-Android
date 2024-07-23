package com.umc.umc_6th_wit_android.wish

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import com.umc.umc_6th_wit_android.R
import com.umc.umc_6th_wit_android.databinding.FragmentWishBinding


class WishFragment : Fragment() {
    lateinit var binding: FragmentWishBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentWishBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // GridLayoutManager 설정
        binding.recyclerView.layoutManager = GridLayoutManager(context, 2)
        binding.recyclerView.adapter = WishAdapter(getWishItems())

        // 버튼 클릭 시 선택 상태 및 텍스트 색상 변경
        binding.btnCart.isSelected = true // 초기 선택 상태 설정
        binding.btnCart.setTextColor(resources.getColor(R.color.selectedTextColor)) // 초기 선택된 버튼 텍스트 색상 설정
        binding.btnFolder.setTextColor(resources.getColor(R.color.unselectedTextColor)) // 초기 선택되지 않은 버튼 텍스트 색상 설정

        binding.btnCart.setOnClickListener {
            setButtonSelected(binding.btnCart, binding.btnFolder)
        }
        binding.btnFolder.setOnClickListener {
            setButtonSelected(binding.btnFolder, binding.btnCart)
        }
    }

    private fun setButtonSelected(selectedButton: View, otherButton: View) {
        binding.btnCart.isSelected = false
        binding.btnFolder.isSelected = false
        selectedButton.isSelected = true

        if (selectedButton.id == R.id.btn_cart) {
            binding.btnCart.setTextColor(resources.getColor(R.color.selectedTextColor))
            binding.btnFolder.setTextColor(resources.getColor(R.color.unselectedTextColor))
        } else {
            binding.btnCart.setTextColor(resources.getColor(R.color.unselectedTextColor))
            binding.btnFolder.setTextColor(resources.getColor(R.color.selectedTextColor))
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
    }

    private fun getWishItems(): List<WishItem> {
        // 예제 데이터를 생성합니다.
        val itemList = listOf(
            WishItem(0, R.drawable.item_ex, "아이템 1", "367","3,151", 4.4),
            WishItem(1, R.drawable.item_ex, "아이템 2", "367","3,151", 4.4),
            WishItem(0, R.drawable.item_ex, "아이템 3", "367","3,151", 4.4),
            WishItem(1, R.drawable.item_ex, "아이템 4", "367","3,151", 4.4),
            WishItem(0, R.drawable.item_ex, "아이템 5", "367","3,151", 4.4),
            WishItem(1, R.drawable.item_ex, "아이템 6", "367","3,151", 4.4),
        )
        binding.wishCount.text = itemList.size.toString()
        return itemList
    }
}
