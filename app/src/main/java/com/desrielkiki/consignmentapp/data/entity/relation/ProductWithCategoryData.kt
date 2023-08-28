package com.desrielkiki.consignmentapp.data.entity.relation

import android.os.Parcelable
import androidx.room.Embedded
import androidx.room.Relation
import com.desrielkiki.consignmentapp.data.entity.CategoryData
import com.desrielkiki.consignmentapp.data.entity.ProductData
import kotlinx.parcelize.Parcelize

@Parcelize
data class ProductWithCategoryData (
    @Embedded
    var productData: ProductData,
    @Relation(
        parentColumn = "categoryId",
        entityColumn = "id",
    )
    val categoryData: CategoryData?
):Parcelable
