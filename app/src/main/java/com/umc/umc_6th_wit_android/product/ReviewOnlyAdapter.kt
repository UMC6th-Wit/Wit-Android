package com.umc.umc_6th_wit_android.product

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.umc.umc_6th_wit_android.R
import com.umc.umc_6th_wit_android.data.remote.product.Review
import com.umc.umc_6th_wit_android.databinding.ItemReviewDetailBinding

class ReviewOnlyAdapter(val items: ArrayList<Review>) : RecyclerView.Adapter<ReviewOnlyAdapter.MyViewHolder>() {

    // ViewHolder 클래스
    inner class MyViewHolder(val binding: ItemReviewDetailBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        // View Binding을 사용하여 뷰를 인플레이트합니다.
        val binding = ItemReviewDetailBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        // 현재 위치의 데이터를 가져옵니다.
        val review = items[position]

        // 데이터 클래스의 값을 ViewHolder에 바인딩합니다.
        holder.binding.reviewNameTv.text = review.user_name
        holder.binding.reviewDetailTv.text = review.content
        holder.binding.reviewHelpPeopleNumTv.text = review.helpful_count.toString()

/*
        Glide.with(holder.itemView.context)
            .load(review.user_profile_image ?: R.drawable.review_profile_image) // null이면 기본 이미지를 로드
            .into(holder.binding.reviewProfileImageIv)
*/

//        holder.binding.ratingBar.rating = review.rating.toFloat()

/*
        // RecyclerView 설정 및 어댑터 설정
        val reviewImages = review.images.split(",")
        val imageAdapter = ReviewOnlyImagesRVAdapter(reviewImages)
        holder.binding.imageRvOnly.layoutManager = LinearLayoutManager(holder.itemView.context, LinearLayoutManager.HORIZONTAL, false)
        holder.binding.imageRvOnly.adapter = imageAdapter
*/

        // 초기 태그 설정
        holder.binding.reviewHelpBtnIv.tag = R.drawable.review_help_btn_image

        // 이미지뷰 클릭 이벤트 처리
        holder.binding.reviewHelpBtnIv.setOnClickListener {
            val currentImage = holder.binding.reviewHelpBtnIv.tag as Int

            if (currentImage == R.drawable.review_help_btn_image) {
                holder.binding.reviewHelpBtnIv.setImageResource(R.drawable.review_helped_btn_iv)
                holder.binding.reviewHelpBtnIv.tag = R.drawable.review_helped_btn_iv
            } else {
                holder.binding.reviewHelpBtnIv.setImageResource(R.drawable.review_help_btn_image)
                holder.binding.reviewHelpBtnIv.tag = R.drawable.review_help_btn_image
            }
        }
    }

    override fun getItemCount(): Int {
        return items.size
    }
}
