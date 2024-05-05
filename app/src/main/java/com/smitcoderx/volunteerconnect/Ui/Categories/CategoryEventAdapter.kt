package com.smitcoderx.volunteerconnect.Ui.Categories

import android.annotation.SuppressLint
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.smitcoderx.volunteerconnect.Model.Events.DataFetch
import com.smitcoderx.volunteerconnect.R
import com.smitcoderx.volunteerconnect.Utils.Constants.TAG
import com.smitcoderx.volunteerconnect.databinding.LayoutEventItemBinding
import java.util.Locale

class CategoryEventAdapter(private val listener: OnCategory) :
    RecyclerView.Adapter<CategoryEventAdapter.CategoryEventViewHolder>() {

    inner class CategoryEventViewHolder(val binding: LayoutEventItemBinding) :
        ViewHolder(binding.root) {


    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryEventViewHolder {
        val binding =
            LayoutEventItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CategoryEventViewHolder(binding)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: CategoryEventViewHolder, position: Int) {
        val currentItem = differ.currentList[position]
        currentItem.let { item ->

            Glide.with(holder.itemView).load(item.eventPicture)
                .transition(DrawableTransitionOptions.withCrossFade())
                .placeholder(R.drawable.img)
                .into(holder.binding.ivEventImg)

            holder.binding.tvEventName.text = item.name
            holder.binding.tvEventVisibility.text = item.visibility?.replaceFirstChar {
                if (it.isLowerCase()) it.titlecase(
                    Locale.ENGLISH
                ) else it.toString()
            }
            holder.binding.tvEventCategory.text = item.category?.joinToString()
            holder.binding.tvEventPrice.text =
                if (item.isPaid == true) "₹${item.price}" else "Free"
            holder.binding.tvEventQuestions.text = "${item.question?.size} Questions"

            if(item.isSaved) {
                holder.binding.ivEventFav.setImageDrawable(
                    ContextCompat.getDrawable(
                        holder.itemView.context, R.drawable.ic_liked
                    )
                )
            } else {
                holder.binding.ivEventFav.setImageDrawable(
                    ContextCompat.getDrawable(
                        holder.itemView.context, R.drawable.heart
                    )
                )
            }

            holder.binding.ivEventFav.setOnClickListener {
                if (item.isSaved) {
                    holder.binding.ivEventFav.setImageDrawable(
                        ContextCompat.getDrawable(
                            it.context, R.drawable.heart
                        )
                    )
                    item.isSaved = false
                    Log.d(TAG, "onBindViewHolder: Unliked")
                } else {
                    holder.binding.ivEventFav.setImageDrawable(
                        ContextCompat.getDrawable(
                            it.context, R.drawable.ic_liked
                        )
                    )
                    item.isSaved = true
                    Log.d(TAG, "onBindViewHolder: Liked")
                }
                listener.onEventFavClick(item)
            }

            holder.binding.root.setOnClickListener {
                val adapterPosition = holder.bindingAdapterPosition
                if (adapterPosition != RecyclerView.NO_POSITION) {
                    if (item != null) {
                        listener.onEventHandleClick(eventData = item)
                    }
                }
            }

        }
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    private val differCallback = object : DiffUtil.ItemCallback<DataFetch>() {
        override fun areItemsTheSame(oldItem: DataFetch, newItem: DataFetch): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: DataFetch, newItem: DataFetch): Boolean {
            return oldItem == newItem
        }
    }

    val differ = AsyncListDiffer(this, differCallback)

    interface OnCategory {
        fun onEventHandleClick(eventData: DataFetch)
        fun onEventFavClick(eventData: DataFetch)
    }
}