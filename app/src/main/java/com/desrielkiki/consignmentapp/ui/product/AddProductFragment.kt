package com.desrielkiki.consignmentapp.ui.product

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.LiveData
import androidx.lifecycle.map
import androidx.navigation.fragment.findNavController
import com.desrielkiki.consignmentapp.R
import com.desrielkiki.consignmentapp.data.ConsignmentDatabase
import com.desrielkiki.consignmentapp.data.entity.CategoryData
import com.desrielkiki.consignmentapp.data.entity.ProductData
import com.desrielkiki.consignmentapp.databinding.FragmentAddProductBinding
import com.desrielkiki.consignmentapp.ui.SharedViewModel

class AddProductFragment : Fragment() {

    private var _binding: FragmentAddProductBinding? = null
    private val binding get() = _binding!!

    private val mProductViewModel: ProductViewModel by viewModels()
    private val sharedViewModel: SharedViewModel by viewModels()


    private lateinit var spinnerAdapter: ArrayAdapter<String>
    private lateinit var database: ConsignmentDatabase
    private lateinit var categoriesLiveData: LiveData<List<CategoryData>>
    private lateinit var spinnerData: LiveData<List<String>>
    private var categoryId: Int = 0


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentAddProductBinding.inflate(inflater, container, false)

        binding.btnAddProduct.setOnClickListener {
            insertDataToDb()
        }

        binding.etProductPrice.addTextChangedListener(priceTextWatcher)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        database = ConsignmentDatabase.getDatabase(requireContext())
        categoriesLiveData = database.consignmentDao().getCategoryData()

        spinnerData = categoriesLiveData.map { categoryDataList ->
            categoryDataList.map { categoryData -> categoryData.categoryName }
        }

        spinnerData.observe(viewLifecycleOwner) { spinnerItems ->
            if (::spinnerAdapter.isInitialized) {
                spinnerAdapter.clear()
                spinnerAdapter.addAll(spinnerItems)
            } else {
                val updatedSpinnerItems = spinnerItems.toMutableList()
                updatedSpinnerItems.add(0, "Select Category")

                spinnerAdapter = object : ArrayAdapter<String>(
                    requireContext(),
                    android.R.layout.simple_spinner_dropdown_item,
                    updatedSpinnerItems
                ) {
                    override fun isEnabled(position: Int): Boolean {
                        // Disable the first item (hint)
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
                spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                binding.spProductCategory.adapter = spinnerAdapter
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
                    // Pastikan posisi yang dipilih valid (bukan posisi hint "Select Category")
                    if (position > 0) {
                        val selectedCategoryName = spinnerAdapter.getItem(position)
                        val selectedCategoryData = categoriesLiveData.value?.find {
                            it.categoryName == selectedCategoryName
                        }

                        // selectedCategoryData sekarang berisi data Category yang sesuai dengan categoryName yang dipilih
                        selectedCategoryData?.let { categoryData ->
                            val selectedCategoryId = categoryData.id
                            categoryId = selectedCategoryId
                        }
                    }
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {
                    // Tidak ada item yang dipilih
                }
            }
    }

    private fun insertDataToDb() {
        val selectedCategory = binding.spProductCategory.selectedItem as? String
        if (selectedCategory == null || selectedCategory == "Select Category") {
            Toast.makeText(requireContext(), "Category must be selected!", Toast.LENGTH_SHORT).show()
        } else {
            val pCategory = categoryId
            val pName = binding.etProductName.text.toString()
            val pPrice = binding.etProductPrice.text.toString()

            try {
                val maxPrice = Long.MAX_VALUE
                val cleanPrice = pPrice.replace("[^\\d]".toRegex(), "") // Hapus karakter non-numeric dari harga
                val price = cleanPrice.toLong()

                if (price <= maxPrice) {
                    val validation = mProductViewModel.verifyDataFromUser(pName, price) // Ganti cleanPrice dengan price
                    if (validation) {
                        val newData = ProductData(
                            0,
                            pCategory,
                            pName,
                            price
                        )
                        mProductViewModel.insertProduct(newData)
                        Toast.makeText(requireContext(), "Successfully added!", Toast.LENGTH_SHORT).show()
                        findNavController().navigate(R.id.action_addProductFragment_to_productFragment)
                    } else {
                        Toast.makeText(requireContext(), "Please fill in all the fields", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    // Tampilkan pesan kesalahan jika harga terlalu besar
                    Toast.makeText(requireContext(), "Price is too large.", Toast.LENGTH_SHORT).show()
                }
            } catch (e: NumberFormatException) {
                // Handle pesan kesalahan jika input harga tidak valid
                Toast.makeText(requireContext(), "Invalid price format. Enter a valid number.", Toast.LENGTH_SHORT).show()
            }
        }
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

