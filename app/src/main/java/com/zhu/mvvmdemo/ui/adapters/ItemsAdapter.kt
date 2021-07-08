package com.zhu.mvvmdemo.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.zhu.mvvmdemo.databinding.FragmentItemBinding
import com.zhu.mvvmdemo.data.ContentResponse.PlaceholderItem


class ItemsAdapter(
        private val onClick: (PlaceholderItem) -> Unit)
    : ListAdapter<PlaceholderItem, ItemsAdapter.ViewHolder>(ItemDiffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = FragmentItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding, onClick)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
    }

    inner class ViewHolder(private val binding: FragmentItemBinding, val onClick: (PlaceholderItem) -> Unit) : RecyclerView.ViewHolder(binding.root) {

        private var currentItem: PlaceholderItem? = null

        init {
            itemView.setOnClickListener {
                currentItem?.let {
                    onClick(it)
                }
            }
        }

        fun bind(item: PlaceholderItem) {
            currentItem = item
            binding.itemNumber.text = item.id
        }
    }
}

object ItemDiffCallback : DiffUtil.ItemCallback<PlaceholderItem>() {
    override fun areItemsTheSame(oldItem: PlaceholderItem, newItem: PlaceholderItem): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(oldItem: PlaceholderItem, newItem: PlaceholderItem): Boolean {
        return oldItem.id == newItem.id
    }
}