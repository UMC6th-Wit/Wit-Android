package com.umc.umc_6th_wit_android.product

import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.umc.umc_6th_wit_android.data.remote.product.Review
import com.umc.umc_6th_wit_android.data.remote.product.TopHelpfulReview
import com.umc.umc_6th_wit_android.databinding.ItemReviewBinding

class ReviewMinRVAdapter (val items: List<Review>)
    : RecyclerView.Adapter<ReviewMinRVAdapter.ReviewMinViewHolder>() {

    val TAG = "ReviewMinRVAdapter"

    override fun getItemCount(): Int = items.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReviewMinViewHolder {
        val itemBinding = ItemReviewBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ReviewMinViewHolder(itemBinding)
    }
    override fun onBindViewHolder(holder: ReviewMinViewHolder, position: Int) {
        Glide.with( holder.itemView.context).load(items[position].images).into(holder.binding.reviewPhotoIv)
        holder.binding.reviewTv.text= items[position].content
    }

    inner class ReviewMinViewHolder(val binding: ItemReviewBinding)
        : RecyclerView.ViewHolder(binding.root) {
//        init {
//            //RecyclerView 항목 클릭 시 외부 click 이벤트 리스너 호출
//            binding.root.setOnClickListener{
//                listener?.onItemClick(it, adapterPosition)  // RecyclerView 항목 클릭 시 외부에서 지정한 리스너 호출
//            }
        }
    }


    var listener : OnItemClickListener? = null

    interface OnItemClickListener {
        fun onItemClick(view : View, position : Int)
    }

    fun setOnItemClickListener(listener: OnItemClickListener?) {
//        this.listener = listener
//    }
}