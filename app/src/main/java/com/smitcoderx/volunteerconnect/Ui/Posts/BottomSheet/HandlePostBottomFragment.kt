package com.smitcoderx.volunteerconnect.Ui.Posts.BottomSheet

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.smitcoderx.volunteerconnect.Model.Posts.PostsData
import com.smitcoderx.volunteerconnect.R
import com.smitcoderx.volunteerconnect.Ui.Posts.PostsViewModel
import com.smitcoderx.volunteerconnect.Utils.DataStoreUtil
import com.smitcoderx.volunteerconnect.Utils.ResponseState
import com.smitcoderx.volunteerconnect.Utils.hasInternetConnection
import com.smitcoderx.volunteerconnect.databinding.FragmentBottomHandlePostBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@AndroidEntryPoint
class HandlePostBottomFragment : BottomSheetDialogFragment(R.layout.fragment_bottom_handle_post) {

    private lateinit var binding: FragmentBottomHandlePostBinding
    private val viewModel by viewModels<PostsViewModel>()
    private lateinit var prefs: DataStoreUtil
    private val args by navArgs<HandlePostBottomFragmentArgs>()
    private lateinit var postsData: PostsData

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentBottomHandlePostBinding.bind(view)

        prefs = DataStoreUtil(requireContext())
        viewModel.isNetworkConnectedLiveData.value = requireContext().hasInternetConnection()
        postsData = args.postData

        binding.cardDeletePost.setOnClickListener {
            viewModel.deletePost(prefs.getToken().toString(), postsData.id.toString())
        }

        binding.cardEditPost.setOnClickListener {
            findNavController().navigate(
                HandlePostBottomFragmentDirections.actionHandlePostBottomFragmentToAddPostBottomFragment(
                    args.forumData, args.postData
                )
            )
        }

//        findNavController().currentBackStackEntry?.savedStateHandle?.getLiveData<String>("postUpdate")
//            ?.observe(viewLifecycleOwner) {
//
//            }



        handlePostDelete()
    }

    private fun handlePostDelete() {
        viewModel.deletePostLiveData.observe(viewLifecycleOwner) {
            when (it) {
                is ResponseState.Success -> {
                    hideLoading()
                    Toast.makeText(requireContext(), "Post Deleted", Toast.LENGTH_SHORT).show()
                    lifecycleScope.launch {
                        delay(1000)
                        findNavController().previousBackStackEntry?.savedStateHandle?.set(
                            "prePostDelete",
                            "Deleted"
                        )
                        findNavController().popBackStack()
                    }
                }

                is ResponseState.Loading -> showLoading()

                is ResponseState.Error -> {
                    hideLoading()
                    binding.tvErrorMsg.text = it.message.toString()
                }
            }
        }
    }

    private fun showLoading() {
        binding.progressLoading.visibility = View.VISIBLE
        binding.cardEditPost.visibility = View.GONE
        binding.cardDeletePost.visibility = View.GONE
    }

    private fun hideLoading() {
        binding.apply {
            progressLoading.visibility = View.GONE
            binding.cardEditPost.visibility = View.VISIBLE
            binding.cardDeletePost.visibility = View.VISIBLE
        }
    }

}