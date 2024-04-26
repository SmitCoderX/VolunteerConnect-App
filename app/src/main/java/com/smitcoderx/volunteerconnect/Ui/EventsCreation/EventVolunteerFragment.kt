package com.smitcoderx.volunteerconnect.Ui.EventsCreation

import android.annotation.SuppressLint
import android.app.TimePickerDialog
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.ArrayAdapter
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.TimePicker
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.util.Pair
import androidx.core.view.children
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.android.material.chip.Chip
import com.google.android.material.datepicker.CalendarConstraints
import com.google.android.material.datepicker.DateValidatorPointForward
import com.google.android.material.datepicker.MaterialDatePicker
import com.smitcoderx.volunteerconnect.Model.Events.Data
import com.smitcoderx.volunteerconnect.R
import com.smitcoderx.volunteerconnect.Ui.EventsCreation.Sheets.ForumDialogFragment
import com.smitcoderx.volunteerconnect.Ui.Home.HomeViewModel
import com.smitcoderx.volunteerconnect.Utils.Constants.FORUM_NAME
import com.smitcoderx.volunteerconnect.Utils.Constants.TAG
import com.smitcoderx.volunteerconnect.Utils.DataStoreUtil
import com.smitcoderx.volunteerconnect.Utils.LoadingInterface
import com.smitcoderx.volunteerconnect.Utils.ResponseState
import com.smitcoderx.volunteerconnect.Utils.hasInternetConnection
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
    private val homeViewModel by viewModels<HomeViewModel>()
    private var listener: LoadingInterface? = null
    private var forumName: String = ""
    private var questionsList = mutableListOf<String>()


    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentEventVolunteerBinding.bind(view)

        val eventDataModel = args.eventData
        prefs = DataStoreUtil(requireContext())
        eventViewModel.isNetworkConnectedLiveData.value = requireContext().hasInternetConnection()
        homeViewModel.isNetworkConnectedLiveData.value = requireContext().hasInternetConnection()
        homeViewModel.getCategoryList()
        handleCategoryData()
        handleChipGroups()
        handleRadioGroups()
        updateEventStatus()

        binding.tilStartTime.editText?.setOnClickListener {
            datePickerDialog()
        }

        binding.checkboxForum.setOnCheckedChangeListener { _, isChecked ->
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

        binding.addQuestions.setOnClickListener {
            findNavController().navigate(EventVolunteerFragmentDirections.actionEventVolunteerFragmentToAddQuestionsBottomFragment())
        }

        findNavController().currentBackStackEntry?.savedStateHandle?.getLiveData<List<String>>("questions")
            ?.observe(viewLifecycleOwner) {
                if (it.isNullOrEmpty()) {
                    binding.tvAddQuestions.text = getString(R.string.add_ques)
                } else {
                    binding.tvAddQuestions.text = "${it.size} Questions"
                    questionsList.addAll(it)
                }
            }

        binding.btnNext.setOnClickListener {
            val data = Data(
                eventPicture = eventDataModel.eventPicture,
                address = eventDataModel.address,
                category = binding.chipCategory.children.map { (it as Chip).text.toString() }
                    .toList(),
                coordinates = listOf(24.32342, 10.3423423),
                desc = eventDataModel.desc,
                email = eventDataModel.email,
                phone = eventDataModel.phone,
                name = eventDataModel.name,
                volunteerCount = binding.etVolunteerCount.text.toString().toInt(),
                skills = binding.chipSkills.children.map { (it as Chip).text.toString() }.toList(),
                visibility = binding.radioEventType.getCheckedRadioText(binding.radioEventType.checkedRadioButtonId),
                isPaid = binding.tilFee.editText?.text.toString().isNotEmpty(),
                price = if (binding.etFee.text?.toString()
                        ?.isEmpty() == true
                ) 0 else Integer.parseInt(binding.etFee.text.toString()),
                isPaying = binding.radioEventPaid.getCheckedRadioText(binding.radioEventPaid.checkedRadioButtonId)
                    .equals("Paid", ignoreCase = true),
                payment = if (binding.etPayment.text?.toString()
                        ?.isEmpty() == true
                ) 0 else Integer.parseInt(binding.etPayment.text.toString()),
                isGoodiesProvided = binding.etGoodies.text?.isNotEmpty(),
                isResource = binding.checkboxResource.isChecked,
                goodies = binding.etGoodies.text.toString(),
                eventStartDataAndTime = Date(binding.etStart.text.toString()).toString(),
                eventEndingDateAndTime = Date(binding.etEnd.text.toString()).toString(),
                isForumCreated = binding.checkboxForum.isChecked,
                forumName = forumName,
                gallery = listOf(
                    binding.tilGallery1.editText?.text.toString(),
                    binding.tilGallery2.editText?.text.toString(),
                    binding.tilGallery3.editText?.text.toString(),
                    binding.tilGallery4.editText?.text.toString(),
                ),
                question = questionsList

            )
            eventViewModel.createEvent(prefs.getToken().toString(), data)
            showLoading()
        }

        binding.ivBack.setOnClickListener {
            findNavController().popBackStack()
        }

    }

    private fun handleCategoryData() {
        val arrayAdapter =
            ArrayAdapter<String>(requireContext(), android.R.layout.select_dialog_item)
        homeViewModel.categoryDataLiveData.observe(viewLifecycleOwner) {
            when (it) {
                is ResponseState.Success -> {
                    it.data?.data?.map { item -> item?.category?.split("\n")!![1] }
                        ?.let { it1 -> arrayAdapter.addAll(it1.toList()) }
                    binding.etCategory.apply {
                        threshold = 1
                        setAdapter(arrayAdapter)
                    }
                }

                else -> {
                    Log.d(TAG, "handleCategoryData: ")
                }
            }
        }
    }

    private fun updateEventStatus() {
        eventViewModel.createEventLiveData.observe(viewLifecycleOwner) {
            when (it) {
                is ResponseState.Success -> {
                    hideLoading()
                    Toast.makeText(requireContext(), "Event Created", Toast.LENGTH_SHORT).show()
                    findNavController().popBackStack(R.id.homeOrgFragment, false)
                }

                is ResponseState.Error -> {
                    hideLoading()
                    Toast.makeText(requireContext(), it.message.toString(), Toast.LENGTH_SHORT)
                        .show()
                    Log.d(TAG, "updateEventStatus: ${it.message.toString()}")
                }

                is ResponseState.Loading -> {
                    showLoading()
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

    private fun addChipToGroup(type: String, skill: String) {
        val chip = Chip(context)
        chip.text = skill
        chip.chipIcon =
            ContextCompat.getDrawable(requireContext(), R.drawable.ic_launcher_background)
        chip.isChipIconVisible = false
        chip.isCloseIconVisible = true
        // necessary to get single selection working
        chip.isClickable = true
        chip.isCheckable = false
        if (type.equals("Category", ignoreCase = true)) {
            binding.chipCategory.addView(chip as View)
            chip.setOnCloseIconClickListener {
                binding.chipCategory.removeView(chip as View)
            }
        } else {
            binding.chipSkills.addView(chip as View)
            chip.setOnCloseIconClickListener {
                binding.chipSkills.removeView(chip as View)
            }
        }

    }

    private fun handleChipGroups() {
        binding.tilSkills.editText?.setOnEditorActionListener { v, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                binding.tilSkills.editText?.imeOptions = EditorInfo.IME_ACTION_DONE
                if (v.text.isNotEmpty()) {
                    addChipToGroup("Skills", binding.tilSkills.editText?.text.toString())
                    v.text = ""
                }
            }
            true
        }

        binding.tilCategory.editText?.setOnEditorActionListener { v, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                binding.tilCategory.editText?.imeOptions = EditorInfo.IME_ACTION_DONE
                if (v.text.isNotEmpty()) {
                    addChipToGroup("Category", binding.tilCategory.editText?.text.toString())
                    v.text = ""
                }
            }
            true
        }
    }

    private fun handleRadioGroups() {
        binding.radioEventType.setOnCheckedChangeListener { _, checkedId ->
            when (checkedId) {
                binding.rPrivate.id -> {
                    binding.tilPrivate.visibility = View.VISIBLE
                }

                binding.rFees.id -> {
                    binding.tilFee.visibility = View.VISIBLE
                }

                else -> {
                    binding.tilPrivate.visibility = View.GONE
                    binding.tilFee.visibility = View.GONE
                }
            }
        }

        binding.radioEventPaid.setOnCheckedChangeListener { _, checkedId ->
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
            forumName = name
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
