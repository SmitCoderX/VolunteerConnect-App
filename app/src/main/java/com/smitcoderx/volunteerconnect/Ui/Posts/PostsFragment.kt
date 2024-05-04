package com.smitcoderx.volunteerconnect.Ui.Posts

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
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.smitcoderx.volunteerconnect.Model.Forum.ForumData
import com.smitcoderx.volunteerconnect.Model.Posts.PostsData
import com.smitcoderx.volunteerconnect.R
import com.smitcoderx.volunteerconnect.Utils.Constants
import com.smitcoderx.volunteerconnect.Utils.Constants.TAG
import com.smitcoderx.volunteerconnect.Utils.DataStoreUtil
import com.smitcoderx.volunteerconnect.Utils.LoadingInterface
import com.smitcoderx.volunteerconnect.Utils.ResponseState
import com.smitcoderx.volunteerconnect.Utils.hasInternetConnection
import com.smitcoderx.volunteerconnect.databinding.FragmentPostsBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@AndroidEntryPoint
class PostsFragment : Fragment(R.layout.fragment_posts), SwipeRefreshLayout.OnRefreshListener,
    PostAdapter.OnHandleActions {

    private lateinit var binding: FragmentPostsBinding
    private val viewModel by viewModels<PostsViewModel>()
    private lateinit var prefs: DataStoreUtil
    private val args by navArgs<PostsFragmentArgs>()
    private var forumData: ForumData? = null
    private var listener: LoadingInterface? = null
    private lateinit var postAdapter: PostAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentPostsBinding.bind(view)

        prefs = DataStoreUtil(requireContext())
        forumData = args.forumData
        viewModel.isNetworkConnectedLiveData.value = requireContext().hasInternetConnection()
        viewModel.handlePostList(prefs.getToken().toString(), forumData?.id.toString())


        if (prefs.getRole().equals("organization", ignoreCase = true)) {
            binding.addPost.visibility = View.VISIBLE
        } else {
            binding.addPost.visibility = View.GONE
        }

        binding.addPost.setOnClickListener {
            findNavController().navigate(
                PostsFragmentDirections.actionPostsFragmentToAddPostBottomFragment(
                    args.forumData,
                    null
                )
            )
        }



        binding.tvCategoryType.text = args.forumData?.forumName.toString()

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

        binding.ivBack.setOnClickListener {
            findNavController().popBackStack()
        }

        postAdapter = PostAdapter(this, prefs.getRole().toString())
        handlePostList()

        binding.rvEvents.apply {
            setHasFixedSize(true)
            adapter = postAdapter
        }

        binding.cardRetry.setOnClickListener {
            binding.llError.visibility = View.GONE
            showLoading()
            listener?.showLoading()
            binding.shimmerEffect.startShimmerAnimation()
            viewModel.handlePostList(prefs.getToken().toString(), forumData?.id.toString())
        }


        findNavController().currentBackStackEntry?.savedStateHandle?.getLiveData<String>("prePostDelete")
            ?.observe(viewLifecycleOwner) {
                viewModel.handlePostList(prefs.getToken().toString(), forumData?.id.toString())
                postAdapter.notifyDataSetChanged()
            }


        findNavController().currentBackStackEntry?.savedStateHandle?.getLiveData<String>("postCreate")
            ?.observe(viewLifecycleOwner) {
                viewModel.handlePostList(prefs.getToken().toString(), forumData?.id.toString())
            }

    }

    @SuppressLint("SetTextI18n")
    private fun handlePostList() {
        viewModel.postLiveLiveData.observe(viewLifecycleOwner) {
            when (it) {
                is ResponseState.Success -> {
                    Log.d(Constants.TAG, "handlePostList: ${it.data}")

                    binding.shimmerEffect.stopShimmerAnimation()
                    listener?.hideLoading()
                    hideLoading()
                    if (prefs.getRole().equals("organization", ignoreCase = true)) {
                        binding.addPost.visibility = View.VISIBLE
                    } else {
                        binding.addPost.visibility = View.GONE
                    }
                    if (it.data?.data.isNullOrEmpty()) {
                        binding.llError.visibility = View.VISIBLE
                        binding.cardRetry.visibility = View.GONE
                        binding.lottieView.setAnimation(R.raw.no_data)
                        binding.lottieView.loop(true)
                        binding.lottieView.playAnimation()
                        binding.tvErrorMsg.text = "Oops! No Posts Found To Show"

                    } else {
                        binding.llError.visibility = View.GONE
                        postAdapter.differ.submitList(it.data?.data)
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
            binding.ivBack.visibility = View.GONE
            binding.addPost.visibility = View.GONE
        }
    }

    private fun hideLoading() {
        binding.apply {
            binding.shimmerEffect.visibility = View.GONE
            binding.llError.visibility = View.GONE
            binding.rvEvents.visibility = View.VISIBLE
            binding.tvCategoryType.visibility = View.VISIBLE
            binding.ivBack.visibility = View.VISIBLE
            binding.addPost.visibility = View.VISIBLE

        }
    }

    override fun onRefresh() {
        binding.llError.visibility = View.GONE
        listener?.showLoading()
        showLoading()
        binding.shimmerEffect.startShimmerAnimation()
        lifecycleScope.launch {
            delay(1500)
            viewModel.handlePostList(prefs.getToken().toString(), forumData?.id.toString())
            listener?.hideLoading()
            hideLoading()
            if(prefs.getRole().equals("organization", ignoreCase = true)) {
                binding.addPost.visibility = View.VISIBLE
            } else {
                binding.addPost.visibility = View.GONE
            }
            binding.categorySwipeLayout.isRefreshing = false
            binding.shimmerEffect.stopShimmerAnimation()
            binding.llError.visibility = View.GONE
        }
    }

    override fun onResume() {
        super.onResume()
        handlePostList()
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

    override fun onClick(data: PostsData) {
        findNavController().navigate(
            PostsFragmentDirections.actionPostsFragmentToHandlePostBottomFragment(
                data,
                args.forumData!!
            )
        )
    }
}