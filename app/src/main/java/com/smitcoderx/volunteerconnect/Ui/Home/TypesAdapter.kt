package com.smitcoderx.volunteerconnect.Ui.Home

import android.R
import android.graphics.Color
import android.graphics.PorterDuff
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.smitcoderx.volunteerconnect.databinding.LayoutTypesItemBinding


class TypesAdapter : RecyclerView.Adapter<TypesAdapter.TypesViewHolder>(){

    inner class TypesViewHolder(val binding: LayoutTypesItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TypesViewHolder {
        val binding = LayoutTypesItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return TypesViewHolder(binding)
    }

    override fun onBindViewHolder(holder: TypesViewHolder, position: Int) {
        val currentItem = differ.currentList[position]

       currentItem.let {item ->
           holder.binding.tvType.text = item.types
           holder.binding.llCard.background.setTint(item.color)
       }
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    private val differCallback = object : DiffUtil.ItemCallback<TypesModel>() {
        override fun areItemsTheSame(oldItem: TypesModel, newItem: TypesModel): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: TypesModel, newItem: TypesModel): Boolean {
            return oldItem == newItem
        }
    }

    val differ = AsyncListDiffer(this, differCallback)


}