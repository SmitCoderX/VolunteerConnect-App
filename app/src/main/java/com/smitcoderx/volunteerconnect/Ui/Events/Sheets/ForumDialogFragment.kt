package com.smitcoderx.volunteerconnect.Ui.Events.Sheets

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.fragment.app.DialogFragment
import com.smitcoderx.volunteerconnect.R
import com.smitcoderx.volunteerconnect.Utils.Constants.FORUM_NAME
import com.smitcoderx.volunteerconnect.databinding.FragmentDialogForumBinding
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class ForumDialogFragment(private val nameListener: OnSheetWork) :
    DialogFragment(R.layout.fragment_dialog_forum) {

    private lateinit var binding: FragmentDialogForumBinding


    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.setCancelable(false)
        dialog.setCanceledOnTouchOutside(false)
        return dialog
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentDialogForumBinding.bind(view)

        val bundle = arguments
        val eventName = bundle?.getString(FORUM_NAME)

        binding.ivClose.setOnClickListener {
            nameListener.onSheetClose("")
            dialog?.dismiss()
        }

        binding.tilForumName.editText?.setText(eventName)

        binding.btnSetName.setOnClickListener {
            if (validate()) {
                binding.tilForumName.isErrorEnabled = false
                nameListener.onSheetClose(binding.tilForumName.editText?.text.toString())
                dialog?.dismiss()
            } else {
                binding.tilForumName.error = "Name cannot be Empty"
            }
        }
    }

    private fun validate(): Boolean {
        return binding.tilForumName.editText?.text.toString().isNotEmpty()
    }

    override fun onResume() {
        super.onResume()
        val params: ViewGroup.LayoutParams = dialog?.window!!.attributes
        dialog?.window!!.setGravity(Gravity.CENTER)
        params.width = ViewGroup.LayoutParams.MATCH_PARENT
        params.height = ViewGroup.LayoutParams.MATCH_PARENT
        dialog?.window?.attributes = params as WindowManager.LayoutParams
    }

    interface OnSheetWork {
        fun onSheetClose(name: String)
    }
}
