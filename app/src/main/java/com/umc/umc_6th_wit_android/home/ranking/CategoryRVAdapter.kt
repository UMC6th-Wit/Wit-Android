package com.umc.umc_6th_wit_android.home.ranking

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.umc.umc_6th_wit_android.R
import com.umc.umc_6th_wit_android.data.local.CategoryDto
import com.umc.umc_6th_wit_android.data.remote.home.ProductVer2
import com.umc.umc_6th_wit_android.data.remote.search.Souvenir
import com.umc.umc_6th_wit_android.databinding.ItemRankingBinding

class CategoryRVAdapter (
    val items : ArrayList<ProductVer2>,
    val part : String,
    val category: Int?,
    private val getCategory : (category: Int?, cursor: Int?) -> Unit,
    private val addCart : (id: Int) -> Unit,
    private val delCart : (id : Int) -> Unit
)
    : RecyclerView.Adapter<CategoryRVAdapter.RankingCateogoryViewHolder>() {
    val TAG = "RankingCategoryRVAdapter"
    var currentCursor: Int? = null

    override fun getItemCount(): Int = items.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RankingCateogoryViewHolder {
        val itemBinding = ItemRankingBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return RankingCateogoryViewHolder(itemBinding)
    }

    override fun onBindViewHolder(holder: RankingCateogoryViewHolder, position: Int) {
        // 스크롤이 끝에 도달했을 때 더 많은 아이템을 로드
        if (position == items.size - 1 && currentCursor != null) {
            getCategory(category, currentCursor)//category는 고정 값
        }else if(currentCursor == null){
            getCategory(category, 0)//category는 고정 값
        }

        if(part != "ranking"){
            holder.itemBinding.rankingNum.visibility = View.GONE
        }
        Log.d("SIZECATEGORY", items.size.toString())
        holder.itemBinding.rankingNum.text = "${position + 1}"
        Glide.with( holder.itemView.context).load(items[position].imageUrl).into(holder.itemBinding.itemCoverImgIv)
        holder.itemBinding.itemTitleTv.text = items[position].name
        holder.itemBinding.itemYenTv2.text = items[position].enPrice.toString() + "¥"
        holder.itemBinding.itemWonTv2.text = items[position].wonPrice.toString() +"₩"
        holder.itemBinding.itemStarTv.text = String.format("%.1f", items[position].rating)
        holder.itemBinding.itemReviewNumTv.text = "(${items[position].reviewCount})"

        if(!items[position].isHeart){
            holder.itemBinding.likeIv.setImageResource(R.drawable.home_off_heart)
        }else{
            holder.itemBinding.likeIv.setImageResource(R.drawable.on_heart)
        }

        //test
        holder.itemBinding.likeIv.setOnClickListener {
            if(!items[position].isHeart){
                addCart(items[position].id)
                items[position].isHeart = true
                holder.itemBinding.likeIv.setImageResource(R.drawable.on_heart)
            }else{
                delCart(items[position].id)
                items[position].isHeart = false
                holder.itemBinding.likeIv.setImageResource(R.drawable.home_off_heart)
            }
        }
    }
    // 데이터 추가를 위한 메서드
    fun addItems(newItems: ArrayList<ProductVer2>) {
        val startPos = items.size
        items.addAll(newItems)
        notifyItemRangeInserted(startPos, newItems.size)
    }

    inner class RankingCateogoryViewHolder(val itemBinding: ItemRankingBinding)
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