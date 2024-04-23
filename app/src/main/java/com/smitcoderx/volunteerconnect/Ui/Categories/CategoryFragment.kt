package com.smitcoderx.volunteerconnect.Ui.Categories

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
import androidx.navigation.fragment.navArgs
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout.OnRefreshListener
import com.smitcoderx.volunteerconnect.Model.Events.Data
import com.smitcoderx.volunteerconnect.R
import com.smitcoderx.volunteerconnect.Utils.Constants.TAG
import com.smitcoderx.volunteerconnect.Utils.LoadingInterface
import com.smitcoderx.volunteerconnect.Utils.ResponseState
import com.smitcoderx.volunteerconnect.Utils.hasInternetConnection
import com.smitcoderx.volunteerconnect.databinding.FragmentCategoryBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@AndroidEntryPoint
class CategoryFragment : Fragment(R.layout.fragment_category), CategoryEventAdapter.OnCategory,
    OnRefreshListener {

    private lateinit var binding: FragmentCategoryBinding
    private val categoryViewModel by viewModels<CategoryViewModel>()
    private val args by navArgs<CategoryFragmentArgs>()
    private var listener: LoadingInterface? = null
    private lateinit var categoryEventAdapter: CategoryEventAdapter
    private lateinit var category: String

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentCategoryBinding.bind(view)

        categoryViewModel.isNetworkConnectedLiveData.value =
            requireContext().hasInternetConnection()
        category = args.categoryData.category?.split("\n")?.get(1).toString()
        val heading = args.categoryData.category?.split("\n")?.get(0).toString()
        binding.tvCategoryType.text = "$heading$category"
        categoryViewModel.getEventListCategoryWise(category)

        handleCategoryData()

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

        binding.ivBack.setOnClickListener {
            findNavController().popBackStack()
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
            categoryViewModel.getEventListCategoryWise(category.toString())
        }

    }

    @SuppressLint("SetTextI18n")
    private fun handleCategoryData() {
        categoryViewModel.eventCategoryWiseLiveData.observe(viewLifecycleOwner) {
            when (it) {
                is ResponseState.Success -> {
                    Log.d(TAG, "handleCategoryDataSuccess: ${it.data}")
                    binding.shimmerEffect.stopShimmerAnimation()
                    listener?.hideLoading()
                    hideLoading()
                    if (it.data.isNullOrEmpty()) {
                        binding.llError.visibility = View.VISIBLE
                        binding.cardRetry.visibility = View.GONE
                        binding.lottieView.setAnimation(R.raw.no_data)
                        binding.lottieView.loop(true)
                        binding.lottieView.playAnimation()
                        binding.tvErrorMsg.text = "Oops! No Events Found To Participate"

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

    override fun onEventHandleClick(eventData: Data) {
        Log.d(TAG, "onEventHandleClick: ")
    }

    override fun onEventFavClick(eventData: Data) {
        Log.d(TAG, "onEventFavClick: ")
    }

    private fun showLoading() {
        binding.apply {
            binding.shimmerEffect.visibility = View.VISIBLE
            binding.rvEvents.visibility = View.GONE
            binding.llError.visibility = View.GONE
            binding.ivBack.visibility = View.GONE
            binding.tvCategoryType.visibility = View.GONE
        }
    }

    private fun hideLoading() {
        binding.apply {
            binding.shimmerEffect.visibility = View.GONE
            binding.llError.visibility = View.GONE
            binding.rvEvents.visibility = View.VISIBLE
            binding.ivBack.visibility = View.VISIBLE
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
            categoryViewModel.getEventListCategoryWise(category)
            listener?.hideLoading()
            hideLoading()
            binding.categorySwipeLayout.isRefreshing = false
            binding.shimmerEffect.stopShimmerAnimation()
        }
    }

    override fun onResume() {
        super.onResume()
        handleCategoryData()
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
}