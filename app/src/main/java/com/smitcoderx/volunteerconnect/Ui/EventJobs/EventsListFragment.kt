package com.smitcoderx.volunteerconnect.Ui.EventJobs

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.smitcoderx.volunteerconnect.R
import com.smitcoderx.volunteerconnect.databinding.FragmentEventListBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class EventsListFragment: Fragment(R.layout.fragment_event_list) {

    private lateinit var binding: FragmentEventListBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentEventListBinding.bind(view)
    }

}