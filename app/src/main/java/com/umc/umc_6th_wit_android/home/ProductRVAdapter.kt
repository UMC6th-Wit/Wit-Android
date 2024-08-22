package com.umc.umc_6th_wit_android.home

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.umc.umc_6th_wit_android.R
import com.umc.umc_6th_wit_android.data.remote.home.Product
import com.umc.umc_6th_wit_android.databinding.ItemCustomBinding

class ProductRVAdapter(val items: ArrayList<Product>)
    : RecyclerView.Adapter<ProductRVAdapter.CustomViewHolder>() {

    val TAG = "ProductRVAdapter"

    override fun getItemCount(): Int = items.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomViewHolder {
        val itemBinding = ItemCustomBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CustomViewHolder(itemBinding)
    }
    override fun onBindViewHolder(holder: CustomViewHolder, position: Int) {
        Glide.with(holder.itemView.context)
            .load(items[position].imageUrl)
            .into(holder.itemBinding.itemCoverImgIv)
        holder.itemBinding.itemTitleTv.text = items[position].name
        holder.itemBinding.itemYenTv2.text = items[position].enPrice.toString() + "¥"
        holder.itemBinding.itemWonTv2.text = items[position].wonPrice.toString() +"₩"
        holder.itemBinding.itemStarTv.text = String.format("%.2f", items[position].rating)
//        holder.itemBinding.itemStarTv.text = items[position].rating.toString()
        holder.itemBinding.itemReviewNumTv.text = "(${items[position].reviewCount})"

        //하트 눌린건지 여부.
        if(items[position].isHeart != 1){
            holder.itemBinding.likeIv.setImageResource(R.drawable.off_heart)
        }else{
            holder.itemBinding.likeIv.setImageResource(R.drawable.on_heart)
        }

        //test
        holder.itemBinding.likeIv.setOnClickListener {
            if(items[position].isHeart != 1){
                items[position].isHeart = 1 //SearchResponse에서 isHeart 임시로 var로 해놓음 val로 변경해야함.
                holder.itemBinding.likeIv.setImageResource(R.drawable.on_heart)
            }else{
                items[position].isHeart = 0
                holder.itemBinding.likeIv.setImageResource(R.drawable.off_heart)
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