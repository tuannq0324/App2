package com.example.app2.activity.activity1

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.OnScrollListener
import com.example.app2.activity.ViewModelFactory
import com.example.app2.adapter.ImageAdapter
import com.example.app2.database.AppDatabase
import com.example.app2.database.MainRepository
import com.example.app2.databinding.ActivityMainBinding
import com.example.app2.extention.launchWhenStarted
import com.example.app2.activity.activity2.MainActivity2
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }
    private val viewModel by lazy {
        ViewModelProvider(
            this, ViewModelFactory(MainRepository(AppDatabase.getInstance(this)))
        )[MainViewModel::class.java]
    }
    private val mAdapter by lazy {
        ImageAdapter(data = arrayListOf(), listener = viewModel::updateSelect)
    }

    private fun initData() {
        lifecycleScope.launch {
            viewModel.imageViewItems.collect {
                launchWhenStarted {
                    mAdapter.addAll(it)
                }
            }
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        viewModel.fetchData()

        initData()

        initRecyclerView()

        initListener()
    }

    private fun initRecyclerView() {
        binding.apply {
            rvImage.adapter = mAdapter
            rvImage.addOnScrollListener(object : OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)
                    if (!recyclerView.canScrollVertically(1)) {
                        viewModel.loadMore()
                    }
                }
            })
        }
    }

    private fun initListener() {
        binding.apply {
            btnActivity2.setOnClickListener {
                startActivity(Intent(this@MainActivity, MainActivity2::class.java))
            }
        }
    }
}