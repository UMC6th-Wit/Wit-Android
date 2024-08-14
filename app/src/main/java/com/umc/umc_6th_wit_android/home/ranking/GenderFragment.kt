import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.umc.umc_6th_wit_android.databinding.FragmentGenderBinding
import com.umc.umc_6th_wit_android.databinding.FragmentPeriodBinding

class GenderFragment : Fragment() {

    // Interface 정의
    interface OnGenderSelectedListener {
        fun onGenderSelected(gender: String)
    }

    private var _binding: FragmentGenderBinding? = null
    private val binding get() = _binding!!

    private lateinit var selectedLayout: LinearLayout
    private var listener: OnGenderSelectedListener? = null
    private var selectedGender: String? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        // 부모 액티비티가 인터페이스를 구현했는지 확인
        if (context is OnGenderSelectedListener) {
            listener = context
        } else {
            throw RuntimeException("$context must implement OnGenderSelectedListener")
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Fragment에 전달된 gender 값을 받습니다.
        arguments?.let {
            selectedGender = it.getString("gender")
        }
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentGenderBinding.inflate(inflater, container, false)

        // 각 LinearLayout에 클릭 리스너 설정
        binding.layoutAll.setOnClickListener { selectGender(binding.layoutAll) }
        binding.layoutFemale.setOnClickListener { selectGender(binding.layoutFemale) }
        binding.layoutMale.setOnClickListener { selectGender(binding.layoutMale) }

        selectedLayout = binding.layoutAll
        // 선택된 성별에 따라 UI를 업데이트
        when (selectedGender) {
            "여성" -> {
                // 여성 버튼을 선택 상태로 설정
                selectedLayout = binding.layoutFemale
            }
            "남성" -> {
                // 남성 버튼을 선택 상태로 설정
                selectedLayout = binding.layoutMale
            }
            "성별 전체" -> {
                // 전체 성별을 선택 상태로 설정
                selectedLayout = binding.layoutAll
            }
        }


        updateUI(selectedLayout, true)

        return binding.root
    }

    companion object {
        @JvmStatic
        fun newInstance(gender: String) =
            GenderFragment().apply {
                arguments = Bundle().apply {
                    putString("gender", gender)
                }
            }
    }
    private fun selectGender(layout: LinearLayout) {
        if (layout != selectedLayout) {
            // 이전 선택된 항목을 초기 상태로 돌림
            updateUI(selectedLayout, false)
            // 새로 선택된 항목의 UI 업데이트
            selectedLayout = layout
            updateUI(selectedLayout, true)
        }
        // 부모 액티비티에 선택된 성별 전달
        val textView = selectedLayout.getChildAt(0) as TextView
        listener?.onGenderSelected(textView.text.toString())
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
        // 연령대 TextView 및 기타 UI 요소를 기본값으로 설정
        _binding?.let {
            selectGender(binding.layoutAll)
        }
    }
    override fun onDestroyView() {
        super.onDestroyView()
        // ViewBinding 해제
        _binding = null
    }
}