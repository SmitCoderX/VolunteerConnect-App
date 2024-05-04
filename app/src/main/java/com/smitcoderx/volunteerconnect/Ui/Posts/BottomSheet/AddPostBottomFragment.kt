package com.smitcoderx.volunteerconnect.Ui.Posts.BottomSheet

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.smitcoderx.volunteerconnect.Model.Posts.PostsData
import com.smitcoderx.volunteerconnect.R
import com.smitcoderx.volunteerconnect.Ui.Home.HomeViewModel
import com.smitcoderx.volunteerconnect.Ui.Posts.PostsViewModel
import com.smitcoderx.volunteerconnect.Utils.DataStoreUtil
import com.smitcoderx.volunteerconnect.Utils.ResponseState
import com.smitcoderx.volunteerconnect.Utils.hasInternetConnection
import com.smitcoderx.volunteerconnect.databinding.FragmentBottomAddPostBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AddPostBottomFragment : BottomSheetDialogFragment(R.layout.fragment_bottom_add_post) {

    private lateinit var binding: FragmentBottomAddPostBinding
    private lateinit var prefs: DataStoreUtil
    private val args by navArgs<AddPostBottomFragmentArgs>()
    private val viewModel by viewModels<PostsViewModel>()
    private val homeViewModel by viewModels<HomeViewModel>()

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentBottomAddPostBinding.bind(view)

        prefs = DataStoreUtil(requireContext())
        viewModel.isNetworkConnectedLiveData.value = requireContext().hasInternetConnection()
        homeViewModel.isNetworkConnectedLiveData.value = requireContext().hasInternetConnection()
        homeViewModel.getCurrentLoggedinUser(prefs.getToken().toString())

        if (args.postData != null) {
            binding.tilTitle.editText?.setText(args.postData?.postName.toString())
            binding.tilBody.editText?.setText(args.postData?.postDetails.toString())
            binding.tilUploadEventPic.editText?.setText(args.postData?.postImg.toString())
            binding.btnNext.text = "Update"
        } else {
            binding.btnNext.text = "Create Post"
        }

        handleCurrentUser()
        handleCreatePost()
        handleUpdatePost()

    }

    private fun handleCurrentUser() {
        homeViewModel.userLiveData.observe(viewLifecycleOwner) { user ->
            when (user) {
                is ResponseState.Success -> {
                    binding.btnNext.setOnClickListener {
                        val data = PostsData(
                            createdByName = user.data?.data?.name,
                            createdByProfile = user.data?.data?.photos,
                            forumName = args.forumData?.forumName.toString(),
                            forumId = args.forumData?.id.toString(),
                            postDetails = binding.tilBody.editText?.text.toString(),
                            postName = binding.tilTitle.editText?.text.toString(),
                            postImg = binding.tilUploadEventPic.editText?.text.toString(),
                        )
                        if (args.postData != null) {
                            viewModel.updatePost(
                                prefs.getToken().toString(), args.postData?.id.toString(), data
                            )
                        } else {
                            viewModel.createPost(prefs.getToken().toString(), data)
                        }
                    }
                }

                is ResponseState.Error -> {
                    Toast.makeText(requireContext(), user.message.toString(), Toast.LENGTH_SHORT)
                        .show()
                    findNavController().popBackStack()
                }

                is ResponseState.Loading -> {

                }
            }
        }
    }

    private fun handleCreatePost() {
        viewModel.createPostLiveData.observe(viewLifecycleOwner) {
            when (it) {
                is ResponseState.Success -> {
                    hideLoading()
                    Toast.makeText(requireContext(), "Post Created", Toast.LENGTH_SHORT).show()
                    findNavController().previousBackStackEntry?.savedStateHandle?.set(
                        "postCreate",
                        "Created"
                    )
                    findNavController().popBackStack()
                }

                is ResponseState.Loading -> {
                    showLoading()
                }

                is ResponseState.Error -> {
                    hideLoading()
                    binding.tvErrorMsg.text = it.message.toString()
                }
            }
        }
    }

    private fun handleUpdatePost() {
        viewModel.updatePostLiveData.observe(viewLifecycleOwner) {
            when (it) {
                is ResponseState.Success -> {
                    hideLoading()
                    Toast.makeText(requireContext(), "Post Updated", Toast.LENGTH_SHORT).show()
                    findNavController().previousBackStackEntry?.savedStateHandle?.set(
                        "postUpdate",
                        "Updated"
                    )
                    findNavController().navigate(
                        AddPostBottomFragmentDirections.actionAddPostBottomFragmentToPostsFragment2(
                            args.forumData
                        )
                    )
                }

                is ResponseState.Loading -> {
                    showLoading()
                }

                is ResponseState.Error -> {
                    hideLoading()
                    binding.tvErrorMsg.text = it.message.toString()
                }
            }
        }
    }

    private fun showLoading() {
        binding.apply {
            progressLoading.visibility = View.VISIBLE
            tilTitle.visibility = View.GONE
            tvTitle.visibility = View.GONE
            tilBody.visibility = View.GONE
            tvBody.visibility = View.GONE
            tilUploadEventPic.visibility = View.GONE
            tvUpload.visibility = View.GONE
            btnNext.visibility = View.GONE
        }
    }

    private fun hideLoading() {
        binding.apply {
            progressLoading.visibility = View.GONE
            tilTitle.visibility = View.VISIBLE
            tvTitle.visibility = View.VISIBLE
            tilBody.visibility = View.VISIBLE
            tvBody.visibility = View.VISIBLE
            tilUploadEventPic.visibility = View.VISIBLE
            tvUpload.visibility = View.VISIBLE
            btnNext.visibility = View.VISIBLE
        }
    }

}