package com.example.clusterproject.view

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.clusterproject.MainActivity
import com.example.clusterproject.base.BaseFragment
import com.example.clusterproject.databinding.FragmentIpcBinding
import com.example.data.model.RecentClient

class IpcFragment : BaseFragment<MainActivity>() {

    private lateinit var binding: FragmentIpcBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentIpcBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onStart() {
        super.onStart()
        val client = RecentClient.client
        Log.d(TAG, "onStart: ClientData::$client")
        binding.tvConnectionStatus.text =
            if (client == null) {
                "No connected client"
            } else {
                "Last Connected Client Info"
            }
        binding.tvPkgName.text = client?.clientPackageName
        binding.tvPID.text = client?.clientProcessId
        binding.tvClientData.text = client?.clientData
    }

    companion object {
        private const val TAG = "IpcFragment"
    }
}