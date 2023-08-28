package com.desrielkiki.consignmentapp.data.entity

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Entity(tableName = "store_table")
@Parcelize
data class StoreData(
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0,
    var storeName: String,
    var storeAddress: String,
    var storeOwner: String,
    var storeContact: String
) : Parcelable