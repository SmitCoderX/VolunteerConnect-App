package com.smitcoderx.volunteerconnect.Ui.Events

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.smitcoderx.volunteerconnect.R
import com.smitcoderx.volunteerconnect.Ui.Events.Models.Data
import com.smitcoderx.volunteerconnect.databinding.FragmentEventBinding

class EventFragment : Fragment(R.layout.fragment_event) {

    private lateinit var binding: FragmentEventBinding

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentEventBinding.bind(view)

        binding.cardAddImage.setOnClickListener {
            if (!binding.cardImg.isVisible) {
                binding.cardImg.visibility = View.VISIBLE
                binding.tvAddImg.text = "Change Image"
                binding.cardDeleteImage.visibility = View.VISIBLE
            }
        }

        binding.cardDeleteImage.setOnClickListener {
            binding.cardImg.visibility = View.GONE
            binding.tvAddImg.text = "Upload Image"
            binding.cardDeleteImage.visibility = View.GONE
        }

        binding.btnNext.setOnClickListener {
            val eventData = Data(
                name = binding.tilTitle.editText?.text.toString().trim(),
                desc = binding.tilDesc.editText?.text.toString().trim(),
                address = binding.tilAddress.editText?.text.toString().trim(),
                phone = binding.tilMobile.editText?.text.toString().trim(),
                email = binding.tilEmail.editText?.text.toString().trim(),
            )
            val action =
                EventFragmentDirections.actionEventFragmentToEventVolunteerFragment(eventData)
            findNavController().navigate(action)
        }


    }

}