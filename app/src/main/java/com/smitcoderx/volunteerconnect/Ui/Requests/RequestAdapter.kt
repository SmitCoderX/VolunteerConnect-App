package com.smitcoderx.volunteerconnect.Ui.Requests

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.smitcoderx.volunteerconnect.Model.Requests.RequestsData
import com.smitcoderx.volunteerconnect.R
import com.smitcoderx.volunteerconnect.databinding.LayoutRequestItemBinding
import java.util.Locale

class RequestAdapter(private val listener: OnRequestHandle, private val role: String) :
    RecyclerView.Adapter<RequestAdapter.RequestViewHolder>() {

    inner class RequestViewHolder(val binding: LayoutRequestItemBinding) : ViewHolder(
        binding.root
    )

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RequestViewHolder {
        val binding =
            LayoutRequestItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return RequestViewHolder(binding)
    }

    override fun onBindViewHolder(holder: RequestViewHolder, position: Int) {
        val currentItem = differ.currentList[position]

        currentItem.let {
//            if (role.equals("organization", ignoreCase = true)) {
//
//            } else {
//
//            }

            holder.binding.apply {
                Glide.with(holder.itemView).load(it.appliedByProfile)
                    .transition(DrawableTransitionOptions.withCrossFade())
                    .placeholder(R.drawable.img).into(ivProfile)

                tvEventName.text = it.eventName?.replaceFirstChar {
                    if (it.isLowerCase()) it.titlecase(
                        Locale.getDefault()
                    ) else it.toString()
                }

                tvSendRequest.text = it.appliedBy

                tvDesc.text = if (role.equals(
                        "organization", ignoreCase = true
                    )
                ) "Sended a request to Join the Event" else "Will Notify when you request will be updated."
                it.answers.forEach { map ->
                    map?.entries?.forEach { (k, v) ->
                        tvAnswers.append("$k: $v \n")
                    }
                }

                var drop = false
                root.setOnClickListener {
                    val adapterPosition = holder.bindingAdapterPosition
                    if (adapterPosition != RecyclerView.NO_POSITION) {
                        if (currentItem != null) {
                            if (drop) {
                                drop = false
                                dropDown.rotation = 0f
                                tvAnswers.visibility = View.GONE
                                tvAnswersTitle.visibility = View.GONE
                            } else {
                                drop = true
                                dropDown.rotation = 180f
                                tvAnswers.visibility = View.VISIBLE
                                tvAnswersTitle.visibility = View.VISIBLE
                            }
                        }
                    }
                }

                dropDown.setOnClickListener {
                    val adapterPosition = holder.bindingAdapterPosition
                    if (adapterPosition != RecyclerView.NO_POSITION) {
                        if (currentItem != null) {
                            if (drop) {
                                drop = false
                                dropDown.rotation = 0f
                                tvAnswers.visibility = View.GONE
                                tvAnswersTitle.visibility = View.GONE
                            } else {
                                drop = true
                                dropDown.rotation = 180f
                                tvAnswers.visibility = View.VISIBLE
                                tvAnswersTitle.visibility = View.VISIBLE
                            }
                        }
                    }
                }

                btnDone.setOnClickListener {
                    val adapterPosition = holder.bindingAdapterPosition
                    if (adapterPosition != RecyclerView.NO_POSITION) {
                        if (currentItem != null) {
                            listener.onRequestAccept(currentItem)
                        }
                    }
                }

                btnClose.setOnClickListener {
                    val adapterPosition = holder.bindingAdapterPosition
                    if (adapterPosition != RecyclerView.NO_POSITION) {
                        if (currentItem != null) {
                            listener.onRequestDecline(currentItem)
                        }
                    }
                }
            }
        }
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    private val differCallback = object : DiffUtil.ItemCallback<RequestsData>() {
        override fun areItemsTheSame(oldItem: RequestsData, newItem: RequestsData): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: RequestsData, newItem: RequestsData): Boolean {
            return oldItem == newItem
        }
    }

    val differ = AsyncListDiffer(this, differCallback)

    interface OnRequestHandle {
        fun onRequestAccept(requestsData: RequestsData)
        fun onRequestDecline(requestsData: RequestsData)

    }
}