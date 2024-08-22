package com.umc.umc_6th_wit_android.wish

import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.umc.umc_6th_wit_android.R
import com.umc.umc_6th_wit_android.databinding.ItemWishBinding
import java.text.NumberFormat

// WishAdapter 클래스 정의: RecyclerView.Adapter를 상속받아 WishItem 리스트를 관리합니다.
class WishAdapter(
    private var items: MutableList<WishItem>,
    private val selectionListener: SelectionListener,
    private val loadMoreItems: (cursor: Int?, limit: Int?) -> Unit
): RecyclerView.Adapter<WishAdapter.WishViewHolder>() {

    // 선택된 아이템들을 저장하는 MutableSet
    val selectedItems = mutableSetOf<WishItem>()
    // 편집 모드 여부를 나타내는 변수
    private var isEditMode = false
    var currentCursor: Int? = null
    private val limit = 20 // 한 번에 가져올 아이템 수

    // ViewHolder를 생성합니다.
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WishViewHolder {
        val binding = ItemWishBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return WishViewHolder(binding)
    }

    // ViewHolder와 데이터를 바인딩합니다.
    override fun onBindViewHolder(holder: WishViewHolder, position: Int) {
        holder.bind(items[position])

        // 스크롤이 끝에 도달했을 때 더 많은 아이템을 로드
        if (position == items.size - 1) {
            loadMoreItems(currentCursor, limit)
        }
    }

    // 데이터 추가를 위한 메서드
    fun addItems(newItems: List<WishItem>) {
        val startPos = items.size
        items.addAll(newItems)
        notifyItemRangeInserted(startPos, newItems.size)
    }

    // 아이템 개수를 반환합니다.
    override fun getItemCount(): Int = items.size

    // 편집 모드를 설정합니다.
    fun setEditMode(isEditMode: Boolean) {
        this.isEditMode = isEditMode
        // 편집 모드 변경을 어댑터에 알림
        notifyDataSetChanged()
    }

    // 선택된 아이템들을 삭제합니다.
    fun deleteSelectedItems() {
        // items를 MutableList로 변환하여 selectedItems를 제거합니다.
        val mutableItems = items.toMutableList()
        mutableItems.removeAll(selectedItems)
        selectedItems.clear()
        // 새로운 리스트로 items를 대체하고 어댑터에 변경 사항을 알립니다.
        updateItems(mutableItems)
    }

    // 아이템을 선택합니다.
    fun selectItem(item: Any) {
        selectedItems.add(item as WishItem)
        // 선택된 아이템 개수 변경을 리스너에 알림
        selectionListener.onSelectionItemChanged(selectedItems.size)
    }

    // 아이템 선택을 해제합니다.
    fun deselectItem(item: Any) {
        selectedItems.remove(item)
        // 선택된 아이템 개수 변경을 리스너에 알림
        selectionListener.onSelectionItemChanged(selectedItems.size)
    }

    // 아이템 리스트를 업데이트합니다.
    private fun updateItems(newItems: List<WishItem>) {
        items = newItems.toMutableList()
        // 데이터가 변경되었음을 어댑터에 알림
        notifyDataSetChanged()
    }

    // ViewHolder 클래스 정의: 아이템의 뷰를 관리합니다.
    inner class WishViewHolder(private val binding: ItemWishBinding) : RecyclerView.ViewHolder(binding.root) {
        // 아이템을 바인딩합니다.
        fun bind(item: WishItem) {
            // 아이템 이미지를 설정
            item.image?.let { imageUrl ->
                Glide.with(binding.itemImage.context)
                    .load(imageUrl)
                    .into(binding.itemImage)
            }

            // 아이템 제목을 설정
            binding.itemTitle.text = item.name
            // 일본 엔화 가격을 설정
            binding.priceJpy.text = NumberFormat.getNumberInstance().format(item.en_price)
            // 한국 원화 가격을 설정
            binding.priceKrw.text = NumberFormat.getNumberInstance().format(item.won_price)
            // 아이템 평점을 설정
            binding.itemRating.text = item.average_rating.toString()
            // 아이템 리뷰 수를 설정
            binding.itemNop.text = item.review_count.toString()

            // 편집 모드에 따른 UI 설정을 합니다.
            if (isEditMode) {
                // 편집 모드일 때 아이템 선택 아이콘을 보이게 합니다.
                binding.icSelect.visibility = View.VISIBLE
            } else {
                // 편집 모드가 아닐 때 아이템 선택 아이콘을 숨깁니다.
                binding.icSelect.visibility = View.GONE
                // 선택된 아이템 리스트에서 제거
                selectedItems.remove(item)
                // 아이템 선택 아이콘을 초기 상태로 설정
                binding.icSelect.setImageResource(R.drawable.ic_unselected)
                // 아이템 이미지의 색상 필터 제거
                binding.itemImage.clearColorFilter()
            }

            // 아이템 클릭 리스너를 설정합니다.
            itemView.setOnClickListener {
                if (isEditMode) {
                    // 편집 모드일 때
                    if (selectedItems.contains(item)) {
                        // 선택된 아이템을 선택 해제합니다.
                        deselectItem(item)
                        selectedItems.remove(item)
                        binding.icSelect.setImageResource(R.drawable.ic_unselected)
                        binding.itemImage.clearColorFilter() // 효과를 원래대로 되돌림
                    } else {
                        // 선택되지 않은 아이템을 선택합니다.
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
