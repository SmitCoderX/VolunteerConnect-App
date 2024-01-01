package com.smitcoderx.volunteerconnect.Ui.Home

import android.os.Bundle
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.android.material.badge.BadgeDrawable
import com.google.android.material.badge.ExperimentalBadgeUtils
import com.smitcoderx.volunteerconnect.R
import com.smitcoderx.volunteerconnect.databinding.FragmentHomeBinding


@ExperimentalBadgeUtils
class HomeFragment : Fragment(R.layout.fragment_home) {

    private lateinit var binding: FragmentHomeBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentHomeBinding.bind(view)
        binding.ivNotification.clipToOutline = false
        val badgeDrawable = BadgeDrawable.create(requireContext())
        badgeDrawable.isVisible = true
        badgeDrawable.badgeGravity = BadgeDrawable.TOP_END
    }
}