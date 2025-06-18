package com.example.seacatering.ui.user.menu

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.seacatering.databinding.ItemMenuMealBinding
import com.example.seacatering.model.DataMealPlan

class MealPlanAdapter(
    private val onViewDetailClick: (DataMealPlan) -> Unit
) : ListAdapter<DataMealPlan, MealPlanAdapter.MealPlanViewHolder>(DIFF) {

    inner class MealPlanViewHolder(val binding: ItemMenuMealBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: DataMealPlan) {
            binding.tvTitle.text = item.name
            binding.tvDescription.text = item.shortDescription
            binding.tvPrice.text = "Rp${item.price}/Meal"
            Glide.with(binding.root).load(item.imageUri).into(binding.imgDietPlan)

            binding.btnViewDetails.setOnClickListener {
                onViewDetailClick(item)
            }
        }
    }

    companion object {
        val DIFF = object : DiffUtil.ItemCallback<DataMealPlan>() {
            override fun areItemsTheSame(oldItem: DataMealPlan, newItem: DataMealPlan): Boolean =
                oldItem.id == newItem.id

            override fun areContentsTheSame(oldItem: DataMealPlan, newItem: DataMealPlan): Boolean =
                oldItem == newItem
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MealPlanViewHolder {
        val binding =
            ItemMenuMealBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MealPlanViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MealPlanViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}
