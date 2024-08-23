package com.umc.umc_6th_wit_android.wish

import android.app.Activity
import android.app.Dialog
import android.content.Context
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
import android.widget.EditText
import android.widget.TextView
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.umc.umc_6th_wit_android.MainActivity
import com.umc.umc_6th_wit_android.R
import com.umc.umc_6th_wit_android.databinding.FragmentWishBinding
import com.umc.umc_6th_wit_android.login.TokenManager
import java.io.Serializable


// WishFragment 클래스: 위시리스트 및 보드의 UI를 관리하는 프래그먼트
class WishFragment : Fragment(), SelectionListener, WishView {
    // ViewBinding 객체 선언
    lateinit var binding: FragmentWishBinding
    // 어댑터 객체 선언
    private lateinit var wishAdapter: WishAdapter
    lateinit var boardsAdapter: BoardsAdapter
    private lateinit var folderPopUpAdapter: FolderPopUpAdapter
    // 편집 모드 상태 변수
    private var isEditMode = false
    private var isPopUp = false

    private val ADD_FOLDER_REQUEST_CODE = 1
    private val wishService = WishService()
    private lateinit var tokenManager: TokenManager

    // Fragment의 View를 생성하는 메소드
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        (activity as? MainActivity)?.setBottomNavigationViewVisibility(true) // main_bnv 보이기
        // ViewBinding을 이용해 Fragment의 레이아웃을 설정
        binding = FragmentWishBinding.inflate(inflater, container, false)
        wishService.setWishView(this)
        // TokenManager 초기화
        tokenManager = TokenManager(requireContext().getSharedPreferences("auth_prefs", Context.MODE_PRIVATE))
        return binding.root
    }

    // View가 생성된 후 호출되는 메소드
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // FragmentResultListener를 통해 데이터를 수신
        parentFragmentManager.setFragmentResultListener("wishFragmentKey", this) { key, bundle ->
            val result = bundle.getString("resultKey")
            if (result == "refresh") {
                // 상태 변경 및 필요한 작업 수행
                boardsAdapter.resetBoards()
                loadMoreBoards(1, 20)
            }
        }

        // 위시리스트와 보드의 레이아웃을 GridLayout으로 설정
        binding.recyclerView.layoutManager = GridLayoutManager(context, 2)
        binding.boardsRecyclerView.layoutManager = GridLayoutManager(context, 2)

        // 어댑터를 초기화하고 RecyclerView에 설정
        wishAdapter = WishAdapter(requireContext(), mutableListOf(), this) { currentCursor, limit ->
            loadMoreItems(currentCursor, limit)
        }

        boardsAdapter = BoardsAdapter(mutableListOf(), this) { currentCursor, limit ->
            loadMoreBoards(currentCursor, limit)
        }
        binding.recyclerView.adapter = wishAdapter
        binding.boardsRecyclerView.adapter = boardsAdapter


        // 아이템과 보드의 선택 상태에 따른 버튼 상태 업데이트
        updateItemButtonState(wishAdapter.selectedItems.size)
        updateBoardButtonState(boardsAdapter.selectedBoards.size)

        // 초기 버튼 상태 설정 (장바구니 버튼이 선택된 상태)
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

        // 보드 추가 버튼 클릭 이벤트 설정
        binding.boardAdd.setOnClickListener {
            val intent = Intent(activity, FolderActivity::class.java)

            // 빈 리스트 생성 및 추가
            val emptyList = ArrayList<WishItem>()
            intent.putExtra("selected_items", emptyList)
            startActivityForResult(intent, ADD_FOLDER_REQUEST_CODE)
        }

        // 보드 편집 버튼 클릭 이벤트 설정
        binding.boardEdit.setOnClickListener {
            toggleBoardEditMode()
        }

        // 보드 이름 수정 버튼 클릭 이벤트 설정
        binding.btnBoardRename.setOnClickListener {
            showReNamePopup()
        }

        // 보드 삭제 버튼 클릭 이벤트 설정
        binding.btnBoardDelete.setOnClickListener {
            showDeletePopup()
        }

        // 뒤로가기 버튼 처리 search->home
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                // MainActivity의 selectHomeFragment() 호출
                (activity as? MainActivity)?.selectHomeFragment()
            }
        })
    }

    fun loadMoreItems(cursor: Int?, limit: Int?) {
        val accessToken = tokenManager.getAccessToken()
        if (accessToken != null) {
            wishService.getWishList(accessToken, cursor, limit)
        }
    }

    fun loadMoreBoards(cursor: Int?, limit: Int?) {
        val accessToken = tokenManager.getAccessToken()
        if (accessToken != null) {
            wishService.getWishBoardList(accessToken, cursor, limit)
        }
    }

    // Activity 결과 처리
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == ADD_FOLDER_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            Log.d("folderAdd", "success")
            boardsAdapter.resetBoards()
            loadMoreBoards(1, 20)
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

    // 뷰 상태 설정 함수
    private fun setView(selected: Boolean) {
        if (selected) {
            // 장바구니 버튼이 선택된 경우
            if (isEditMode) {
                toggleBoardEditMode()
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
            // 처음 데이터
            wishAdapter.resetItems()
            loadMoreItems(1, 20)
        } else {
            // 폴더 버튼이 선택된 경우
            if (isEditMode) {
                toggleEditMode()
            }
            isPopUp = false
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
            // 처음 데이터 로드
            boardsAdapter.resetBoards()
            loadMoreBoards(1, 20)
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
    }

    // 아이템 선택 상태에 따라 버튼의 활성화를 업데이트하는 함수
    private fun updateItemButtonState(selectedItemCount: Int) {
        val isEnabled = selectedItemCount > 0
        if (isEditMode) {
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
        val isEnabledOne = selectedBoardCount == 1


        binding.btnBoardDelete.isEnabled = isEnabled
        binding.btnBoardRename.isEnabled = isEnabledOne
        if (isEnabled) {
            binding.btnBoardDelete.alpha = 1.0f
        } else {
            binding.btnBoardDelete.alpha = 0.5f
        }
        if (isEnabledOne) {
            binding.btnBoardRename.alpha = 1.0f
        } else {
            binding.btnBoardRename.alpha = 0.5f
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
        val request = WishBoardListDelRequest(
            product_ids = wishAdapter.selectedItems.map { it.product_id }
        )
        val accessToken = tokenManager.getAccessToken()
        if (accessToken != null) {
            wishService.delCart(accessToken, request)
        }
        // 선택된 아이템을 삭제하고 편집 모드를 종료
        wishAdapter.deleteSelectedItems()
        toggleEditMode()
        // 남은 아이템 개수를 업데이트
        binding.wishCount.text = wishAdapter.itemCount.toString()
    }

    // 보드 이름 수정 팝업을 표시하는 함수
    private fun showReNamePopup() {
        // 삭제 확인 팝업 레이아웃을 인플레이트하고 다이얼로그를 생성
        val dialogView = layoutInflater.inflate(R.layout.confirm_rename_popup, null)
        val dialog = Dialog(requireContext())
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(dialogView)

        // 팝업 타이틀과 메시지 설정
        val tvPopupTitle = dialogView.findViewById<TextView>(R.id.tv_popup_title)
        val tvPopupText = dialogView.findViewById<EditText>(R.id.folder_name_edit_text)
        tvPopupTitle.text = "폴더 이름 수정"
        tvPopupText.setText(boardsAdapter.returnNameSelectedBoard().folder_name)

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
        // 이름 변경 버튼 클릭 이벤트 리스너 설정
        dialogView.findViewById<Button>(R.id.btn_board_rename).setOnClickListener {
            // 폴더 이름 변경 로직
            val wishService = WishService()
            wishService.setWishView(this)
            val request = WishListEditRequest(
                folder_id = boardsAdapter.selectedBoards.first().folder_id,
                new_folder_name = tvPopupText.text.toString()
            )
            val accessToken = tokenManager.getAccessToken()
            if (accessToken != null) {
                wishService.postWishListReName(accessToken, request)
            }
            renameSelectedBoard(tvPopupText.text.toString())
            dialog.dismiss()
        }
        dialog.show()
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
        tvPopupTitle.text = "해당 폴더를 삭제할까요?"
        tvPopupMessage.text = "해당 폴더에 있는 제품이 모두 삭제됩니다."

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
            val accessToken = tokenManager.getAccessToken()

            val request = WishBoardDelRequest(
                folder_ids = boardsAdapter.selectedBoards.map { it.folder_id }
            )
            // 폴더 삭제 로직
            if (accessToken != null) {
                wishService.delWishBoardList(accessToken, request)
            }
            deleteSelectedBoards()
            dialog.dismiss()
        }
        dialog.show()
    }

    private fun renameSelectedBoard(edited_name: String){
        // 선택된 보드 이름을 바꾸고 편집 모드를 종료
        boardsAdapter.reNameSelectedBoard(edited_name)
        toggleBoardEditMode()
    }

    // 선택된 보드를 삭제하는 함수
    private fun deleteSelectedBoards() {
        // 선택된 보드를 삭제하고 편집 모드를 종료
        boardsAdapter.deleteSelectedBoards()
        toggleBoardEditMode()
        // 남은 보드 개수를 업데이트
        binding.boardQuantity.text = boardsAdapter.itemCount.toString()
    }

    // 폴더에 추가하는 기능을 구현하는 함수
    private fun addToFolder() {
        // 폴더 담기 팝업 레이아웃을 인플레이트하고 다이얼로그를 생성
        val dialogView = layoutInflater.inflate(R.layout.folder_select_popup, null)
        val dialog = Dialog(requireContext())
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(dialogView)

        isPopUp = true

        //다이얼로그 어댑터 연결
        folderPopUpAdapter = FolderPopUpAdapter(mutableListOf(), this) { currentCursor, limit ->
            loadMoreItems(currentCursor, limit)
        }
        val recyclerView = dialogView.findViewById<RecyclerView>(R.id.folder_recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(requireContext()) // LayoutManager 설정
        recyclerView.adapter = folderPopUpAdapter
        // 처음 데이터 로드
        folderPopUpAdapter.resetBoards()
        loadMoreBoards(1, 20)

        // 팝업 창을 중앙에 배치하고 가로 넓이를 300dp로 설정
        val metrics = resources.displayMetrics
        val width = (300 * metrics.density).toInt()

        dialog.window?.setLayout(
            width,
            WindowManager.LayoutParams.WRAP_CONTENT
        )
        dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
        dialog.window?.attributes?.gravity = Gravity.CENTER

        // 새 폴더 버튼 클릭 이벤트 리스너 설정
        dialogView.findViewById<Button>(R.id.btn_new_folder).setOnClickListener {
            val intent = Intent(activity, FolderActivity::class.java)

            // getSelectedItems()의 리턴값을 ArrayList로 변환하여 추가
            val selectedItemsList = ArrayList(wishAdapter.selectedItems)
            intent.putExtra("selected_items", selectedItemsList as Serializable)

            startActivityForResult(intent, ADD_FOLDER_REQUEST_CODE)
            isPopUp = false
            if (isEditMode) {
                toggleEditMode()
            }
            dialog.dismiss()
        }
        // 담기 완료 버튼 클릭 이벤트 리스너 설정
        dialogView.findViewById<Button>(R.id.btn_add_folder).setOnClickListener {
            val request = WishListAddRequest(
                product_ids = wishAdapter.selectedItems.map { it.product_id },
                folder_id = folderPopUpAdapter.selectedFolders.map { it.folder_id }
            )
            val accessToken = tokenManager.getAccessToken()
            if (accessToken != null) {
                wishService.postWishtoBoard(accessToken, request)
            }
            isPopUp = false
            if (isEditMode) {
                toggleEditMode()
            }
            dialog.dismiss()
        }
        dialog.show()
    }

    // Fragment가 DestroyView될 때 호출되는 메소드
    override fun onDestroyView() {
        super.onDestroyView()
    }

    // 위시 아이템 리스트를 생성하는 함수 (예제 데이터 사용)
    private fun getWishItems(): List<WishItem> {
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
        binding.wishCount.text = itemList.size.toString()
        return itemList
    }

    // 위시 보드 리스트를 생성하는 함수 (예제 데이터 사용)
    private fun getWishboards(): List<Wishboard> {
        val boardList = listOf(
            Wishboard(0, "보드1", listOf(), 1),
            Wishboard(1, "보드2", listOf(), 1),
            Wishboard(2, "보드3", listOf(), 1),
            Wishboard(3, "보드4", listOf(), 1),
            Wishboard(4, "보드5", listOf(), 1),
            Wishboard(5, "보드6", listOf(), 1),
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

    //위시리스트 장바구니 목록 조회 성공
    override fun onGetWishListSuccess(code: String, result: WishItemResult) {
        Log.d("cart", result.nextCursor.toString())
        wishAdapter.addItems(result.products)
        wishAdapter.currentCursor = result.nextCursor
        binding.wishCount.text = result.count.toString()
    }

    //위시리스트 장바구니 목록 조회 실패
    override fun onGetWishListFailure(code: String, message: String){
        Log.d("cart", "Fail")
    }

    //위시리스트 폴더 조회 성공
    override fun onGetWishBoardListSuccess(code: String, result: WishBoardItemResult) {
        if(isPopUp){
            Log.d("board_popup", "Success")
            folderPopUpAdapter.addBoards(result.folders)
            folderPopUpAdapter.currentCursor = result.nextCursor
        } else {
            Log.d("board", "Success")
            boardsAdapter.addBoards(result.folders)
            boardsAdapter.currentCursor = result.nextCursor
            binding.boardQuantity.text = result.count.toString()
        }
    }

    //위시리스트 폴더 조회 실패
    override fun onGetWishBoardListFailure(code: String, message: String) {
        Log.d("board", "Fail")
    }

    //위시리스트 폴더 상품 담기 성공
    override fun onPostWishtoBoardSuccess(code: String, result: WishBoardItemResult) {
        TODO("Not yet implemented")
    }

    //위시리스트 폴더 상품 담기 실패
    override fun onPostWishtoBoardFailure(code: String, message: String) {
        TODO("Not yet implemented")
    }

    //위시리스트 폴더 이름 변경 성공
    override fun onPostWishListReNameSuccess(code: String, message: String) {
        TODO("Not yet implemented")
    }

    //위시리스트 폴더 이름 변경 실패
    override fun onPostWishListReNameFailure(code: String, message: String) {
        TODO("Not yet implemented")
    }

    //위시리스트 폴더 삭제 성공
    override fun onDeleteWishBoardListSuccess(code: String, message: String) {
        TODO("Not yet implemented")
    }

    //위시리스트 폴더 삭제 실패
    override fun onDeleteWishBoardListFailure(code: String, message: String) {
        TODO("Not yet implemented")
    }

    //위시리스트 장바구니 추가 성공
    override fun onPostAddCartSuccess(code: String, response: CartItem) {
        TODO("Not yet implemented")
    }

    //위시리스트 장바구니 추가 실패
    override fun onPostAddCartFailure(code: String, error: String) {
        TODO("Not yet implemented")
    }

    override fun onPostDelCartSuccess(code: String, message: String) {
        TODO("Not yet implemented")
    }

    override fun onPostDelCartFailure(code: String, message: String) {
        TODO("Not yet implemented")
    }
}
