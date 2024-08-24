import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.umc.umc_6th_wit_android.databinding.FragmentPanelBinding

class PanelFragment : Fragment() {
    private var imgRes: Int = 0
    private var detail: String? = null
    private var title: String? = null
    private lateinit var binding: FragmentPanelBinding

    companion object {
        private const val ARG_IMG_RES = "img_res"
        private const val ARG_DETAIL = "detail"
        private const val ARG_TITLE = "title"

        fun newInstance(imgRes: Int, detail: String, title: String): PanelFragment {
            val fragment = PanelFragment()
            val args = Bundle().apply {
                putInt(ARG_IMG_RES, imgRes)
                putString(ARG_DETAIL, detail)
                putString(ARG_TITLE, title)
            }
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            imgRes = it.getInt(ARG_IMG_RES)
            detail = it.getString(ARG_DETAIL)
            title = it.getString(ARG_TITLE)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentPanelBinding.inflate(inflater, container, false)

        binding.homePanelBgIv.setImageResource(imgRes) // imgRes 값을 이용하여 이미지 설정
        binding.homePanelDetailTv.text = detail
        binding.homePanelTitleTv.text = title

        return binding.root
    }
}
