package com.umc.umc_6th_wit_android.home.ranking

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.umc.umc_6th_wit_android.databinding.FragmentPeriodBinding

class PeriodFragment : Fragment() {

    // Interface 정의
    interface OnPeriodSelectedListener {
        fun onPeriodSelected(period: String)
    }

//    lateinit var binding: FragmentPeriodBinding
    private var _binding: FragmentPeriodBinding? = null
    private val binding get() = _binding!!

    private lateinit var selectedLayout: LinearLayout
    private var listener: OnPeriodSelectedListener? = null
    private var selectedPeriod: String? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        // 부모 액티비티가 인터페이스를 구현했는지 확인
        if (context is OnPeriodSelectedListener) {
            listener = context
        } else {
            throw RuntimeException("$context must implement OnPeriodSelectedListener")
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            selectedPeriod = it.getString("period")
        }
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentPeriodBinding.inflate(inflater, container, false)

        // 각 LinearLayout에 클릭 리스너 설정
        binding.layoutReal.setOnClickListener { selectPeriod(binding.layoutReal) }
        binding.layoutDaily.setOnClickListener { selectPeriod(binding.layoutDaily) }
        binding.layoutWeekly.setOnClickListener { selectPeriod(binding.layoutWeekly) }
        binding.layoutMonthly.setOnClickListener { selectPeriod(binding.layoutMonthly) }

        selectedLayout = binding.layoutReal
        when (selectedPeriod) {
            "실시간 랭킹" -> {
                selectedLayout = binding.layoutReal
            }
            "일간 랭킹" -> {
                selectedLayout = binding.layoutDaily
            }
            "주간 랭킹" -> {
                selectedLayout = binding.layoutWeekly
            }
            "일간 랭킹" -> {
                selectedLayout = binding.layoutMonthly
            }
        }

        updateUI(selectedLayout, true)


        return binding.root
    }
    companion object {
        @JvmStatic
        fun newInstance(period: String) =
            PeriodFragment().apply {
                arguments = Bundle().apply {
                    putString("period", period)
                }
            }
    }
    private fun selectPeriod(layout: LinearLayout) {
        if (layout != selectedLayout) {
            // 이전 선택된 항목을 초기 상태로 돌림
            updateUI(selectedLayout, false)
            // 새로 선택된 항목의 UI 업데이트
            selectedLayout = layout
            updateUI(selectedLayout, true)
        }
        // 부모 액티비티에 선택된 기간 전달
        val textView = selectedLayout.getChildAt(0) as TextView
        listener?.onPeriodSelected(textView.text.toString())
    }
    private fun updateUI(layout: LinearLayout, isSelected: Boolean) {
        val textView = layout.getChildAt(0) as TextView
        val imageView = layout.getChildAt(1) as ImageView

        if (isSelected) {
            textView.setTextColor(Color.BLACK)
            imageView.visibility = View.VISIBLE
        } else {
            textView.setTextColor(Color.parseColor("#868686"))
            imageView.visibility = View.GONE
        }
    }
    fun resetUI() {
        _binding?.let {
            selectPeriod(binding.layoutReal)
        }
    }
    override fun onDestroyView() {
        super.onDestroyView()
        // ViewBinding 해제
        _binding = null
    }
}