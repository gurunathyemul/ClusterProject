package com.example.newproject

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.viewpager2.widget.ViewPager2
import com.example.newproject.adapter.ViewPagerAdapter
import com.example.newproject.base.BaseActivity
import com.example.newproject.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : BaseActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        initUI()
        registerListeners()
    }

    override fun onBackPressed() {
        super.onBackPressed()
        /*   val label = navController.currentDestination?.label
           // Update the selected item in the bottom navigation view
           binding.bottomNav.menu.forEach {
               if (it.title == label) it.isChecked = true
           }*/  //for navigation with nav controller
    }

    override fun initUI() {
//        setUpNavController()
        val adapter = ViewPagerAdapter(this)
        binding.viewPager.adapter = adapter
    }

    //initialising nav controller
    private fun setUpNavController() {
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.mainNavHostFragment) as NavHostFragment
        navController = navHostFragment.navController
    }

    override fun registerListeners() {
        //menu item click listener
        binding.bottomNav.setOnItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.actionNews -> {
                    binding.viewPager.currentItem = 0
                    //navController.navigate(R.id.newsFragment)
                    true
                }

                R.id.actionContact -> {
                    binding.viewPager.currentItem = 1
                    //navController.navigate(R.id.contactFragment)
                    true
                }

                R.id.actionBle -> {
                    binding.viewPager.currentItem = 2
                    //navController.navigate(R.id.bleFragment)
                    true
                }

                R.id.actionIPC -> {
                    binding.viewPager.currentItem = 3
                    //navController.navigate(R.id.ipcFragment)
                    true
                }

                else -> false
            }
        }

        // Add a page change listener to ViewPager2
        binding.viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                // Update the selected item in the BottomNavigationView
                binding.bottomNav.menu.getItem(position).isChecked = true
            }
        })
    }


    companion object {
        private const val TAG = "MainActivity"
    }

}