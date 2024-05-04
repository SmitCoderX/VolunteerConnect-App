package com.smitcoderx.volunteerconnect.Ui.Posts

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.smitcoderx.volunteerconnect.Model.Posts.PostsData
import com.smitcoderx.volunteerconnect.R
import com.smitcoderx.volunteerconnect.databinding.LayoutPostItemBinding
import java.text.SimpleDateFormat
import java.util.Locale

class PostAdapter(private val listener: OnHandleActions, private val role: String) :
    RecyclerView.Adapter<PostAdapter.PostViewHolder>() {

    inner class PostViewHolder(val binding: LayoutPostItemBinding) : ViewHolder(
        binding.root
    )

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder {
        val binding =
            LayoutPostItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PostViewHolder(binding)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: PostViewHolder, position: Int) {
        val currentItem = differ.currentList[position]

        currentItem.let {


            holder.binding.apply {

                Glide.with(holder.itemView).load(it.postImg)
                    .transition(DrawableTransitionOptions.withCrossFade())
                    .placeholder(R.drawable.img).into(ivPostImg)

                ivPostImg.visibility = if (it.postImg.isNullOrEmpty()) View.GONE else View.VISIBLE

                tvPostName.text = it.postName
                tvPostDesc.text = it.postDetails
                tvPostBy.text = it.createdByName

                val inputFormat =
                    SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
                val outputFormat = SimpleDateFormat("dd MMM yyyy", Locale.ENGLISH)

                val date = inputFormat.parse(it.createdAt.toString())
                val formattedDate = date?.let { it1 -> outputFormat.format(it1) }
                tvCreatedAt.text = formattedDate

                if (role.equals("organization", ignoreCase = true)) {
                    root.setOnClickListener {
                        val adapterPosition = holder.bindingAdapterPosition
                        if (adapterPosition != RecyclerView.NO_POSITION) {
                            if (currentItem != null) {
                                listener.onClick(currentItem)
                            }
                        }
                    }
                }
            }
        }
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    private val differCallback = object : DiffUtil.ItemCallback<PostsData>() {
        override fun areItemsTheSame(oldItem: PostsData, newItem: PostsData): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: PostsData, newItem: PostsData): Boolean {
            return oldItem == newItem
        }
    }

    val differ = AsyncListDiffer(this, differCallback)

    interface OnHandleActions {
        fun onClick(data: PostsData)
    }
}