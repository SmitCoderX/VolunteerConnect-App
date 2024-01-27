package com.smitcoderx.volunteerconnect.Ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.animation.AnimationUtils
import com.smitcoderx.volunteerconnect.R
import com.smitcoderx.volunteerconnect.Ui.Register.RegisterAsFragmentDirections
import com.smitcoderx.volunteerconnect.Utils.DataStoreUtil
import com.smitcoderx.volunteerconnect.Utils.LoadingInterface
import com.smitcoderx.volunteerconnect.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity(), LoadingInterface {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navHost = supportFragmentManager.findFragmentById(R.id.container_view) as NavHostFragment
        val navController = navHost.navController
        binding.bottomNav.setupWithNavController(navController)
        val prefs = DataStoreUtil(applicationContext)
        if(prefs.getLoggedIn()) {
            if(prefs.getRole().equals("organization")) {
                navController.navigate(R.id.homeOrgFragment)
            } else {
                navController.navigate(RegisterAsFragmentDirections.actionRegisterAsFragmentToHomeFragment())
            }
            navController.clearBackStack(R.id.registerAsFragment)
        }

        navController.addOnDestinationChangedListener { _, destination, _ ->
            if (destination.id == R.id.homeFragment || destination.id == R.id.homeOrgFragment || destination.id == R.id.profileBottomSheetFragment || destination.id == R.id.addBottomSheetFragment) {
                binding.bottomNav.visibility = View.VISIBLE
            } else {
                binding.bottomNav.visibility = View.GONE
            }
        }
    }

    override fun hideLoading() {
        binding.apply {
            loading.visibility = View.GONE
        }
    }

    override fun showLoading() {
        binding.apply {
            loading.visibility = View.VISIBLE
        }
    }

}