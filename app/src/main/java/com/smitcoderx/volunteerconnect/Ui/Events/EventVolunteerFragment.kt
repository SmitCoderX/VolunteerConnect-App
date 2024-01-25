package com.smitcoderx.volunteerconnect.Ui.Events

import android.os.Bundle
import android.view.View
import android.view.inputmethod.EditorInfo
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.google.android.material.chip.Chip
import com.smitcoderx.volunteerconnect.R
import com.smitcoderx.volunteerconnect.databinding.FragmentEventVolunteerBinding

class EventVolunteerFragment : Fragment(R.layout.fragment_event_volunteer) {

    private lateinit var binding: FragmentEventVolunteerBinding
    private val args by navArgs<EventVolunteerFragmentArgs>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentEventVolunteerBinding.bind(view)
        val eventDataModel = args.eventData

        handleSkills()
        handleRadioGroups()
    }

    private fun addChipToGroup(skill: String) {
        val chip = Chip(context)
        chip.text = skill
        chip.chipIcon =
            ContextCompat.getDrawable(requireContext(), R.drawable.ic_launcher_background)
        chip.isChipIconVisible = false
        chip.isCloseIconVisible = true
        // necessary to get single selection working
        chip.isClickable = true
        chip.isCheckable = false
        binding.chipSkills.addView(chip as View)
        chip.setOnCloseIconClickListener {
            binding.chipSkills.removeView(chip as View)
        }
    }

    private fun handleSkills() {
        binding.tilSkills.editText?.setOnEditorActionListener { v, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                binding.tilSkills.editText?.imeOptions = EditorInfo.IME_ACTION_DONE
                if (v.text.isNotEmpty()) {
                    addChipToGroup(binding.tilSkills.editText?.text.toString())
                    v.text = ""
                }
            }
            true
        }
    }

    private fun handleRadioGroups() {
        binding.radioEventType.setOnCheckedChangeListener { group, checkedId ->
            if (checkedId == binding.rPrivate.id) {
                binding.tilPrivate.visibility = View.VISIBLE
            } else {
                binding.tilPrivate.visibility = View.GONE
            }
        }

        binding.radioEventPaid.setOnCheckedChangeListener { group, checkedId ->
            if (checkedId == binding.rPaid.id) {
                binding.tilPayment.visibility = View.VISIBLE
            } else {
                binding.tilPayment.visibility = View.GONE
            }
        }
    }

}