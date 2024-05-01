package com.smitcoderx.volunteerconnect.Ui.Requests

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.smitcoderx.volunteerconnect.Model.Requests.RequestsData
import com.smitcoderx.volunteerconnect.R
import com.smitcoderx.volunteerconnect.Ui.Home.HomeViewModel
import com.smitcoderx.volunteerconnect.Utils.Constants.TAG
import com.smitcoderx.volunteerconnect.Utils.DataStoreUtil
import com.smitcoderx.volunteerconnect.Utils.ResponseState
import com.smitcoderx.volunteerconnect.Utils.hasInternetConnection
import com.smitcoderx.volunteerconnect.databinding.FragmentForumRequestBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ForumRequestFragment : Fragment(R.layout.fragment_forum_request),
    RequestAdapter.OnRequestHandle {

    private lateinit var binding: FragmentForumRequestBinding
    private lateinit var prefs: DataStoreUtil
    private val requestViewModel by viewModels<RequestViewModel>()
    private val homeViewModel by viewModels<HomeViewModel>()
    private lateinit var requestAdapter: RequestAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentForumRequestBinding.bind(view)

        prefs = DataStoreUtil(requireContext())
        requestAdapter = RequestAdapter(this, prefs.getRole().toString())
        homeViewModel.isNetworkConnectedLiveData.value = requireContext().hasInternetConnection()
        homeViewModel.getCurrentLoggedinUser(prefs.getToken().toString())

        binding.rvRequest.apply {
            setHasFixedSize(true)
            adapter = requestAdapter
        }

        handleGetMe()
        if (prefs.getRole().equals("organization", ignoreCase = true)) {
            handleRecipientRequestData()
        } else {
            handleRequesterRequestData()
        }

        handleRequestStatus()

    }

    private fun handleGetMe() {
        homeViewModel.userLiveData.observe(viewLifecycleOwner) {
            when (it) {
                is ResponseState.Success -> {
                    Log.d(TAG, "handleGetMeRequest: ${it.data?.data}")
                    requestViewModel.getRecipientData(
                        token = prefs.getToken().toString(),
                        id = it.data?.data?.id.toString()
                    )

                    Log.d(TAG, "handleGetMe: ${it.data?.data?.id}")

                    requestViewModel.getRequesterData(
                        token = prefs.getToken().toString(),
                        id = it.data?.data?.id.toString()
                    )
                }

                is ResponseState.Loading -> {

                }

                is ResponseState.Error -> {
                    Log.d(TAG, "handleGetMeRequestError: ${it.message}")
                }
            }
        }
    }

    private fun handleRecipientRequestData() {
        requestViewModel.recipientLiveData.observe(viewLifecycleOwner) {
            when (it) {
                is ResponseState.Success -> {
                    Log.d(TAG, "handleRequestData: ${it.data.toString()}")
                    requestAdapter.differ.submitList(it.data?.filter { item -> item?.status == 0 })
                }

                is ResponseState.Loading -> {

                }

                is ResponseState.Error -> {
                    Log.d(TAG, "handleRequestDataError: ${it.message}")

                }
            }
        }
    }

    private fun handleRequesterRequestData() {
        requestViewModel.requesterLiveData.observe(viewLifecycleOwner) {
            when (it) {
                is ResponseState.Success -> {
                    Log.d(TAG, "handleRequestData: ${it.data.toString()}")
                    requestAdapter.differ.submitList(it.data)
                }

                is ResponseState.Loading -> {

                }

                is ResponseState.Error -> {
                    Log.d(TAG, "handleRequestDataError: ${it.message}")

                }
            }
        }
    }

    private fun handleRequestStatus() {
        requestViewModel.handleRequest.observe(viewLifecycleOwner) {
            when (it) {
                is ResponseState.Success -> {
                    Log.d(TAG, "handleRequestStatus: ${it.data}")
                }

                is ResponseState.Loading -> {

                }

                is ResponseState.Error -> {
                    Log.d(TAG, "handleRequestStatus: ${it.message}")
                }
            }
        }
    }

    override fun onRequestAccept(requestsData: RequestsData) {
        Log.d(TAG, "onRequestAccept: True")
        val data = RequestsData(
            status = 2
        )
        requestViewModel.handleRequest(
            prefs.getToken().toString(),
            requestsData.id.toString(),
            data
        )
    }

    override fun onRequestDecline(requestsData: RequestsData) {
        Log.d(TAG, "onRequestDecline: True")
        val data = RequestsData(
            status = 3
        )
        requestViewModel.handleRequest(
            prefs.getToken().toString(),
            requestsData.id.toString(),
            data
        )
    }

}