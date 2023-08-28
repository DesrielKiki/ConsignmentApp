package com.desrielkiki.consignmentapp.ui

import android.app.Application
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.desrielkiki.consignmentapp.data.entity.CategoryData
import com.desrielkiki.consignmentapp.data.entity.DepositData
import com.desrielkiki.consignmentapp.data.entity.DepositProductData
import com.desrielkiki.consignmentapp.data.entity.ProductData
import com.desrielkiki.consignmentapp.data.entity.StoreData
import com.desrielkiki.consignmentapp.data.entity.relation.DepositWithStoreData
import com.desrielkiki.consignmentapp.data.entity.relation.ProductWithCategoryData
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class SharedViewModel (application: Application): AndroidViewModel(application){

    // check empty database
    val emptyDataBase: MutableLiveData<Boolean> = MutableLiveData(false)
    fun checkIfProductEmpty(productWithCategoryData: List<ProductWithCategoryData>){
        emptyDataBase.value = productWithCategoryData.isEmpty()
    }
    fun checkIfCategoryEmpty(categoryData: List<CategoryData>){
        emptyDataBase.value = categoryData.isEmpty()
    }
    fun checkIfStoreEmpty(storeData: List<StoreData>){
        emptyDataBase.value = storeData.isEmpty()
    }
    fun checkIfDepositEmpty(depositWithStoreData: List<DepositWithStoreData>) {
        emptyDataBase.value = depositWithStoreData.isEmpty()
    }
    fun formatRupiah(amount: Int): String {
        val numberFormat = NumberFormat.getCurrencyInstance(
            Locale(
                "id",
                "ID"
            )
        ) // Set locale to Indonesia (ID)
        return numberFormat.format(amount)
    }
     fun formatCurrency(amount: Int): String {
        val formatter = NumberFormat.getCurrencyInstance(Locale("id", "ID")) // Indonesia Locale
        return formatter.format(amount.toDouble())
    }
    fun customDateFormat(startDate: Date): String {
        val dayOfWeek = SimpleDateFormat("EEEE", Locale.getDefault()).format(startDate)
        val dayOfMonth = SimpleDateFormat("d", Locale.getDefault()).format(startDate)
        val monthName = SimpleDateFormat("MMMM", Locale.getDefault()).format(startDate)
        val year = SimpleDateFormat("yyyy", Locale.getDefault()).format(startDate)

        return "$dayOfWeek, $dayOfMonth, $monthName, $year"
    }
    fun formatPriceWithSeparator(price: String): String {
        val decimalFormatSymbols = DecimalFormatSymbols(Locale.getDefault())
        decimalFormatSymbols.groupingSeparator = '.'
        val decimalFormat = DecimalFormat("#,###", decimalFormatSymbols)
        return decimalFormat.format(price.toLongOrNull() ?: 0)
    }
}