package com.umc.umc_6th_wit_android.home

import android.R
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.umc.umc_6th_wit_android.data.local.PopupDto
import com.umc.umc_6th_wit_android.databinding.ItemPopupBinding


class HomePopupRVAdapter (val items : ArrayList<PopupDto>)
    : RecyclerView.Adapter<HomePopupRVAdapter.PopupViewHolder>() {
    val TAG = "HomePopupRVAdapter"

    override fun getItemCount(): Int = items.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PopupViewHolder {
        val itemBinding = ItemPopupBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PopupViewHolder(itemBinding)
    }

    override fun onBindViewHolder(holder: PopupViewHolder, position: Int) {
        holder.itemBinding.popupTv.text = items[position].title
        holder.itemBinding.popupTimeTv.text = items[position].time.toString() + "시간 전"

        // 삭제 버튼 클릭 리스너 설정
        holder.itemBinding.popupDeleteIv.setOnClickListener {
            removeItem(position)
        }
    }

    // 아이템 제거를 알리기 위한 콜백 인터페이스
    interface OnItemRemovedListener {
        fun onItemRemoved(newCount: Int)
    }

    var itemRemovedListener: OnItemRemovedListener? = null
    // 아이템 삭제 메서드
    private fun removeItem(position: Int) {
        items.removeAt(position) // 리스트에서 아이템 제거
        notifyItemRemoved(position) // 어댑터에 변경 사항 알림
        notifyItemRangeChanged(position, items.size) // 나머지 아이템의 위치 변경 알림
        // 아이템 제거를 알림
        itemRemovedListener?.onItemRemoved(items.size)
    }
    inner class PopupViewHolder(val itemBinding: ItemPopupBinding)
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