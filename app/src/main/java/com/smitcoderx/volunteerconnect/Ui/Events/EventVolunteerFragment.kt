package com.smitcoderx.volunteerconnect.Ui.Events

import android.annotation.SuppressLint
import android.app.TimePickerDialog
import android.content.Context
import android.os.Bundle
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.TimePicker
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.util.Pair
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.google.android.material.chip.Chip
import com.google.android.material.datepicker.CalendarConstraints
import com.google.android.material.datepicker.DateValidatorPointForward
import com.google.android.material.datepicker.MaterialDatePicker
import com.smitcoderx.volunteerconnect.R
import com.smitcoderx.volunteerconnect.Ui.Events.Sheets.ForumDialogFragment
import com.smitcoderx.volunteerconnect.Utils.Constants.FORUM_NAME
import com.smitcoderx.volunteerconnect.Utils.DataStoreUtil
import com.smitcoderx.volunteerconnect.Utils.LoadingInterface
import com.smitcoderx.volunteerconnect.Utils.ResponseState
import com.smitcoderx.volunteerconnect.databinding.FragmentEventVolunteerBinding
import dagger.hilt.android.AndroidEntryPoint
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale


@AndroidEntryPoint
class EventVolunteerFragment : Fragment(R.layout.fragment_event_volunteer),
    ForumDialogFragment.OnSheetWork {

    private lateinit var binding: FragmentEventVolunteerBinding
    private val args by navArgs<EventVolunteerFragmentArgs>()
    private val calendar = Calendar.getInstance()
    private lateinit var prefs: DataStoreUtil
    private val eventViewModel by viewModels<EventViewModel>()
    private var listener: LoadingInterface? = null


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentEventVolunteerBinding.bind(view)
        val eventDataModel = args.eventData
        prefs = DataStoreUtil(requireContext())

        handleSkills()
        handleRadioGroups()
        updateEventStatus()

        binding.tilStartTime.editText?.setOnClickListener {
            datePickerDialog()
        }

        binding.checkboxForum.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                val addItemDialog = ForumDialogFragment(this)
                val bundle = Bundle()
                bundle.putString(FORUM_NAME, "Forum-${eventDataModel.name}")
                addItemDialog.arguments = bundle
                addItemDialog.show(childFragmentManager, addItemDialog.tag)
            } else {
                binding.checkboxForum.text = requireContext().getText(R.string.forum_label)
            }
        }

        binding.btnNext.setOnClickListener {
            val data = Data(
                address = eventDataModel.address,
                category = listOf(binding.etCategory.text.toString()),
                coordinates = listOf(24.32342, 10.3423423),
                desc = eventDataModel.desc,
                email = eventDataModel.email,
                phone = eventDataModel.phone,
                name = eventDataModel.name,
                volunteerCount = binding.etVolunteerCount.text.toString().toInt(),
                skills = listOf("data", "check"),
                visibility = binding.radioEventType.getCheckedRadioText(binding.radioEventType.checkedRadioButtonId),
                isPaid = binding.radioEventPaid.getCheckedRadioText(binding.radioEventPaid.checkedRadioButtonId)
                    .equals("Paid", ignoreCase = true),
                price = Integer.parseInt(binding.etPayment.text.toString()),
                isGoodiesProvided = !binding.etGoodies.text.isNullOrBlank(),
                goodies = binding.etGoodies.text.toString(),
                eventStartDataAndTime = Date(binding.etStart.text.toString()),
                eventEndingDateAndTime = Date(binding.etEnd.text.toString()),
            )
            eventViewModel.createEvent(prefs.getToken().toString(), data)
        }
    }

    private fun updateEventStatus() {
        eventViewModel.createEventLiveData.observe(viewLifecycleOwner) {
            when (it) {
                is ResponseState.Success -> {
                    hideLoading()
                    Toast.makeText(requireContext(), "Event Created", Toast.LENGTH_SHORT).show()

                }

                is ResponseState.Error -> {
                    hideLoading()
                    Toast.makeText(requireContext(), it.message.toString(), Toast.LENGTH_SHORT)
                        .show()
                }

                is ResponseState.Loading -> {

                }
            }
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is LoadingInterface) {
            listener = context
        } else {
            throw ClassCastException("$context must implement LoadingInterface")
        }
    }


    private fun showLoading() {
        listener?.showLoading()
        binding.ivBack.isClickable = false
    }

    private fun hideLoading() {
        listener?.hideLoading()
        binding.ivBack.isClickable = true
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
            context, { _: TimePicker?, hourOfDay: Int, minuteOfDay: Int ->
                binding.tilStartTime.editText?.text?.append(", $hourOfDay:$minuteOfDay")
            }, hour, minute, true // 24-hour format
        )

        val timeEndPickerDialog = TimePickerDialog(
            context, { _: TimePicker?, hourOfDay: Int, minuteOfDay: Int ->
                binding.tilEndTime.editText?.text?.append(", $hourOfDay:$minuteOfDay")
            }, hour, minute, true // 24-hour format
        )

        timeStartPickerDialog.setTitle("Event Start Time")
        timeEndPickerDialog.setTitle("Event End Time")
        timeEndPickerDialog.show()
        timeStartPickerDialog.show()
    }

    @SuppressLint("SetTextI18n")
    override fun onSheetClose(name: String) {
        if (name.isEmpty()) {
            binding.checkboxForum.isChecked = false
        } else {
            binding.checkboxForum.text = "Forum Will be Created by Name: $name"
        }
    }

    companion object {
        fun RadioGroup.getCheckedRadioText(id: Int): String {
            val radioBtn = this.findViewById<RadioButton>(id)
            return radioBtn.text.toString().lowercase()
        }
    }
}