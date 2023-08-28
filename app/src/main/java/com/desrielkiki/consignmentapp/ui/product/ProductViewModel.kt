package com.desrielkiki.consignmentapp.ui.product

import android.app.Application
import android.text.TextUtils
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.desrielkiki.consignmentapp.data.entity.ProductData
import com.desrielkiki.consignmentapp.data.entity.relation.ProductWithCategoryData
import com.desrielkiki.consignmentapp.data.ConsignmentRepository

class ProductViewModel(application: Application) : AndroidViewModel(application) {
    private val consignmentRepository: ConsignmentRepository = ConsignmentRepository(application )

    val getProductData: LiveData<List<ProductData>> = consignmentRepository.getProductData()
    val getProductWithCategoryData: LiveData<List<ProductWithCategoryData>> = consignmentRepository.getProductWithCategoryData()

    fun insertProduct(productData: ProductData) {
        consignmentRepository.insertProduct(productData)
    }

    fun updateProduct(productData: ProductData) {
        consignmentRepository.updateProduct(productData)
    }

    fun deleteProduct(productData: ProductData) {
        consignmentRepository.deleteProduct(productData)
    }

    fun verifyDataFromUser(name: String, price: Long): Boolean {
        return name.isNotEmpty() && price > 0
    }
}