package com.desrielkiki.consignmentapp.data.helper

import androidx.recyclerview.widget.DiffUtil
import com.desrielkiki.consignmentapp.data.entity.CategoryData
import com.desrielkiki.consignmentapp.data.entity.ProductData
import com.desrielkiki.consignmentapp.data.entity.relation.ProductWithCategoryData

class ProductDiffUtil(
    private val oldProductList: List<ProductWithCategoryData>,
    private val newProductList: List<ProductWithCategoryData>
) : DiffUtil.Callback() {
    override fun getOldListSize(): Int = oldProductList.size
    override fun getNewListSize(): Int = newProductList.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldProductList[oldItemPosition].productData.id == newProductList[newItemPosition].productData.id
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldProductList[oldItemPosition].productData.id == newProductList[newItemPosition].productData.id
                && oldProductList[oldItemPosition].productData.productName == newProductList[newItemPosition].productData.productName
    }
}