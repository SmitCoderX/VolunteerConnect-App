package com.smitcoderx.volunteerconnect.Ui.Home

import android.graphics.Color
import android.graphics.PorterDuff
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.smitcoderx.volunteerconnect.Model.Category.CategoryData
import com.smitcoderx.volunteerconnect.databinding.LayoutTypesItemBinding


class TypesAdapter(private val listener: OnEvents) : RecyclerView.Adapter<TypesAdapter.TypesViewHolder>() {

    inner class TypesViewHolder(val binding: LayoutTypesItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TypesViewHolder {
        val binding =
            LayoutTypesItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return TypesViewHolder(binding)
    }

    override fun onBindViewHolder(holder: TypesViewHolder, position: Int) {
        val currentItem = differ.currentList[position]

        currentItem.let { item ->
            holder.binding.tvType.text = item.category
            holder.binding.llCard.background.setColorFilter(
                Color.parseColor(item.color),
                PorterDuff.Mode.SRC_ATOP
            )

            holder.binding.root.setOnClickListener {
                val adapterPosition = holder.bindingAdapterPosition
                if(adapterPosition != RecyclerView.NO_POSITION) {
                    if(item != null) {
                        listener.onEventClick(item)
                    }
                }
            }
        }
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    private val differCallback = object : DiffUtil.ItemCallback<CategoryData>() {
        override fun areItemsTheSame(oldItem: CategoryData, newItem: CategoryData): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: CategoryData, newItem: CategoryData): Boolean {
            return oldItem == newItem
        }
    }

    val differ = AsyncListDiffer(this, differCallback)


    interface OnEvents{
        fun onEventClick(category: CategoryData)
    }

}