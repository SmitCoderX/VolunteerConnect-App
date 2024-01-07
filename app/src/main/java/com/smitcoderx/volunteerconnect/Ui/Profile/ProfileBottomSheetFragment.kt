package com.smitcoderx.volunteerconnect.Ui.Profile

import android.os.Bundle
import android.view.View
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.smitcoderx.volunteerconnect.R
import com.smitcoderx.volunteerconnect.databinding.FragmentBottomSheetProfileBinding

class ProfileBottomSheetFragment: BottomSheetDialogFragment(R.layout.fragment_bottom_sheet_profile) {

    private lateinit var binding: FragmentBottomSheetProfileBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentBottomSheetProfileBinding.bind(view)
    }

}