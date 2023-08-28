package com.desrielkiki.consignmentapp.ui.store

import android.app.Application
import android.text.TextUtils
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.desrielkiki.consignmentapp.data.entity.StoreData
import com.desrielkiki.consignmentapp.data.ConsignmentRepository

class StoreViewModel(application: Application) : AndroidViewModel(application) {

    private val consignmentRepository: ConsignmentRepository = ConsignmentRepository(application )

    val getStoreData: LiveData<List<StoreData>> = consignmentRepository.getStoreData()

    fun insertStore(storeData: StoreData) {
        consignmentRepository.insertStore(storeData)
    }

    fun updateStore(storeData: StoreData) {
        consignmentRepository.updateStore(storeData)
    }

    fun deleteStore(storeData: StoreData) {
        consignmentRepository.deleteStore(storeData)
    }

    fun verifyDataFromUser(name: String, address: String, owner: String, contact: String): Boolean {
        return if (TextUtils.isEmpty(name) || TextUtils.isEmpty(address) || TextUtils.isEmpty(owner) || TextUtils.isEmpty(
                contact
            )
        ) {
            false
        } else !(name.isEmpty() || address.isEmpty() || TextUtils.isEmpty(owner) || TextUtils.isEmpty(
            contact
        ))
    }
}