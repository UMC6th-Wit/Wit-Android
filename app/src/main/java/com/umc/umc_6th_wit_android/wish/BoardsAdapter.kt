package com.umc.umc_6th_wit_android.wish

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.umc.umc_6th_wit_android.MainActivity
import com.umc.umc_6th_wit_android.R
import com.umc.umc_6th_wit_android.databinding.BoardWishBinding

// BoardsAdapter.kt

class BoardsAdapter(private var wishboards: List<Wishboard>, private val selectionListener: SelectionListener) :  RecyclerView.Adapter<BoardsAdapter.BoardViewHolder>() {

    val selectedBoards = mutableSetOf<Wishboard>()
    private var isEditMode = false

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BoardViewHolder {
        val binding = BoardWishBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return BoardViewHolder(binding)
    }

    override fun onBindViewHolder(holder: BoardViewHolder, position: Int) {
        holder.bind(wishboards[position])
    }

    override fun getItemCount() = wishboards.size

    fun addBoard(wishboard: Wishboard) {
        wishboard.id = wishboards.last().id+1
        wishboards = wishboards.toMutableList().apply { add(wishboard) }
        notifyItemInserted(wishboards.size - 1)
        notifyDataSetChanged()
    }

    fun setEditMode(isEditMode: Boolean) {
        this.isEditMode = isEditMode
        notifyDataSetChanged()
    }

    fun deleteSelectedBoards() {
        // items를 MutableList로 변환하여 selectedItems 제거
        val mutableBoards = wishboards.toMutableList()
        mutableBoards.removeAll(selectedBoards)
        selectedBoards.clear()
        // 새로운 리스트로 items 대체하고 어댑터에 변경 사항 알림
        updateBoards(mutableBoards)
    }

    fun selectBoard(board: Any) {
        selectedBoards.add(board as Wishboard)
        selectionListener.onSelectionBoardChanged(selectedBoards.size)
    }

    fun deselectBoard(board: Any) {
        selectedBoards.remove(board)
        selectionListener.onSelectionBoardChanged(selectedBoards.size)
    }

    private fun updateBoards(newBoards: List<Wishboard>) {
        wishboards = newBoards
        notifyDataSetChanged()
    }

    inner class BoardViewHolder(private val binding: BoardWishBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(board: Wishboard){
            binding.boardTitle.text = board.title
            binding.boardQuantity.text = board.quantity.toString()
            // 이미지를 설정합니다
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

            if(!isEditMode && board.id == wishboards.last().id){
                binding.boardQuantity.setPadding(0, 0, 0, 250)
            } else {
                binding.boardQuantity.setPadding(0, 0, 0, 10)
            }

            if(isEditMode){
                binding.boardImage.setBackgroundColor(Color.parseColor("#80FFFFFF")) // 투명도 50%의 하얀 색상
                binding.boardImage1.setColorFilter(Color.parseColor("#80FFFFFF")) // 투명도 50%의 하얀 색상
                binding.boardImage2.setColorFilter(Color.parseColor("#80FFFFFF")) // 투명도 50%의 하얀 색상
                binding.boardImage3.setColorFilter(Color.parseColor("#80FFFFFF")) // 투명도 50%의 하얀 색상
                binding.boardImage1.setBackgroundColor(Color.parseColor("#80E9E9E9"))
                binding.boardImage2.setBackgroundColor(Color.parseColor("#80E9E9E9"))
                binding.boardImage3.setBackgroundColor(Color.parseColor("#80E9E9E9"))
            } else {
                binding.icBoardSelect.visibility = View.GONE
                selectedBoards.remove(board)
                binding.boardImage.setBackgroundColor(Color.parseColor("#FFFFFF")) //하얀 색상
                binding.boardImage1.clearColorFilter() // 효과를 원래대로 되돌림
                binding.boardImage2.clearColorFilter() // 효과를 원래대로 되돌림
                binding.boardImage3.clearColorFilter() // 효과를 원래대로 되돌림
                binding.boardImage1.setBackgroundColor(Color.parseColor("#E9E9E9"))
                binding.boardImage2.setBackgroundColor(Color.parseColor("#E9E9E9"))
                binding.boardImage3.setBackgroundColor(Color.parseColor("#E9E9E9"))
            }
            itemView.setOnClickListener {
                if (isEditMode) {
                    if (selectedBoards.contains(board)) {
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
                        selectBoard(board)
                        selectedBoards.add(board)
                        binding.icBoardSelect.visibility = View.VISIBLE
//                        binding.boardImage.setBackgroundColor(Color.parseColor("#80000000")) // 투명도 50%의 검은 색상
                        binding.boardImage1.setColorFilter(Color.parseColor("#80000000")) // 투명도 50%의 검은 색상
                        binding.boardImage2.setColorFilter(Color.parseColor("#80000000")) // 투명도 50%의 검은 색상
                        binding.boardImage3.setColorFilter(Color.parseColor("#80000000")) // 투명도 50%의 검은 색상
                        binding.boardImage1.setBackgroundColor(Color.parseColor("#80000000"))
                        binding.boardImage2.setBackgroundColor(Color.parseColor("#80000000"))
                        binding.boardImage3.setBackgroundColor(Color.parseColor("#80000000"))
                    }
                } else {
                    val boardTitle = board.title
                    val context = itemView.context
                    if (context is MainActivity) {
                        context.changeWishListFragment(boardTitle)
                    }
                }
            }
        }
    }
}
