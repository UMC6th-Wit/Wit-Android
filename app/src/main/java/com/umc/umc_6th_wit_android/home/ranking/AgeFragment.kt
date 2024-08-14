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
import com.umc.umc_6th_wit_android.databinding.FragmentAgeBinding
import com.umc.umc_6th_wit_android.home.ranking.PeriodFragment


class AgeFragment : Fragment() {

    // Interface 정의
    interface OnAgeSelectedListener {
        fun onAgeSelected(age: String)
    }
//    lateinit var binding: FragmentAgeBinding
    private var _binding: FragmentAgeBinding? = null
    private val binding get() = _binding!!

    private lateinit var selectedLayout: LinearLayout
    private var listener: OnAgeSelectedListener? = null
    private var selectedAge: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            selectedAge = it.getString("age")
        }
    }
    override fun onAttach(context: Context) {
        super.onAttach(context)
        // 부모 액티비티가 인터페이스를 구현했는지 확인
        if (context is OnAgeSelectedListener) {
            listener = context
        } else {
            throw RuntimeException("$context must implement OnAgeSelectedListener")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // ViewBinding 초기화
        _binding = FragmentAgeBinding.inflate(inflater, container, false)

        // 각 LinearLayout에 클릭 리스너 설정
        binding.layoutAll.setOnClickListener { selectAge(binding.layoutAll) }
        binding.layout10.setOnClickListener { selectAge(binding.layout10) }
        binding.layout20.setOnClickListener { selectAge(binding.layout20) }
        binding.layout30.setOnClickListener { selectAge(binding.layout30) }
        binding.layout40.setOnClickListener { selectAge(binding.layout40) }

        selectedLayout = binding.layoutAll
        // 선택된 성별에 따라 UI를 업데이트
        when (selectedAge) {
            "연령대 전체" -> {
                selectedLayout = binding.layoutAll
            }
            "10대" -> {
                selectedLayout = binding.layout10
            }
            "20대" -> {
                selectedLayout = binding.layout20
            }
            "30대" -> {
                selectedLayout = binding.layout30
            }
            "40대" -> {
                selectedLayout = binding.layout40
            }
        }

        updateUI(selectedLayout, true)


        return binding.root
    }
    companion object {
        @JvmStatic
        fun newInstance(age: String) =
            AgeFragment().apply {
                arguments = Bundle().apply {
                    putString("age", age)
                }
            }
    }

    private fun selectAge(layout: LinearLayout) {
        if (layout != selectedLayout) {
            // 이전 선택된 항목을 초기 상태로 돌림
            updateUI(selectedLayout, false)
            // 새로 선택된 항목의 UI 업데이트
            selectedLayout = layout
            updateUI(selectedLayout, true)
        }
        // 부모 액티비티에 선택된 연령대 전달
        val textView = selectedLayout.getChildAt(0) as TextView
        listener?.onAgeSelected(textView.text.toString())
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
            selectAge(binding.layoutAll)
        }
    }
    //메모리 누수 방지
    override fun onDestroyView() {
        super.onDestroyView()
        // ViewBinding 해제
        _binding = null
    }
}