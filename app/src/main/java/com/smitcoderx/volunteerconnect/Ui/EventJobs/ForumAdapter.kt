package com.smitcoderx.volunteerconnect.Ui.EventJobs

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.smitcoderx.volunteerconnect.Model.Forum.ForumData
import com.smitcoderx.volunteerconnect.R
import com.smitcoderx.volunteerconnect.databinding.LayoutForumItemBinding
import java.util.Locale

class ForumAdapter(private val listener: OnClickHandle) :
    RecyclerView.Adapter<ForumAdapter.ForumViewHolder>() {

    inner class ForumViewHolder(val binding: LayoutForumItemBinding) : ViewHolder(
        binding.root
    )

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ForumViewHolder {
        val binding =
            LayoutForumItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ForumViewHolder(binding)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ForumViewHolder, position: Int) {
        val currentItem = differ.currentList[position]

        currentItem.let {


            holder.binding.apply {

                Glide.with(holder.itemView).load(ivEventImg.toString())
                    .transition(DrawableTransitionOptions.withCrossFade())
                    .placeholder(R.drawable.img).into(ivEventImg)

                tvForumName.text = it.forumName
                tvEventName.text = it.eventName?.replaceFirstChar {
                    if (it.isLowerCase()) it.titlecase(
                        Locale.getDefault()
                    ) else it.toString()
                }
                tvForumPosts.text = "${it.posts?.size} Posts"
                tvForumParticipants.text = "${it.participants?.size} Participants"
                tvForumDesc.text = if (it.desc.isNullOrEmpty()) "No Description" else it.desc

                root.setOnClickListener {
                    val adapterPosition = holder.bindingAdapterPosition
                    if (adapterPosition != RecyclerView.NO_POSITION) {
                        if (currentItem != null) {
                            listener.onForumClick(currentItem)
                        }
                    }
                }

            }
        }
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    private val differCallback = object : DiffUtil.ItemCallback<ForumData>() {
        override fun areItemsTheSame(oldItem: ForumData, newItem: ForumData): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: ForumData, newItem: ForumData): Boolean {
            return oldItem == newItem
        }
    }

    val differ = AsyncListDiffer(this, differCallback)

    interface OnClickHandle {
        fun onForumClick(data: ForumData)

    }
}