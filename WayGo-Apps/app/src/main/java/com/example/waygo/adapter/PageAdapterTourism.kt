package com.example.waygo.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.waygo.data.response.TourismItems
import com.example.waygo.databinding.TourismItemsBinding

class PageAdapterTourism (private val onItemClickCallback: OnItemClickCallback) :
    PagingDataAdapter<TourismItems, PageAdapterTourism.MyViewHolder>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = TourismItemsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding, onItemClickCallback)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val data = getItem(position)
        if (data != null) {
            holder.bind(data)
            Log.d("ADAPTER", data.name)
        }
    }

    class MyViewHolder(
        private val binding: TourismItemsBinding,
        private val onItemClickCallback: OnItemClickCallback,
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(data: TourismItems) {
            binding.textName.text = data.name
            Glide.with(binding.root).load(data.photoUrl).into(binding.imageProfile)
            binding.tourismItems.setOnClickListener {
                onItemClickCallback.onItemClicked(data.id)
            }
        }
    }

    interface OnItemClickCallback {
        fun onItemClicked(id: String)
    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<TourismItems>() {
            override fun areItemsTheSame(
                oldItem: TourismItems,
                newItem: TourismItems,
            ): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(
                oldItem: TourismItems,
                newItem: TourismItems,
            ): Boolean {
                return oldItem.id == newItem.id
            }
        }
    }
}