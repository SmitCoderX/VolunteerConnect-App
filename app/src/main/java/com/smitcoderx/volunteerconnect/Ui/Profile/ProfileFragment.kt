package com.smitcoderx.volunteerconnect.Ui.Profile

import android.content.Context
import android.os.Bundle
import android.util.Patterns
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.smitcoderx.volunteerconnect.Model.User.UpateData
import com.smitcoderx.volunteerconnect.R
import com.smitcoderx.volunteerconnect.Utils.Constants.TAG
import com.smitcoderx.volunteerconnect.Utils.DataStoreUtil
import com.smitcoderx.volunteerconnect.Utils.LoadingInterface
import com.smitcoderx.volunteerconnect.Utils.ResponseState
import com.smitcoderx.volunteerconnect.databinding.FragmentProfileBinding
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber


@AndroidEntryPoint
class ProfileFragment : Fragment(R.layout.fragment_profile) {

    private lateinit var binding: FragmentProfileBinding
    private lateinit var prefs: DataStoreUtil
    private val profileViewModel by viewModels<ProfileViewModel>()
    private var listener: LoadingInterface? = null


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentProfileBinding.bind(view)

        prefs = DataStoreUtil(requireContext())

        setDetails()
        updateStatus()

        binding.tilEmail.setEndIconOnClickListener {
            binding.tilEmail.isEnabled = true
        }

        binding.tilName.setEndIconOnClickListener {
            binding.tilName.isEnabled = true
        }

        binding.tilMobile.setEndIconOnClickListener {
            binding.tilMobile.isEnabled = true
        }

        binding.ivBack.setOnClickListener {
            findNavController().popBackStack()
        }

        binding.btnRegister.setOnClickListener {
            if (validateName() && validateEmail() && validateMobile()) {
                val updateData = UpateData(
                    binding.tilName.editText?.text.toString(),
                    binding.tilEmail.editText?.text.toString(),
                    binding.tilMobile.editText?.text.toString(),
                )

                profileViewModel.updateProfileData(prefs.getToken().toString(), updateData)
                showLoading()
            }
        }


    }

    private fun setDetails() {

        profileViewModel.getProfileDataLiveData.observe(viewLifecycleOwner) {
            when (it) {
                is ResponseState.Success -> {
                    Timber.tag(TAG).i("data: ${it.data}")
                    binding.shimmerEffect.stopShimmerAnimation()
                    binding.innerCon.visibility = View.VISIBLE
                    binding.apply {
                        Glide.with(requireContext()).load(it.data?.data?.photos).into(ivProfile)
                        tilUsername.editText?.setText(it.data?.data?.username)
                        tilName.editText?.setText(it.data?.data?.name)
                        tilEmail.editText?.setText(it.data?.data?.email)
                        tilMobile.editText?.setText(it.data?.data?.phoneNumber)
                    }
                    binding.shimmerEffect.visibility = View.GONE

                }

                is ResponseState.Error -> {
                    binding.shimmerEffect.stopShimmerAnimation()
                    binding.shimmerEffect.visibility = View.GONE
                    Toast.makeText(requireContext(), it.message.toString(), Toast.LENGTH_SHORT)
                        .show()
                    Timber.tag(TAG).i("dataError: ${it.message}")

                }

                is ResponseState.Loading -> {

                }
            }
        }
    }

    private fun updateStatus() {
        profileViewModel.updateProfileData.observe(viewLifecycleOwner) {
            when (it) {
                is ResponseState.Success -> {
                    hideLoading()
                    binding.apply {
                        etNameLogin.isEnabled = false
                        etEmailLogin.isEnabled = false
                        etMobileNo.isEnabled = false
                    }
                    Toast.makeText(requireContext(), "Updated", Toast.LENGTH_SHORT).show()

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

    override fun onResume() {
        super.onResume()
        profileViewModel.getProfileData(prefs.getToken().toString())
        binding.shimmerEffect.startShimmerAnimation()
    }

    override fun onPause() {
        super.onPause()
        binding.shimmerEffect.stopShimmerAnimation()
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

    private fun validateName(): Boolean {
        return if (binding.tilName.editText?.text.isNullOrEmpty()) {
            binding.tilName.error = "Fields cannot be Empty"
            false
        } else {
            binding.tilName.isErrorEnabled = false
            true
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

    private fun validateMobile(): Boolean {
        val isValid: Boolean
        if (binding.tilMobile.editText?.text.isNullOrEmpty()) {
            binding.tilMobile.error = "Field cannot be Empty"
            isValid = false
        } else {
            if (!Patterns.PHONE.matcher(binding.tilMobile.editText?.text.toString())
                    .matches() || binding.tilMobile.editText?.text?.length != 10
            ) {
                binding.tilMobile.error = "Enter a Valid Mobile Number"
                isValid = false
            } else {
                binding.tilMobile.isErrorEnabled = false
                isValid = true
            }
        }
        return isValid
    }
}