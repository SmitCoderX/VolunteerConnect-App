package com.smitcoderx.volunteerconnect.Ui.Login

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.smitcoderx.volunteerconnect.API.LoginData
import com.smitcoderx.volunteerconnect.R
import com.smitcoderx.volunteerconnect.Utils.Constants.TAG
import com.smitcoderx.volunteerconnect.Utils.ResponseState
import com.smitcoderx.volunteerconnect.databinding.FragmentLoginBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginFragment: Fragment(R.layout.fragment_login) {

    private lateinit var binding: FragmentLoginBinding
    private val loginViewModel by viewModels<LoginViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentLoginBinding.bind(view)

        loginStatus()

        binding.btnLogin.setOnClickListener {
             val loginData = LoginData("", binding.tilEmail.editText?.text.toString(), binding.tilPass.editText?.text.toString())
            loginViewModel.loginUser(loginData)
        }
    }

    private fun loginStatus() {
        loginViewModel.signInDataLiveData.observe(requireActivity()) {
            when(it) {
                is ResponseState.Success -> {
                    Toast.makeText(requireContext(), it.data?.token.toString(), Toast.LENGTH_SHORT).show()
                    Log.d(TAG, "loginStatusSuccess: ${it.data?.token.toString()}")
                }

                is ResponseState.Error -> {
                    Log.d(TAG, "loginStatusError: ${it.message}")
                }

                is ResponseState.Loading -> {
                    Log.d(TAG, "loginStatusLoading: Loading.....")
                }
            }
        }
    }
}