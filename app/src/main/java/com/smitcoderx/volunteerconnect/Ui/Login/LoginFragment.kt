package com.smitcoderx.volunteerconnect.Ui.Login

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Patterns
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.smitcoderx.volunteerconnect.API.LoginData
import com.smitcoderx.volunteerconnect.R
import com.smitcoderx.volunteerconnect.Utils.ResponseState
import com.smitcoderx.volunteerconnect.Utils.drawableToBitmap
import com.smitcoderx.volunteerconnect.Utils.morphDoneAndRevert
import com.smitcoderx.volunteerconnect.databinding.FragmentLoginBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginFragment : Fragment(R.layout.fragment_login) {

    private lateinit var binding: FragmentLoginBinding
    private val loginViewModel by viewModels<LoginViewModel>()
    private lateinit var loginData: LoginData

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentLoginBinding.bind(view)

        loginStatus()


        binding.tvNoAcc.setOnClickListener {
            findNavController().popBackStack()
        }


        binding.btnLogin.setOnClickListener {

            if (binding.tilEmail.editText?.text.isNullOrEmpty()) {
                binding.tilEmail.error = "Field cannot be empty"
            } else if (binding.tilPass.editText?.text.isNullOrEmpty()) {
                binding.tilPass.error = "Field Cannot be Empty"
            } else {
                if (Patterns.EMAIL_ADDRESS.matcher(binding.tilEmail.editText?.text.toString())
                        .matches()
                ) {
                    loginData = LoginData(
                        "",
                        binding.tilEmail.editText?.text.toString(),
                        binding.tilPass.editText?.text.toString()
                    )

                } else {
                    loginData = LoginData(
                        binding.tilEmail.editText?.text.toString(),
                        "",
                        binding.tilPass.editText?.text.toString()
                    )
                }
                loginViewModel.loginUser(loginData)
            }
        }
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private fun loginStatus() {
        loginViewModel.signInDataLiveData.observe(requireActivity()) {
            when (it) {
                is ResponseState.Success -> {
                    binding.btnLogin.morphDoneAndRevert(
                        requireContext(), requireContext().getColor(R.color.accent_color),
                        drawableToBitmap(context?.getDrawable(R.drawable.ic_success)!!),
                        coroutineScope = lifecycleScope
                    ) {
                        Toast.makeText(
                            requireContext(),
                            "Logged in Successfully",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }

                is ResponseState.Error -> {
                    binding.btnLogin.morphDoneAndRevert(
                        requireContext(),
                        requireContext().getColor(R.color.accent_color),
                        drawableToBitmap(context?.getDrawable(R.drawable.ic_close)!!),
                        coroutineScope = lifecycleScope
                    ) {
                        Toast.makeText(requireContext(), it.message.toString(), Toast.LENGTH_SHORT)
                            .show()
                    }
                }

                is ResponseState.Loading -> {

                }
            }
        }

    }
}