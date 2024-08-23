package com.umc.umc_6th_wit_android.product

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.umc.umc_6th_wit_android.data.remote.product.TopHelpfulReview
import com.umc.umc_6th_wit_android.databinding.ItemProductImgMinBinding
import com.umc.umc_6th_wit_android.databinding.ItemProductImgOnlyBinding
import com.umc.umc_6th_wit_android.databinding.ItemReviewBinding

class ReviewOnlyImagesRVAdapter (val items: List<String>)
    : RecyclerView.Adapter<ReviewOnlyImagesRVAdapter.ReviewOnlyImagesViewHolder>() {

    val TAG = "item_product_img_only"

    override fun getItemCount(): Int = items.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReviewOnlyImagesViewHolder {
        val itemBinding = ItemProductImgOnlyBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ReviewOnlyImagesViewHolder(itemBinding)
    }
    override fun onBindViewHolder(holder: ReviewOnlyImagesViewHolder, position: Int) {
        val imageUrl = items[position]
        Glide.with(holder.itemView.context)
            .load(imageUrl)
            .into(holder.binding.reviewPhotoIv)

    }

    inner class ReviewOnlyImagesViewHolder(val binding: ItemProductImgOnlyBinding)
        : RecyclerView.ViewHolder(binding.root) {
        init {
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
        this.listener = listener
    }
}