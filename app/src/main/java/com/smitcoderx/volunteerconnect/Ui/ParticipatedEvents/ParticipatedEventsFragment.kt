package com.smitcoderx.volunteerconnect.Ui.ParticipatedEvents

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.smitcoderx.volunteerconnect.Model.Events.DataFetch
import com.smitcoderx.volunteerconnect.R
import com.smitcoderx.volunteerconnect.Ui.Categories.CategoryEventAdapter
import com.smitcoderx.volunteerconnect.Ui.Home.HomeViewModel
import com.smitcoderx.volunteerconnect.Utils.Constants
import com.smitcoderx.volunteerconnect.Utils.DataStoreUtil
import com.smitcoderx.volunteerconnect.Utils.LoadingInterface
import com.smitcoderx.volunteerconnect.Utils.ResponseState
import com.smitcoderx.volunteerconnect.Utils.hasInternetConnection
import com.smitcoderx.volunteerconnect.databinding.FragmentParticipatedEventsBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ParticipatedEventsFragment : Fragment(R.layout.fragment_participated_events),
    CategoryEventAdapter.OnCategory, SwipeRefreshLayout.OnRefreshListener {

    private lateinit var binding: FragmentParticipatedEventsBinding
    private val viewModel by viewModels<ParticipatedEventViewModel>()
    private val homeViewModel by viewModels<HomeViewModel>()
    private lateinit var prefs: DataStoreUtil
    private var listener: LoadingInterface? = null
    private lateinit var categoryEventAdapter: CategoryEventAdapter
    private lateinit var id: String

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentParticipatedEventsBinding.bind(view)

        prefs = DataStoreUtil(requireContext())
        viewModel.isNetworkConnectedLiveData.value = requireContext().hasInternetConnection()
        homeViewModel.isNetworkConnectedLiveData.value = requireContext().hasInternetConnection()
        homeViewModel.getCurrentLoggedinUser(prefs.getToken().toString())

        binding.tvCategoryType.text = if (prefs.getRole()
                .equals("organization", ignoreCase = true)
        ) "Created Events" else "Participated Events"

        binding.categorySwipeLayout.setOnRefreshListener(this)
        try {
            val f = binding.categorySwipeLayout.javaClass.getDeclaredField("mCircleView")
            f.isAccessible = true
            val imageView = f.get(binding.categorySwipeLayout) as ImageView
            imageView.setImageDrawable(null)
            imageView.background =
                ContextCompat.getDrawable(requireContext(), android.R.color.transparent)
        } catch (e: NoSuchFieldException) {
            e.printStackTrace()
        } catch (e: IllegalAccessException) {
            e.printStackTrace()
        }

        categoryEventAdapter = CategoryEventAdapter(this)

        handleCurrentUser()
        if (prefs.getRole().equals("organization", ignoreCase = true)) {
            handleEventsListNGO()
        } else {
            handleEventsListUser()
        }

        binding.rvEvents.apply {
            setHasFixedSize(true)
            adapter = categoryEventAdapter
        }

        binding.cardRetry.setOnClickListener {
            binding.llError.visibility = View.GONE
            showLoading()
            listener?.showLoading()
            binding.shimmerEffect.startShimmerAnimation()
            viewModel.getEventsByNgo(homeViewModel.userLiveData.value?.data?.data?.id.toString())
            viewModel.getEventsByUser(homeViewModel.userLiveData.value?.data?.data?.id.toString())
        }

    }

    private fun handleCurrentUser() {
        homeViewModel.userLiveData.observe(viewLifecycleOwner) {
            when (it) {
                is ResponseState.Success -> {
                    id = it.data?.data.toString()
                    viewModel.getEventsByNgo(it.data?.data?.id.toString())
                    viewModel.getEventsByUser(it.data?.data?.id.toString())

                }

                is ResponseState.Error -> {
                }

                is ResponseState.Loading -> {
                }
            }
        }
    }

    @SuppressLint("SetTextI18n")
    private fun handleEventsListNGO() {
        viewModel.eventNgoLiveData.observe(viewLifecycleOwner) {
            when (it) {
                is ResponseState.Success -> {
                    Log.d(Constants.TAG, "handleEventsNGODataSuccess: ${it.data}")
                    binding.shimmerEffect.stopShimmerAnimation()
                    listener?.hideLoading()
                    hideLoading()
                    if (it.data.isNullOrEmpty()) {
                        binding.llError.visibility = View.VISIBLE
                        binding.cardRetry.visibility = View.GONE
                        binding.lottieView.setAnimation(R.raw.no_data)
                        binding.lottieView.loop(true)
                        binding.lottieView.playAnimation()
                        binding.tvErrorMsg.text = "Oops! No Events Found To Show"

                    } else {
                        binding.llError.visibility = View.GONE
                        categoryEventAdapter.differ.submitList(it.data)
                    }

                }

                is ResponseState.Loading -> {
                    binding.shimmerEffect.startShimmerAnimation()
                    showLoading()
                }

                is ResponseState.Error -> {
                    lifecycleScope.launch {
                        delay(1000)
                        binding.shimmerEffect.stopShimmerAnimation()
                        showLoading()
                        listener?.hideLoading()
                        binding.shimmerEffect.visibility = View.GONE
                        binding.llError.visibility = View.VISIBLE
                        binding.tvErrorMsg.text = it.message.toString()

                    }
                }
            }
        }
    }

    @SuppressLint("SetTextI18n")
    private fun handleEventsListUser() {
        viewModel.eventUserLiveData.observe(viewLifecycleOwner) {
            when (it) {
                is ResponseState.Success -> {
                    Log.d(Constants.TAG, "handleEventsUserDataSuccess: ${it.data}")
                    binding.shimmerEffect.stopShimmerAnimation()
                    listener?.hideLoading()
                    hideLoading()
                    if (it.data.isNullOrEmpty()) {
                        binding.llError.visibility = View.VISIBLE
                        binding.cardRetry.visibility = View.GONE
                        binding.lottieView.setAnimation(R.raw.no_data)
                        binding.lottieView.loop(true)
                        binding.lottieView.playAnimation()
                        binding.tvErrorMsg.text = "Oops! No Events Found To Show"

                    } else {
                        binding.llError.visibility = View.GONE
                        categoryEventAdapter.differ.submitList(it.data)
                    }

                }

                is ResponseState.Loading -> {
                    binding.shimmerEffect.startShimmerAnimation()
                    showLoading()
                }

                is ResponseState.Error -> {
                    lifecycleScope.launch {
                        delay(1000)
                        binding.shimmerEffect.stopShimmerAnimation()
                        showLoading()
                        listener?.hideLoading()
                        binding.shimmerEffect.visibility = View.GONE
                        binding.llError.visibility = View.VISIBLE
                        binding.tvErrorMsg.text = it.message.toString()

                    }
                }
            }
        }
    }

    override fun onEventHandleClick(eventData: DataFetch) {
        findNavController().navigate(
            ParticipatedEventsFragmentDirections.actionActionCommunityToSingleEventFragment(
                eventData
            )
        )
    }

    private fun showLoading() {
        binding.apply {
            binding.shimmerEffect.visibility = View.VISIBLE
            binding.rvEvents.visibility = View.GONE
            binding.llError.visibility = View.GONE
            binding.tvCategoryType.visibility = View.GONE
        }
    }

    private fun hideLoading() {
        binding.apply {
            binding.shimmerEffect.visibility = View.GONE
            binding.llError.visibility = View.GONE
            binding.rvEvents.visibility = View.VISIBLE
            binding.tvCategoryType.visibility = View.VISIBLE

        }
    }

    override fun onRefresh() {
        binding.llError.visibility = View.GONE
        listener?.showLoading()
        showLoading()
        binding.shimmerEffect.startShimmerAnimation()
        lifecycleScope.launch {
            delay(1500)
            viewModel.getEventsByNgo(homeViewModel.userLiveData.value?.data?.data?.id.toString())
            viewModel.getEventsByUser(homeViewModel.userLiveData.value?.data?.data?.id.toString())
            listener?.hideLoading()
            hideLoading()
            binding.categorySwipeLayout.isRefreshing = false
            binding.shimmerEffect.stopShimmerAnimation()
            binding.llError.visibility = View.GONE
        }
    }

    override fun onResume() {
        super.onResume()
        if (prefs.getRole().equals("organization", ignoreCase = true)) {
            handleEventsListNGO()
        } else {
            handleEventsListUser()
        }
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

    override fun onEventFavClick(eventData: DataFetch) {

    }
}