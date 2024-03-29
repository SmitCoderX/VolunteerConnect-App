package com.smitcoderx.volunteerconnect.Ui.Home

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout.OnRefreshListener
import com.bumptech.glide.Glide
import com.google.android.material.badge.BadgeDrawable
import com.google.android.material.badge.ExperimentalBadgeUtils
import com.smitcoderx.volunteerconnect.Model.Category.CategoryData
import com.smitcoderx.volunteerconnect.Model.User.UserDataModel
import com.smitcoderx.volunteerconnect.R
import com.smitcoderx.volunteerconnect.Utils.DataStoreUtil
import com.smitcoderx.volunteerconnect.Utils.LoadingInterface
import com.smitcoderx.volunteerconnect.Utils.ResponseState
import com.smitcoderx.volunteerconnect.Utils.hasInternetConnection
import com.smitcoderx.volunteerconnect.databinding.FragmentHomeBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale


@ExperimentalBadgeUtils
@AndroidEntryPoint
class HomeFragment : Fragment(R.layout.fragment_home), OnRefreshListener, TypesAdapter.OnEvents {

    private lateinit var binding: FragmentHomeBinding
    private val homeViewModel by viewModels<HomeViewModel>()
    private val typeAdapter = TypesAdapter(this)
    private var listener: LoadingInterface? = null
    private lateinit var prefs: DataStoreUtil
    private var userData: UserDataModel? = null

    @SuppressLint("UseCompatLoadingForDrawables")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentHomeBinding.bind(view)

        prefs = DataStoreUtil(requireContext())
        homeViewModel.isNetworkConnectedLiveData.value = requireContext().hasInternetConnection()
        homeViewModel.getCurrentLoggedinUser(prefs.getToken().toString())
        homeViewModel.getCategoryList()

        binding.ivNotification.clipToOutline = false
        val badgeDrawable = BadgeDrawable.create(requireContext())
        badgeDrawable.isVisible = true
        badgeDrawable.badgeGravity = BadgeDrawable.TOP_END

        binding.swipeLayout.setOnRefreshListener(this)
        try {
            val f = binding.swipeLayout.javaClass.getDeclaredField("mCircleView")
            f.isAccessible = true
            val imageView = f.get(binding.swipeLayout) as ImageView
            imageView.setImageDrawable(null)
            imageView.background = requireContext().getDrawable(android.R.color.transparent)
        } catch (e: NoSuchFieldException) {
            e.printStackTrace()
        } catch (e: IllegalAccessException) {
            e.printStackTrace()
        }

        handleCurrentUser()
        handleCategoryData()

        binding.rvTypes.apply {
            setHasFixedSize(false)
            adapter = typeAdapter
        }

        binding.maps.setOnClickListener {
            val action = HomeFragmentDirections.actionHomeFragmentToMapsFragment()
            findNavController().navigate(action)
        }

        binding.ivProfile.setOnClickListener {
            val action =
                HomeFragmentDirections.actionHomeFragmentToProfileBottomSheetFragment(userData?.data!!)
            findNavController().navigate(action)
        }

