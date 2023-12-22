package com.smitcoderx.volunteerconnect.Ui.Register

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.smitcoderx.volunteerconnect.R
import com.smitcoderx.volunteerconnect.databinding.FragmentRegisterAsBinding

class RegisterAsFragment: Fragment(R.layout.fragment_register_as) {

    private lateinit var binding: FragmentRegisterAsBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentRegisterAsBinding.bind(view)

        binding.tvNoAcc.setOnClickListener {
            findNavController().navigate(RegisterAsFragmentDirections.actionRegisterAsFragmentToLoginFragment())
        }


    }
}