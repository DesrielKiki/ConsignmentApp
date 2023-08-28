package com.desrielkiki.consignmentapp.data.entity

import android.os.Parcelable
import android.provider.ContactsContract.CommonDataKinds.StructuredName
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Entity(tableName = "deposit_product_table", foreignKeys = [
    ForeignKey(
        entity = ProductData::class,
        parentColumns = ["id"],
        childColumns = ["productId"],
        onDelete = ForeignKey.CASCADE,
        onUpdate = ForeignKey.CASCADE
    )
])
@Parcelize
class DepositProductData(
    @PrimaryKey(autoGenerate = true)
    var id: Int,
    val depositId: Long,
    var productId: Int,
    var qty: Int,
    var returnQty: Int
) : Parcelable
