package com.smitcoderx.volunteerconnect.Ui.EventsCreation

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.smitcoderx.volunteerconnect.Model.Events.Data
import com.smitcoderx.volunteerconnect.R
import com.smitcoderx.volunteerconnect.databinding.FragmentEventBinding

class EventFragment : Fragment(R.layout.fragment_event) {

    private lateinit var binding: FragmentEventBinding

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentEventBinding.bind(view)

        binding.btnNext.setOnClickListener {
            val eventData = Data(
                eventPicture = binding.tilUploadEventPic.editText?.text.toString().trim(),
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

        binding.ivBack.setOnClickListener {
            findNavController().popBackStack()
        }

    }

}