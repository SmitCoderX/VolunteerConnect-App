package com.smitcoderx.volunteerconnect.Ui

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.smitcoderx.volunteerconnect.R
import com.smitcoderx.volunteerconnect.Utils.DataStoreUtil
import com.smitcoderx.volunteerconnect.Utils.LoadingInterface
import com.smitcoderx.volunteerconnect.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity(), LoadingInterface {

    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val navHost =
            supportFragmentManager.findFragmentById(R.id.container_view) as NavHostFragment
        navController = navHost.navController
        binding.bottomNav.setupWithNavController(navController)
        val prefs = DataStoreUtil(applicationContext)
        if (prefs.getLoggedIn()) {
            if (prefs.getRole().equals("organization")) {
                navController.navigate(R.id.homeOrgFragment)
                binding.bottomNav.menu.findItem(R.id.homeFragment).setVisible(false)
                binding.bottomNav.menu.findItem(R.id.homeOrgFragment).setVisible(true)
                    .setChecked(true)
            } else {
                navController.navigate(R.id.homeFragment)
            }
//            navController.clearBackStack(R.id.registerAsFragment)
        }

        navController.addOnDestinationChangedListener { _, destination, _ ->
            if (prefs.getRole().equals("organization")) {
                binding.bottomNav.menu.findItem(R.id.homeFragment).setVisible(false)
                binding.bottomNav.menu.findItem(R.id.homeOrgFragment).setVisible(true)
            } else {
                binding.bottomNav.menu.findItem(R.id.homeFragment).setVisible(true)
                binding.bottomNav.menu.findItem(R.id.homeOrgFragment).setVisible(false)
            }

            if (destination.id == R.id.homeFragment || destination.id == R.id.homeOrgFragment || destination.id == R.id.profileBottomSheetFragment || destination.id == R.id.addBottomSheetFragment || destination.id == R.id.action_jobs || destination.id == R.id.action_connections || destination.id == R.id.action_community) {
                binding.bottomNav.visibility = View.VISIBLE
            } else {
                binding.bottomNav.visibility = View.GONE
            }
        }

//        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
//            override fun handleOnBackPressed() {
//                if (!findNavController(R.id.container_view).popBackStack()) {
//                    // If the back stack is empty, close the app
//                    navController.popBackStack(navController.graph.startDestinationId, true)
//
//                    finish()
//                }
//            }
//        })

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

    private var doubleBackToExitPressedOnce = false
    override fun onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed()
            finish()
            return
        }

        this.doubleBackToExitPressedOnce = true
        Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show()

        Handler(Looper.getMainLooper()).postDelayed(Runnable {
            doubleBackToExitPressedOnce = false
        }, 2000)
    }

}