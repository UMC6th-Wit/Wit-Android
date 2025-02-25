package com.umc.umc_6th_wit_android.home

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.umc.umc_6th_wit_android.data.remote.home.Product
import com.umc.umc_6th_wit_android.data.remote.home.ProductVer2
import com.umc.umc_6th_wit_android.databinding.ItemHomeCustomBinding
import ddwu.com.mobile.finalreport.data.PersonalDto

class HomeCustomRVAdapter<T>(val items: ArrayList<T>, val rvType: String) : RecyclerView.Adapter<HomeCustomRVAdapter<T>.CustomViewHolder>() {
    val TAG = "HomeCustomRVAdapter"

    override fun getItemCount(): Int = items.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomViewHolder {
        val itemBinding = ItemHomeCustomBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CustomViewHolder(itemBinding)
    }

    override fun onBindViewHolder(holder: CustomViewHolder, position: Int) {
        val context = holder.itemBinding.root.context
        val heightInDp = 160
        val heightInPx = (heightInDp * context.resources.displayMetrics.density).toInt()

        if (rvType == "food") {
            val layoutParams = holder.itemBinding.itemCoverImgCv.layoutParams
            layoutParams.height = heightInPx
            holder.itemBinding.itemCoverImgCv.layoutParams = layoutParams
        }
        val item = items[position]

        when (item) {
            is ProductVer2 -> {
                holder.itemBinding.itemTitleTv.text = item.name
                holder.itemBinding.itemYenTv.text = item.enPrice.toString() + "¥"
                holder.itemBinding.itemStarTv.text = String.format("%.1f", item.rating)
                holder.itemBinding.itemReviewNumTv.text = "(" + item.reviewCount.toString() + ")"
                Glide.with(holder.itemView.context).load(item.imageUrl).into(holder.itemBinding.itemCoverImgIv)
            }
            is Product -> {
                holder.itemBinding.itemTitleTv.text = item.name
                holder.itemBinding.itemYenTv.text = item.enPrice.toString() + "¥" // `price` 사용
                holder.itemBinding.itemStarTv.text = String.format("%.1f", item.rating)
                holder.itemBinding.itemReviewNumTv.text = "(" + item.reviewCount.toString() + ")"
                Glide.with(holder.itemView.context).load(item.imageUrl).into(holder.itemBinding.itemCoverImgIv)
            }
        }
    }

    inner class CustomViewHolder(val itemBinding: ItemHomeCustomBinding)
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