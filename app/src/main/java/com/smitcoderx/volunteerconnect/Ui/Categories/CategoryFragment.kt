package com.smitcoderx.volunteerconnect.Ui.Categories

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.smitcoderx.volunteerconnect.R
import com.smitcoderx.volunteerconnect.Utils.Constants.TAG
import com.smitcoderx.volunteerconnect.Utils.ResponseState
import com.smitcoderx.volunteerconnect.Utils.hasInternetConnection
import com.smitcoderx.volunteerconnect.databinding.FragmentCategoryBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CategoryFragment : Fragment(R.layout.fragment_category) {

    private lateinit var binding: FragmentCategoryBinding
    private val categoryViewModel by viewModels<CategoryViewModel>()
    private val args by navArgs<CategoryFragmentArgs>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentCategoryBinding.bind(view)

        categoryViewModel.isNetworkConnectedLiveData.value =
            requireContext().hasInternetConnection()
        val category = args.categoryData.category?.split("\n")?.get(1)
        categoryViewModel.getEventListCategoryWise(category.toString())

        handleCategoryData()

    }

    private fun handleCategoryData() {
        categoryViewModel.eventCategoryWiseLiveData.observe(viewLifecycleOwner) {
            when (it) {
                is ResponseState.Success -> {
                    Log.d(TAG, "handleCategoryDataSuccess: ${it.data}")
                    it.data?.forEach {stream ->
                        binding.tvCheck.append("${stream?.name}\n")
                    }
                }

                is ResponseState.Loading -> {
                    Log.d(TAG, "handleCategoryDataLoading:")
                }

                is ResponseState.Error -> {
                    Log.d(TAG, "handleCategoryDataSuccess: ${it.message}")
                }
            }
        }
    }

}