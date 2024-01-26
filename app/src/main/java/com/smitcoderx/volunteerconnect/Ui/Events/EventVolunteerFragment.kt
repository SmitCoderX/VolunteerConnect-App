package com.smitcoderx.volunteerconnect.Ui.Events

import android.app.TimePickerDialog
import android.os.Bundle
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.TimePicker
import androidx.core.content.ContextCompat
import androidx.core.util.Pair
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.google.android.material.chip.Chip
import com.google.android.material.datepicker.CalendarConstraints
import com.google.android.material.datepicker.DateValidatorPointForward
import com.google.android.material.datepicker.MaterialDatePicker
import com.smitcoderx.volunteerconnect.R
import com.smitcoderx.volunteerconnect.databinding.FragmentEventVolunteerBinding
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale


class EventVolunteerFragment : Fragment(R.layout.fragment_event_volunteer) {

    private lateinit var binding: FragmentEventVolunteerBinding
    private val args by navArgs<EventVolunteerFragmentArgs>()
    private val calendar = Calendar.getInstance()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentEventVolunteerBinding.bind(view)
        val eventDataModel = args.eventData

        handleSkills()
        handleRadioGroups()

        binding.tilStartTime.editText?.setOnClickListener {
            datePickerDialog()
        }
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

    private fun datePickerDialog() {
        val builder: MaterialDatePicker.Builder<Pair<Long, Long>> =
            MaterialDatePicker.Builder.dateRangePicker()
        builder.setTitleText("Select Event Dates")
        val currentTime = calendar.timeInMillis
        val constraintsBuilder = CalendarConstraints.Builder()
        constraintsBuilder.setStart(currentTime)
        constraintsBuilder.setValidator(DateValidatorPointForward.now())
        builder.setCalendarConstraints(constraintsBuilder.build())
        val datePicker = builder.build()
        datePicker.addOnPositiveButtonClickListener {
            val endDate = it.second
            // Formating the selected dates as strings
            val sdf = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
            val startDateString: String = sdf.format(Date(currentTime))
            val endDateString: String = sdf.format(Date(endDate))

            binding.tilStartTime.editText?.setText(startDateString)
            binding.tilEndTime.editText?.setText(endDateString)

            showTimePickerDialog()
        }

        datePicker.show(childFragmentManager, "DATE_PICKER")
    }

    private fun showTimePickerDialog() {
        val hour = calendar[Calendar.HOUR_OF_DAY]
        val minute = calendar[Calendar.MINUTE]
        val timeStartPickerDialog = TimePickerDialog(
            context,
            { _: TimePicker?, hourOfDay: Int, minuteOfDay: Int ->
                binding.tilStartTime.editText?.text?.append(", $hourOfDay:$minuteOfDay")
            },
            hour,
            minute,
            true // 24-hour format
        )

        val timeEndPickerDialog = TimePickerDialog(
            context,
            { _: TimePicker?, hourOfDay: Int, minuteOfDay: Int ->
                binding.tilEndTime.editText?.text?.append(", $hourOfDay:$minuteOfDay")
            },
            hour,
            minute,
            true // 24-hour format
        )

        timeStartPickerDialog.setTitle("Event Start Time")
        timeEndPickerDialog.setTitle("Event End Time")
        timeEndPickerDialog.show()
        timeStartPickerDialog.show()
    }


}