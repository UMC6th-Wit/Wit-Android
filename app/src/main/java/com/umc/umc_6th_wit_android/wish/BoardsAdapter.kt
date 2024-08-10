package com.umc.umc_6th_wit_android.wish

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.umc.umc_6th_wit_android.MainActivity
import com.umc.umc_6th_wit_android.R
import com.umc.umc_6th_wit_android.databinding.BoardWishBinding

// BoardsAdapter 클래스 정의: RecyclerView.Adapter를 상속받아 Wishboard 리스트를 관리합니다.
class BoardsAdapter(private var wishboards: List<Wishboard>, private val selectionListener: SelectionListener) : RecyclerView.Adapter<BoardsAdapter.BoardViewHolder>() {

    // 선택된 보드들을 저장하는 MutableSet
    val selectedBoards = mutableSetOf<Wishboard>()
    // 편집 모드 여부를 나타내는 변수
    private var isEditMode = false

    // ViewHolder를 생성합니다.
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BoardViewHolder {
        val binding = BoardWishBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return BoardViewHolder(binding)
    }

    // ViewHolder와 데이터를 바인딩합니다.
    override fun onBindViewHolder(holder: BoardViewHolder, position: Int) {
        holder.bind(wishboards[position])
    }

    // 아이템 개수를 반환합니다.
    override fun getItemCount() = wishboards.size

    // 새로운 보드를 추가합니다.
    fun addBoard(wishboard: Wishboard) {
        // 새로운 보드의 ID를 마지막 보드의 ID + 1로 설정
        wishboard.id = wishboards.last().id + 1
        // 새로운 보드를 추가한 리스트로 교체
        wishboards = wishboards.toMutableList().apply { add(wishboard) }
        // 새로운 아이템 삽입을 어댑터에 알림
        notifyItemInserted(wishboards.size - 1)
        // 데이터가 변경되었음을 어댑터에 알림
        notifyDataSetChanged()
    }

    // 편집 모드를 설정합니다.
    fun setEditMode(isEditMode: Boolean) {
        this.isEditMode = isEditMode
        // 편집 모드 변경을 어댑터에 알림
        notifyDataSetChanged()
    }

    // 선택된 보드를 삭제합니다.
    fun deleteSelectedBoards() {
        // items를 MutableList로 변환하여 selectedItems를 제거합니다.
        val mutableBoards = wishboards.toMutableList()
        mutableBoards.removeAll(selectedBoards)
        selectedBoards.clear()
        // 새로운 리스트로 items를 대체하고 어댑터에 변경 사항을 알립니다.
        updateBoards(mutableBoards)
    }

    // 보드를 선택합니다.
    fun selectBoard(board: Any) {
        selectedBoards.add(board as Wishboard)
        // 선택된 보드 개수 변경을 리스너에 알림
        selectionListener.onSelectionBoardChanged(selectedBoards.size)
    }

    // 보드 선택을 해제합니다.
    fun deselectBoard(board: Any) {
        selectedBoards.remove(board)
        // 선택된 보드 개수 변경을 리스너에 알림
        selectionListener.onSelectionBoardChanged(selectedBoards.size)
    }

    // 보드 리스트를 업데이트합니다.
    private fun updateBoards(newBoards: List<Wishboard>) {
        wishboards = newBoards
        // 데이터가 변경되었음을 어댑터에 알림
        notifyDataSetChanged()
    }

