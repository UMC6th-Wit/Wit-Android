package com.umc.umc_6th_wit_android.wish

import android.app.Dialog
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.view.WindowManager
import android.widget.Button
import android.widget.TextView
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import com.umc.umc_6th_wit_android.MainActivity
import com.umc.umc_6th_wit_android.R
import com.umc.umc_6th_wit_android.databinding.FragmentWishlistBinding
import kotlin.properties.Delegates

// WishListFragment 클래스 정의: 위시리스트를 관리하는 프래그먼트
class WishListFragment : Fragment(), SelectionListener, WishListView {

    // ViewBinding 객체 선언
    lateinit var binding: FragmentWishlistBinding

    // 어댑터 객체 선언
    private lateinit var wishListAdapter: WishListAdapter
    // 편집 모드 상태 변수
    private var isEditMode = false
    private val wishService = WishService()
    private var boardId = 0

    // Fragment의 View를 생성하는 메소드
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // ViewBinding을 이용해 Fragment의 레이아웃을 설정
        binding = FragmentWishlistBinding.inflate(inflater, container, false)
        wishService.setWishListView(this)
        return binding.root
    }

    // View가 생성된 후 호출되는 메소드
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // 위시리스트의 레이아웃을 GridLayout으로 설정
        binding.wishListRecyclerView.layoutManager = GridLayoutManager(context, 2)

        // 어댑터를 초기화하고 RecyclerView에 설정
        wishListAdapter = WishListAdapter(mutableListOf(), this) { currentCursor, limit ->
            loadMoreItems(currentCursor, limit)
        }
        binding.wishListRecyclerView.adapter = wishListAdapter

        boardId = arguments?.getInt("boardId")!!
        // 처음 데이터 로드
        loadMoreItems(1, 20)

        // 아이템의 선택 상태에 따른 버튼 상태 업데이트
        updateItemButtonState(wishListAdapter.selectedItems.size)

        // 편집 모드 버튼 클릭 이벤트 설정
        binding.wishListEditModeButtons.setOnClickListener {
            // 빈 클릭 이벤트 설정
        }

        // 뒤로 가기 버튼 클릭 이벤트 설정
        binding.wishListBack.setOnClickListener {
            if (isEditMode) {
                (activity as? MainActivity)?.setBottomNavigationViewVisibility(true) // main_bnv 보이기
            }
            parentFragmentManager.popBackStack()
        }

        // 뒤로 가기 버튼 콜백 설정
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                if (isEditMode) {
                    (activity as? MainActivity)?.setBottomNavigationViewVisibility(true) // main_bnv 보이기
                }
                parentFragmentManager.popBackStack()
            }
        })

        // 편집 버튼 클릭 이벤트 설정
        binding.wishListEdit.setOnClickListener {
            toggleEditMode()
        }

        // 삭제 버튼 클릭 이벤트 설정
        binding.btnDelete.setOnClickListener {
            showDeletePopup()
        }

        // 폴더에 추가 버튼 클릭 이벤트 설정
        binding.btnAddToFolder.setOnClickListener {
            addToFolder()
        }

        // 화면 전환 시 데이터 수신
        val boardTitle = arguments?.getString("boardTitle")
        if (boardTitle != null) {
            binding.wishListTitle.text = boardTitle.toString()
        }
        Log.d("folder", "boardId: $boardId")
    }

    fun loadMoreItems(cursor: Int?, limit: Int?) {
        if (boardId != null) {
            wishService.getWishBoard(61, boardId, cursor, limit)
        }
    }

    // 위시리스트 편집 모드를 토글하는 함수
    private fun toggleEditMode() {
        isEditMode = !isEditMode
        wishListAdapter.setEditMode(isEditMode)
        // 편집 모드 버튼의 가시성을 설정
        binding.wishListEditModeButtons.visibility = if (isEditMode) View.VISIBLE else View.GONE
        if (isEditMode) {
            binding.wishListItemText.text = "개 선택"
            binding.wishListEdit.text = "취소"
            binding.wishListEdit.setTextColor(resources.getColor(R.color.edit_red, null)) // 텍스트 색상 변경
            (activity as? MainActivity)?.setBottomNavigationViewVisibility(false) // main_bnv 숨기기
        } else {
            binding.wishListItemText.text = "개의 위시템"
            binding.wishListEdit.text = "편집"
            binding.wishListEdit.setTextColor(resources.getColor(R.color.edit_blue, null)) // 텍스트 색상 변경
            (activity as? MainActivity)?.setBottomNavigationViewVisibility(true) // main_bnv 보이기
        }
    }

    // 아이템 선택 상태에 따라 버튼의 활성화를 업데이트하는 함수
    private fun updateItemButtonState(selectedItemCount: Int) {
        val isEnabled = selectedItemCount > 0
        if (isEditMode) {
            binding.wishListCount.text = selectedItemCount.toString()
        }
        // 선택된 아이템의 수에 따라 각 버튼의 활성화 상태와 투명도를 설정
        for (i in 0 until binding.wishListEditModeButtons.childCount) {
            val child = binding.wishListEditModeButtons.getChildAt(i)
            child.isEnabled = isEnabled
            if (isEnabled) {
                child.alpha = 1.0f
            } else {
                child.alpha = 0.5f
            }
        }
    }

    // 아이템 선택 상태가 변경될 때 호출되는 콜백 메소드
    override fun onSelectionItemChanged(count: Int) {
        updateItemButtonState(count)
    }

    // 보드 선택 상태가 변경될 때 호출되는 콜백 메소드
    override fun onSelectionBoardChanged(count: Int) {
        // 구현되지 않은 부분
    }

    // 선택된 아이템을 삭제하는 함수
    private fun deleteSelectedItems() {
        // 선택된 아이템을 삭제하고 편집 모드를 종료
        wishListAdapter.deleteSelectedItems()
        toggleEditMode()
        // 남은 아이템 개수를 업데이트
        binding.wishListCount.text = wishListAdapter.itemCount.toString()
    }

    // 삭제 확인 팝업을 표시하는 함수
    private fun showDeletePopup() {
        // 삭제 확인 팝업 레이아웃을 인플레이트하고 다이얼로그를 생성
        val dialogView = layoutInflater.inflate(R.layout.confirm_delete_popup, null)
        val dialog = Dialog(requireContext())
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(dialogView)

        // 팝업 타이틀과 메시지 설정
        val tvPopupTitle = dialogView.findViewById<TextView>(R.id.tv_popup_title)
        val tvPopupMessage = dialogView.findViewById<TextView>(R.id.tv_popup_message)
        tvPopupTitle.text = "내 폴더에서 삭제할까요?"
        tvPopupMessage.text = "현재 폴더에서 선택한 제품이 삭제됩니다."

        // 팝업 창을 중앙에 배치하고 가로 넓이를 300dp로 설정
        val metrics = resources.displayMetrics
        val width = (300 * metrics.density).toInt()

        dialog.window?.setLayout(
            width,
            WindowManager.LayoutParams.WRAP_CONTENT
        )
        dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
        dialog.window?.attributes?.gravity = Gravity.CENTER

        // 취소 버튼 클릭 이벤트 리스너 설정
        dialogView.findViewById<Button>(R.id.btn_cancel).setOnClickListener {
            dialog.dismiss()
        }
        // 삭제 버튼 클릭 이벤트 리스너 설정
        dialogView.findViewById<Button>(R.id.btn_delete).setOnClickListener {
            // 폴더 삭제 로직 추가
            deleteSelectedItems()
            dialog.dismiss()
        }
        dialog.show()
    }

    // 폴더에 추가하는 기능을 구현하는 함수 (현재 구현되지 않음)
    private fun addToFolder() {
        // 폴더에 추가하는 기능 구현
    }

    // Fragment가 DestroyView될 때 호출되는 메소드
    override fun onDestroyView() {
        super.onDestroyView()
    }

    // 위시 아이템 리스트를 생성하는 함수 (예제 데이터 사용)
    private fun getWishListItems(): List<WishItem> {
        // 예제 데이터를 생성
        val itemList = listOf(
            WishItem(0, "아이템 1", 367, 3151, R.drawable.item_ex.toString(), 4.4, 1, true),
            WishItem(1, "아이템 2", 367, 3151, R.drawable.item_ex.toString(), 4.4, 2, true),
            WishItem(2, "아이템 3", 367, 3151, R.drawable.item_ex.toString(), 4.4, 3, true),
            WishItem(3, "아이템 4", 367, 3151, R.drawable.item_ex.toString(), 4.4, 4, true),
            WishItem(4, "아이템 5", 367, 3151, R.drawable.item_ex.toString(), 4.4, 5, true),
            WishItem(5, "아이템 6", 367, 3151, R.drawable.item_ex.toString(), 4.4, 6, true),
        )
        // 아이템 개수를 설정
        binding.wishListCount.text = itemList.size.toString()
        return itemList
    }

    // dp를 px로 변환하는 함수
    private fun dpToPx(dp: Int): Int {
        val density = resources.displayMetrics.density
        return (dp * density).toInt()
    }

    //위시리스트 폴더 상세 조회 성공
    override fun onGetWishBoardSuccess(code: String, result: WishItemResult) {
        Log.d("board_product", "Success")
        wishListAdapter.addItems(result.products)
        wishListAdapter.currentCursor = result.nextCursor
        binding.wishListCount.text = result.count.toString()
    }

    //위시리스트 폴더 상세 조회 실패
    override fun onGetWishBoardFailure(code: String, message: String) {
        Log.d("board_product", "Fail")
    }

    //위시리스트 폴더 상품 담기 성공
    override fun onPostWishtoBoardSuccess(code: String, result: WishBoardItemResult) {
        TODO("Not yet implemented")
    }

    //위시리스트 폴더 상품 담기 실패
    override fun onPostWishtoBoardFailure(code: String, message: String) {
        TODO("Not yet implemented")
    }

    //위시리스트 폴더 목록 삭제 성공
    override fun onDeleteToWishBoardSuccess(code: String, message: String) {
        TODO("Not yet implemented")
    }

    //위시리스트 폴더 목록 삭제 실패
    override fun onDeleteToWishBoardFailure(code: String, message: String) {
        TODO("Not yet implemented")
    }
}
