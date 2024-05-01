package com.smitcoderx.volunteerconnect.Ui.ParticipatedEvents

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.smitcoderx.volunteerconnect.R
import com.smitcoderx.volunteerconnect.databinding.FragmentParticipatedEventsBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ParticipatedEventsFragment : Fragment(R.layout.fragment_participated_events) {

    private lateinit var binding: FragmentParticipatedEventsBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentParticipatedEventsBinding.bind(view)


    }

}