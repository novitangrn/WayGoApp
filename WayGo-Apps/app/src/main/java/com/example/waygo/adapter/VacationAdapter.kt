package com.example.waygo.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.waygo.data.response.AllTouristSpotsItem
import com.example.waygo.databinding.VacationItemsBinding
import org.checkerframework.checker.units.qual.A

class VacationAdapter() : PagingDataAdapter<AllTouristSpotsItem, VacationAdapter.ViewHolder>(DIFF_CALLBACK) {

    private lateinit var onItemClickCallback: OnItemClickCallback

    fun setOnClickCallback(onItemClickCallback: OnItemClickCallback){
        this.onItemClickCallback = onItemClickCallback
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = VacationItemsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        if (item != null){
            holder.bind(item)
            holder.itemView.setOnClickListener{
                onItemClickCallback.onItemClicked(item)
            }
        }
    }

    class ViewHolder(val binding: VacationItemsBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: AllTouristSpotsItem){
            Glide.with(binding.root.context)
                .load(item.imageUrl)
                .into(binding.imgItemPhoto)
            binding.apply {
                tvItemName.text = item.name
                tvItemCategory.text = item.category
            }
        }

    }

    interface OnItemClickCallback{
        fun onItemClicked(data: AllTouristSpotsItem)
    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<AllTouristSpotsItem>() {
            override fun areItemsTheSame(oldItem: AllTouristSpotsItem, newItem: AllTouristSpotsItem): Boolean {
                return oldItem == newItem
            }
            @SuppressLint("DiffUtilEquals")
            override fun areContentsTheSame(oldItem: AllTouristSpotsItem, newItem: AllTouristSpotsItem): Boolean {
                return oldItem == newItem
            }
        }
    }
}