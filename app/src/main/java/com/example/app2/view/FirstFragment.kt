package com.example.app2.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.OnScrollListener
import com.example.app2.R
import com.example.app2.activity.ViewModelFactory
import com.example.app2.activity.activity1.MainViewModel
import com.example.app2.adapter.ImageAdapter
import com.example.app2.database.AppDatabase
import com.example.app2.database.MainRepository
import com.example.app2.databinding.FragmentFirstBinding
import com.example.app2.extention.launchWhenStarted
import kotlinx.coroutines.launch

class FirstFragment : Fragment() {
    private val binding: FragmentFirstBinding by lazy { FragmentFirstBinding.inflate(layoutInflater) }

    private lateinit var activityViewModel: MainViewModel

    private val mAdapter by lazy {
        ImageAdapter(data = arrayListOf(), listener = activityViewModel::updateSelect)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
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

        activityViewModel.fetchData()

        initData()

        initView()

        initListener()
    }

    private fun initData() {
        lifecycleScope.launch {
            activityViewModel.imageViewItems.collect {
                launchWhenStarted {
                    mAdapter.addAll(it)
                }
            }
        }
    }

    private fun initView() {
        binding.apply {
            rvImage.adapter = mAdapter
            rvImage.addOnScrollListener(object : OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)
                    if (!recyclerView.canScrollVertically(1)) {
                        activityViewModel.loadMore()
                    }
                }
            })
        }
    }

    private fun initListener() {
        binding.apply {
            btnSecondFragment.setOnClickListener {
                findNavController().navigate(R.id.action_firstFragment_to_secondFragment)
            }
        }
    }
}