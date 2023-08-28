package com.desrielkiki.consignmentapp.ui.deposit

import android.app.Application
import android.text.TextUtils
import android.view.View
import android.widget.AdapterView
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.desrielkiki.consignmentapp.data.ConsignmentDatabase
import com.desrielkiki.consignmentapp.data.entity.DepositData
import com.desrielkiki.consignmentapp.data.entity.DepositProductData
import com.desrielkiki.consignmentapp.data.entity.DepositStatus
import com.desrielkiki.consignmentapp.data.entity.relation.DepositWithStoreData
import com.desrielkiki.consignmentapp.data.entity.relation.SubDepositWithProductData
import com.desrielkiki.consignmentapp.data.ConsignmentRepository
import com.desrielkiki.consignmentapp.data.entity.ProductData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.Calendar
import java.util.Date

class DepositViewModel(application: Application) : AndroidViewModel(application) {
    private val consignmentRepository: ConsignmentRepository = ConsignmentRepository(application )
    private val consignmentDatabase: ConsignmentDatabase =
        ConsignmentDatabase.getDatabase(application)

    //datePicker
    private val _isStartDatePickerVisible = MutableLiveData<Boolean>()
    val isStartDatePickerVisible: LiveData<Boolean> = _isStartDatePickerVisible
    private val _isFinishDatePickerVisible = MutableLiveData<Boolean>()
    val isFinishDatePickerVisible: LiveData<Boolean> = _isFinishDatePickerVisible
    private val _selectedStartDate = MutableLiveData<Date>()
    val selectedStartDate: LiveData<Date> = _selectedStartDate
    private val _selectedFinishDate = MutableLiveData<Date>()
    val selectedFinishDate: LiveData<Date> = _selectedFinishDate

    val getDepositData: LiveData<List<DepositData>> = consignmentRepository.getDepositData()
    val getDepositWithStoreData: LiveData<List<DepositWithStoreData>> = consignmentRepository.getDepositWithStoreData()
    val getDepositProductData: LiveData<List<DepositProductData>> =
        consignmentRepository.getSubDepositData()
    val getSubDepositWithProductData: LiveData<List<SubDepositWithProductData>> = consignmentRepository.getSubDepositWithProductData()
    val getDepositWithoutProduct: LiveData<List<DepositData>> = consignmentRepository.getDepositWithoutProductData()
    private val _currentDepositId = MutableLiveData<Long>()
    val currentDepositId: LiveData<Long>
        get() = _currentDepositId

    fun insertDepositAndGetId(depositData: DepositData) {
        viewModelScope.launch {
            val newDepositId = consignmentRepository.insertAndGetIdDeposit(depositData)
            _currentDepositId.value = newDepositId
        }
    }

    fun insertDepositProduct(depositProductData: DepositProductData) {
        consignmentRepository.insertSubDeposit(depositProductData)
    }

    fun updateDeposit(depositData: DepositData) {
        consignmentRepository.updateDeposit(depositData)
    }

    fun updateDepositProduct(depositProductData: DepositProductData) {
        viewModelScope.launch(Dispatchers.IO) {
            consignmentRepository.updateSubDeposit(depositProductData)
        }
    }

    fun deleteDepositAndProduct(depositId: Long) {
        consignmentRepository.deleteDepositAndProduct(depositId)
    }

    fun verifySubDepositData(qty: String): Boolean {
        return if (TextUtils.isEmpty(qty)) {
            false
        } else !(qty.isEmpty())
    }

    fun verifyFinishDeposit(store: String, depositDate: String, finishDate: String): Boolean {
        return if (TextUtils.isEmpty(store) || TextUtils.isEmpty(depositDate) || TextUtils.isEmpty(
                finishDate
            )
        ) {
            false
        } else !(store.isEmpty() || depositDate.isEmpty() || finishDate.isEmpty())
    }

    fun verifyDataFromUser(depositDate: String): Boolean {
        return if ( TextUtils.isEmpty(depositDate)) {
            false
        } else !(depositDate.isEmpty())
    }

    fun showStartDatePicker() {
        _isStartDatePickerVisible.value = true
    }

    fun showFinishDatePicker() {
        _isFinishDatePickerVisible.value = true
    }

    fun onStartDateSelected(year: Int, month: Int, dayOfMonth: Int) {
        val calendar = Calendar.getInstance().apply {
            set(year, month, dayOfMonth)
        }
        val selectedDate = calendar.time
        _selectedStartDate.value = selectedDate
        _isStartDatePickerVisible.value = false
    }

    fun onFinishDateSelected(year: Int, month: Int, dayOfMonth: Int) {
        val calendar = Calendar.getInstance().apply {
            set(year, month, dayOfMonth)
        }
        val selectedDate = calendar.time
        _selectedFinishDate.value = selectedDate
        _isFinishDatePickerVisible.value = false
    }

    val listener: AdapterView.OnItemSelectedListener = object :
        AdapterView.OnItemSelectedListener {
        override fun onNothingSelected(parent: AdapterView<*>?) {

        }

        override fun onItemSelected(
            parent: AdapterView<*>?,
            view: View?,
            position: Int,
            id: Long,
        ) {
        }
    }

    fun filterProduct(depositId: Long): LiveData<List<SubDepositWithProductData>> {
        return consignmentRepository.filterProduct(depositId)
    }

    fun filterDeposit(storeId: Int): LiveData<List<DepositWithStoreData>> {
        return consignmentRepository.filterDepositByStore(storeId)
    }

    fun parseStatus(depositStatus: String): DepositStatus {
        return when (depositStatus) {
            "deposit" -> {
                DepositStatus.Deposit
            }

            "finish" -> {
                DepositStatus.Finish
            }

            else -> DepositStatus.Deposit
        }
    }

    fun calculateDepositRevenue(
        depositProducts: List<DepositProductData>,
        productDataList: List<ProductData>,
        depositID: Long
    ): Double {
        var totalRevenue = 0.0

        val relevantDepositProducts = depositProducts.filter { it.depositId == depositID }

        // Calculate total revenue from each deposit product
        for (depositProduct in relevantDepositProducts) {
            val productData = productDataList.find { it.id == depositProduct.productId }
            if (productData != null) {

                val soldQty = depositProduct.qty - depositProduct.returnQty
                totalRevenue += (soldQty * productData.productPrice.toDouble())
               /* val returnQty = depositProduct.returnQty
                totalRevenue += (returnQty * productData.productPrice.toDouble())*/
            }
        }
        return totalRevenue
    }
}
