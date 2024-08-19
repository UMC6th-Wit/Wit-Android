package com.umc.umc_6th_wit_android.list

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import com.umc.umc_6th_wit_android.CosmeticActivity
import com.umc.umc_6th_wit_android.DailyActivity
import com.umc.umc_6th_wit_android.FoodActivity
import com.umc.umc_6th_wit_android.MedicineActivity
import com.umc.umc_6th_wit_android.MainActivity
import com.umc.umc_6th_wit_android.databinding.FragmentListBinding

class ListFragment : Fragment() {
    private lateinit var binding: FragmentListBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        (activity as? MainActivity)?.setBottomNavigationViewVisibility(true) // main_bnv 보이기
        binding = FragmentListBinding.inflate(inflater, container, false)
        setupCategorySelection()
        setupNavigation()

        val view2 = binding.listView2
        // 전체 화면 높이에서 325dp를 뺀 값을 픽셀로 변환
        val totalHeight = resources.displayMetrics.heightPixels
        val heightToRemove = requireContext().dpToPx(349)
        val newHeight = totalHeight - heightToRemove

        // LayoutParams를 설정하여 높이를 업데이트
        val params = view2.layoutParams
        params.height = newHeight
        view2.layoutParams = params

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // 뒤로가기 버튼 처리 search->home
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                // MainActivity의 selectHomeFragment() 호출
                (activity as? MainActivity)?.selectHomeFragment()
            }
        })
    }

    private fun setupCategorySelection() {
        val textViews = listOf(
            binding.listFood,
            binding.listBeauty,
            binding.listMedicine,
            binding.listLife
        )

        // dp 값을 px 값으로 변환합니다. Context는 안전하게 가져옵니다.
        val sectionsOffsetPx = listOf(0, 185, 412, 597).map {
            requireContext().dpToPx(it) // `requireContext()`를 사용하여 Context를 안전하게 가져옴
        }

        textViews.forEachIndexed { index, textView ->
            textView.setOnClickListener {
                highlightSelectedCategory(textView, textViews)
                binding.listScroll.smoothScrollTo(0, sectionsOffsetPx[index])
            }
        }
    }

    private fun Context.dpToPx(dp: Int): Int {
        return (dp * resources.displayMetrics.density).toInt()
    }


    private fun highlightSelectedCategory(selected: TextView, allTextViews: List<TextView>) {
        allTextViews.forEach {
            it.setBackgroundColor(Color.TRANSPARENT)  // 모든 텍스트 뷰의 배경색을 초기화
        }
        selected.setBackgroundColor(Color.parseColor("#FFDC70"))  // 선택된 뷰의 배경색 변경
    }

    private fun setupNavigation() {
        // Food 텍스트 뷰 클릭 시 FoodActivity로 이동
        binding.listFoodTv1.setOnClickListener {
            startActivity(Intent(requireContext(), FoodActivity::class.java))
        }
        binding.listFoodTv2.setOnClickListener {
            startActivity(Intent(requireContext(), FoodActivity::class.java))
        }
        binding.listFoodTv3.setOnClickListener {
            startActivity(Intent(requireContext(), FoodActivity::class.java))
        }
        binding.listFoodTv4.setOnClickListener {
            startActivity(Intent(requireContext(), FoodActivity::class.java))
        }

        // Cosmetic 텍스트 뷰 클릭 시 CosmeticActivity로 이동
        binding.listCosmeticTv1.setOnClickListener {
            startActivity(Intent(requireContext(), CosmeticActivity::class.java))
        }
        binding.listCosmeticTv2.setOnClickListener {
            startActivity(Intent(requireContext(), CosmeticActivity::class.java))
        }
        binding.listCosmeticTv3.setOnClickListener {
            startActivity(Intent(requireContext(), CosmeticActivity::class.java))
        }
        binding.listCosmeticTv4.setOnClickListener {
            startActivity(Intent(requireContext(), CosmeticActivity::class.java))
        }
        binding.listCosmeticTv5.setOnClickListener {
            startActivity(Intent(requireContext(), CosmeticActivity::class.java))
        }

        // Medicine 텍스트 뷰 클릭 시 MedicineActivity로 이동
        binding.listMedicineTv1.setOnClickListener {
            startActivity(Intent(requireContext(), MedicineActivity::class.java))
        }
        binding.listMedicineTv2.setOnClickListener {
            startActivity(Intent(requireContext(), MedicineActivity::class.java))
        }
        binding.listMedicineTv3.setOnClickListener {
            startActivity(Intent(requireContext(), MedicineActivity::class.java))
        }
        binding.listMedicineTv4.setOnClickListener {
            startActivity(Intent(requireContext(), MedicineActivity::class.java))
        }

        // Daily 텍스트 뷰 클릭 시 MedicineActivity로 이동
        binding.listDailyTv1.setOnClickListener {
            startActivity(Intent(requireContext(), DailyActivity::class.java))
        }
        binding.listDailyTv2.setOnClickListener {
            startActivity(Intent(requireContext(), DailyActivity::class.java))
        }
        binding.listDailyTv3.setOnClickListener {
            startActivity(Intent(requireContext(), DailyActivity::class.java))
        }
        binding.listDailyTv4.setOnClickListener {
            startActivity(Intent(requireContext(), DailyActivity::class.java))
        }
    }
}


