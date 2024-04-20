package com.smitcoderx.volunteerconnect.Ui.Events.Sheets


import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.core.content.ContextCompat
import androidx.core.view.setPadding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.textfield.TextInputEditText
import com.smitcoderx.volunteerconnect.R
import com.smitcoderx.volunteerconnect.Utils.Constants.TAG
import com.smitcoderx.volunteerconnect.databinding.FragmentAddQuestionBottomBinding


class AddQuestionsBottomFragment :
    BottomSheetDialogFragment(R.layout.fragment_add_question_bottom) {

    private lateinit var binding: FragmentAddQuestionBottomBinding
    private val editTextList = mutableListOf<Any>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentAddQuestionBottomBinding.bind(view)



        binding.btnAdd.setOnClickListener {
            addQuestion()
        }

        binding.btnDelete.setOnClickListener {
            deleteQuestion()
        }
    }

    private fun addQuestion() {
        val layout = binding.llQuestions
        val question = TextInputEditText(requireContext())
        question.setPadding(50)
        question.setBackgroundDrawable(
            ContextCompat.getDrawable(
                requireContext(), R.drawable.rounded_bottom_bg
            )
        )
        question.tag = layout.childCount
        editTextList.add(question.tag)
        Log.d(TAG, "addQuestion: $editTextList")
        question.setHint(R.string.add_ques)
        layout.addView(question, layout.childCount)

    }

    private fun deleteQuestion() {
        val layout = binding.llQuestions
        if (layout.getChildAt(0) != null) {
            layout.removeViewAt(0)
            editTextList.removeAt(0)
            Log.d(TAG, "deleteQuestion: $editTextList")
        }
    }
}