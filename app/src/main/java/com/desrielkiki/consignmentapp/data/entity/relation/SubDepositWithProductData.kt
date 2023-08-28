package com.desrielkiki.consignmentapp.data.entity.relation

import android.os.Parcelable
import androidx.room.Embedded
import androidx.room.Relation
import com.desrielkiki.consignmentapp.data.entity.DepositProductData
import com.desrielkiki.consignmentapp.data.entity.ProductData
import kotlinx.parcelize.Parcelize

@Parcelize
data class SubDepositWithProductData(
    @Embedded
    var depositProductData: DepositProductData,
    @Relation(
        parentColumn = "productId",
        entityColumn = "id"
    )
    val productData: ProductData
) : Parcelable