        binding.cardRetry.setOnClickListener {
            binding.llError.visibility = View.GONE
            binding.shimmerEffect.visibility = View.VISIBLE
            binding.shimmerEffect.startShimmerAnimation()

            homeViewModel.getCurrentLoggedinUser(prefs.getToken().toString())
            homeViewModel.getCategoryList()
//            handleCurrentUser()
//            handleCategoryData()
        }

    }


    private fun handleCurrentUser() {
        homeViewModel.userLiveData.observe(viewLifecycleOwner) {
            when (it) {
                is ResponseState.Success -> {
                    userData = it.data
                    binding.shimmerEffect.stopShimmerAnimation()
                    binding.tvWelcome.text = generateGreeting(it.data?.data?.username.toString())
                    Glide.with(requireContext()).load(it.data?.data?.photos).into(binding.ivProfile)
                    binding.shimmerEffect.visibility = View.GONE
                    hideError()
                }

                is ResponseState.Error -> {
                    lifecycleScope.launch {
                        delay(1000)
                        binding.shimmerEffect.stopShimmerAnimation()
                        binding.shimmerEffect.visibility = View.GONE
//                        Toast.makeText(requireContext(), it.message.toString(), Toast.LENGTH_SHORT)
//                            .show()
                        binding.tvErrorMsg.text = it.message.toString()
                        showError()
                    }
                }

                is ResponseState.Loading -> {

                }
            }
        }
    }

    private fun handleCategoryData() {
        homeViewModel.categoryDataLiveData.observe(viewLifecycleOwner) {
            when (it) {
                is ResponseState.Success -> {
                    binding.shimmerEffect.stopShimmerAnimation()
                    typeAdapter.differ.submitList(it.data?.data)
                    binding.shimmerEffect.visibility = View.GONE
                    hideError()
                }

                is ResponseState.Error -> {
                    lifecycleScope.launch {
                        delay(1000)
                        binding.shimmerEffect.stopShimmerAnimation()
                        binding.shimmerEffect.visibility = View.GONE
//                        Toast.makeText(requireContext(), it.message.toString(), Toast.LENGTH_SHORT)
//                            .show()
                        binding.tvErrorMsg.text = it.message.toString()
                        showError()
                    }

                }

                is ResponseState.Loading -> {

                }
            }
        }
    }


    private fun generateGreeting(username: String): String {
        val c = Calendar.getInstance()
        val timeOfDay: Int = c.get(Calendar.HOUR_OF_DAY)
        binding.tvDate.text = dateFormatter(c.time)
        var greeting = ""

        when (timeOfDay) {
            in 0..11 -> {
                greeting = "Good Morning"
            }

            in 12..15 -> {
                greeting = "Good Afternoon"
            }

            in 16..20 -> {
                greeting = "Good Evening"
            }

            in 21..23 -> {
                greeting = "Good Night"
            }
        }

        return "$greeting, $username"
    }

    @SuppressLint("SimpleDateFormat")
    private fun dateFormatter(date: Date?): String {
        val sdf = SimpleDateFormat("dd MMM yyyy", Locale.ENGLISH)
        var s = ""
        try {
            val dt = date?.let { sdf.format(it) }
            s = dt.toString()
        } catch (e: ParseException) {
            e.printStackTrace()
        }
        return s
    }


    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is LoadingInterface) {
            listener = context
        } else {
            throw ClassCastException("$context must implement LoadingInterface")
        }
    }

    override fun onRefresh() {
        binding.llError.visibility = View.GONE
        binding.homeView.visibility = View.GONE
        listener?.showLoading()
        binding.shimmerEffect.visibility = View.VISIBLE
        binding.shimmerEffect.startShimmerAnimation()
        lifecycleScope.launch {
            delay(1500)
            homeViewModel.getCurrentLoggedinUser(prefs.getToken().toString())
            homeViewModel.getCategoryList()
            listener?.hideLoading()
            binding.swipeLayout.isRefreshing = false
            binding.shimmerEffect.stopShimmerAnimation()
            binding.shimmerEffect.visibility = View.GONE
            binding.homeView.visibility = View.VISIBLE
        }

    }

    override fun onResume() {
        super.onResume()
//        homeViewModel.getCurrentLoggedinUser(prefs.getToken().toString())
//        homeViewModel.getCategoryList()
        binding.shimmerEffect.startShimmerAnimation()
    }

    override fun onPause() {
        super.onPause()
        binding.shimmerEffect.stopShimmerAnimation()
    }

    private fun showError() {
        binding.apply {
            tvWelcome.visibility = View.GONE
            tvDate.visibility = View.GONE
            ivNotification.visibility = View.GONE
            ivProfile.visibility = View.GONE
            tvTitle.visibility = View.GONE
            rvTypes.visibility = View.GONE
            tvMaps.visibility = View.GONE
            cardMap.visibility = View.GONE
            tvOrgs.visibility = View.GONE
            rvOrgs.visibility = View.GONE
            tvReviews.visibility = View.GONE
            rvReview.visibility = View.GONE
            llError.visibility = View.VISIBLE
        }
    }

    private fun hideError() {
        binding.apply {
            tvWelcome.visibility = View.VISIBLE
            tvDate.visibility = View.VISIBLE
            ivNotification.visibility = View.VISIBLE
            ivProfile.visibility = View.VISIBLE
            tvTitle.visibility = View.VISIBLE
            rvTypes.visibility = View.VISIBLE
            tvMaps.visibility = View.VISIBLE
            cardMap.visibility = View.VISIBLE
            tvOrgs.visibility = View.VISIBLE
            rvOrgs.visibility = View.VISIBLE
            tvReviews.visibility = View.VISIBLE
            rvReview.visibility = View.VISIBLE
            llError.visibility = View.GONE
        }
    }

    override fun onEventClick(category: CategoryData) {
        val action = HomeFragmentDirections.actionHomeFragmentToCategoryFragment(category)
        findNavController().navigate(action)
    }


}