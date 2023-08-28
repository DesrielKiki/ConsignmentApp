package com.desrielkiki.consignmentapp.data.entity

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import androidx.room.Relation
import kotlinx.parcelize.Parcelize
import java.math.BigInteger

@Entity(
    tableName = "product_table", foreignKeys = [
        ForeignKey(
            entity = CategoryData::class,
            parentColumns = ["id"],
            childColumns = ["categoryId"],
            onDelete = ForeignKey.CASCADE,
            onUpdate = ForeignKey.CASCADE
        )
    ]
)

@Parcelize
data class ProductData(
    @PrimaryKey(autoGenerate = true)
    var id: Int,
    var categoryId: Int,
    var productName: String,
    var productPrice: Long,

    ) : Parcelable
