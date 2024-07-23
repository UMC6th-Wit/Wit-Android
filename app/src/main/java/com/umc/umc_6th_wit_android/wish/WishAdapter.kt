package com.umc.umc_6th_wit_android.wish

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.umc.umc_6th_wit_android.databinding.ItemWishBinding

class WishAdapter(private val items: List<WishItem>) : RecyclerView.Adapter<WishAdapter.WishViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WishViewHolder {
        val binding = ItemWishBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return WishViewHolder(binding)
    }

    override fun onBindViewHolder(holder: WishViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount(): Int = items.size

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
        }
    }
}
