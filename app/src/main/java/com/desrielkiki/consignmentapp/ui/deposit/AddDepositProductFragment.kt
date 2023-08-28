package com.desrielkiki.consignmentapp.ui.deposit

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.TextView
import android.widget.Toast
import androidx.core.view.isEmpty
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.map
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.desrielkiki.consignmentapp.R
import com.desrielkiki.consignmentapp.data.ConsignmentDatabase
import com.desrielkiki.consignmentapp.data.entity.DepositProductData
import com.desrielkiki.consignmentapp.data.entity.ProductData
import com.desrielkiki.consignmentapp.databinding.FragmentAddDepositProductBinding

class AddDepositProductFragment : Fragment() {

    private var _binding: FragmentAddDepositProductBinding? = null
    private val binding get() = _binding!!

    private var btnAddIsClicked = false
    private val depositViewModel: DepositViewModel by viewModels()

    private lateinit var consignmentDatabase: ConsignmentDatabase
    private lateinit var productSpinnerAdapter: ArrayAdapter<String>
    private lateinit var productLiveData: LiveData<List<ProductData>>
    private lateinit var productSpinnerData: LiveData<List<String>>

    private val productAdapter: DepositProductAdapter by lazy { DepositProductAdapter() }
    private val args: AddDepositProductFragmentArgs by navArgs()
    private var depositId: Long = 0L
    private var selectedProductId: Int = 0


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        _binding = FragmentAddDepositProductBinding.inflate(inflater, container, false)

        binding.btnSave.setOnClickListener {
            buttonSaveClicked()
        }

        binding.btnAddProduct.setOnClickListener {
            btnAddIsClicked = true
            insertSubDepositData()
        }
        setupProductRecyclerView()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        //set spinner
        consignmentDatabase = ConsignmentDatabase.getDatabase(requireContext())
        productLiveData = consignmentDatabase.consignmentDao().getProductData()

        productSpinnerData = productLiveData.map { productDataList ->
            productDataList.map { productData -> productData.productName }
        }
        productSpinnerData.observe(viewLifecycleOwner) { spinnerItems ->
            if (::productSpinnerAdapter.isInitialized) {
                productSpinnerAdapter.clear()
                productSpinnerAdapter.addAll(spinnerItems)
            } else {
                val updatedSpinnerItems = spinnerItems.toMutableList()
                updatedSpinnerItems.add(0, "select Product")

                productSpinnerAdapter = object : ArrayAdapter<String>(
                    requireContext(),
                    android.R.layout.simple_spinner_dropdown_item,
                    updatedSpinnerItems
                ) {
                    override fun isEnabled(position: Int): Boolean {
                        return position != 0
                    }

                    override fun getDropDownView(
                        position: Int,
                        convertView: View?,
                        parent: ViewGroup,
                    ): View {
                        val view = super.getDropDownView(position, convertView, parent)
                        val textView = view.findViewById<TextView>(android.R.id.text1)
                        textView.isEnabled = position != 0
                        return view
                    }
                }
                productSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                binding.spProduct.adapter = productSpinnerAdapter
            }
        }
        binding.spProduct.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    // Pastikan posisi yang dipilih valid (bukan posisi hint "Select Category")
                    if (position > 0) {
                        val selectedProductName = productSpinnerAdapter.getItem(position)
                        val selectedProductData = productLiveData.value?.find {
                            it.productName == selectedProductName
                        }

                        // selectedCategoryData sekarang berisi data Category yang sesuai dengan categoryName yang dipilih
                        selectedProductData?.let { productData ->
                            val selectedProductId = productData.id
                            this@AddDepositProductFragment.selectedProductId = selectedProductId
                        }
                    }
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {
                    // Tidak ada item yang dipilih
                }
            }
    }

    private fun buttonSaveClicked() {
        val qtyText = binding.etQty.text.toString()
        val selectedProduct = binding.spProduct.selectedItem as? String

        if (qtyText.isNotEmpty() && selectedProduct != null && selectedProduct != "select Product") {
            insertSubDepositData()
            findNavController().navigate(R.id.action_addDepositProductFragment_to_depositFragment)
        } else if (selectedProduct == "select Product") {
            Toast.makeText(
                requireContext(),
                "Please select a product",
                Toast.LENGTH_SHORT
            ).show()
        } else {
            if (binding.rvAddedProduct.isEmpty()) {
                Toast.makeText(
                    requireContext(),
                    "please add a product first",
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                findNavController().navigate(R.id.action_addDepositProductFragment_to_depositFragment)
            }
        }
    }

    private fun insertSubDepositData() {
        val product = selectedProductId
        val qty = binding.etQty.text.toString()
        val depositId = args.depositId
        val validation = depositViewModel.verifySubDepositData( qty)
        val selectedProduct = binding.spProduct.selectedItem as? String


        if (selectedProduct == null || selectedProduct == "select Product") {
            Toast.makeText(
                requireContext(),
                "product must be filled",
                Toast.LENGTH_SHORT
            ).show()
        } else if (validation) {
            val newData = DepositProductData(
                0,
                depositId,
                product,
                qty.toInt(),
                0
            )
            depositViewModel.insertDepositProduct(newData)
            if (btnAddIsClicked) {
                binding.etQty.text.clear()
                filterProduct()
            }
            Toast.makeText(requireContext(), "Successfully added!", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(
                requireContext(),
                "Please fill in all the fields",
                Toast.LENGTH_SHORT
            )
                .show()
        }
    }

    private fun filterProduct() {
        depositId = args.depositId
        depositViewModel.filterProduct(depositId)
            .observe(viewLifecycleOwner, Observer { productList ->
                productList?.let {
                    productAdapter.setProductData(it)
                }
            })
    }

    private fun setupProductRecyclerView() {
        val recyclerView = binding.rvAddedProduct
        recyclerView.adapter = productAdapter
        recyclerView.layoutManager = LinearLayoutManager(requireActivity())
    }

    override fun onDestroyView() {

        super.onDestroyView()
        _binding = null
    }
}