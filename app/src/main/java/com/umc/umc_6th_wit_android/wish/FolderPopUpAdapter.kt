package com.umc.umc_6th_wit_android.wish

import android.app.Dialog
import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.umc.umc_6th_wit_android.MainActivity
import com.umc.umc_6th_wit_android.databinding.BoardWishBinding
import com.umc.umc_6th_wit_android.databinding.FolderSelectPopupBinding
import com.umc.umc_6th_wit_android.databinding.ItemPopupBinding
import com.umc.umc_6th_wit_android.databinding.ItemRadioFolderBinding

class FolderPopUpAdapter(private var folders: List<Wishboard>, private val selectionListener: SelectionListener) : RecyclerView.Adapter<FolderPopUpAdapter.FolderPopUpViewHolder>() {

    // 선택된 보드들을 저장하는 MutableSet
    val selectedFolders = mutableSetOf<Wishboard>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FolderPopUpViewHolder {
        val binding = ItemRadioFolderBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return FolderPopUpViewHolder(binding)
    }

    override fun getItemCount(): Int = folders.size

    override fun onBindViewHolder(holder: FolderPopUpViewHolder, position: Int) {
        holder.bind(folders[position], position)
    }

    // 폴더를 선택합니다.
    fun selectBoard(board: Wishboard) {
        selectedFolders.add(board)
        // 선택된 보드 개수 변경을 리스너에 알림
        selectionListener.onSelectionBoardChanged(selectedFolders.size)
    }

    // 폴더 선택을 해제합니다.
    fun deselectBoard(board: Wishboard) {
        selectedFolders.remove(board)
        // 선택된 보드 개수 변경을 리스너에 알림
        selectionListener.onSelectionBoardChanged(selectedFolders.size)
    }

    // 선택된 폴더를 반환합니다.
    fun returnNameSelectedBoard() = selectedFolders

    inner class FolderPopUpViewHolder(private val binding: ItemRadioFolderBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(board: Wishboard, position: Int) {
            binding.rgBtn1.text = board.folder_Name

            // 첫 번째 아이템의 경우 folder_line을 GONE으로 설정
            if (position == 0) {
                binding.folderLine.visibility = View.GONE
            } else {
                binding.folderLine.visibility = View.VISIBLE
            }

            // 라디오 버튼 클릭 리스너 추가
            binding.rgBtn1.setOnClickListener {
                if (binding.rgBtn1.isChecked) {
                    // 이미 체크되어 있으면 체크 해제하고 deselectBoard 호출
                    binding.rgBtn1.isChecked = false
                    deselectBoard(board)
                } else {
                    // 체크되어 있지 않으면 체크하고 selectBoard 호출
                    binding.rgBtn1.isChecked = true
                    selectBoard(board)
                }
            }
        }
    }
}
