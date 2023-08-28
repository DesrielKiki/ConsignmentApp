package com.desrielkiki.consignmentapp.ui.product

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.desrielkiki.consignmentapp.R
import com.desrielkiki.consignmentapp.databinding.FragmentProductBinding
import com.desrielkiki.consignmentapp.ui.SharedViewModel
import com.desrielkiki.consignmentapp.ui.category.CategoryViewModel

class ProductFragment : Fragment() {

    private val sProductViewModel: ProductViewModel by viewModels()
    private val sharedViewModel: SharedViewModel by viewModels()
    private val categoryViewModel:CategoryViewModel by viewModels()

    private var _binding: FragmentProductBinding? = null
    private val binding get() = _binding!!

    private val adapter: ProductAdapter by lazy { ProductAdapter() }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        _binding = FragmentProductBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = this
        binding.viewModel = sharedViewModel

        categoryViewModel.getCategoryData.observe(viewLifecycleOwner, Observer { categoryList ->
            binding.fabAddProduct.setOnClickListener {
                if (categoryList.isNotEmpty()){
                    findNavController().navigate(R.id.action_productFragment_to_addProductFragment)
                }else{
                    Toast.makeText(requireContext(), "Please add a category first", Toast.LENGTH_SHORT).show()
                }
            }
        })
        setupRecyclerView()

        sProductViewModel.getProductWithCategoryData.observe(viewLifecycleOwner, Observer { data ->
            sharedViewModel.checkIfProductEmpty(data)
            adapter.setData(data)
        })
        return binding.root
    }

    private fun setupRecyclerView() {
        val recyclerView = binding.rvProduct
        recyclerView.adapter = adapter
        recyclerView.layoutManager = GridLayoutManager(requireContext(), 2)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}