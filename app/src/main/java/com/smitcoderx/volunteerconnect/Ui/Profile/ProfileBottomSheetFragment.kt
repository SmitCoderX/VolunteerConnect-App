package com.smitcoderx.volunteerconnect.Ui.Profile

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.smitcoderx.volunteerconnect.R
import com.smitcoderx.volunteerconnect.Utils.DataStoreUtil
import com.smitcoderx.volunteerconnect.databinding.FragmentBottomSheetProfileBinding

class ProfileBottomSheetFragment :
    BottomSheetDialogFragment(R.layout.fragment_bottom_sheet_profile) {

    private lateinit var binding: FragmentBottomSheetProfileBinding
    private val args by navArgs<ProfileBottomSheetFragmentArgs>()
    private lateinit var prefs: DataStoreUtil

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentBottomSheetProfileBinding.bind(view)

        val userData = args.userData
        prefs = DataStoreUtil(requireContext())

        Glide.with(requireContext()).load(userData.photos.toString()).into(binding.ivSheetProfile)
        binding.tvSheetName.text = userData.name?.split(" ")?.get(0) ?: userData.name
        binding.tvSheetUsername.text = "@${userData.username}"
        binding.tvSheetEmail.text = userData.email

        binding.cardSaved.setOnClickListener {
            findNavController().navigate(
                ProfileBottomSheetFragmentDirections.actionProfileBottomSheetFragmentToSavedFragment()
            )
        }


        binding.btnLogout.setOnClickListener {
            prefs.logoutUser()
            findNavController().navigate(ProfileBottomSheetFragmentDirections.actionProfileBottomSheetFragmentToLoginFragment())
        }

        binding.cardProfile.setOnClickListener {
            findNavController().navigate(ProfileBottomSheetFragmentDirections.actionProfileBottomSheetFragmentToProfileFragment())
        }
    }
}