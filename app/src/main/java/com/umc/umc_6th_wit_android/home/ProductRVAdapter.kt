package com.umc.umc_6th_wit_android.home

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.umc.umc_6th_wit_android.R
import com.umc.umc_6th_wit_android.data.remote.home.Product
import com.umc.umc_6th_wit_android.data.remote.home.ProductVer2
import com.umc.umc_6th_wit_android.databinding.ItemCustomBinding

class ProductRVAdapter<T>(
    val items: ArrayList<T>,
    private val addCart: (id: Int) -> Unit,
    private val delCart: (id: Int) -> Unit
) : RecyclerView.Adapter<ProductRVAdapter<T>.CustomViewHolder>() {

    val TAG = "ProductRVAdapter"

    override fun getItemCount(): Int = items.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomViewHolder {
        val itemBinding = ItemCustomBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CustomViewHolder(itemBinding)
    }
    override fun onBindViewHolder(holder: CustomViewHolder, position: Int) {
        val item = items[position]
        when (item) {
            is ProductVer2 -> {
                Glide.with(holder.itemView.context)
                    .load(item.imageUrl)
                    .into(holder.itemBinding.itemCoverImgIv)
                holder.itemBinding.itemTitleTv.text = item.name
                holder.itemBinding.itemYenTv2.text = item.enPrice.toString() + "¥"
                holder.itemBinding.itemWonTv2.text = item.wonPrice.toString() + "₩"
                holder.itemBinding.itemStarTv.text = String.format("%.1f", item.rating)
                holder.itemBinding.itemReviewNumTv.text = "(${item.reviewCount})"
                //하트 눌린건지 여부.
                if(!item.isHeart){
                    holder.itemBinding.likeIv.setImageResource(R.drawable.off_heart)
                }else{
                    holder.itemBinding.likeIv.setImageResource(R.drawable.on_heart)
                }
                holder.itemBinding.likeIv.setOnClickListener {
                    handleHeartClick(item, holder)
                }
            }
            is Product -> {
                Glide.with(holder.itemView.context)
                    .load(item.imageUrl)
                    .into(holder.itemBinding.itemCoverImgIv)
                holder.itemBinding.itemTitleTv.text = item.name
                holder.itemBinding.itemYenTv2.text = item.enPrice.toString() + "¥"
                holder.itemBinding.itemWonTv2.text = item.wonPrice.toString() + "₩"
                holder.itemBinding.itemStarTv.text = String.format("%.1f", item.rating)
                holder.itemBinding.itemReviewNumTv.text = "(${item.reviewCount})"
                if (item.isHeart != 1) {
                    holder.itemBinding.likeIv.setImageResource(R.drawable.off_heart)
                } else {
                    holder.itemBinding.likeIv.setImageResource(R.drawable.on_heart)
                }
                holder.itemBinding.likeIv.setOnClickListener {
                    handleHeartClick(item, holder)
                }
            }
        }

    }
    private fun handleHeartClick(item: Any, holder: CustomViewHolder) {
        when (item) {
            is ProductVer2 -> {
                if (!item.isHeart) {
                    addCart(item.id)
                    item.isHeart = true
                    holder.itemBinding.likeIv.setImageResource(R.drawable.on_heart)
                } else {
                    delCart(item.id)
                    item.isHeart = false
                    holder.itemBinding.likeIv.setImageResource(R.drawable.off_heart)
                }
            }
            is Product -> {
                if (item.isHeart != 1) {
                    addCart(item.id)
                    item.isHeart = 1
                    holder.itemBinding.likeIv.setImageResource(R.drawable.on_heart)
                } else {
                    delCart(item.id)
                    item.isHeart = 0
                    holder.itemBinding.likeIv.setImageResource(R.drawable.off_heart)
                }
            }
        }
    }
    inner class CustomViewHolder(val itemBinding: ItemCustomBinding)
        : RecyclerView.ViewHolder(itemBinding.root) {
        init {
            /*RecyclerView 항목 클릭 시 외부 click 이벤트 리스너 호출*/
            itemBinding.root.setOnClickListener{
                listener?.onItemClick(it, adapterPosition)  // RecyclerView 항목 클릭 시 외부에서 지정한 리스너 호출
            }
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