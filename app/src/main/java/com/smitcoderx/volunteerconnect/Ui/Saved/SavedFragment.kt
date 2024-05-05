package com.smitcoderx.volunteerconnect.Ui.Saved

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.smitcoderx.volunteerconnect.Model.Events.DataFetch
import com.smitcoderx.volunteerconnect.R
import com.smitcoderx.volunteerconnect.Ui.Categories.CategoryEventAdapter
import com.smitcoderx.volunteerconnect.Ui.Home.HomeViewModel
import com.smitcoderx.volunteerconnect.Utils.Constants
import com.smitcoderx.volunteerconnect.databinding.FragmentSavedBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SavedFragment : Fragment(R.layout.fragment_saved), CategoryEventAdapter.OnCategory {

    private lateinit var binding: FragmentSavedBinding
    private val viewModel by viewModels<HomeViewModel>()
    private lateinit var savedAdapter: CategoryEventAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentSavedBinding.bind(view)

        savedAdapter = CategoryEventAdapter(this)
        handleSavedEvents()

        binding.rvEvents.apply {
            setHasFixedSize(true)
            adapter = savedAdapter
        }

        binding.ivBack.setOnClickListener {
            findNavController().popBackStack()
        }

    }

    private fun handleSavedEvents() {
        viewModel.savedEvents().observe(viewLifecycleOwner) {
            Log.d(Constants.TAG, "handleSavedEvents: $it")
            if (it.isNullOrEmpty()) {
                binding.llError.visibility = View.VISIBLE
                binding.rvEvents.visibility = View.GONE
                binding.cardRetry.visibility = View.GONE
                binding.lottieView.setAnimation(R.raw.no_data)
                binding.lottieView.loop(true)
                binding.lottieView.playAnimation()
                binding.tvErrorMsg.text = "Oops! No Saved Events"

            } else {
                binding.llError.visibility = View.GONE
                binding.rvEvents.visibility = View.VISIBLE
                savedAdapter.differ.submitList(it)
            }
        }
    }

    override fun onEventHandleClick(eventData: DataFetch) {
        findNavController().navigate(
            SavedFragmentDirections.actionSavedFragmentToSingleEventFragment(eventData)
        )
    }

    override fun onEventFavClick(eventData: DataFetch) {
        if (eventData.isSaved) {
            viewModel.insertEvent(eventData)
        } else {
            viewModel.deleteEvent(eventData)
        }
    }
}