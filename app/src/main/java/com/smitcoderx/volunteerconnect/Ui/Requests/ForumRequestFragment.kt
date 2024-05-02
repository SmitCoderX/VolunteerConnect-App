package com.smitcoderx.volunteerconnect.Ui.Requests

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
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
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.util.Timer
import java.util.TimerTask

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
        handleRecipientRequestData()
        handleRequesterRequestData()
        handleRequestStatus()

    }

    private fun handleGetMe() {
        homeViewModel.userLiveData.observe(viewLifecycleOwner) {
            when (it) {
                is ResponseState.Success -> {
                    hideLoading()
                    Log.d(TAG, "handleGetMeRequest: ${it.data?.data}")
                    val timer = Timer()
                    timer.scheduleAtFixedRate(object : TimerTask() {
                        override fun run() {
                            if (prefs.getRole().equals("organization", ignoreCase = true)) {
                                requestViewModel.getRecipientData(
                                    token = prefs.getToken().toString(),
                                    id = it.data?.data?.id.toString()
                                )
                            } else {
                                requestViewModel.getRequesterData(
                                    token = prefs.getToken().toString(),
                                    id = it.data?.data?.id.toString()
                                )
                            }
                        }
                    }, 0, 20000)
                    Log.d(TAG, "handleGetMe: ${it.data?.data?.id}")
                }

                is ResponseState.Loading -> {
                    showLoading()
//                    binding.llError.visibility = View.GONE
                }

                is ResponseState.Error -> {
                    Log.d(TAG, "handleGetMeRequestError: ${it.message}")
                    hideLoading()
                    binding.llError.visibility = View.VISIBLE
                    binding.tvErrorMsg.text = it.message.toString()
                }
            }
        }
    }

    private fun handleRecipientRequestData() {
        GlobalScope.launch(Dispatchers.Main) {
            requestViewModel.recipientLiveData.observe(viewLifecycleOwner) {
                when (it) {
                    is ResponseState.Success -> {
                        requestAdapter.differ.submitList(it.data?.filter { item -> item?.status == 1 })

                    }

                    is ResponseState.Loading -> {

                    }

                    is ResponseState.Error -> {
                        Log.d(TAG, "handleRequestDataError: ${it.message}")
                    }
                }
            }
        }
    }

    private fun handleRequesterRequestData() {
        GlobalScope.launch(Dispatchers.Main) {
            requestViewModel.requesterLiveData.observe(viewLifecycleOwner) {
                when (it) {
                    is ResponseState.Success -> {
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
    }

    private fun handleRequestStatus() {
        requestViewModel.handleRequest.observe(viewLifecycleOwner) {
            when (it) {
                is ResponseState.Success -> {
                    Log.d(TAG, "handleRequestStatus: ${it.data}")
                    Toast.makeText(requireContext(), it.data?.message, Toast.LENGTH_SHORT).show()
                }

                is ResponseState.Loading -> {
//                    Toast.makeText(requireContext(), "Please Wait...", Toast.LENGTH_SHORT).show()
                }

                is ResponseState.Error -> {
                    Log.d(TAG, "handleRequestStatus: ${it.message}")
                    Toast.makeText(requireContext(), it.message, Toast.LENGTH_SHORT).show()
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
            prefs.getToken().toString(), requestsData.id.toString(), data
        )
    }

    override fun onRequestDecline(requestsData: RequestsData) {
        Log.d(TAG, "onRequestDecline: True")
        val data = RequestsData(
            status = 3
        )
        requestViewModel.handleRequest(
            prefs.getToken().toString(), requestsData.id.toString(), data
        )
    }

    private fun showLoading() {
        binding.apply {
            progressLoading.visibility = View.VISIBLE
            rvRequest.visibility = View.GONE
//            llApply.visibility = View.GONE
        }
    }

    private fun hideLoading() {
        binding.apply {
            progressLoading.visibility = View.GONE
            rvRequest.visibility = View.VISIBLE
//            llApply.visibility = View.VISIBLE
        }
    }

}