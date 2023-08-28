package com.desrielkiki.consignmentapp.data.entity

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Entity(tableName = "deposit_table", foreignKeys = [
    ForeignKey(
        entity = StoreData::class,
        parentColumns = ["id"],
        childColumns = ["storeId"],
        onDelete = ForeignKey.CASCADE,
        onUpdate = ForeignKey.CASCADE
    )
])
@Parcelize
data class DepositData(
    @PrimaryKey(autoGenerate = true)
    var id: Long,
    var storeId: Int,
    var depositDate: String,
    var depositStatus: DepositStatus,
    var finishDate: String = ""

):Parcelable
