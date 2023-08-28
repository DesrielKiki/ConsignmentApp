package com.desrielkiki.consignmentapp.data.helper

import androidx.recyclerview.widget.DiffUtil
import com.desrielkiki.consignmentapp.data.entity.DepositProductData
import com.desrielkiki.consignmentapp.data.entity.relation.SubDepositWithProductData

class DepositProductDiffUtil(
    private val oldProductList: List<SubDepositWithProductData>,
    private val newProductList: List<SubDepositWithProductData>
) : DiffUtil.Callback() {
    override fun getOldListSize(): Int = oldProductList.size
    override fun getNewListSize(): Int = newProductList.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldProductList[oldItemPosition].depositProductData.id == newProductList[newItemPosition].depositProductData.id
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldProductList[oldItemPosition].depositProductData.id == newProductList[newItemPosition].depositProductData.id
    }
}