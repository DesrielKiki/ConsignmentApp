package com.desrielkiki.consignmentapp.ui.deposit

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.desrielkiki.consignmentapp.R
import com.desrielkiki.consignmentapp.databinding.FragmentDepositBinding
import com.desrielkiki.consignmentapp.ui.SharedViewModel
import com.desrielkiki.consignmentapp.ui.product.ProductViewModel
import com.desrielkiki.consignmentapp.ui.store.StoreViewModel

class DepositFragment : Fragment() {

    private var _binding: FragmentDepositBinding? = null
    private val binding get() = _binding!!

    private val adapter: DepositAdapter by lazy { DepositAdapter() }
    private val depositViewModel: DepositViewModel by viewModels()
    private val storeViewModel: StoreViewModel by viewModels()
    private val productViewModel: ProductViewModel by viewModels()
    private val sharedViewModel: SharedViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentDepositBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = this
        binding.viewModel = sharedViewModel

        storeViewModel.getStoreData.observe(viewLifecycleOwner, Observer { storeData ->
            productViewModel.getProductData.observe(viewLifecycleOwner, Observer { productData ->
                binding.fabAddDeposit.setOnClickListener{
                    if (storeData.isNotEmpty() && productData.isNotEmpty()){
                        findNavController().navigate(R.id.action_depositFragment_to_addDepositFragment)
                    }else if (storeData.isEmpty() && productData.isEmpty()){
                        Toast.makeText(requireContext(), "Please add product and store", Toast.LENGTH_SHORT).show()
                    }else if (productData.isEmpty()){
                        Toast.makeText(requireContext(), "Please add product first", Toast.LENGTH_SHORT).show()
                    }else if (storeData.isEmpty()){
                        Toast.makeText(requireContext(), "Please add store first", Toast.LENGTH_SHORT).show()
                    }
                }
            })
        })
        depositViewModel.getDepositWithStoreData.observe(viewLifecycleOwner, Observer { data ->
            sharedViewModel.checkIfDepositEmpty(data)
            adapter.setDepositData(data)
        })
        setupRecyclerView()

        return binding.root
    }

    private fun setupRecyclerView() {
        val recyclerView = binding.rvDeposit
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(requireActivity())
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

