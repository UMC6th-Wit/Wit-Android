package com.umc.umc_6th_wit_android.wish

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.umc.umc_6th_wit_android.R
import com.umc.umc_6th_wit_android.databinding.ItemWishBinding

class WishAdapter(private var items: List<WishItem>, private val selectionListener: SelectionListener) : RecyclerView.Adapter<WishAdapter.WishViewHolder>() {

    val selectedItems = mutableSetOf<WishItem>()
    private var isEditMode = false

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WishViewHolder {
        val binding = ItemWishBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return WishViewHolder(binding)
    }

    override fun onBindViewHolder(holder: WishViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount(): Int = items.size

    fun setEditMode(isEditMode: Boolean) {
        this.isEditMode = isEditMode
        notifyDataSetChanged()
    }

    fun deleteSelectedItems() {
        // items를 MutableList로 변환하여 selectedItems 제거
        val mutableItems = items.toMutableList()
        mutableItems.removeAll(selectedItems)
        selectedItems.clear()
        // 새로운 리스트로 items 대체하고 어댑터에 변경 사항 알림
        updateItems(mutableItems)
    }

    fun selectItem(item: Any) {
        selectedItems.add(item as WishItem)
        selectionListener.onSelectionItemChanged(selectedItems.size)
    }

    fun deselectItem(item: Any) {
        selectedItems.remove(item)
        selectionListener.onSelectionItemChanged(selectedItems.size)
    }

    private fun updateItems(newItems: List<WishItem>) {
        items = newItems
        notifyDataSetChanged()
    }

    inner class WishViewHolder(private val binding: ItemWishBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: WishItem) {
//            Glide.with(binding.itemImage.context)
//                .load(item.image)
//                .into(binding.itemImage)
            binding.itemImage.setImageResource(item.image!!)
            binding.itemTitle.text = item.title
            binding.priceJpy.text = item.jpy
            binding.priceKrw.text = item.krw
            binding.itemRating.text = item.rating.toString()
            binding.itemNop.text = item.nop.toString()

            if(!isEditMode && item.id == items.last().id){
                binding.bottomItem.setPadding(0, 0, 0, 300)
            } else {
                binding.bottomItem.setPadding(0, 0, 0, 10)
            }
            if(isEditMode){
                binding.icSelect.visibility = View.VISIBLE
            } else {
                binding.icSelect.visibility = View.GONE
                selectedItems.remove(item)
                binding.icSelect.setImageResource(R.drawable.ic_unselected)
                binding.itemImage.clearColorFilter() // 효과를 원래대로 되돌림
            }
            itemView.setOnClickListener {
                if (isEditMode) {
                    if (selectedItems.contains(item)) {
                        deselectItem(item)
                        selectedItems.remove(item)
                        binding.icSelect.setImageResource(R.drawable.ic_unselected)
                        binding.itemImage.clearColorFilter() // 효과를 원래대로 되돌림
                    } else {
                        selectItem(item)
                        selectedItems.add(item)
                        binding.icSelect.setImageResource(R.drawable.ic_selected)
                        binding.itemImage.setColorFilter(Color.parseColor("#80000000")) // 투명도 50%의 검은 색상
                    }
                }
            }
        }
    }
}
