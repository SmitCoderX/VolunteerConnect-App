package com.smitcoderx.volunteerconnect.Ui.HomeOrg

import android.animation.ValueAnimator
import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout.OnRefreshListener
import com.bumptech.glide.Glide
import com.google.android.material.badge.BadgeDrawable
import com.smitcoderx.volunteerconnect.Model.User.UserDataModel
import com.smitcoderx.volunteerconnect.R
import com.smitcoderx.volunteerconnect.Ui.Home.HomeViewModel
import com.smitcoderx.volunteerconnect.Utils.DataStoreUtil
import com.smitcoderx.volunteerconnect.Utils.LoadingInterface
import com.smitcoderx.volunteerconnect.Utils.ResponseState
import com.smitcoderx.volunteerconnect.databinding.FragmentHomeOrgBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

@AndroidEntryPoint
class HomeOrgFragment : Fragment(R.layout.fragment_home_org), OnRefreshListener, TriggerCount {

    private lateinit var binding: FragmentHomeOrgBinding
    private val homeViewModel by viewModels<HomeViewModel>()
    private var listener: LoadingInterface? = null
    private lateinit var prefs: DataStoreUtil
    private var userData: UserDataModel? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentHomeOrgBinding.bind(view)

        prefs = DataStoreUtil(requireContext())
        homeViewModel.getCurrentLoggedinUser(prefs.getToken().toString())

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

        val dashAdapter = HomeDashboardAdapter(this)

        val data = listOf(
            HomeOrgModel(1, "Number of Events Live", "5", R.drawable.give),
            HomeOrgModel(2, "Followers", "105", R.drawable.tealeaf),
            HomeOrgModel(3, "No of Events(last Month)", "10", R.drawable.hand),
            HomeOrgModel(4, "Completed Events", "50", R.drawable.milestone),
            HomeOrgModel(5, "From Date", "10 Jan 2024", R.drawable.hourglass),
            HomeOrgModel(6, "Total Volunteer Requests", "50", R.drawable.reachout),
            HomeOrgModel(7, "Number of Volunteers", "500", R.drawable.collaboration),
            HomeOrgModel(8, "Posts", "25", R.drawable.post),
            HomeOrgModel(9, "Forums", "5", R.drawable.workshop),
            HomeOrgModel(10, "Certifications", "10", R.drawable.certificate),
        )

        dashAdapter.differ.submitList(data)

        binding.rvDash.apply {
            isNestedScrollingEnabled = false
            adapter = dashAdapter
            layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
        }

        binding.ivProfile.setOnClickListener {
            val action =
                HomeOrgFragmentDirections.actionHomeOrgFragmentToProfileBottomSheetFragment(userData?.data!!)
            findNavController().navigate(action)
        }

        binding.cardRetry.setOnClickListener {
            binding.llError.visibility = View.GONE
            binding.shimmerEffect.visibility = View.VISIBLE
            binding.shimmerEffect.startShimmerAnimation()

            homeViewModel.getCurrentLoggedinUser(prefs.getToken().toString())
            homeViewModel.getCategoryList()
        }

        binding.fabAdd.setOnClickListener {
            val action =
                HomeOrgFragmentDirections.actionHomeOrgFragmentToAddBottomSheetFragment(userData?.data!!)
            findNavController().navigate(action)
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
        listener?.showLoading()
        binding.shimmerEffect.visibility = View.VISIBLE
        binding.shimmerEffect.startShimmerAnimation()
        lifecycleScope.launch {
            delay(1500)
            homeViewModel.getCurrentLoggedinUser(prefs.getToken().toString())
            listener?.hideLoading()
            binding.swipeLayout.isRefreshing = false
            binding.shimmerEffect.stopShimmerAnimation()
            binding.shimmerEffect.visibility = View.GONE
        }

    }

    override fun onResume() {
        super.onResume()
        handleCurrentUser()
        binding.shimmerEffect.startShimmerAnimation()
    }

    override fun onPause() {
        super.onPause()
        binding.shimmerEffect.visibility = View.VISIBLE
        binding.shimmerEffect.stopShimmerAnimation()
    }

    private fun showError() {
        binding.apply {
            tvWelcome.visibility = View.GONE
            tvDate.visibility = View.GONE
            ivNotification.visibility = View.GONE
            ivProfile.visibility = View.GONE
            rvDash.visibility = View.GONE
            llError.visibility = View.VISIBLE
        }
    }

    private fun hideError() {
        binding.apply {
            tvWelcome.visibility = View.VISIBLE
            tvDate.visibility = View.VISIBLE
            ivNotification.visibility = View.VISIBLE
            ivProfile.visibility = View.VISIBLE
            rvDash.visibility = View.VISIBLE
            llError.visibility = View.GONE
        }
    }

    override fun startCount(valueAnimator: ValueAnimator?) {
        lifecycleScope.launch {
            delay(500)
            valueAnimator?.start()
        }
    }

    override fun OnItemClick(item: HomeOrgModel) {
        when (item.id) {
            1 -> {

            }

            2 -> {

            }

            3 -> {

            }

            4 -> {

            }

            5 -> {

            }

            6 -> {

            }

            7 -> {

            }

            8 -> {

            }

            9 -> {

            }

            10 -> {

            }
        }
    }
}