package com.desrielkiki.consignmentapp.data.entity

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Entity(tableName = "category_table")
@Parcelize
data class CategoryData(
    @PrimaryKey (autoGenerate = true)
    var id : Int,
    var categoryName :String
):Parcelable
