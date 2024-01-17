package com.smitcoderx.volunteerconnect.Ui.HomeOrg

import android.animation.ValueAnimator
import android.annotation.SuppressLint
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.smitcoderx.volunteerconnect.databinding.LayoutDashBinding
import java.util.Random


class HomeDashboardAdapter(private val triggerCount: TriggerCount) :
    RecyclerView.Adapter<HomeDashboardAdapter.HomeDashboardViewHolder>() {

    inner class HomeDashboardViewHolder(private val binding: LayoutDashBinding) :
        ViewHolder(binding.root) {

            private var concatText = "+"
            fun bind(data: HomeOrgModel) {
                binding.card.background = generateRandomColor()
                binding.tvTitle.text = data.title
                val valueAnimator = (if (data.id == 5) 0 else data.value?.toInt())?.let {
                    ValueAnimator.ofInt(
                        it
                    )
                }
                valueAnimator?.addUpdateListener {
                    when (data.id) {
                        1, 3, 4 -> {
                            concatText = "+ Events"
                        }

                        2 -> {
                            concatText = "+ Followers"
                        }

                        6, 7 -> {
                            concatText = "+ Volunteers"
                        }

                        8 -> {
                            concatText = "+ Posts"
                        }

                        9 -> {
                            concatText = "+ Forums"
                        }

                        10 -> {
                            concatText = "+ Certifcates"
                        }
                    }
                    binding.tvAns.text =
                        if (data.id == 5) data.value else valueAnimator.animatedValue.toString() + concatText
                }
                valueAnimator?.duration = 1500
                triggerCount.startCount(valueAnimator)

                binding.card.setOnClickListener {
                    val position = bindingAdapterPosition
                    if(position != RecyclerView.NO_POSITION) {
                        val card = differ.currentList[position]
                       card?.let {
                           triggerCount.OnItemClick(card)
                       }
                    }
                }
            }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeDashboardViewHolder {
        val binding = LayoutDashBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return HomeDashboardViewHolder(binding)
    }

    @SuppressLint("Recycle", "SetTextI18n")
    override fun onBindViewHolder(holder: HomeDashboardViewHolder, position: Int) {
        val currentItem = differ.currentList[position]
        holder.bind(currentItem)


    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    private val differCallback = object : DiffUtil.ItemCallback<HomeOrgModel>() {
        override fun areItemsTheSame(oldItem: HomeOrgModel, newItem: HomeOrgModel): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: HomeOrgModel, newItem: HomeOrgModel): Boolean {
            return oldItem == newItem
        }
    }

    val differ = AsyncListDiffer(this, differCallback)
}

private fun generateRandomColor(): GradientDrawable {
    val r = Random()
    val red: Int = r.nextInt(255 - 0 + 1) + 0
    val green: Int = r.nextInt(255 - 0 + 1) + 0
    val blue: Int = r.nextInt(255 - 0 + 1) + 0

    val draw = GradientDrawable()
    draw.cornerRadius = 15f
    draw.setColor(Color.rgb(red, green, blue))
    return draw
}

interface TriggerCount {
    fun startCount(valueAnimator: ValueAnimator?)
    fun OnItemClick(item: HomeOrgModel)
}