package com.smitcoderx.volunteerconnect.Ui.Maps

import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.smitcoderx.volunteerconnect.R
import com.smitcoderx.volunteerconnect.databinding.FragmentBottomMapEventBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MapEventBottomFragment : BottomSheetDialogFragment(R.layout.fragment_bottom_map_event) {

    private lateinit var binding: FragmentBottomMapEventBinding
    private val args by navArgs<MapEventBottomFragmentArgs>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentBottomMapEventBinding.bind(view)

        Glide.with(requireActivity()).load(args.eventData?.eventPicture)
            .placeholder(R.drawable.img).transition(DrawableTransitionOptions.withCrossFade())
            .into(binding.ivEvent)

        binding.tvSheetName.text = args.eventData?.name
        binding.tvSheetDesc.text = args.eventData?.desc

        binding.btnNext.setOnClickListener {
            findNavController().navigate(
                MapEventBottomFragmentDirections.actionMapEventBottomFragmentToSingleEventFragment(
                    args.eventData
                )
            )
        }

    }
}