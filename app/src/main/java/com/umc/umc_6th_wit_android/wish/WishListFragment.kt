package com.umc.umc_6th_wit_android.wish

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import com.umc.umc_6th_wit_android.MainActivity
import com.umc.umc_6th_wit_android.R
import com.umc.umc_6th_wit_android.databinding.FragmentWishlistBinding

class WishListFragment : Fragment(), SelectionListener {

    lateinit var binding: FragmentWishlistBinding

    private lateinit var wishListAdapter: WishListAdapter
    // 편집 모드 상태 변수
    private var isEditMode = false

    // Fragment의 View를 생성하는 메소드
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // ViewBinding을 이용해 Fragment의 레이아웃을 설정
        binding = FragmentWishlistBinding.inflate(inflater, container, false)
        return binding.root
    }

    // View가 생성된 후 호출되는 메소드
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // 위시리스트와 보드의 레이아웃을 GridLayout으로 설정
        binding.wishListRecyclerView.layoutManager = GridLayoutManager(context, 2)

        // 어댑터를 초기화하고 RecyclerView에 설정
        wishListAdapter = WishListAdapter(getWishListItems(), this)
        binding.wishListRecyclerView.adapter = wishListAdapter

        // 아이템과 보드의 선택 상태에 따른 버튼 상태 업데이트
        updateItemButtonState(wishListAdapter.selectedItems.size)

        binding.wishListBack.setOnClickListener {
            parentFragmentManager.popBackStack()
        }

        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                parentFragmentManager.popBackStack()
            }
        })

        // 편집 버튼 클릭 이벤트 설정
        binding.wishListEdit.setOnClickListener {
            toggleEditMode()
        }

        // 삭제 버튼 클릭 이벤트 설정
        binding.btnDelete.setOnClickListener {
            deleteSelectedItems()
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
        // RecyclerView의 높이를 동적으로 변경
        val layoutParams = binding.wishListRecyclerView.layoutParams
        layoutParams.height = if (isEditMode) dpToPx(500) else dpToPx(600)
        binding.wishListRecyclerView.layoutParams = layoutParams
    }

    // 아이템 선택 상태에 따라 버튼의 활성화를 업데이트하는 함수
    private fun updateItemButtonState(selectedItemCount: Int) {
        val isEnabled = selectedItemCount > 0
        if(isEditMode){
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

    }

    // 선택된 아이템을 삭제하는 함수
    private fun deleteSelectedItems() {
        // 선택된 아이템을 삭제하고 편집 모드를 종료
        wishListAdapter.deleteSelectedItems()
        isEditMode = !isEditMode
        wishListAdapter.setEditMode(isEditMode)
        binding.wishListEditModeButtons.visibility = if (isEditMode) View.VISIBLE else View.GONE
        binding.wishListEdit.text = "편집"
        binding.wishListEdit.setTextColor(resources.getColor(R.color.edit_blue, null)) // 텍스트 색상 변경
        (activity as? MainActivity)?.setBottomNavigationViewVisibility(true) // main_bnv 보이기
        // RecyclerView의 높이를 동적으로 변경
        val layoutParams = binding.wishListRecyclerView.layoutParams
        layoutParams.height = if (isEditMode) dpToPx(500) else dpToPx(600)
        binding.wishListRecyclerView.layoutParams = layoutParams
        // 남은 아이템 개수를 업데이트
        binding.wishListCount.text = wishListAdapter.itemCount.toString()
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
            WishItem(0, R.drawable.item_ex, "아이템 1", "367","3,151", 4.4, 1),
            WishItem(1, R.drawable.item_ex, "아이템 2", "367","3,151", 4.4, 2),
            WishItem(2, R.drawable.item_ex, "아이템 3", "367","3,151", 4.4, 3),
            WishItem(3, R.drawable.item_ex, "아이템 4", "367","3,151", 4.4, 4),
            WishItem(4, R.drawable.item_ex, "아이템 5", "367","3,151", 4.4, 5),
            WishItem(5, R.drawable.item_ex, "아이템 6", "367","3,151", 4.4, 6),
        )
        // 아이템 개수를 설정
        binding.wishListCount.text = itemList.size.toString()
        Log.d("count", itemList.size.toString())
        return itemList
    }
    // dp를 px로 변환하는 함수
    private fun dpToPx(dp: Int): Int {
        val density = resources.displayMetrics.density
        return (dp * density).toInt()
    }
}