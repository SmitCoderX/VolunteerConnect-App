package com.smitcoderx.volunteerconnect.Ui.Home

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout.OnRefreshListener
import com.bumptech.glide.Glide
import com.google.android.material.badge.BadgeDrawable
import com.google.android.material.badge.ExperimentalBadgeUtils
import com.smitcoderx.volunteerconnect.R
import com.smitcoderx.volunteerconnect.Utils.DataStoreUtil
import com.smitcoderx.volunteerconnect.Utils.LoadingInterface
import com.smitcoderx.volunteerconnect.Utils.ResponseState
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
class HomeFragment : Fragment(R.layout.fragment_home), OnRefreshListener {

    private lateinit var binding: FragmentHomeBinding
    private val homeViewModel by viewModels<HomeViewModel>()
    private val typeAdapter = TypesAdapter()
    private var listener: LoadingInterface? = null
    private lateinit var prefs: DataStoreUtil

    @SuppressLint("UseCompatLoadingForDrawables")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentHomeBinding.bind(view)

        prefs = DataStoreUtil(requireContext())
        homeViewModel.getLoggedInData(prefs.getToken().toString())

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
            val action = HomeFragmentDirections.actionHomeFragmentToProfileBottomSheetFragment()
            findNavController().navigate(action)
        }

    }


    private fun handleCurrentUser() {
        homeViewModel.userLiveData.observe(viewLifecycleOwner) {
            when (it) {
                is ResponseState.Success -> {
                    binding.shimmerEffect.stopShimmerAnimation()
                    binding.tvWelcome.text = generateGreeting(it.data?.data?.username.toString())
                    Glide.with(requireContext()).load(it.data?.data?.photos).into(binding.ivProfile)
                    binding.shimmerEffect.visibility = View.GONE
                }

                is ResponseState.Error -> {
                    Toast.makeText(requireContext(), it.message.toString(), Toast.LENGTH_SHORT)
                        .show()
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
                }

                is ResponseState.Error -> {
                    lifecycleScope.launch {
                        delay(1000)
                        binding.shimmerEffect.stopShimmerAnimation()
                        Toast.makeText(requireContext(), it.message.toString(), Toast.LENGTH_SHORT)
                            .show()
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
        listener?.showLoading()
        binding.shimmerEffect.visibility = View.VISIBLE
        binding.shimmerEffect.startShimmerAnimation()
        lifecycleScope.launch {
            delay(1500)
            homeViewModel.getCategoryList()
            listener?.hideLoading()
            binding.swipeLayout.isRefreshing = false
            binding.shimmerEffect.stopShimmerAnimation()
            binding.shimmerEffect.visibility = View.GONE
        }

    }

    override fun onResume() {
        super.onResume()
        homeViewModel.getCategoryList()
        binding.shimmerEffect.startShimmerAnimation()
    }

    override fun onPause() {
        super.onPause()
        binding.shimmerEffect.stopShimmerAnimation()
    }


}