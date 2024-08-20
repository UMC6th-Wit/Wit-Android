package com.umc.umc_6th_wit_android.home.notice

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.umc.umc_6th_wit_android.data.remote.notice.Notice
import com.umc.umc_6th_wit_android.databinding.ItemNoticeBinding

class NoticeRVAdapter (val items :List<Notice>)
: RecyclerView.Adapter<NoticeRVAdapter.PopupViewHolder>() {
    val TAG = "NoticeRVAdapter"

    override fun getItemCount(): Int = items.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PopupViewHolder {
        val itemBinding =
            ItemNoticeBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PopupViewHolder(itemBinding)
    }

    override fun onBindViewHolder(holder: PopupViewHolder, position: Int) {
        holder.itemBinding.noticeTitleTv.text = "[${items[position].title}]"
        holder.itemBinding.noticeContentTv.text = items[position].content
        val dateTime = items[position].createdAt
        val date = dateTime.substringBefore("T")
        holder.itemBinding.noticeTimeTv.text = date
    }

    inner class PopupViewHolder(val itemBinding: ItemNoticeBinding) :
        RecyclerView.ViewHolder(itemBinding.root) {
        init {
            /*RecyclerView 항목 클릭 시 외부 click 이벤트 리스너 호출*/
            itemBinding.root.setOnClickListener {
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
        fun onItemLongClick(view: View, position: Int): Boolean
    }

    var longClickListener: OnItemLongClickListener? = null

    fun setOnItemLongClickListener(listener: OnItemLongClickListener?) {
        this.longClickListener = listener
    }

    var listener: OnItemClickListener? = null

    interface OnItemClickListener {
        fun onItemClick(view: View, position: Int)
    }

    fun setOnItemClickListener(listener: OnItemClickListener?) {
        this.listener = listener
    }
}