package com.example.clusterproject.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.clusterproject.MainActivity
import com.example.clusterproject.base.BaseFragment
import com.example.clusterproject.databinding.FragmentBleBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class BleFragment : BaseFragment<MainActivity>() {
    private lateinit var binding: FragmentBleBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentBleBinding.inflate(inflater, container, false)
        return binding.root
    }

    companion object {
        private const val TAG = "BleFragment"
    }
}