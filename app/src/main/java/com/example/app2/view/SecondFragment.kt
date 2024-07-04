package com.example.app2.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.example.app2.activity.ViewModelFactory
import com.example.app2.activity.activity1.MainViewModel
import com.example.app2.adapter.ImageAdapter
import com.example.app2.database.AppDatabase
import com.example.app2.database.MainRepository
import com.example.app2.databinding.FragmentSecondBinding
import kotlinx.coroutines.launch

class SecondFragment : Fragment() {
    private val binding: FragmentSecondBinding by lazy { FragmentSecondBinding.inflate(layoutInflater) }

    private lateinit var activityViewModel: MainViewModel

    private val mAdapter by lazy {
        ImageAdapter(data = arrayListOf(), listener = activityViewModel::updateSelect)
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        activity?.let {
            activityViewModel = ViewModelProvider(
                it, ViewModelFactory(MainRepository(AppDatabase.getInstance(it)))
            )[MainViewModel::class.java]
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initView()

        initData()
    }

    private fun initView() {
        binding.apply {
            rvImage.adapter = mAdapter
        }
    }

    private fun initData() {
        lifecycleScope.launch {
            activityViewModel.imageViewItemsSelected.collect {
                if (it.isNotEmpty()) {
                    mAdapter.addAll(it)
                } else mAdapter.addAll(arrayListOf())
            }
        }
    }

}