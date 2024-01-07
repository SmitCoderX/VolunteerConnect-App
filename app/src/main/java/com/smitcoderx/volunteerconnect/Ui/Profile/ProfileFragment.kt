package com.smitcoderx.volunteerconnect.Ui.Profile

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.smitcoderx.volunteerconnect.R
import com.smitcoderx.volunteerconnect.databinding.FragmentProfileBinding

class ProfileFragment: Fragment(R.layout.fragment_profile) {

    private lateinit var binding: FragmentProfileBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentProfileBinding.bind(view)



    }

}