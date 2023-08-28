package com.desrielkiki.consignmentapp.ui.product

import android.app.AlertDialog
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.LiveData
import androidx.lifecycle.map
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.desrielkiki.consignmentapp.R
import com.desrielkiki.consignmentapp.data.ConsignmentDatabase
import com.desrielkiki.consignmentapp.data.entity.CategoryData
import com.desrielkiki.consignmentapp.data.entity.ProductData
import com.desrielkiki.consignmentapp.databinding.FragmentUpdateProductBinding
import com.desrielkiki.consignmentapp.ui.SharedViewModel

class UpdateProductFragment : Fragment() {

    private val args by navArgs<UpdateProductFragmentArgs>()
    private val sProductViewModel: ProductViewModel by viewModels()
    private val sharedViewModel: SharedViewModel by viewModels()
    private var _binding: FragmentUpdateProductBinding? = null
    private val binding get() = _binding!!

    private lateinit var consignmentDatabase: ConsignmentDatabase
    private lateinit var spinnerAdapter: ArrayAdapter<String>
    private lateinit var productCategoriesLiveData: LiveData<List<ProductData>>
    private lateinit var categoriesLiveData: LiveData<List<CategoryData>>
    private lateinit var spinnerData: LiveData<List<String>>

    private var categoryId: Int = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentUpdateProductBinding.inflate(inflater, container, false)
        binding.args = args
        setHasOptionsMenu(true)
        binding.etProductPrice.addTextChangedListener(priceTextWatcher)
        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.update_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_save -> updateProduct()
            R.id.menu_delete -> deleteProduct()
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        binding.etProductPrice.setText(args.productWithCategoryData.productData.productPrice.toString())
        consignmentDatabase = ConsignmentDatabase.getDatabase(requireContext())
        categoriesLiveData = consignmentDatabase.consignmentDao().getCategoryData()
        productCategoriesLiveData = consignmentDatabase.consignmentDao().getProductData()


        spinnerData = categoriesLiveData.map { categoryDataList ->
            categoryDataList.map { categoryData -> categoryData.categoryName }
        }

        //set spinner data
        spinnerData.observe(viewLifecycleOwner) { spinnerItems ->
            if (::spinnerAdapter.isInitialized) {
                spinnerAdapter.clear()
                spinnerAdapter.addAll(spinnerItems)
            } else {
                spinnerAdapter = ArrayAdapter(
                    requireContext(),
                    android.R.layout.simple_spinner_item,
                    spinnerItems
                )
                spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                binding.spProductCategory.adapter = spinnerAdapter
            }
        }
        //fetch data in spinner update
        productCategoriesLiveData.observe(viewLifecycleOwner) { productList ->
            categoriesLiveData.observe(viewLifecycleOwner) { categoryList ->
                val spinnerItems = categoryList.map { category ->
                    category.categoryName
                }

                // Clear dan update data spinnerAdapter
                spinnerAdapter.clear()
                spinnerAdapter.addAll(spinnerItems)

                val selectedCategoryName = args.productWithCategoryData.categoryData?.categoryName
                val selectedCategoryIndex = categoryList.indexOfFirst { category ->
                    category.categoryName == selectedCategoryName
                }
                binding.spProductCategory.setSelection(selectedCategoryIndex)
            }
        }
        binding.spProductCategory.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long,
                ) {
                    //get new category Id
                    val selectedCategoryName = spinnerAdapter.getItem(position)
                    val selectedCategoryData = categoriesLiveData.value?.find {
                        it.categoryName == selectedCategoryName
                    }
                    selectedCategoryData?.let { categoryData ->
                        val selectedCategoryId = categoryData.id
                        categoryId = selectedCategoryId
                    }
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {
                    // Tidak ada item yang dipilih
                }
            }
    }


    private fun updateProduct() {
        val productCategory = categoryId
        val productName = binding.etProductName.text.toString()
        val productPrice = binding.etProductPrice.text.toString()

        try {
            val maxPrice = Long.MAX_VALUE
            val cleanPrice =
                productPrice.replace("[^\\d]".toRegex(), "") // Hapus karakter non-numeric dari harga
            val price = cleanPrice.toLong()

            if (price <= maxPrice) {
                val validation =
                    sProductViewModel.verifyDataFromUser(productName, price)
                if (validation) {
                    val updatedProduct = args.productWithCategoryData.productData.copy(
                        categoryId = productCategory,
                        productName = productName,
                        productPrice = price
                    )
                    sProductViewModel.updateProduct(updatedProduct)
                    Toast.makeText(requireContext(), "succesfully update!", Toast.LENGTH_SHORT)
                        .show()
                    findNavController().navigate(R.id.action_updateProductFragment_to_productFragment)
                } else {
                    Toast.makeText(
                        requireContext(),
                        "Please fill in all the fields",
                        Toast.LENGTH_SHORT
                    )
                        .show()
                }
            } else {
                // Tampilkan pesan kesalahan jika harga terlalu besar
                Toast.makeText(requireContext(), "Price is too large.", Toast.LENGTH_SHORT).show()
            }
        } catch (e: NumberFormatException) {
            // Handle pesan kesalahan jika input harga tidak valid
            Toast.makeText(
                requireContext(),
                "Invalid price format. Enter a valid number.",
                Toast.LENGTH_SHORT
            ).show()
        }
    }


    private fun deleteProduct() {
        val builder = AlertDialog.Builder(requireContext())
        builder.setPositiveButton("yes") { _, _ ->
            sProductViewModel.deleteProduct(args.productWithCategoryData.productData)
            Toast.makeText(
                requireContext(),
                "succesfully removed: '${args.productWithCategoryData.productData.productName}'",
                Toast.LENGTH_SHORT
            ).show()
            findNavController().navigate(R.id.action_updateProductFragment_to_productFragment)
        }
        builder.setNegativeButton("No") { _, _ -> }
        builder.setIcon(R.drawable.ic_warning)
        builder.setTitle("DELETE PRODUCT")
        builder.setMessage("are you sure you want to delete this product?")
        builder.create().show()
    }
    private val priceTextWatcher = object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            // Remove the listener to prevent recursive calls
            binding.etProductPrice.removeTextChangedListener(this)

            // Remove non-numeric characters from the input
            val cleanPrice = s.toString().replace("[^\\d]".toRegex(), "")

            // Format the clean price and set it back to the EditText
            val formattedPrice = sharedViewModel.formatPriceWithSeparator(cleanPrice)
            binding.etProductPrice.setText(formattedPrice)

            // Set the cursor position back to the end of the text
            binding.etProductPrice.setSelection(formattedPrice.length)

            // Add the listener back
            binding.etProductPrice.addTextChangedListener(this)
        }

        override fun afterTextChanged(s: Editable?) {}
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}