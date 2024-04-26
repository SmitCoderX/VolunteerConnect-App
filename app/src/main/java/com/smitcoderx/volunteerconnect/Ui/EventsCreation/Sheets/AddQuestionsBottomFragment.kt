package com.smitcoderx.volunteerconnect.Ui.EventsCreation.Sheets


import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.findNavController
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.smitcoderx.volunteerconnect.R
import com.smitcoderx.volunteerconnect.databinding.FragmentAddQuestionBottomBinding


class AddQuestionsBottomFragment :
    BottomSheetDialogFragment(R.layout.fragment_add_question_bottom) {

    private lateinit var binding: FragmentAddQuestionBottomBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentAddQuestionBottomBinding.bind(view)

        binding.btnClose.setOnClickListener {
            findNavController().popBackStack()
        }

        binding.btnDone.setOnClickListener {
            if (validate()) {
                findNavController().previousBackStackEntry?.savedStateHandle?.set(
                    "questions",
                    questionList()
                )
                findNavController().popBackStack()
            }
        }
    }

    private fun questionList(): List<String> {
        return listOf(
            binding.tilQuestion1.editText?.text.toString(),
            binding.tilQuestion2.editText?.text.toString(),
            binding.tilQuestion3.editText?.text.toString(),
            binding.tilQuestion4.editText?.text.toString()
        )
    }

    private fun validate(): Boolean {
        return binding.tilQuestion1.editText?.text.toString()
            .isNotEmpty() && binding.tilQuestion2.editText?.text.toString()
            .isNotEmpty() && binding.tilQuestion3.editText?.text.toString()
            .isNotEmpty() && binding.tilQuestion4.editText?.text.toString().isNotEmpty()
    }

}