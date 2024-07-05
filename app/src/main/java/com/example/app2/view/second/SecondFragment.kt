package com.example.app2.view.second

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.app2.database.AppDatabase
import com.example.app2.database.MainRepository
import com.example.app2.databinding.FragmentSecondBinding
import kotlinx.coroutines.launch

class SecondFragment : Fragment() {
    private var _binding: FragmentSecondBinding? = null

    private val binding: FragmentSecondBinding get() = _binding!!

    private val secondViewModel by viewModels<SecondViewModel>(
        factoryProducer = {
            SecondViewModelFactory(
                repository = MainRepository(
                    AppDatabase.getInstance(
                        requireContext()
                    )
                )
            )
        }
    )

    private val mAdapter by lazy {
        SecondFragmentAdapter(
            data = arrayListOf(),
            listener = secondViewModel::updateSelect,
        )
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSecondBinding.inflate(inflater, container, false)
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
            secondViewModel.imageViewItemsSelected.collect {
                if (it.isNotEmpty()) {
                    mAdapter.addAll(it)
                } else mAdapter.addAll(arrayListOf())
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}