package com.desrielkiki.consignmentapp.ui.category

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import com.desrielkiki.consignmentapp.data.entity.CategoryData
import com.desrielkiki.consignmentapp.databinding.FragmentAddCategoryBinding
import com.desrielkiki.consignmentapp.databinding.FragmentCategoryBinding
import com.desrielkiki.consignmentapp.ui.SharedViewModel
import com.google.android.material.bottomsheet.BottomSheetDialog

class CategoryFragment : Fragment() {

    private var _binding: FragmentCategoryBinding? = null
    private val binding get() = _binding!!


    private lateinit var addCategoryBinding: FragmentAddCategoryBinding
    private lateinit var bottomSheetDialog: BottomSheetDialog

    private val categoryViewModel: CategoryViewModel by viewModels ()
    private val sharedViewModel: SharedViewModel by viewModels()
    private val adapter: CategoryAdapter by lazy { CategoryAdapter() }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentCategoryBinding.inflate(inflater,container, false)
        binding.lifecycleOwner = this
        binding.viewModel = sharedViewModel

        setupRecyclerView()
        binding.fabAddCategory.setOnClickListener{
            openCategoryFragment()
        }

        categoryViewModel.getCategoryData.observe(viewLifecycleOwner, Observer{ data ->
            sharedViewModel.checkIfCategoryEmpty(data)
            adapter.setData(data)
        })
        return binding.root
    }

    private fun openCategoryFragment() {
        bottomSheetDialog = BottomSheetDialog(requireContext())
        addCategoryBinding = FragmentAddCategoryBinding.inflate(layoutInflater)
        bottomSheetDialog.setContentView(addCategoryBinding.root)

        addCategoryBinding.btnAddCategory.setOnClickListener{
            insertDataToDb()
        }
        bottomSheetDialog.show()
    }

    private fun insertDataToDb() {
        val cName = addCategoryBinding.etCategoryName.text.toString()

        val validation = categoryViewModel.verifyDataFromUser(cName)
        if (validation) {
            val newData = CategoryData(
                0, cName
            )
            categoryViewModel.insertCategory(newData)
            Toast.makeText(requireContext(), "Succesfully added!", Toast.LENGTH_SHORT).show()
            bottomSheetDialog.dismiss()

        } else {
            Toast.makeText(requireContext(), "Please fill in all the fields", Toast.LENGTH_SHORT).show()
        }

    }

    private fun setupRecyclerView() {
        val recyclerView = binding.rvCategory
        recyclerView.adapter = adapter
        recyclerView.layoutManager = GridLayoutManager(context,2)
    }
}