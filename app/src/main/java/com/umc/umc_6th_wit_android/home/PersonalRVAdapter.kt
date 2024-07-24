package com.umc.umc_6th_wit_android.home

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.umc.umc_6th_wit_android.databinding.ItemHomePersonalBinding
import ddwu.com.mobile.finalreport.data.PersonalDto

class PersonalRVAdapter(val items : ArrayList<PersonalDto>, val rvType : String)
    : RecyclerView.Adapter<PersonalRVAdapter.PersonalViewHolder>() {
    val TAG = "PersonalRVAdapter"

    override fun getItemCount(): Int = items.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PersonalViewHolder {
        val itemBinding = ItemHomePersonalBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PersonalViewHolder(itemBinding)
    }

    override fun onBindViewHolder(holder: PersonalViewHolder, position: Int) {
        val context = holder.itemBinding.root.context
        val heightInDp = 160
        val heightInPx = (heightInDp * context.resources.displayMetrics.density).toInt()

        if (rvType == "food") {
            val layoutParams = holder.itemBinding.itemCoverImgCv.layoutParams
            layoutParams.height = heightInPx
            holder.itemBinding.itemCoverImgCv.layoutParams = layoutParams
        }
        holder.itemBinding.itemTitleTv.text = items[position].title
        holder.itemBinding.itemYenTv.text = items[position].yen + "¥"
        holder.itemBinding.itemStarTv.text = items[position].star
        holder.itemBinding.itemReviewNumTv.text = "(" + items[position].reviewNum.toString() + ")"
        holder.itemBinding.itemCoverImgIv.setImageResource(items[position].image)
    }

    inner class PersonalViewHolder(val itemBinding: ItemHomePersonalBinding)
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