package com.desrielkiki.consignmentapp.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.desrielkiki.consignmentapp.data.entity.CategoryData
import com.desrielkiki.consignmentapp.data.entity.DepositData
import com.desrielkiki.consignmentapp.data.entity.DepositProductData
import com.desrielkiki.consignmentapp.data.entity.ProductData
import com.desrielkiki.consignmentapp.data.entity.StoreData

@Database(entities = [ProductData::class, CategoryData::class, StoreData::class, DepositData::class, DepositProductData::class], version = 1, exportSchema = false)
abstract class ConsignmentDatabase : RoomDatabase() {

    abstract fun consignmentDao(): ConsignmentDao


    companion object {

        @Volatile
        private var INSTANCE: ConsignmentDatabase? = null

        fun getDatabase(context: Context): ConsignmentDatabase {
            val tempInstance = INSTANCE
            if (tempInstance != null) {
                return tempInstance
            }
            synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    ConsignmentDatabase::class.java,
                    "consignment_database"

                ).fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                return instance
            }
        }
    }
}