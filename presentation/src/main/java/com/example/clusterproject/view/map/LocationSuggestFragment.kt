package com.example.clusterproject.view.map

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.TextView
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.clusterproject.BR
import com.example.clusterproject.MapsActivity
import com.example.clusterproject.R
import com.example.clusterproject.base.BaseFragment
import com.example.clusterproject.databinding.FragmentSuggestLocationMapBinding
import com.example.clusterproject.util.CheckInternet
import com.example.clusterproject.viewmodel.MapViewModel
import com.mapmyindia.sdk.demo.kotlin.plugin.com.example.clusterproject.DirectionActivity
import com.mappls.sdk.maps.MapplsMap
import com.mappls.sdk.maps.OnMapReadyCallback
import com.mappls.sdk.maps.annotations.IconFactory
import com.mappls.sdk.maps.annotations.MarkerOptions
import com.mappls.sdk.maps.camera.CameraUpdateFactory
import com.mappls.sdk.maps.geometry.LatLng
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class LocationSuggestFragment : BaseFragment<MapsActivity>(), OnMapReadyCallback, TextWatcher,
    TextView.OnEditorActionListener {

    private lateinit var binding: FragmentSuggestLocationMapBinding
    private var mapplsMap: MapplsMap? = null
    private var handler = Handler(Looper.getMainLooper())
    private val mapViewModel: MapViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSuggestLocationMapBinding.inflate(inflater, container, false)
        binding.setVariable(BR.mViewModel, mapViewModel)
        binding.lifecycleOwner = viewLifecycleOwner
        initUI()
        initObservers()
        return binding.root
    }

    override fun initUI() {
        super.initUI()
        binding.autoSuggest.apply {
            addTextChangedListener(this@LocationSuggestFragment)
            setOnEditorActionListener(this@LocationSuggestFragment)
        }
    }

    override fun initObservers() {
        super.initObservers()
        //lat lng is null of eLocation
        mapViewModel.observeSelectedLocation.removeObservers(viewLifecycleOwner)
        mapViewModel.observeSelectedLocation.observe(viewLifecycleOwner) { eLocation ->
            Log.d(TAG, "initObservers:mapplsPin ${eLocation?.mapplsPin}")
            eLocation?.let {
                MarkerOptions().apply {
//                    position = LatLng(eLocation.latitude, eLocation.longitude)
                    mapplsPin = eLocation.mapplsPin
                    icon =
                        IconFactory.getInstance(requireActivity())
                            .fromResource(R.drawable.placeholder)
                }.also {
                    mapplsMap?.addMarker(it)
                }
            }
            Intent(requireActivity(), DirectionActivity::class.java).putExtra(
                "Dest",
                eLocation?.mapplsPin
            ).also {
                startActivity(it)
            }
        }
    }

    override fun onMapReady(mapplsMap: MapplsMap) {
        this.mapplsMap = mapplsMap
        mapplsMap.moveCamera(
            CameraUpdateFactory.newLatLngZoom(
                LatLng(
                    22.553147478403194,
                    77.23388671875
                ), 4.0
            )
        )
    }

    override fun onMapError(p0: Int, p1: String?) {
        TODO("Not yet implemented")
    }

    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

    override fun afterTextChanged(s: Editable?) {}

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
        Log.d(TAG, "onTextChanged: ${s.toString()}")
        handler.postDelayed({
            if (s?.length!! > 2) {
                if (CheckInternet.isNetworkAvailable(requireContext())) {
                    autoSuggestApi(s.toString())
                }
            }
        }, 300)
    }

    override fun onEditorAction(v: TextView?, actionId: Int, event: KeyEvent?): Boolean {
        if (actionId == EditorInfo.IME_ACTION_SEARCH) {
            autoSuggestApi(v?.getText().toString())
            binding.autoSuggest.clearFocus();
            val aa =
                requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            aa.hideSoftInputFromWindow(binding.autoSuggest.windowToken, 0)
            return true;
        }
        return false;
    }

    private fun autoSuggestApi(query: String) {
        Log.d(TAG, "autoSuggestApi: query::$query")
        lifecycleScope.launch(Dispatchers.IO) {
            mapViewModel.getAutoSuggestLocList(query)
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

    companion object {
        private const val TAG = "AutoSuggestMapFragment"
    }

}