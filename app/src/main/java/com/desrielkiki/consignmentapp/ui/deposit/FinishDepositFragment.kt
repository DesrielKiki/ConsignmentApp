package com.desrielkiki.consignmentapp.ui.deposit

import FinishDepositAdapter
import android.app.DatePickerDialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.desrielkiki.consignmentapp.R
import com.desrielkiki.consignmentapp.data.entity.DepositStatus
import com.desrielkiki.consignmentapp.databinding.FragmentFinishDepositBinding
import com.desrielkiki.consignmentapp.ui.SharedViewModel
import java.util.Calendar
import java.util.Date

class FinishDepositFragment : Fragment() {

    private val args by navArgs<FinishDepositFragmentArgs>()
    private var _binding: FragmentFinishDepositBinding? = null
    private val binding get() = _binding!!

    private var depositId: Long = 0L
    private val productAdapter: FinishDepositAdapter by lazy { FinishDepositAdapter() }
    private val depositViewModel: DepositViewModel by viewModels()
    private val sharedViewModel: SharedViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        _binding = FragmentFinishDepositBinding.inflate(inflater, container, false)

        binding.tvFinishDate.setOnClickListener {
            depositViewModel.showFinishDatePicker()
        }

        depositViewModel.selectedFinishDate.observe(viewLifecycleOwner) { finishDate ->
            binding.tvFinishDate.text =
                sharedViewModel.customDateFormat(finishDate ?: Date())
        }

        depositViewModel.isFinishDatePickerVisible.observe(viewLifecycleOwner) { isVisible ->
            if (isVisible) {
                showFinishDatePickerDialog()
            }
        }
        setHasOptionsMenu(true)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        depositViewModel.getDepositProductData.observe(viewLifecycleOwner, Observer {
            filterProduct()
        })
        setupProductRecyclerView()
        binding.tvStoreName.text = args.depositWithStoreData.storeData.storeName.toString()

        productAdapter.onReturnQtyChanged.observe(viewLifecycleOwner) { (productPosition, returnQty) ->
            // Update the returnQty value in the ViewModel when it changes in the adapter
            val updatedProductData = productAdapter.getProductList()[productPosition]
            updatedProductData.depositProductData.returnQty = returnQty
            depositViewModel.updateDepositProduct(updatedProductData.depositProductData)
        }
        super.onViewCreated(view, savedInstanceState)
    }

    private fun showFinishDatePickerDialog() {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        val finishDatePickerDialog = DatePickerDialog(
            requireContext(),
            { _, selectedYear, selectedMonth, selectedDayOfMonth ->
                depositViewModel.onFinishDateSelected(
                    selectedYear,
                    selectedMonth,
                    selectedDayOfMonth
                )
            },
            year, month, day
        )
        finishDatePickerDialog.show()
    }

    private fun filterProduct() {
        depositId = args.depositWithStoreData.depositData.id
        depositViewModel.filterProduct(depositId)
            .observe(viewLifecycleOwner, Observer { productList ->
                productList?.let {
                    productAdapter.setProductData(it)
                    Log.d("filtered product", "filtered product sudah di set")
                }
            })
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.finish_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_finishDeposit -> finishDeposit()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun finishDeposit() {
        val storeName = args.depositWithStoreData.storeData.storeName.toString()
        val depositDate = args.depositWithStoreData.depositData.depositDate
        val finishDate = binding.tvFinishDate.text.toString()
        val validation = depositViewModel.verifyFinishDeposit(storeName, depositDate, finishDate)

        if (validation) {
            args.depositWithStoreData.depositData.depositStatus = DepositStatus.Finish
            args.depositWithStoreData.depositData.finishDate = finishDate

            // Update the deposit data in the ViewModel and database
            depositViewModel.updateDeposit(args.depositWithStoreData.depositData)

            // Get the list of deposit products from the FinishDepositAdapter
            val depositProducts = productAdapter.getProductList()

            // Launch a coroutine to update each deposit product in the database
            depositProducts.forEach { productData ->
                depositViewModel.updateDepositProduct(productData.depositProductData)
            }

            // Update the returnQty values in the adapter based on the updated data from the ViewModel
            depositViewModel.getSubDepositWithProductData.observe(
                viewLifecycleOwner,
                Observer { productList ->
                    productList?.let {
                        productAdapter.setProductData(it)
                    }
                })
            findNavController().navigate(R.id.action_finishDepositFragment_to_depositFragment)
            Toast.makeText(requireContext(), "Successfully update deposit", Toast.LENGTH_SHORT)
                .show()
        } else {
            Toast.makeText(
                requireContext(),
                "Please fill in all the fields",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    private fun setupProductRecyclerView() {
        val recyclerView = binding.rvFinishProduct
        recyclerView.adapter = productAdapter
        recyclerView.layoutManager = LinearLayoutManager(requireActivity())
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}