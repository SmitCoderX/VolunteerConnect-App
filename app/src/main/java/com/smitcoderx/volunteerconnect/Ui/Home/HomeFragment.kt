package com.smitcoderx.volunteerconnect.Ui.Home

import android.graphics.Color
import android.graphics.ColorFilter
import android.os.Bundle
import android.view.View
import android.view.ViewTreeObserver.OnGlobalLayoutListener
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.google.android.material.badge.BadgeDrawable
import com.google.android.material.badge.BadgeUtils
import com.google.android.material.badge.ExperimentalBadgeUtils
import com.mappls.sdk.maps.Mappls.getApplicationContext
import com.smitcoderx.volunteerconnect.R
import com.smitcoderx.volunteerconnect.databinding.FragmentHomeBinding
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


@ExperimentalBadgeUtils
class HomeFragment : Fragment(R.layout.fragment_home) {

    private lateinit var binding: FragmentHomeBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentHomeBinding.bind(view)
        binding.ivNotification.clipToOutline = false //Important with a CardView
        val badgeDrawable = BadgeDrawable.create(requireContext())
        badgeDrawable.isVisible = true
        badgeDrawable.badgeGravity = BadgeDrawable.TOP_END

        val types = listOf(
            TypesModel("Find \nOrganizations", Color.RED),
            TypesModel("Find \nNGOs", Color.BLUE),
            TypesModel("Find \nColleges", requireContext().getColor(R.color.purple)),
            TypesModel("Find \nEvents", Color.MAGENTA),
            TypesModel("Find \nOthers", requireContext().getColor(R.color.dark_orange)),
        )

        val typeAdapter = TypesAdapter()
        typeAdapter.differ.submitList(types)

        binding.rvTypes.apply {
            setHasFixedSize(true)
            adapter = typeAdapter
        }
    }
}