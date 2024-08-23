package com.umc.umc_6th_wit_android.home

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.umc.umc_6th_wit_android.R
import com.umc.umc_6th_wit_android.data.remote.search.Souvenir
import com.umc.umc_6th_wit_android.databinding.ItemCustomBinding
import com.umc.umc_6th_wit_android.wish.WishItem


class CustomRVAdapter (
    val items : ArrayList<Souvenir>,
    private val getSearches: (cursor: Int?, limit: Int?) -> Unit,
    private val addCart : (id: Int) -> Unit,
    private val delCart : (id : Int) -> Unit
)
    : RecyclerView.Adapter<CustomRVAdapter.CustomViewHolder>() {
    val TAG = "CustomRVAdapter"
    var currentCursor: Int? = null
    private val limit = 20 // 한 번에 가져올 아이템 수

    override fun getItemCount(): Int = items.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomViewHolder {
        val itemBinding = ItemCustomBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CustomViewHolder(itemBinding)
    }

    override fun onBindViewHolder(holder: CustomViewHolder, position: Int) {
        // 스크롤이 끝에 도달했을 때 더 많은 아이템을 로드
        if (position == items.size - 1 && currentCursor != 0) {
            getSearches(currentCursor, limit)
        }

        Glide.with( holder.itemView.context).load(items[position].imageUrl).into(holder.itemBinding.itemCoverImgIv)
        holder.itemBinding.itemTitleTv.text = items[position].name
        holder.itemBinding.itemYenTv2.text = items[position].enPrice.toString() + "¥"
        holder.itemBinding.itemWonTv2.text = items[position].wonPrice.toString() +"₩"
        holder.itemBinding.itemStarTv.text = String.format("%.1f", items[position].rating)
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
                addCart(items[position].id)
                items[position].isHeart = 1 //SearchResponse에서 isHeart 임시로 var로 해놓음 val로 변경해야함.
                holder.itemBinding.likeIv.setImageResource(R.drawable.on_heart)
            }else{
                delCart(items[position].id)
                items[position].isHeart = 0
                holder.itemBinding.likeIv.setImageResource(R.drawable.off_heart)
            }
        }
    }

    // 데이터 추가를 위한 메서드
    fun addItems(newItems: ArrayList<Souvenir>) {
        val startPos = items.size
        items.addAll(newItems)
        notifyItemRangeInserted(startPos, newItems.size)
    }
    inner class CustomViewHolder(val itemBinding: ItemCustomBinding)
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