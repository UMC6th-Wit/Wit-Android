package com.umc.umc_6th_wit_android.wish

import android.app.Activity
import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.view.WindowManager
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import com.umc.umc_6th_wit_android.MainActivity
import com.umc.umc_6th_wit_android.R
import com.umc.umc_6th_wit_android.databinding.FragmentWishBinding

// WishFragment 클래스는 위시리스트 및 보드의 UI를 관리하는 프래그먼트입니다.
class WishFragment : Fragment(), SelectionListener {
    // ViewBinding 객체 선언
    lateinit var binding: FragmentWishBinding
    // 어댑터 객체 선언
    private lateinit var wishAdapter: WishAdapter
    private lateinit var boardsAdapter: BoardsAdapter
    // 편집 모드 상태 변수
    private var isEditMode = false

    private val ADD_FOLDER_REQUEST_CODE = 1

    // Fragment의 View를 생성하는 메소드
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // ViewBinding을 이용해 Fragment의 레이아웃을 설정
        binding = FragmentWishBinding.inflate(inflater, container, false)
        return binding.root
    }

    // View가 생성된 후 호출되는 메소드
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // 위시리스트와 보드의 레이아웃을 GridLayout으로 설정
        binding.recyclerView.layoutManager = GridLayoutManager(context, 2)
        binding.boardsRecyclerView.layoutManager = GridLayoutManager(context, 2)

        // 어댑터를 초기화하고 RecyclerView에 설정
        wishAdapter = WishAdapter(getWishItems(), this)
        boardsAdapter = BoardsAdapter(getWishboards(), this)
        binding.recyclerView.adapter = wishAdapter
        binding.boardsRecyclerView.adapter = boardsAdapter

        // 아이템과 보드의 선택 상태에 따른 버튼 상태 업데이트
        updateItemButtonState(wishAdapter.selectedItems.size)
        updateBoardButtonState(boardsAdapter.selectedBoards.size)

        // 초기 버튼 상태 설정 (장바구니 버튼이 선택된 상태)
        // 화면 전환 시 데이터 수신
