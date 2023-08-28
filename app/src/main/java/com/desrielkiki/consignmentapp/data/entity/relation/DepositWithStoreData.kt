package com.desrielkiki.consignmentapp.data.entity.relation

import android.os.Parcelable
import androidx.room.Embedded
import androidx.room.Relation
import com.desrielkiki.consignmentapp.data.entity.DepositData
import com.desrielkiki.consignmentapp.data.entity.StoreData
import kotlinx.parcelize.Parcelize

@Parcelize
data class DepositWithStoreData(
    @Embedded
    var depositData: DepositData,
    @Relation(
        parentColumn = "storeId",
        entityColumn = "id",
    )
    val storeData: StoreData
):Parcelable