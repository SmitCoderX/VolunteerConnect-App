package com.smitcoderx.volunteerconnect.Ui.ForgotPassword

import android.content.Context
import android.os.Bundle
import android.util.Patterns
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.smitcoderx.volunteerconnect.Model.Auth.LoginData
import com.smitcoderx.volunteerconnect.R
import com.smitcoderx.volunteerconnect.Utils.LoadingInterface
import com.smitcoderx.volunteerconnect.Utils.ResponseState
import com.smitcoderx.volunteerconnect.databinding.FragmentForgotPasswordBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.lang.ClassCastException

@AndroidEntryPoint
class ForgotPasswordFragment : Fragment(R.layout.fragment_forgot_password) {

    private lateinit var binding: FragmentForgotPasswordBinding
    private val fpViewModel by viewModels<ForgotPasswordViewModel>()
    private var listener: LoadingInterface? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentForgotPasswordBinding.bind(view)

        forgotPasswordStatus()

        binding.btnLogin.setOnClickListener {
            if (validateEmail()) {
                val loginData = LoginData(
                    email = binding.tilEmail.editText?.text.toString().trim()
                )
                fpViewModel.forgetPassword(loginData)
                showLoading()
            }
        }

        binding.ivBack.setOnClickListener {
            findNavController().popBackStack()
        }
    }

    private fun validateEmail(): Boolean {
        val isValid: Boolean
        if (binding.tilEmail.editText?.text.isNullOrEmpty()) {
            binding.tilEmail.error = "Field cannot be Empty"
            isValid = false
        } else {
            if (!Patterns.EMAIL_ADDRESS.matcher(binding.tilEmail.editText?.text.toString())
                    .matches()
            ) {
                binding.tilEmail.error = "Enter a Valid Email"
                isValid = false
            } else {
                binding.tilEmail.isErrorEnabled = false
                isValid = true
            }
        }
        return isValid
    }

    private fun forgotPasswordStatus() {
        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                fpViewModel.fpDataLiveData.observe(requireActivity()) {
                    when (it) {
                        is ResponseState.Success -> {
                            hideLoading()
                            Toast.makeText(
                                requireContext(),
                                it.data?.message.toString(),
                                Toast.LENGTH_SHORT
                            ).show()
                        }

                        is ResponseState.Error -> {
                            hideLoading()
                            Toast.makeText(
                                requireContext(),
                                it.message.toString(),
                                Toast.LENGTH_SHORT
                            ).show()
                        }

                        is ResponseState.Loading -> {

                        }
                    }
                }
            }
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if(context is LoadingInterface) {
            listener = context
        } else {
            throw ClassCastException("$context must implement LoadingInterface")
        }
    }


    private fun showLoading() {
        binding.apply {
            listener?.showLoading()
            etEmailLogin.isEnabled = false
        }
    }

    private fun hideLoading() {
        binding.apply {
            listener?.hideLoading()
            etEmailLogin.isEnabled = true
        }
    }

}