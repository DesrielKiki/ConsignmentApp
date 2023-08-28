package com.desrielkiki.consignmentapp.data

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.desrielkiki.consignmentapp.data.entity.CategoryData
import com.desrielkiki.consignmentapp.data.entity.DepositData
import com.desrielkiki.consignmentapp.data.entity.DepositProductData
import com.desrielkiki.consignmentapp.data.entity.DepositStatus
import com.desrielkiki.consignmentapp.data.entity.ProductData
import com.desrielkiki.consignmentapp.data.entity.StoreData
import com.desrielkiki.consignmentapp.data.entity.relation.DepositWithDepositProductData
import com.desrielkiki.consignmentapp.data.entity.relation.DepositWithStoreData
import com.desrielkiki.consignmentapp.data.entity.relation.ProductWithCategoryData
import com.desrielkiki.consignmentapp.data.entity.relation.SubDepositWithProductData

@Dao
interface ConsignmentDao {

    //insert
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertStore(storeData: StoreData)
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertCategory(categoryData: CategoryData)
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertProduct(productData: ProductData)
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertDeposit(depositData: DepositData):Long
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertSubDeposit(depositProductData: DepositProductData)

    //update
    @Update
    fun updateStore(storeData: StoreData)
    @Update
    fun updateCategory(categoryData: CategoryData)
    @Update
    fun updateProduct(productData: ProductData)
    @Update
    fun updateDeposit(depositData: DepositData)
    @Update
    fun updateSubDeposit(depositProductData: DepositProductData)

    //delete
    @Delete
    fun deleteStore(storeData: StoreData)
    @Delete
    fun deleteCategory(categoryData: CategoryData)
    @Delete
    fun deleteProduct(productData: ProductData)
    @Delete
    fun deleteDeposit(depositData: DepositData)
    @Delete
    fun deleteSubDeposit(depositProductData: DepositProductData)

    //delete with filter
    @Query("DELETE FROM deposit_product_table WHERE depositId = :depositId")
    fun deleteFilteredProduct(depositId: Long)
    @Query("DELETE FROM deposit_table WHERE id = :depositId")
    fun deleteDepositById(depositId: Long)

    //getData
    @Query("SELECT * FROM STORE_TABLE ORDER BY id ASC")
    fun getStoreData(): LiveData<List<StoreData>>
    @Query("SELECT * FROM category_table ORDER BY id ASC")
    fun getCategoryData(): LiveData<List<CategoryData>>
    @Query("SELECT * FROM product_table")
    fun getProductData(): LiveData<List<ProductData>>
    @Transaction
    @Query("SELECT * FROM product_table")
    fun getProductWithCategoryData(): LiveData<List<ProductWithCategoryData>>
    @Query("SELECT * FROM deposit_table")
    fun getDepositData(): LiveData<List<DepositData>>
    @Transaction
    @Query("SELECT * FROM deposit_table")
    fun getDepositWithStoreData(): LiveData<List<DepositWithStoreData>>
    @Transaction
    @Query("SELECT * FROM deposit_product_table")
    fun getSubDepositData(): LiveData<List<DepositProductData>>
    @Transaction
    @Query("SELECT * FROM deposit_product_table")
    fun getSubDepositWithProductData(): LiveData<List<SubDepositWithProductData>>
    @Transaction
    @Query("SELECT * FROM deposit_product_table")
    fun getDepositWithSubDepositData(): LiveData<List<DepositWithDepositProductData>>

    @Transaction
    @Query("SELECT * FROM deposit_table WHERE id NOT IN (SELECT DISTINCT depositId FROM deposit_product_table)")
    fun getDepositsWithoutProducts(): LiveData<List<DepositData>>

    //getDataWithFilter
    @Query("SELECT * FROM deposit_table WHERE storeId = :storeId")
    fun filterDepositByStore(storeId: Int): LiveData<List<DepositWithStoreData>>
    @Query("SELECT * FROM deposit_table WHERE depositStatus = :deposit")
    fun filterUnfinishedDeposit(deposit: DepositStatus): LiveData<List<DepositWithStoreData>>
    @Query("SELECT * FROM deposit_product_table WHERE depositId = :depositId")
    fun filterProduct(depositId: Long): LiveData<List<SubDepositWithProductData>>
}