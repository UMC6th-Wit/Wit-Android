package com.umc.umc_6th_wit_android.search

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.umc.umc_6th_wit_android.databinding.ItemSearchBinding

class SearchRVAdapter (val items : ArrayList<String>)
    : RecyclerView.Adapter<SearchRVAdapter.SearchViewHolder>() {
    val TAG = "SearchRVAdapter"

    override fun getItemCount(): Int = items.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchViewHolder {
        val itemBinding = ItemSearchBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return SearchViewHolder(itemBinding)
    }

    override fun onBindViewHolder(holder: SearchViewHolder, position: Int) {
        holder.itemBinding.tv.text = items[position]

        // 삭제 버튼 클릭 리스너 설정
        holder.itemBinding.deleteBtn.setOnClickListener {
            removeItem(position)
        }
    }
    // 아이템 삭제 메서드
    private fun removeItem(position: Int) {
        items.removeAt(position) // 리스트에서 아이템 제거
        notifyItemRemoved(position) // 어댑터에 변경 사항 알림
        notifyItemRangeChanged(position, items.size) // 나머지 아이템의 위치 변경 알림
    }
    inner class SearchViewHolder(val itemBinding: ItemSearchBinding)
        : RecyclerView.ViewHolder(itemBinding.root) {
        init {
            /*RecyclerView 항목 클릭 시 외부 click 이벤트 리스너 호출*/
            itemBinding.root.setOnClickListener{
                listener?.onItemClick(it, adapterPosition)  // RecyclerView 항목 클릭 시 외부에서 지정한 리스너 호출
                Log.d(TAG, items[adapterPosition].toString())
            }
            itemBinding.root.setOnLongClickListener {
                Log.d(TAG, "long Click")
                longClickListener?.onItemLongClick(it, adapterPosition)
                true
            }

        }
    }

    interface OnItemLongClickListener {
        fun onItemLongClick(view: View, position: Int) : Boolean
    }
    var longClickListener: OnItemLongClickListener? = null

    fun setOnItemLongClickListener (listener: OnItemLongClickListener?) {
        this.longClickListener = listener
    }

    var listener : OnItemClickListener? = null

    interface OnItemClickListener {
        fun onItemClick(view : View, position : Int)
    }

    fun setOnItemClickListener(listener: OnItemClickListener?) {
        this.listener = listener
    }
}