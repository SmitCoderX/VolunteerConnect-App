package com.smitcoderx.volunteerconnect.Ui.Maps

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.LocationManager
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.smitcoderx.volunteerconnect.R
import com.smitcoderx.volunteerconnect.Ui.Categories.CategoryViewModel
import com.smitcoderx.volunteerconnect.Utils.ResponseState
import com.smitcoderx.volunteerconnect.databinding.FragmentMapsBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MapsFragment : Fragment(R.layout.fragment_maps), OnMapReadyCallback {

    private lateinit var binding: FragmentMapsBinding
    private lateinit var supportFragment: SupportMapFragment
    private lateinit var mMap: GoogleMap
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private val LOCATION_PERMISSION_REQUEST_CODE = 1
    private var isLocationEnabled = false
    private val viewModel by viewModels<CategoryViewModel>()

    private val requestLocationEnableLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { _ ->
            // After the user returns from the location settings activity, check if location services are enabled
            checkLocationAvailability()
        }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentMapsBinding.bind(view)

        supportFragment = childFragmentManager.findFragmentById(R.id.map_view) as SupportMapFragment

        supportFragment.getMapAsync(this)

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity())

        viewModel.getEventList()

        binding.focusButton.setOnClickListener {
            showCurrentLocation()
        }

        binding.ivBack.setOnClickListener {
            findNavController().popBackStack()
        }
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        // Check for location availability
        checkLocationAvailability()

        if (hasLocationPermission()) {
            showCurrentLocation()
        } else {
            requestLocationPermission()
        }

        viewModel.eventLiveData.observe(viewLifecycleOwner) {
            when (it) {
                is ResponseState.Success -> {
                    it.data?.forEach { item ->
                        mMap.addMarker(
                            MarkerOptions().position(
                                    LatLng(
                                        item?.coordinates?.get(0) ?: 0.0,
                                        item?.coordinates?.get(1) ?: 0.0
                                    )
                                ).title(item?.name).snippet(item?.desc)
                        )
                    }

                    mMap.setOnMarkerClickListener { marker ->
                        val selectedMarkerData =
                            it.data?.find { item -> item?.name == marker.title }
                        if (selectedMarkerData != null) {
                            findNavController().navigate(
                                MapsFragmentDirections.actionMapsFragmentToMapEventBottomFragment(
                                    selectedMarkerData
                                )
                            )
                        }
                        true
                    }
                }

                is ResponseState.Loading -> {

                }

                is ResponseState.Error -> {
                }
            }
        }
    }

    private fun checkLocationAvailability() {
        isLocationEnabled = isLocationEnabled()
        if (isLocationEnabled) {
            // Location is now enabled, show current location
            showCurrentLocation()
        } else {
            // Location is disabled, prompt user to enable it
            showLocationServicesDialog()
        }
    }

    private fun showLocationServicesDialog() {
        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle("Location Services Disabled")
            .setMessage("Please enable location services to use this app")
            .setPositiveButton("OK") { _, _ ->
                // Open location settings
                openLocationSettings()
            }.setNegativeButton("Cancel") { dialog, _ ->
                dialog.dismiss()
            }.setCancelable(false).show()
    }

    private fun openLocationSettings() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
            requestLocationEnableLauncher.launch(intent)
        } else {
            val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
            startActivity(intent)
        }
    }

    private fun isLocationEnabled(): Boolean {
        val locationManager =
            requireContext().getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
    }

    private fun hasLocationPermission(): Boolean {
        return ActivityCompat.checkSelfPermission(
            requireContext(), android.Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
    }

    private fun requestLocationPermission() {
        ActivityCompat.requestPermissions(
            requireActivity(),
            arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION),
            LOCATION_PERMISSION_REQUEST_CODE
        )
    }

    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<out String>, grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                showCurrentLocation()
            } else {
                Toast.makeText(requireContext(), "Permission denied", Toast.LENGTH_SHORT).show()
            }
        }
    }

    @SuppressLint("MissingPermission")
    private fun showCurrentLocation() {
        mMap.isMyLocationEnabled = true

        fusedLocationClient.lastLocation.addOnSuccessListener { location ->
                if (location != null) {
                    val currentLatLng = LatLng(location.latitude, location.longitude)
                    mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(currentLatLng, 15f))
                } else {
                    Toast.makeText(
                        requireContext(), "Unable to get current location", Toast.LENGTH_SHORT
                    ).show()
                }
            }
    }
}