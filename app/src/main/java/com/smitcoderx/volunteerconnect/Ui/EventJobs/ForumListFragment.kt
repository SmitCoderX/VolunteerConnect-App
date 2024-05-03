package com.smitcoderx.volunteerconnect.Ui.EventJobs

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
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.smitcoderx.volunteerconnect.Model.Forum.ForumData
import com.smitcoderx.volunteerconnect.R
import com.smitcoderx.volunteerconnect.Ui.Home.HomeViewModel
import com.smitcoderx.volunteerconnect.Utils.Constants
import com.smitcoderx.volunteerconnect.Utils.DataStoreUtil
import com.smitcoderx.volunteerconnect.Utils.LoadingInterface
import com.smitcoderx.volunteerconnect.Utils.ResponseState
import com.smitcoderx.volunteerconnect.Utils.hasInternetConnection
import com.smitcoderx.volunteerconnect.databinding.FragmentForumListBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ForumListFragment : Fragment(R.layout.fragment_forum_list),
    SwipeRefreshLayout.OnRefreshListener, ForumAdapter.OnClickHandle {

    private lateinit var binding: FragmentForumListBinding
    private val viewModel by viewModels<ForumListViewModel>()
    private val homeViewModel by viewModels<HomeViewModel>()
    private lateinit var prefs: DataStoreUtil
    private var listener: LoadingInterface? = null
    private lateinit var forumAdapter: ForumAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentForumListBinding.bind(view)

        prefs = DataStoreUtil(requireContext())
        homeViewModel.isNetworkConnectedLiveData.value = requireContext().hasInternetConnection()
        viewModel.isNetworkConnectedLiveData.value = requireContext().hasInternetConnection()
        homeViewModel.getCurrentLoggedinUser(prefs.getToken().toString())

        binding.tvCategoryType.text = if (prefs.getRole()
                .equals("organization", ignoreCase = true)
        ) "Created Forums" else "Participated Forums"

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

        forumAdapter = ForumAdapter(this)

        handleCurrentUser()
        if (prefs.getRole().equals("organization", ignoreCase = true)) {
            handleForumListNGO()
        } else {
            handleForumListUser()
        }

        binding.rvEvents.apply {
            setHasFixedSize(true)
            adapter = forumAdapter
        }

        binding.cardRetry.setOnClickListener {
            binding.llError.visibility = View.GONE
            showLoading()
            listener?.showLoading()
            binding.shimmerEffect.startShimmerAnimation()
            viewModel.handleNGOForumList(
                prefs.getToken().toString(),
                homeViewModel.userLiveData.value?.data?.data?.id.toString()
            )
            viewModel.handleUserForumList(
                prefs.getToken().toString(),
                homeViewModel.userLiveData.value?.data?.data?.id.toString()
            )
        }

    }


    private fun handleCurrentUser() {
        homeViewModel.userLiveData.observe(viewLifecycleOwner) {
            when (it) {
                is ResponseState.Success -> {
                    viewModel.handleNGOForumList(
                        prefs.getToken().toString(), it.data?.data?.id.toString()
                    )
                    viewModel.handleUserForumList(
                        prefs.getToken().toString(), it.data?.data?.id.toString()
                    )

                }

                is ResponseState.Error -> {
                }

                is ResponseState.Loading -> {
                }
            }
        }
    }

    @SuppressLint("SetTextI18n")
    private fun handleForumListNGO() {
        viewModel.eventForumListLiveData.observe(viewLifecycleOwner) {
            when (it) {
                is ResponseState.Success -> {
                    Log.d(Constants.TAG, "handleForumNGODataSuccess: ${it.data}")
                    binding.shimmerEffect.stopShimmerAnimation()
                    listener?.hideLoading()
                    hideLoading()
                    if (it.data.isNullOrEmpty()) {
                        binding.llError.visibility = View.VISIBLE
                        binding.cardRetry.visibility = View.GONE
                        binding.lottieView.setAnimation(R.raw.no_data)
                        binding.lottieView.loop(true)
                        binding.lottieView.playAnimation()
                        binding.tvErrorMsg.text = "Oops! No Forums Found To Show"

                    } else {
                        binding.llError.visibility = View.GONE
                        forumAdapter.differ.submitList(it.data)
                    }

                }

                is ResponseState.Loading -> {
                    binding.shimmerEffect.startShimmerAnimation()
                    showLoading()
                }

                is ResponseState.Error -> {
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

    @SuppressLint("SetTextI18n")
    private fun handleForumListUser() {
        viewModel.eventForumListUserLiveData.observe(viewLifecycleOwner) {
            when (it) {
                is ResponseState.Success -> {
                    Log.d(Constants.TAG, "handleForumUserDataSuccess: ${it.data}")
                    binding.shimmerEffect.stopShimmerAnimation()
                    listener?.hideLoading()
                    hideLoading()
                    if (it.data.isNullOrEmpty()) {
                        binding.llError.visibility = View.VISIBLE
                        binding.cardRetry.visibility = View.GONE
                        binding.lottieView.setAnimation(R.raw.no_data)
                        binding.lottieView.loop(true)
                        binding.lottieView.playAnimation()
                        binding.tvErrorMsg.text = "Oops! No Forums Found To Show"

                    } else {
                        binding.llError.visibility = View.GONE
                        forumAdapter.differ.submitList(it.data)
                    }
                }

                is ResponseState.Loading -> {
                    binding.shimmerEffect.startShimmerAnimation()
                    showLoading()
                }

                is ResponseState.Error -> {
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
            viewModel.handleNGOForumList(
                prefs.getToken().toString(),
                homeViewModel.userLiveData.value?.data?.data?.id.toString()
            )
            viewModel.handleUserForumList(
                prefs.getToken().toString(),
                homeViewModel.userLiveData.value?.data?.data?.id.toString()
            )
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
            handleForumListNGO()
        } else {
            handleForumListUser()
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

    override fun onForumClick(data: ForumData) {

    }
}