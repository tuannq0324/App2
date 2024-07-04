package com.example.app2.activity.activity2

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.example.app2.activity.ViewModelFactory
import com.example.app2.adapter.ImageAdapter
import com.example.app2.database.AppDatabase
import com.example.app2.database.MainRepository
import com.example.app2.databinding.ActivityMain2Binding
import kotlinx.coroutines.launch

class MainActivity2 : AppCompatActivity() {
    private val binding by lazy { ActivityMain2Binding.inflate(layoutInflater) }

    private val viewModel by lazy {
        ViewModelProvider(
            this, ViewModelFactory(MainRepository(AppDatabase.getInstance(this)))
        )[Activity2ViewModel::class.java]
    }
    private val mAdapter by lazy {
        ImageAdapter(data = arrayListOf(), listener = viewModel::updateSelect)
    }

    private fun initData() {
        lifecycleScope.launch {
            viewModel.imageViewItems.collect {
                if (it.isNotEmpty()) {
                    mAdapter.addAll(it)
                } else mAdapter.addAll(arrayListOf())
            }
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        initData()

        binding.apply {
            rvImage.adapter = mAdapter
        }
    }
}