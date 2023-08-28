package com.desrielkiki.consignmentapp.ui.category

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.desrielkiki.consignmentapp.R
import com.desrielkiki.consignmentapp.data.entity.CategoryData
import com.desrielkiki.consignmentapp.databinding.FragmentUpdateCategoryBinding

class UpdateCategoryFragment : Fragment() {

    private val args by navArgs<UpdateCategoryFragmentArgs>()
    private var _binding: FragmentUpdateCategoryBinding? = null
    private val binding get() = _binding!!
    private val categoryViewModel: CategoryViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentUpdateCategoryBinding.inflate(inflater, container, false)
        binding.args = args
        setHasOptionsMenu(true)

        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.update_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_delete -> confirmCategoryDelete()
            R.id.menu_save -> saveChanges()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun saveChanges() {
        val categoryName = binding.etCategoryName.text.toString()
        val validation = categoryViewModel.verifyDataFromUser(categoryName)
        if (validation) {
            val updatedCategory = CategoryData(
                args.currentCategory.id, categoryName
            )
            categoryViewModel.updateCategory(updatedCategory)
            Toast.makeText(requireContext(), "succesfully update!", Toast.LENGTH_SHORT).show()
            findNavController().navigate(R.id.action_updateCategoryFragment_to_categoryFragment)
        } else {
            Toast.makeText(requireContext(), "Please fill in all the fields", Toast.LENGTH_SHORT).show()
        }
    }

    private fun confirmCategoryDelete() {
        val builder = AlertDialog.Builder(requireContext())
        builder.setPositiveButton("yes") { _, _ ->
            categoryViewModel.deleteCategory(args.currentCategory)
            Toast.makeText(
                requireContext(),
                "succesfully removed: '${args.currentCategory.categoryName}'",
                Toast.LENGTH_SHORT
            ).show()
            findNavController().navigate(R.id.action_updateCategoryFragment_to_categoryFragment)
        }
        builder.setNegativeButton("No") { _, _ -> }
        builder.setIcon(R.drawable.ic_warning)
        builder.setTitle("Delete '${args.currentCategory.categoryName}'")
        builder.setMessage("are you sure you want to delete this category?")
        builder.create().show()
    }
}