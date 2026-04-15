package com.uzuu.customer.ui.adapter

import android.content.res.ColorStateList
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.uzuu.customer.R
import com.uzuu.customer.databinding.ItemCategoryInHomeBinding
import com.uzuu.customer.domain.model.CategoryItem

class CategoryAdapter(
    private val onClick: (CategoryItem) -> Unit
) : ListAdapter<CategoryItem, CategoryAdapter.ViewHolder>(DIFF) {

    companion object {
        private val DIFF = object : DiffUtil.ItemCallback<CategoryItem>() {
            override fun areItemsTheSame(oldItem: CategoryItem, newItem: CategoryItem) =
                oldItem.id == newItem.id

            override fun areContentsTheSame(oldItem: CategoryItem, newItem: CategoryItem) =
                oldItem == newItem
        }
    }

    inner class ViewHolder(val binding: ItemCategoryInHomeBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemCategoryInHomeBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        val ctx  = holder.itemView.context

        holder.binding.tvNameCategory.text = item.name

        if (item.isSelected) {
            // Nền xanh đậm, chữ trắng
            holder.binding.root.setCardBackgroundColor(
                ContextCompat.getColor(ctx, R.color.blue_primary_400)
            )
            holder.binding.root.strokeColor =
                ContextCompat.getColor(ctx, R.color.blue_primary_400)
            holder.binding.tvNameCategory.setTextColor(
                ContextCompat.getColor(ctx, android.R.color.white)
            )
        } else {
            // Nền nhạt, chữ tối
            holder.binding.root.setCardBackgroundColor(
                ContextCompat.getColor(ctx, R.color.blue_surface_8)
            )
            holder.binding.root.strokeColor =
                ContextCompat.getColor(ctx, R.color.blue_border_5)
            holder.binding.tvNameCategory.setTextColor(
                ContextCompat.getColor(ctx, R.color.blue_text_dark)
            )
        }

        holder.binding.root.setOnClickListener { onClick(item) }
    }
}