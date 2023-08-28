package com.desrielkiki.consignmentapp.ui.home

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.desrielkiki.consignmentapp.data.entity.DepositData
import com.desrielkiki.consignmentapp.data.entity.DepositProductData
import com.desrielkiki.consignmentapp.data.entity.DepositStatus
import com.desrielkiki.consignmentapp.data.entity.ProductData
import com.desrielkiki.consignmentapp.data.entity.relation.DepositWithStoreData
import com.desrielkiki.consignmentapp.data.ConsignmentRepository
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class HomeViewModel(application: Application) : AndroidViewModel(application) {
    private val consignmentRepository: ConsignmentRepository = ConsignmentRepository(application )

    val getDepositData: LiveData<List<DepositData>> = consignmentRepository.getDepositData()
    private val getDepositProductData: LiveData<List<DepositProductData>> =
        consignmentRepository.getSubDepositData()
    private val getProductData: LiveData<List<ProductData>> = consignmentRepository.getProductData()

    fun filterUnfinishedDeposit(deposit: DepositStatus): LiveData<List<DepositWithStoreData>> {
        return consignmentRepository.filterUnfinishedDeposit(deposit)
    }

    private fun calculateDepositRevenue(
        depositProducts: List<DepositProductData>,
        productDataList: List<ProductData>,

        ): Double {
        var totalRevenue = 0.0
        // Calculate total revenue from each deposit product
        for (depositProduct in depositProducts) {
            val productData = productDataList.find { it.id == depositProduct.productId}
            if (productData != null) {
                val soldQty = depositProduct.qty - depositProduct.returnQty
                totalRevenue += (soldQty * productData.productPrice.toDouble())
            }
        }
        return totalRevenue
    }
    fun calculateTotalFinishDepositRevenue(): Double{
        var totalRevenue = 0.0
        getDepositProductData.observeForever { depositProducts ->
            getProductData.observeForever { productDataList ->
                // Filter data deposit yang hanya sudah selesai
                val finishedDeposits = filterFinishedDeposits(getDepositData.value ?: emptyList())

                // Mendapatkan daftar DepositProductData berdasarkan deposit yang sudah selesai
                val depositProductsForFinished = mutableListOf<DepositProductData>()
                for (deposit in finishedDeposits) {
                    val relatedProducts = getRelatedDepositProducts(deposit, depositProducts)
                    depositProductsForFinished.addAll(relatedProducts)
                }

                // Hitung total pendapatan menggunakan data deposit yang sudah selesai
                totalRevenue = calculateDepositRevenue(depositProductsForFinished, productDataList)

                // Remove the observers after calculating revenue to prevent unnecessary updates
                getDepositProductData.removeObserver { }
                getProductData.removeObserver { }
            }
        }

        return totalRevenue
    }

    private val _revenueForCurrentMonth = MutableLiveData<Double>()
    val revenueForCurrentMonth: LiveData<Double> get() = _revenueForCurrentMonth

    fun calculateRevenueForCurrentMonth() {
        val deposits = getDepositData.value ?: emptyList()
        val currentMonth = Calendar.getInstance().get(Calendar.MONTH)

        // Observe changes in depositProductData and productDataList LiveData
        getDepositProductData.observeForever { depositProducts ->
            getProductData.observeForever { productDataList ->
                // Filter data deposit yang hanya termasuk dalam bulan yang diinginkan dan sudah selesai
                val finishedDepositsForCurrentMonth = filterFinishedDeposits(
                    filterDepositByMonth(deposits, currentMonth)
                )

                // Mendapatkan daftar DepositProductData berdasarkan deposit yang sudah difilter
                val depositProductsForMonth = mutableListOf<DepositProductData>()
                for (deposit in finishedDepositsForCurrentMonth) {
                    val relatedProducts = getRelatedDepositProducts(deposit, depositProducts)
                    depositProductsForMonth.addAll(relatedProducts)
                }

                // Hitung total pendapatan menggunakan data deposit yang sudah difilter
                val revenue = calculateDepositRevenue(depositProductsForMonth, productDataList)
                _revenueForCurrentMonth.value = revenue

                // Remove the observers after calculating revenue to prevent unnecessary updates
                getDepositProductData.removeObserver { }
                getProductData.removeObserver { }
            }
        }
    }

    private fun filterDepositByMonth(deposits: List<DepositData>, month: Int): List<DepositData> {
        return deposits.filter { deposit ->
            val depositDate =
                parseDepositDate(deposit.depositDate) // Ubah depositDate menjadi informasi tanggal
            depositDate?.get(Calendar.MONTH) == month
        }
    }
    private fun filterFinishedDeposits(deposits: List<DepositData>): List<DepositData> {
        return deposits.filter { it.depositStatus == DepositStatus.Finish }
    }

    private fun parseDepositDate(dateString: String): Calendar? {
        val dateFormat = SimpleDateFormat("EEEE, d, MMMM, yyyy", Locale.getDefault())
        return try {
            val date = dateFormat.parse(dateString)
            val calendar = Calendar.getInstance()
            calendar.time = date
            calendar
        } catch (e: Exception) {
            null
        }
    }

    private fun getRelatedDepositProducts(
        deposit: DepositData,
        allDepositProducts: List<DepositProductData>,
    ): List<DepositProductData> {
        // Mendapatkan daftar DepositProductData yang terkait dengan deposit tertentu
        return allDepositProducts.filter { depositProduct -> depositProduct.depositId == deposit.id }
    }
}