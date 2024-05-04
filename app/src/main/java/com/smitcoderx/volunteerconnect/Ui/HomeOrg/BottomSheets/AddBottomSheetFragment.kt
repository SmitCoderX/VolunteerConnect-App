package com.smitcoderx.volunteerconnect.Ui.HomeOrg.BottomSheets

import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.findNavController
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.smitcoderx.volunteerconnect.R
import com.smitcoderx.volunteerconnect.databinding.FragmentBottomSheetAddBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AddBottomSheetFragment : BottomSheetDialogFragment(R.layout.fragment_bottom_sheet_add) {

    private lateinit var binding: FragmentBottomSheetAddBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentBottomSheetAddBinding.bind(view)

        binding.cardAddEvent.setOnClickListener {
            findNavController().navigate(AddBottomSheetFragmentDirections.actionAddBottomSheetFragmentToEventFragment())
        }

        binding.cardAddPost.setOnClickListener {
            findNavController().navigate(AddBottomSheetFragmentDirections.actionAddBottomSheetFragmentToActionJobs())
        }

    }

}