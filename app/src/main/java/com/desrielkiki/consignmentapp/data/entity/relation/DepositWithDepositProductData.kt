package com.desrielkiki.consignmentapp.data.entity.relation

import android.os.Parcelable
import androidx.room.Embedded
import androidx.room.Relation
import com.desrielkiki.consignmentapp.data.entity.DepositData
import com.desrielkiki.consignmentapp.data.entity.DepositProductData
import kotlinx.parcelize.Parcelize

@Parcelize
data class DepositWithDepositProductData(
    @Embedded
    var depositProductData: DepositProductData,
    @Relation(
        parentColumn = "depositId",
        entityColumn = "id"
    )
    val depositData: DepositData
) : Parcelable