//        binding.btnCart.isSelected = true
//        val selected = arguments?.getBoolean("selected")
//        if (selected != null) {
//            binding.btnCart.isSelected = selected
//            binding.btnFolder.isSelected = !selected
//        }
//        if(binding.btnCart.isSelected == null){
//            binding.btnCart.isSelected = true
//        }
//

        binding.btnCart.isSelected = true
        setView(binding.btnCart.isSelected)

        // 버튼 클릭 이벤트 설정
        binding.btnCart.setOnClickListener {
            setButtonSelected(binding.btnCart, binding.btnFolder)
        }
        binding.btnFolder.setOnClickListener {
            setButtonSelected(binding.btnFolder, binding.btnCart)
        }

        // 편집 버튼 클릭 이벤트 설정
        binding.wishEdit.setOnClickListener {
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

        // 보드 추가 버튼 클릭 이벤트 설정 (현재 구현되지 않음)
        binding.boardAdd.setOnClickListener {
            val intent = Intent(activity, FolderActivity::class.java)
            startActivityForResult(intent, ADD_FOLDER_REQUEST_CODE)
        }

        // 보드 편집 버튼 클릭 이벤트 설정
        binding.boardEdit.setOnClickListener{
            toggleBoardEditMode()
        }

        // 보드 삭제 버튼 클릭 이벤트 설정
        binding.btnBoardDelete.setOnClickListener{
            showDeletePopup()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == ADD_FOLDER_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            val folderName = data?.getStringExtra("folderName")
            if (folderName != null) {
                val newWishboard = Wishboard(boardsAdapter.itemCount, folderName, 0, emptyList())
                boardsAdapter.addBoard(newWishboard)
                binding.boardQuantity.text = boardsAdapter.itemCount.toString()
            }
        }
    }

    // 버튼 선택 상태를 설정하는 함수
    private fun setButtonSelected(selectedButton: View, otherButton: View) {
        // 두 버튼 모두 선택되지 않은 상태로 초기화
        binding.btnCart.isSelected = false
        binding.btnFolder.isSelected = false
        // 선택된 버튼을 활성화
        selectedButton.isSelected = true
        setView(selectedButton.id == R.id.btn_cart)
    }

    private fun setView(selected: Boolean){
        if(selected){
            // 장바구니 버튼이 선택된 경우
            if(isEditMode){
                // 편집 모드 해제 및 보드 뷰 상태 초기화
                isEditMode = !isEditMode
                boardsAdapter.setEditMode(isEditMode)
                binding.boardEditModeButtons.visibility = if (isEditMode) View.VISIBLE else View.GONE
                binding.boardEdit.text = "편집"
                binding.boardEdit.setTextColor(resources.getColor(R.color.edit_blue, null)) // 텍스트 색상 변경
                (activity as? MainActivity)?.setBottomNavigationViewVisibility(true) // main_bnv 보이기
                // RecyclerView의 높이를 동적으로 변경
                val layoutParams = binding.boardsRecyclerView.layoutParams
                layoutParams.height = if (isEditMode) dpToPx(387) else dpToPx(487)
                binding.boardsRecyclerView.layoutParams = layoutParams
            }
            // 장바구니 버튼의 텍스트 색상을 활성화 색상으로 변경
            binding.btnCart.setTextColor(resources.getColor(R.color.selectedTextColor))
            // 폴더 버튼의 텍스트 색상을 비활성화 색상으로 변경
            binding.btnFolder.setTextColor(resources.getColor(R.color.unselectedTextColor))

            // 폴더 관련 뷰를 숨기고 장바구니 관련 뷰를 표시
            binding.nation.visibility = View.GONE
            binding.line.visibility = View.GONE
            binding.boardTools.visibility = View.GONE
            binding.boardsRecyclerView.visibility = View.GONE

            binding.wishTools.visibility = View.VISIBLE
            binding.recyclerView.visibility = View.VISIBLE
        } else {
            // 폴더 버튼이 선택된 경우
            if(isEditMode){
                // 편집 모드 해제 및 장바구니 뷰 상태 초기화
                isEditMode = !isEditMode
                wishAdapter.setEditMode(isEditMode)
                binding.editModeButtons.visibility = if (isEditMode) View.VISIBLE else View.GONE
                binding.wishEdit.text = "편집"
                binding.wishEdit.setTextColor(resources.getColor(R.color.edit_blue, null)) // 텍스트 색상 변경
                (activity as? MainActivity)?.setBottomNavigationViewVisibility(true) // main_bnv 보이기
                // RecyclerView의 높이를 동적으로 변경
                val layoutParams = binding.recyclerView.layoutParams
                layoutParams.height = if (isEditMode) dpToPx(500) else dpToPx(600)
                binding.recyclerView.layoutParams = layoutParams
            }
            // 폴더 버튼의 텍스트 색상을 활성화 색상으로 변경
            binding.btnCart.setTextColor(resources.getColor(R.color.unselectedTextColor))
            // 장바구니 버튼의 텍스트 색상을 비활성화 색상으로 변경
            binding.btnFolder.setTextColor(resources.getColor(R.color.selectedTextColor))

            // 장바구니 관련 뷰를 숨기고 폴더 관련 뷰를 표시
            binding.wishTools.visibility = View.GONE
            binding.recyclerView.visibility = View.GONE

            binding.nation.visibility = View.VISIBLE
            binding.line.visibility = View.VISIBLE
            binding.boardTools.visibility = View.VISIBLE
            binding.boardsRecyclerView.visibility = View.VISIBLE
        }
    }

    // 위시리스트 편집 모드를 토글하는 함수
    private fun toggleEditMode() {
        isEditMode = !isEditMode
        wishAdapter.setEditMode(isEditMode)
        // 편집 모드 버튼의 가시성을 설정
        binding.editModeButtons.visibility = if (isEditMode) View.VISIBLE else View.GONE
        if (isEditMode) {
            binding.wishItemText.text = "개 선택"
            binding.wishEdit.text = "취소"
            binding.wishEdit.setTextColor(resources.getColor(R.color.edit_red, null)) // 텍스트 색상 변경
            (activity as? MainActivity)?.setBottomNavigationViewVisibility(false) // main_bnv 숨기기
        } else {
            binding.wishItemText.text = "개의 위시템"
            binding.wishEdit.text = "편집"
            binding.wishEdit.setTextColor(resources.getColor(R.color.edit_blue, null)) // 텍스트 색상 변경
            (activity as? MainActivity)?.setBottomNavigationViewVisibility(true) // main_bnv 보이기
        }
        // RecyclerView의 높이를 동적으로 변경
        val layoutParams = binding.recyclerView.layoutParams
        layoutParams.height = if (isEditMode) dpToPx(500) else dpToPx(600)
        binding.recyclerView.layoutParams = layoutParams
    }

    // 보드 편집 모드를 토글하는 함수
    private fun toggleBoardEditMode() {
        isEditMode = !isEditMode
        boardsAdapter.setEditMode(isEditMode)
        // 보드 편집 모드 버튼의 가시성을 설정
        binding.boardEditModeButtons.visibility = if (isEditMode) View.VISIBLE else View.GONE
        if (isEditMode) {
            binding.boardEdit.text = "취소"
            binding.boardEdit.setTextColor(resources.getColor(R.color.edit_red, null)) // 텍스트 색상 변경
            (activity as? MainActivity)?.setBottomNavigationViewVisibility(false) // main_bnv 숨기기
        } else {
            binding.boardEdit.text = "편집"
            binding.boardEdit.setTextColor(resources.getColor(R.color.edit_blue, null)) // 텍스트 색상 변경
            (activity as? MainActivity)?.setBottomNavigationViewVisibility(true) // main_bnv 보이기
        }
        // RecyclerView의 높이를 동적으로 변경
        val layoutParams = binding.boardsRecyclerView.layoutParams
        layoutParams.height = if (isEditMode) dpToPx(387) else dpToPx(487)
        binding.boardsRecyclerView.layoutParams = layoutParams
    }

    // 아이템 선택 상태에 따라 버튼의 활성화를 업데이트하는 함수
    private fun updateItemButtonState(selectedItemCount: Int) {
        val isEnabled = selectedItemCount > 0
        if(isEditMode){
            binding.wishCount.text = selectedItemCount.toString()
        }
        // 선택된 아이템의 수에 따라 각 버튼의 활성화 상태와 투명도를 설정
        for (i in 0 until binding.editModeButtons.childCount) {
            val child = binding.editModeButtons.getChildAt(i)
            child.isEnabled = isEnabled
            if (isEnabled) {
                child.alpha = 1.0f
            } else {
                child.alpha = 0.5f
            }
        }
    }

    // 보드 선택 상태에 따라 버튼의 활성화를 업데이트하는 함수
    private fun updateBoardButtonState(selectedBoardCount: Int) {
        val isEnabled = selectedBoardCount > 0

        // 선택된 보드의 수에 따라 각 버튼의 활성화 상태와 투명도를 설정
        for (i in 0 until binding.boardEditModeButtons.childCount) {
            val child = binding.boardEditModeButtons.getChildAt(i)
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
        updateBoardButtonState(count)
    }

    // 선택된 아이템을 삭제하는 함수
    private fun deleteSelectedItems() {
        // 선택된 아이템을 삭제하고 편집 모드를 종료
        wishAdapter.deleteSelectedItems()
        isEditMode = !isEditMode
        wishAdapter.setEditMode(isEditMode)
        binding.editModeButtons.visibility = if (isEditMode) View.VISIBLE else View.GONE
        binding.wishEdit.text = "편집"
        binding.wishEdit.setTextColor(resources.getColor(R.color.edit_blue, null)) // 텍스트 색상 변경
        (activity as? MainActivity)?.setBottomNavigationViewVisibility(true) // main_bnv 보이기
        // RecyclerView의 높이를 동적으로 변경
        val layoutParams = binding.recyclerView.layoutParams
        layoutParams.height = if (isEditMode) dpToPx(500) else dpToPx(600)
        binding.recyclerView.layoutParams = layoutParams
        // 남은 아이템 개수를 업데이트
        binding.wishCount.text = wishAdapter.itemCount.toString()
    }

    // 삭제 확인 팝업을 표시하는 함수
    private fun showDeletePopup() {
        // 삭제 확인 팝업 레이아웃을 인플레이트하고 다이얼로그를 생성
        val dialogView = layoutInflater.inflate(R.layout.confirm_delete_popup, null)
        val dialog = Dialog(requireContext())
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(dialogView)

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
            deleteSelectedBoards()
            dialog.dismiss()
        }
        dialog.show()
    }

    // 선택된 보드를 삭제하는 함수
    private fun deleteSelectedBoards() {
        // 선택된 보드를 삭제하고 편집 모드를 종료
        boardsAdapter.deleteSelectedBoards()
        isEditMode = !isEditMode
        boardsAdapter.setEditMode(isEditMode)
        binding.boardEditModeButtons.visibility = if (isEditMode) View.VISIBLE else View.GONE
        binding.boardEdit.text = "편집"
        binding.boardEdit.setTextColor(resources.getColor(R.color.edit_blue, null)) // 텍스트 색상 변경
        (activity as? MainActivity)?.setBottomNavigationViewVisibility(true) // main_bnv 보이기
        // RecyclerView의 높이를 동적으로 변경
        val layoutParams = binding.boardsRecyclerView.layoutParams
        layoutParams.height = if (isEditMode) dpToPx(387) else dpToPx(487)
        binding.boardsRecyclerView.layoutParams = layoutParams
        // 남은 보드 개수를 업데이트
        binding.boardQuantity.text = boardsAdapter.itemCount.toString()
    }

    // 폴더에 추가하는 기능을 구현하는 함수
    private fun addToFolder(){

    }
    // Fragment가 DestroyView될 때 호출되는 메소드
    override fun onDestroyView() {
        super.onDestroyView()
    }

    // 위시 아이템 리스트를 생성하는 함수 (예제 데이터 사용)
    private fun getWishItems(): List<WishItem> {
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
        binding.wishCount.text = itemList.size.toString()
        return itemList
    }

    // 위시 보드 리스트를 생성하는 함수 (예제 데이터 사용)
    private fun getWishboards(): List<Wishboard> {
        val boardList = listOf(
            Wishboard(0, "보드1", 23, listOf(R.drawable.rv1, R.drawable.rv2, R.drawable.rv3)),
            Wishboard(1, "보드2", 18, listOf(R.drawable.rv1, R.drawable.rv2)),
            Wishboard(2, "보드3", 23, listOf(R.drawable.rv1)),
            Wishboard(3, "보드4", 18, listOf(R.drawable.rv1, R.drawable.rv2, R.drawable.rv3)),
            Wishboard(4, "보드5", 23, listOf(R.drawable.rv1, R.drawable.rv2)),
            Wishboard(5, "보드6", 18, listOf(R.drawable.rv1)),
        )
        // 보드 개수를 설정
        binding.boardQuantity.text = boardList.size.toString()
        return boardList
    }

    // dp를 px로 변환하는 함수
    private fun dpToPx(dp: Int): Int {
        val density = resources.displayMetrics.density
        return (dp * density).toInt()
    }
}
