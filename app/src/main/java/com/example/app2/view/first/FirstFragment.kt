package com.example.app2.view.first

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
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
        ImageAdapter(data = arrayListOf(), listener = firstViewModel::updateSelect, tryAgain = {
            firstViewModel.fetchData{
                if (!it) Toast.makeText(
                    context,
                    getString(R.string.load_failed_try_again),
                    Toast.LENGTH_SHORT
                ).show()
            }
        })
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {

        _binding = FragmentFirstBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        firstViewModel.fetchData {
            if (!it) Toast.makeText(
                context,
                getString(R.string.load_failed_try_again),
                Toast.LENGTH_SHORT
            ).show()
        }

        initData()

        initView()

        initListener()
    }

    private fun initData() {
        lifecycleScope.launch {
            firstViewModel.imageViewItems.collect {
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
                    if (!recyclerView.canScrollVertically(1) && dy > 0) {
                        firstViewModel.loadMore()
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