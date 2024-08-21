package com.example.clusterproject

import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.example.clusterproject.base.BaseActivity
import com.example.clusterproject.databinding.ActivityMapsBinding

class MapsActivity : BaseActivity() {

    private lateinit var navController: NavController
    private lateinit var binding: ActivityMapsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMapsBinding.inflate(layoutInflater)
        enableEdgeToEdge()
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        initUI()
        registerListeners()
    }

    override fun initUI() {
        setUpNavController()
    }

    //initialising nav controller
    private fun setUpNavController() {
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.mapNavHostFragment) as NavHostFragment
        navController = navHostFragment.navController
    }

    override fun registerListeners() {
        super.registerListeners()

        binding.bottomNav.setOnItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.actionHome -> {
                    navController.navigate(R.id.homeFragment)
                    true
                }

                else -> {
                    Log.d(TAG, "registerListeners: ")
                    true
                }
            }
        }
    }

    companion object {
        private const val TAG = "MapsActivity"
    }
}