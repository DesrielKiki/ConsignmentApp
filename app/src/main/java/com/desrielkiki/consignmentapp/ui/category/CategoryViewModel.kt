package com.desrielkiki.consignmentapp.ui.category

import android.app.Application
import android.text.TextUtils
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.desrielkiki.consignmentapp.data.entity.CategoryData
import com.desrielkiki.consignmentapp.data.ConsignmentRepository

class CategoryViewModel(application: Application): AndroidViewModel(application)  {

    private val consignmentRepository: ConsignmentRepository = ConsignmentRepository(application )

    val getCategoryData: LiveData<List<CategoryData>> = consignmentRepository.getCategoryData()

    fun insertCategory(categoryData: CategoryData){
        consignmentRepository.insertCategory(categoryData)
    }
    fun updateCategory(categoryData: CategoryData){
        consignmentRepository.updateCategory(categoryData)
    }
    fun deleteCategory(categoryData: CategoryData){
        consignmentRepository.deleteCategory(categoryData)
    }

    fun verifyDataFromUser(name: String): Boolean {
        return if (TextUtils.isEmpty(name)) {
            false
        } else !(name.isEmpty())
    }
}