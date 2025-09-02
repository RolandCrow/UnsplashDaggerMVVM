package com.example.unsplashdaggermvvm.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.unsplashdaggermvvm.databinding.ImageItemBinding
import com.example.unsplashdaggermvvm.model.ImagesResponse

class ImagesAdapter(private val navigate: (ImagesResponse, ImageView) -> Unit):
    PagingDataAdapter<ImagesResponse, ImagesAdapter.ViewHolder>(DiffCallback()) {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        return ViewHolder(
            ImageItemBinding.inflate(
                LayoutInflater.from(parent.context), parent,false
            )
        )
    }

    override fun onBindViewHolder(
        holder: ViewHolder,
        position: Int
    ) {
        getItem(position)?.let {
            holder.bind(it,position)
        }
    }

    inner class ViewHolder(
        private val binding: ImageItemBinding
    ): RecyclerView.ViewHolder(binding.root) {
        private var imageResponse: ImagesResponse? = null

        fun bind(imagesResponse: ImagesResponse, position: Int) {
            this.imageResponse = imagesResponse
            binding.apply {
                image = imagesResponse
                shouldRound = true
                binding.root.setOnClickListener {
                    imagesResponse.let {
                        navigate.invoke(it,imageView)
                    }
                }
                executePendingBindings()
            }
        }
    }

    private class DiffCallback: DiffUtil.ItemCallback<ImagesResponse>() {
        override fun areItemsTheSame(oldItem: ImagesResponse, newItem: ImagesResponse): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: ImagesResponse, newItem: ImagesResponse): Boolean {
            return oldItem == newItem
        }
    }
}