    // ViewHolder 클래스 정의: 보드 아이템의 뷰를 관리합니다.
    inner class BoardViewHolder(private val binding: BoardWishBinding) : RecyclerView.ViewHolder(binding.root) {
        // 보드를 바인딩합니다.
        fun bind(board: Wishboard) {
            // 보드 제목을 설정
            binding.boardTitle.text = board.title
            // 보드 수량을 설정
            binding.boardQuantity.text = board.quantity.toString()

            // 이미지를 설정합니다.
            if (board.images.isNotEmpty()) {
                binding.boardImage1.setImageResource(board.images[0])
            } else {
                binding.boardImage1.setImageResource(R.drawable.empty)
            }

            if (board.images.size > 1) {
                binding.boardImage2.setImageResource(board.images[1])
            } else {
                binding.boardImage2.setImageResource(R.drawable.empty)
            }

            if (board.images.size > 2) {
                binding.boardImage3.setImageResource(board.images[2])
            } else {
                binding.boardImage3.setImageResource(R.drawable.empty)
            }

            // 편집 모드에 따른 UI 설정을 합니다.
            if (isEditMode) {
                // 편집 모드일 때 아이템을 반투명하게 표시합니다.
                binding.boardImage.setBackgroundColor(Color.parseColor("#80FFFFFF")) // 투명도 50%의 하얀 색상
                binding.boardImage1.setColorFilter(Color.parseColor("#80FFFFFF")) // 투명도 50%의 하얀 색상
                binding.boardImage2.setColorFilter(Color.parseColor("#80FFFFFF")) // 투명도 50%의 하얀 색상
                binding.boardImage3.setColorFilter(Color.parseColor("#80FFFFFF")) // 투명도 50%의 하얀 색상
                binding.boardImage1.setBackgroundColor(Color.parseColor("#80E9E9E9"))
                binding.boardImage2.setBackgroundColor(Color.parseColor("#80E9E9E9"))
                binding.boardImage3.setBackgroundColor(Color.parseColor("#80E9E9E9"))
            } else {
                // 편집 모드가 아닐 때 아이템을 원래대로 돌립니다.
                binding.icBoardSelect.visibility = View.GONE
                selectedBoards.remove(board)
                binding.boardImage.setBackgroundColor(Color.parseColor("#FFFFFF")) // 하얀 색상
                binding.boardImage1.clearColorFilter() // 효과를 원래대로 되돌림
                binding.boardImage2.clearColorFilter() // 효과를 원래대로 되돌림
                binding.boardImage3.clearColorFilter() // 효과를 원래대로 되돌림
                binding.boardImage1.setBackgroundColor(Color.parseColor("#E9E9E9"))
                binding.boardImage2.setBackgroundColor(Color.parseColor("#E9E9E9"))
                binding.boardImage3.setBackgroundColor(Color.parseColor("#E9E9E9"))
            }

            // 아이템 클릭 리스너를 설정합니다.
            itemView.setOnClickListener {
                if (isEditMode) {
                    // 편집 모드일 때
                    if (selectedBoards.contains(board)) {
                        // 선택된 보드를 선택 해제합니다.
                        deselectBoard(board)
                        selectedBoards.remove(board)
                        binding.icBoardSelect.visibility = View.GONE
                        binding.boardImage.setBackgroundColor(Color.parseColor("#80FFFFFF")) // 투명도 50%의 하얀 색상
                        binding.boardImage1.setColorFilter(Color.parseColor("#80FFFFFF")) // 투명도 50%의 하얀 색상
                        binding.boardImage2.setColorFilter(Color.parseColor("#80FFFFFF")) // 투명도 50%의 하얀 색상
                        binding.boardImage3.setColorFilter(Color.parseColor("#80FFFFFF")) // 투명도 50%의 하얀 색상
                        binding.boardImage1.setBackgroundColor(Color.parseColor("#80E9E9E9"))
                        binding.boardImage2.setBackgroundColor(Color.parseColor("#80E9E9E9"))
                        binding.boardImage3.setBackgroundColor(Color.parseColor("#80E9E9E9"))
                    } else {
                        // 선택되지 않은 보드를 선택합니다.
                        selectBoard(board)
                        selectedBoards.add(board)
                        binding.icBoardSelect.visibility = View.VISIBLE
                        binding.boardImage1.setColorFilter(Color.parseColor("#80000000")) // 투명도 50%의 검은 색상
                        binding.boardImage2.setColorFilter(Color.parseColor("#80000000")) // 투명도 50%의 검은 색상
                        binding.boardImage3.setColorFilter(Color.parseColor("#80000000")) // 투명도 50%의 검은 색상
                        binding.boardImage1.setBackgroundColor(Color.parseColor("#80000000"))
                        binding.boardImage2.setBackgroundColor(Color.parseColor("#80000000"))
                        binding.boardImage3.setBackgroundColor(Color.parseColor("#80000000"))
                    }
                } else {
                    // 편집 모드가 아닐 때
                    val boardTitle = board.title
                    val context = itemView.context
                    if (context is MainActivity) {
                        // MainActivity의 changeWishListFragment 메서드를 호출하여 화면을 변경
                        context.changeWishListFragment(boardTitle)
                    }
                }
            }
        }
    }
}
