package com.desrielkiki.consignmentapp.data.helper

import androidx.recyclerview.widget.DiffUtil
import com.desrielkiki.consignmentapp.data.entity.DepositData
import com.desrielkiki.consignmentapp.data.entity.ProductData
import com.desrielkiki.consignmentapp.data.entity.relation.DepositWithStoreData

class DepositHistoryDiffUtil(
    private val oldDepositList: List<DepositWithStoreData>,
    private val newDepositList: List<DepositWithStoreData>
) : DiffUtil.Callback() {
    override fun getOldListSize(): Int = oldDepositList.size
    override fun getNewListSize(): Int = newDepositList.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldDepositList[oldItemPosition].depositData.id == newDepositList[newItemPosition].depositData.id
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldDepositList[oldItemPosition].depositData.id == newDepositList[newItemPosition].depositData.id
                && oldDepositList[oldItemPosition].depositData.depositDate == newDepositList[newItemPosition].depositData.depositDate
    }
}