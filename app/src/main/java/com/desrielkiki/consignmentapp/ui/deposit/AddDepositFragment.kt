package com.desrielkiki.consignmentapp.ui.deposit

import android.app.DatePickerDialog
import android.os.Bundle
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
import com.desrielkiki.consignmentapp.data.ConsignmentDatabase
import com.desrielkiki.consignmentapp.data.entity.DepositData
import com.desrielkiki.consignmentapp.data.entity.StoreData
import com.desrielkiki.consignmentapp.databinding.FragmentAddDepositBinding
import com.desrielkiki.consignmentapp.ui.SharedViewModel
import java.util.Calendar
import java.util.Date

class AddDepositFragment : Fragment() {

    private var _binding: FragmentAddDepositBinding? = null
    private val binding get() = _binding!!
    private var depositId: Long = 0L

    private val depositViewModel: DepositViewModel by viewModels()
    private val sharedViewModel: SharedViewModel by viewModels()
    private lateinit var consignmentDatabase: ConsignmentDatabase
    private lateinit var storeSpinnerAdapter: ArrayAdapter<String>
    private lateinit var storeLiveData: LiveData<List<StoreData>>
    private lateinit var storeSpinnerData: LiveData<List<String>>

    private var storeId: Int = 0
    private var recreateFragment: Boolean = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        _binding = FragmentAddDepositBinding.inflate(inflater, container, false)
        binding.depositViewModel = depositViewModel
        binding.lifecycleOwner = this
        binding.tvStartDate.setOnClickListener {
            depositViewModel.showStartDatePicker()
        }
        depositViewModel.selectedStartDate.observe(viewLifecycleOwner) { startDate ->
            val formattedDate = sharedViewModel.customDateFormat(startDate ?: Date())
            binding.tvStartDate.text = formattedDate
        }

        depositViewModel.isStartDatePickerVisible.observe(viewLifecycleOwner) { isVisible ->
            if (isVisible) {
                showStartDatePickerDialog()
            }
        }

        binding.btnNext.setOnClickListener {
            insertDataToDb()
        }
        setDefaultSpinner()
        return binding.root
    }

    private fun showStartDatePickerDialog() {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)
        val startDatePickerDialog = DatePickerDialog(
            requireContext(),
            { _, selectedYear, selectedMonth, selectedDayOfMonth ->
                depositViewModel.onStartDateSelected(
                    selectedYear,
                    selectedMonth,
                    selectedDayOfMonth
                )
            },
            year, month, day
        )
        startDatePickerDialog.show()
    }

    private fun setDefaultSpinner() {
        consignmentDatabase = ConsignmentDatabase.getDatabase(requireContext())
        storeLiveData = consignmentDatabase.consignmentDao().getStoreData()

        storeSpinnerData = storeLiveData.map { storeDataList ->
            storeDataList.map { storeData -> storeData.storeName }
        }



        storeSpinnerData.observe(viewLifecycleOwner) { spinnerItems ->
            if (::storeSpinnerAdapter.isInitialized) {
                storeSpinnerAdapter.clear()
                storeSpinnerAdapter.addAll(spinnerItems)
            } else {
                val updatedSpinnerItems = spinnerItems.toMutableList()
                updatedSpinnerItems.add(0, "Select Store")

                storeSpinnerAdapter = object : ArrayAdapter<String>(
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

                storeSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                binding.spStore.adapter = storeSpinnerAdapter
            }
        }
        binding.spStore.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long,
                ) {
                    // Pastikan posisi yang dipilih valid (bukan posisi hint "Select Category")
                    if (position > 0) {
                        val selectedCategoryName = storeSpinnerAdapter.getItem(position)
                        val selectedCategoryData = storeLiveData.value?.find {
                            it.storeName == selectedCategoryName
                        }

                        // selectedCategoryData sekarang berisi data Category yang sesuai dengan categoryName yang dipilih
                        selectedCategoryData?.let { storeData ->
                            val selectedStoreId = storeData.id
                            storeId = selectedStoreId
                            // Lakukan apa pun yang Anda inginkan dengan selectedCategoryId
                            // Misalnya, simpan nilai ini untuk digunakan nanti.
                            Log.d("YourFragment", "Selected Category ID: $selectedStoreId")
                        }
                    }
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {
                    // Tidak ada item yang dipilih
                }
            }
    }

    private fun insertDataToDb() {
        val selectedStore = binding.spStore.selectedItem as? String
        if (selectedStore == null || selectedStore == "Select Store") {
            Toast.makeText(
                requireContext(),
                "store must be filled!",
                Toast.LENGTH_SHORT
            ).show()
        } else {

            val mStore = storeId
            val mDepositDate = binding.tvStartDate.text.toString()
            val validation = depositViewModel.verifyDataFromUser(mDepositDate)
            if (validation) {
                val newData = DepositData(
                    0,
                    mStore,
                    mDepositDate,
                    depositViewModel.parseStatus("")
                )
                depositViewModel.insertDepositAndGetId(newData)

                depositViewModel.currentDepositId.observe(viewLifecycleOwner) { newDepositId ->
                    if (newDepositId != null) {
                        depositId = newDepositId
                        Toast.makeText(
                            requireContext(),
                            "Please add a product",
                            Toast.LENGTH_SHORT
                        ).show()
                        val action =
                            AddDepositFragmentDirections.actionAddDepositFragmentToAddDepositProductFragment(
                                depositId
                            )
                        findNavController().navigate(action)
                    }
                }
            } else {
                Toast.makeText(
                    requireContext(),
                    "Please fill in all the fields",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }
}


