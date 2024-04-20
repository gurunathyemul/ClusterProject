package com.example.newproject

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.example.newproject.base.BaseActivity
import com.example.newproject.databinding.ActivityMainBinding
import com.example.newproject.other.Resource
import com.example.newproject.ui.ContentInterface
import com.example.newproject.viewmodel.NewsDetailsViewModel
import com.example.newproject.viewmodel.NewsHeadlineViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : BaseActivity(), ContentInterface {
    private val newsDetailViewModel: NewsDetailsViewModel by viewModels()
    private lateinit var binding: ActivityMainBinding
    private val mViewModel: NewsHeadlineViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.setVariable(BR.mViewModel, mViewModel)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        newsDetailViewModel.getNewsHeadline("in")
        lifecycleScope.launch {
            newsDetailViewModel.newsHeadlines.collect {
                when (it) {
                    is Resource.Loading -> Log.d(TAG, "onCreate: Loading")
                    is Resource.Success -> {
                        Log.d(TAG, "onCreate: Success ${it.data?.articles}")
                        it.data?.articles?.let { articles ->
                            mViewModel.setNewsHeadlineData(articles, this@MainActivity)
                        }
                    }

                    is Resource.Error -> Log.d(TAG, "onCreate: Error")
                }
            }
        }
    }

    override fun onClick(data: String?) {
        val urlIntent = Intent(
            Intent.ACTION_VIEW,
            Uri.parse(data)
        )
        startActivity(urlIntent)
    }

    companion object {
        private const val TAG = "MainActivity"
    }

}