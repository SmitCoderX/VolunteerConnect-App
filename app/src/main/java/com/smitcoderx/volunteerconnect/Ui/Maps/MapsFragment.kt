package com.smitcoderx.volunteerconnect.Ui.Maps

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.mappls.sdk.maps.MapplsMap
import com.mappls.sdk.maps.OnMapReadyCallback
import com.mappls.sdk.maps.SupportMapFragment
import com.smitcoderx.volunteerconnect.R
import com.smitcoderx.volunteerconnect.databinding.FragmentMapsBinding

class MapsFragment: Fragment(R.layout.fragment_maps), OnMapReadyCallback {

    private lateinit var binding: FragmentMapsBinding
    private var mapplsMap: MapplsMap? = null
    private lateinit var supportFragment: SupportMapFragment

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentMapsBinding.bind(view)

        supportFragment = childFragmentManager.findFragmentById(R.id.map_view) as SupportMapFragment
        supportFragment.onCreate(savedInstanceState)
        supportFragment.getMapAsync(this)

        binding.ivBack.setOnClickListener {
            findNavController().popBackStack()
        }

    }

    override fun onMapReady(maps: MapplsMap) {
        mapplsMap = maps
    }

    override fun onMapError(p0: Int, p1: String?) {
        TODO("Not yet implemented")
    }

    override fun onStart() {
        super.onStart()
        supportFragment.onStart()
    }

    override fun onStop() {
        super.onStop()
        supportFragment.onStop()
    }

    override fun onDestroy() {
        super.onDestroy()
        supportFragment.onDestroy()
    }

    override fun onPause() {
        super.onPause()
        supportFragment.onPause()
    }

    override fun onResume() {
        super.onResume()
        supportFragment.onResume()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        supportFragment.onLowMemory()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        supportFragment.onSaveInstanceState(outState)
    }
}