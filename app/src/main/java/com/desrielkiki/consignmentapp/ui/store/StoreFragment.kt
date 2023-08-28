package com.desrielkiki.consignmentapp.ui.store

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.desrielkiki.consignmentapp.databinding.FragmentStoreBinding
import com.desrielkiki.consignmentapp.ui.SharedViewModel
import com.desrielkiki.consignmentapp.ui.deposit.DepositViewModel

class StoreFragment : Fragment() {
    private val storeViewModel: StoreViewModel by viewModels()
    private val depositViewModel: DepositViewModel by viewModels()
    private val sharedViewModel: SharedViewModel by viewModels()

    private var _binding: FragmentStoreBinding? = null
    private val binding get() = _binding!!

    private val adapter: StoreAdapter by lazy { StoreAdapter() }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentStoreBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = this
        binding.viewModel = sharedViewModel


        storeViewModel.getStoreData.observe(viewLifecycleOwner) { data ->
            sharedViewModel.checkIfStoreEmpty(data)
            adapter.setData(data)
        }
        setupRecyclerView()

        return binding.root
    }

    private fun setupRecyclerView() {
        val recyclerView = binding.rvStore
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(requireActivity())
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
