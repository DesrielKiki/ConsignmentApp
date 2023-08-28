package com.desrielkiki.consignmentapp.data.helper

import androidx.recyclerview.widget.DiffUtil
import com.desrielkiki.consignmentapp.data.entity.CategoryData
import com.desrielkiki.consignmentapp.data.entity.StoreData

class CategoryDiffUtil(
    private val oldCategoryList: List<CategoryData>,
    private val newCategoryList: List<CategoryData>
) : DiffUtil.Callback() {
    override fun getOldListSize(): Int = oldCategoryList.size
    override fun getNewListSize(): Int = newCategoryList.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldCategoryList[oldItemPosition].categoryName == newCategoryList[newItemPosition].categoryName
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldCategoryList[oldItemPosition].categoryName == newCategoryList[newItemPosition].categoryName

    }
}