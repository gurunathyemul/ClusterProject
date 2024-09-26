package com.example.clusterproject.view.map

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import com.example.clusterproject.MapsActivity
import com.example.clusterproject.R
import com.example.clusterproject.base.BaseFragment
import com.example.clusterproject.databinding.FragmentMapHomeBinding
import com.example.clusterproject.util.PermissionUtils
import com.mappls.sdk.maps.MapplsMap
import com.mappls.sdk.maps.OnMapReadyCallback
import com.mappls.sdk.maps.Style
import com.mappls.sdk.maps.annotations.IconFactory
import com.mappls.sdk.maps.annotations.MarkerOptions
import com.mappls.sdk.maps.camera.CameraPosition
import com.mappls.sdk.maps.camera.CameraUpdateFactory
import com.mappls.sdk.maps.geometry.LatLng
import com.mappls.sdk.maps.location.LocationComponent
import com.mappls.sdk.maps.location.LocationComponentActivationOptions
import com.mappls.sdk.maps.location.LocationComponentOptions
import com.mappls.sdk.maps.location.engine.LocationEngine
import com.mappls.sdk.maps.location.engine.LocationEngineCallback
import com.mappls.sdk.maps.location.engine.LocationEngineRequest
import com.mappls.sdk.maps.location.engine.LocationEngineResult
import com.mappls.sdk.maps.location.modes.CameraMode
import com.mappls.sdk.maps.location.modes.RenderMode

//https://docs.mapbox.com/android/legacy/maps/guides/location-component/

class MapHomeFragment : BaseFragment<MapsActivity>(), OnMapReadyCallback {
    private lateinit var binding: FragmentMapHomeBinding
    private lateinit var mapplsMap: MapplsMap
    private lateinit var locationComponent: LocationComponent
    private lateinit var locationEngine: LocationEngine

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMapHomeBinding.inflate(inflater, container, false)
        initUI()
        return binding.root
    }

    override fun initUI() {
        super.initUI()
        locationPermissions()
        //Call the getMapAsync method to set the callback on the fragment.
        binding.mapView.getMapAsync(this)
    }

    override fun onMapReady(map: MapplsMap) {
        Log.d(TAG, "onMapReady: ")
        mapplsMap = map
        map.getStyle { enableLocation(it) }

        val markerOptions: MarkerOptions = MarkerOptions().position(LatLng(17.10, 74.42))
            .icon(IconFactory.getInstance(requireActivity()).fromResource(R.drawable.placeholder))
        markerOptions.title = "Solapur"
        markerOptions.snippet = "Solapur"
        mapplsMap.addMarker(markerOptions)

        moveCamera()
    }

    /**this is done for animating/moving camera to particular position */
    private fun moveCamera() {
        val cameraPosition = CameraPosition.Builder().target(
            LatLng(
                25.321684, 82.987289
            )
        ).zoom(8.0).tilt(0.0).build()
        mapplsMap.cameraPosition = cameraPosition
    }

    //https://docs.mapbox.com/android/maps/api/9.6.0/com/mapbox/mapboxsdk/location/LocationComponentOptions.Builder.html
    @SuppressLint("MissingPermission")
    private fun enableLocation(style: Style) {
        // The LocationComponentOptions class can be used if you prefer to customize the LocationComponent programmatically.
        val locationComponentOptions: LocationComponentOptions =
            LocationComponentOptions.builder(requireContext())//Builder class for constructing a new instance of LocationComponentActivationOptions.
                .trackingGesturesManagement(true)//Set whether gesture threshold should be adjusted when camera is in one of the tracking modes.
                .accuracyColor(
                    ContextCompat.getColor(
                        requireContext(),
                        R.color.colorAccent
                    )
                )//Solid color to use as the accuracy view color property.
                .foregroundDrawable(R.drawable.ic_home)//Supply a Drawable that is to be rendered on top of all of the content in the Location Component layer stack.
                .build()
        // Get an instance of the component LocationComponent
        locationComponent =
            mapplsMap.locationComponent//customized in many different ways. You can set the image drawables, opacities, colors, and more.

        //A class which holds the various options for activating the Maps SDK's LocationComponent to eventually show the device location on the map.
        val locationComponentActivationOptions =
            LocationComponentActivationOptions.builder(requireActivity(), style)
                .locationComponentOptions(locationComponentOptions)
                .build()
        locationComponent.activateLocationComponent(locationComponentActivationOptions)
        locationComponent.isLocationComponentEnabled = true

        locationEngine = locationComponent.locationEngine!!
        //to receiving Location updates in your project.
        val locationEngineRequest = LocationEngineRequest.Builder(DEFAULT_INTERVAL_IN_MILLISECONDS)
            .setPriority(LocationEngineRequest.PRIORITY_HIGH_ACCURACY)
            .setMaxWaitTime(5000)
            .build()
        //to receive location updates when the trip session is stopped,
        locationEngine.requestLocationUpdates(
            locationEngineRequest,
            locationEngineCallback,
            Looper.getMainLooper()
        )
        locationEngine.getLastLocation(locationEngineCallback)
        locationComponent.cameraMode = CameraMode.TRACKING
        locationComponent.renderMode =
            RenderMode.GPS //The RenderMode class contains preset options for the device location image.
    }

    private val locationEngineCallback: LocationEngineCallback<LocationEngineResult> =
        object : LocationEngineCallback<LocationEngineResult> {
            override fun onSuccess(locationEngineResult: LocationEngineResult?) {
                val location = locationEngineResult?.lastLocation
                Log.d(TAG, "onSuccess: location::$location")
                location?.let {
                    mapplsMap.animateCamera(
                        CameraUpdateFactory.newLatLng(
                            LatLng(
                                location.latitude,
                                location.longitude,
                                16.00
                            )
                        )
                    )
                }
            }

            override fun onFailure(exception: Exception) {
                Log.d(TAG, "onFailure: $exception")
            }

        }

    override fun onMapError(p0: Int, p1: String?) {
        Log.d(TAG, "onMapError: $p0 $p1")
    }

    private fun locationPermissions() {
        if (PermissionUtils.haveLocationPermissions(requireActivity())) {
            Log.d(TAG, "locationPermissions: enable")
        } else {
            Log.d(TAG, "locationPermissions: disable")
            locationPermissions.launch(PermissionUtils.LocationPermissions)
        }

    }

    private val locationPermissions =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
            if (permissions.values.all { it }) {
                Log.d(TAG, "locationPermissions: enable")
            } else {
                Log.d(TAG, "locationPermissions: disable")
            }
        }


    override fun onStart() {
        super.onStart()
        binding.mapView.onStart()
    }

    override fun onStop() {
        super.onStop()
        binding.mapView.onStop()
    }

    override fun onDestroy() {
        super.onDestroy()
        binding.mapView.onDestroy()
    }

    override fun onPause() {
        super.onPause()
        binding.mapView.onPause()
    }

    override fun onResume() {
        super.onResume()
        binding.mapView.onResume()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        binding.mapView.onLowMemory()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding.mapView.onDestroy()
    }

    companion object {
        private const val TAG = "MapHomeFragment"
        private const val DEFAULT_INTERVAL_IN_MILLISECONDS: Long = 1000L

    }

}