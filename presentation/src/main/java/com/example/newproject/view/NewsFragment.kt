package com.example.newproject.view

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.newproject.BR
import com.example.newproject.MainActivity
import com.example.newproject.base.BaseFragment
import com.example.newproject.databinding.FragmentNewsBinding
import com.example.newproject.other.Resource
import com.example.newproject.ui.ContentInterface
import com.example.newproject.viewmodel.NewsHeadlineViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class NewsFragment : BaseFragment<MainActivity>(), ContentInterface {

    private lateinit var binding: FragmentNewsBinding
    private val newsDetailViewModel: NewsHeadlineViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentNewsBinding.inflate(inflater, container, false)
        binding.setVariable(BR.mViewModel, newsDetailViewModel)
        initObservers()
        return binding.root
    }

    override fun initObservers() {
        super.initObservers()
        observerNewsData()
    }

    override fun onResume() {
        super.onResume()
        newsDetailViewModel.getNewsHeadline("in")
    }

    private fun observerNewsData() {
        lifecycleScope.launch {
            newsDetailViewModel.newsHeadlines.collect {
                when (it) {
                    is Resource.Loading -> {
                        Log.d(TAG, "onCreate: Loading")
                        binding.apply {
                            pbLoader.visibility = View.VISIBLE
                            rvNews.visibility = View.GONE
                            tvNoInternet.visibility = View.GONE
                        }
                    }

                    is Resource.Success -> {
                        Log.d(TAG, "onCreate: Success ${it.data?.articles}")
                        binding.apply {
                            pbLoader.visibility = View.GONE
                            tvNoInternet.visibility = View.GONE
                            rvNews.visibility = View.VISIBLE
                        }
                        it.data?.articles?.let { articles ->
                            newsDetailViewModel.setNewsHeadlineData(articles, this@NewsFragment)
                        }
                    }

                    is Resource.Error -> {
                        Log.d(TAG, "onCreate: Error")
                        binding.apply {
                            pbLoader.visibility = View.GONE
                            rvNews.visibility = View.GONE
                            tvNoInternet.visibility = View.VISIBLE
                        }
                    }
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

    override fun onDestroyView() {
        super.onDestroyView()
        binding.clNews.removeAllViews()
        newsDetailViewModel.clearData()
    }

    companion object {
        private const val TAG = "NewsFragment"
    }
}