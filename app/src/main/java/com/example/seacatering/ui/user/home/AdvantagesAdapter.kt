package com.example.seacatering.ui.user.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.seacatering.databinding.ItemHomeBinding
import com.example.seacatering.model.DataAdvantages

class AdvantagesAdapter : ListAdapter<DataAdvantages, AdvantagesAdapter.AdvantagesViewHolder>(DIFF) {

    inner class AdvantagesViewHolder(private val binding: ItemHomeBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: DataAdvantages) {
            binding.tvTitleMenu.text = item.title
            binding.tvDescMenu.text = item.shortDescription
            Glide.with(binding.root.context)
                .load(item.imageUri)
                .into(binding.imgDietPlan)
        }
    }

    companion object {
        val DIFF = object : DiffUtil.ItemCallback<DataAdvantages>() {
            override fun areItemsTheSame(oldItem: DataAdvantages, newItem: DataAdvantages): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: DataAdvantages, newItem: DataAdvantages): Boolean {
                return oldItem == newItem
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AdvantagesViewHolder {
        val binding = ItemHomeBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return AdvantagesViewHolder(binding)
    }

    override fun onBindViewHolder(holder: AdvantagesViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}

