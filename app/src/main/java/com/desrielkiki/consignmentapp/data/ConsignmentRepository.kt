package com.desrielkiki.consignmentapp.data

import android.app.Application
import androidx.lifecycle.LiveData
import com.desrielkiki.consignmentapp.data.entity.CategoryData
import com.desrielkiki.consignmentapp.data.entity.DepositData
import com.desrielkiki.consignmentapp.data.entity.DepositProductData
import com.desrielkiki.consignmentapp.data.entity.DepositStatus
import com.desrielkiki.consignmentapp.data.entity.ProductData
import com.desrielkiki.consignmentapp.data.entity.StoreData
import com.desrielkiki.consignmentapp.data.entity.relation.DepositWithStoreData
import com.desrielkiki.consignmentapp.data.entity.relation.ProductWithCategoryData
import com.desrielkiki.consignmentapp.data.entity.relation.SubDepositWithProductData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class ConsignmentRepository(application: Application) {

    private val dao: ConsignmentDao

    private val executorService: ExecutorService = Executors.newSingleThreadExecutor()

    init {
        val db = ConsignmentDatabase.getDatabase(application)
        dao = db.consignmentDao()
    }

    /**
     * get data
     */
    fun getCategoryData(): LiveData<List<CategoryData>> = dao.getCategoryData()
    fun getSubDepositData(): LiveData<List<DepositProductData>> = dao.getSubDepositData()
    fun getSubDepositWithProductData(): LiveData<List<SubDepositWithProductData>> =
        dao.getSubDepositWithProductData()

    fun getDepositData(): LiveData<List<DepositData>> = dao.getDepositData()
    fun getDepositWithStoreData(): LiveData<List<DepositWithStoreData>> =
        dao.getDepositWithStoreData()

    fun getProductData(): LiveData<List<ProductData>> = dao.getProductData()
    fun getProductWithCategoryData(): LiveData<List<ProductWithCategoryData>> =
        dao.getProductWithCategoryData()

    fun getStoreData(): LiveData<List<StoreData>> = dao.getStoreData()
    fun getDepositWithoutProductData(): LiveData<List<DepositData>> = dao.getDepositsWithoutProducts()

    /**
     * insert data
     */

    fun insertCategory(categoryData: CategoryData) {
        executorService.execute { dao.insertCategory(categoryData) }
    }
    fun insertSubDeposit(depositProductData: DepositProductData) {
        executorService.execute { dao.insertSubDeposit(depositProductData) }
    }
    suspend fun insertAndGetIdDeposit(depositData: DepositData): Long {
        return withContext(Dispatchers.IO) {
            dao.insertDeposit(depositData)
        }
    }
    fun insertProduct(productData: ProductData) {
        executorService.execute { dao.insertProduct(productData) }
    }
    fun insertStore(storeData: StoreData) {
        executorService.execute { dao.insertStore(storeData) }
    }

    /**
     * update data
     */
    fun updateCategory(categoryData: CategoryData) {
        executorService.execute { dao.updateCategory(categoryData) }
    }
    fun updateSubDeposit(depositProductData: DepositProductData) {
        executorService.execute { dao.updateSubDeposit(depositProductData) }
    }
    fun updateDeposit(depositData: DepositData) {
        executorService.execute { dao.updateDeposit(depositData) }
    }
    fun updateProduct(productData: ProductData) {
        executorService.execute { dao.updateProduct(productData) }
    }
    fun updateStore(storeData: StoreData) {
        executorService.execute { dao.updateStore(storeData) }
    }

    /**
     * delete data
     */
    fun deleteCategory(categoryData: CategoryData) {
        executorService.execute { dao.deleteCategory(categoryData) }
    }
    fun deleteSubDeposit(depositProductData: DepositProductData) {
        executorService.execute { dao.deleteSubDeposit(depositProductData) }
    }
    fun deleteDepositAndProduct(depositId: Long) {
        executorService.execute {
            dao.deleteDepositById(depositId)
            executorService.execute { dao.deleteFilteredProduct(depositId) }
        }
    }
    fun deleteProduct(productData: ProductData) {
        executorService.execute { dao.deleteProduct(productData) }
    }
    fun deleteStore(storeData: StoreData) {
        executorService.execute { dao.deleteStore(storeData) }
    }
    fun deleteDeposit(depositData: DepositData){
        executorService.execute{dao.deleteDeposit(depositData)}
    }

    /**
     * filter data
     */
    fun filterProduct(depositId: Long): LiveData<List<SubDepositWithProductData>> {
        return dao.filterProduct(depositId)
    }
    fun filterDepositByStore(storeId: Int): LiveData<List<DepositWithStoreData>> {
        return dao.filterDepositByStore(storeId)
    }
    fun filterUnfinishedDeposit(deposit: DepositStatus): LiveData<List<DepositWithStoreData>> {
        return dao.filterUnfinishedDeposit(deposit)
    }
}