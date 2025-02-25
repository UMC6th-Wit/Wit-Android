package com.umc.umc_6th_wit_android.home

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.umc.umc_6th_wit_android.R
import com.umc.umc_6th_wit_android.data.local.CategoryDto
import com.umc.umc_6th_wit_android.data.remote.home.ProductVer2
import com.umc.umc_6th_wit_android.databinding.ItemHomeCategoryBinding
import com.umc.umc_6th_wit_android.login.TokenManager
import com.umc.umc_6th_wit_android.wish.WishBoardItemResult
import com.umc.umc_6th_wit_android.wish.WishItemResult
import com.umc.umc_6th_wit_android.wish.WishListView

class CategoryRVAdapter (
    val items : ArrayList<ProductVer2>,
    private val addCart : (id: Int) -> Unit,
    private val delCart : (id : Int) -> Unit
)
    : RecyclerView.Adapter<CategoryRVAdapter.CateogoryViewHolder>(){
    val TAG = "CategoryRVAdapter"

    override fun getItemCount(): Int = items.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CateogoryViewHolder {
        val itemBinding = ItemHomeCategoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CateogoryViewHolder(itemBinding)
    }

    override fun getItemId(position: Int): Long {
        return super.getItemId(position)
    }

    override fun onBindViewHolder(holder: CateogoryViewHolder, position: Int) {
        holder.itemBinding.rankingNum.text = "${position + 1}"
        Glide.with( holder.itemView.context).load(items[position].imageUrl).into(holder.itemBinding.itemCoverImgIv)
        holder.itemBinding.tvAddress.text = items[position].salesArea
        holder.itemBinding.itemTitleTv.text = items[position].name
        holder.itemBinding.itemYenTv2.text = items[position].enPrice.toString() + "¥"
        holder.itemBinding.itemWonTv2.text = items[position].wonPrice.toString() +"₩"

        //받아온 데이터 세팅
        if(!items[position].isHeart){
            holder.itemBinding.likeIv.setImageResource(R.drawable.home_off_heart)
        }else{
            holder.itemBinding.likeIv.setImageResource(R.drawable.on_heart)
        }

        //test
        holder.itemBinding.likeIv.setOnClickListener {
            if(!items[position].isHeart){
                addCart(items[position].id)
                Log.d("product_id", items[position].id.toString())
                items[position].isHeart = true
                holder.itemBinding.likeIv.setImageResource(R.drawable.on_heart)
            }else{
                delCart(items[position].id)
                items[position].isHeart = false
                holder.itemBinding.likeIv.setImageResource(R.drawable.home_off_heart)
            }
        }
    }

    inner class CateogoryViewHolder(val itemBinding: ItemHomeCategoryBinding)
        : RecyclerView.ViewHolder(itemBinding.root) {
        init {
            /*RecyclerView 항목 클릭 시 외부 click 이벤트 리스너 호출*/
            itemBinding.root.setOnClickListener{
                listener?.onItemClick(it, adapterPosition)  // RecyclerView 항목 클릭 시 외부에서 지정한 리스너 호출
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