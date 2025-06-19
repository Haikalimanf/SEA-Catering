package com.example.seacatering.ui.user.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.seacatering.databinding.ItemReviewBinding
import com.example.seacatering.model.DataTestimonial

class ReviewAdapter: ListAdapter<DataTestimonial, ReviewAdapter.ViewHolder>(DIFF) {
    class ViewHolder(private val binding: ItemReviewBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: DataTestimonial) {
            binding.tvReview.text = item.reviewText
            binding.tvNameUser.text = "By ${item.username}"
            binding.ratingBar.rating = item.rating.toFloat()
        }
    }

    companion object{
        val DIFF = object : DiffUtil.ItemCallback<DataTestimonial>() {
            override fun areItemsTheSame(oldItem: DataTestimonial, newItem: DataTestimonial): Boolean {
                return oldItem.id == newItem.id
            }
            override fun areContentsTheSame(oldItem: DataTestimonial, newItem: DataTestimonial): Boolean {
                return oldItem == newItem
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemReviewBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}