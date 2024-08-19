package com.umc.umc_6th_wit_android.product

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.umc.umc_6th_wit_android.R
import com.umc.umc_6th_wit_android.data.local.ReviewDetailDto

class ReviewOnlyAdapter(private val itemList: List<ReviewDetailDto>) : RecyclerView.Adapter<ReviewOnlyAdapter.MyViewHolder>() {

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imageView: ImageView = itemView.findViewById(R.id.review_help_btn_iv)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_review_detail, parent, false)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val item = itemList[position]

        // 초기 이미지 설정
        holder.imageView.setImageResource(item.imageResId)

        // 이미지 클릭 이벤트 처리
        holder.imageView.setOnClickListener {
            // 현재 이미지 리소스와 다른 이미지로 변경
            if (item.imageResId == R.drawable.review_help_btn_image) {
                item.imageResId = R.drawable.review_helped_btn_image
            } else {
                item.imageResId = R.drawable.review_help_btn_image
            }

            // 이미지 변경 반영
            holder.imageView.setImageResource(item.imageResId)
        }
    }

    override fun getItemCount(): Int {
        return itemList.size
    }
}
