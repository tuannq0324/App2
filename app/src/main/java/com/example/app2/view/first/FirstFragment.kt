package com.example.app2.view.first

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.OnScrollListener
import com.example.app2.R
import com.example.app2.database.AppDatabase
import com.example.app2.database.MainRepository
import com.example.app2.databinding.FragmentFirstBinding
import com.example.app2.utils.extention.launchWhenStarted
import com.example.app2.view.adapter.ImageAdapter
import kotlinx.coroutines.launch

class FirstFragment : Fragment() {

    private var _binding: FragmentFirstBinding? = null

    private val binding: FragmentFirstBinding get() = _binding!!

    private val firstViewModel by viewModels<FirstViewModel>(
        factoryProducer = {
            FirstViewModelFactory(repository = MainRepository(AppDatabase.getInstance(requireContext())))
        }
    )

    private val mAdapter by lazy {
        ImageAdapter(data = arrayListOf(), listener = firstViewModel::updateSelect)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {

        _binding = FragmentFirstBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        firstViewModel.fetchData()

        initData()

        initView()

        initListener()
    }

    private fun initData() {
        lifecycleScope.launch {
            firstViewModel.imageViewItems.collect {
                launchWhenStarted {
                    mAdapter.addAll(it)
                    binding.rvImage.postDelayed({
                        mAdapter.removeLoadMore()
                    },1000L)
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
                        firstViewModel.loadMore()

                        mAdapter.addLoadMore()
                        //timeout
                        rvImage.postDelayed({
                            mAdapter.removeLoadMore()
                        }, 5000L)
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

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null

    }
}