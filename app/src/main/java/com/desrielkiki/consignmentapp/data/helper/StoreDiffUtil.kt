package com.desrielkiki.consignmentapp.data.helper

import androidx.recyclerview.widget.DiffUtil
import com.desrielkiki.consignmentapp.data.entity.StoreData
import com.desrielkiki.consignmentapp.data.entity.relation.DepositWithStoreData

class StoreDiffUtil(
    private val oldStoreList: List<StoreData>,
    private val newStoreList: List<StoreData>
) : DiffUtil.Callback() {
    override fun getOldListSize(): Int = oldStoreList.size
    override fun getNewListSize(): Int = newStoreList.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldStoreList[oldItemPosition].id == newStoreList[newItemPosition].id
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldStoreList[oldItemPosition].id == newStoreList[newItemPosition].id
                && oldStoreList[oldItemPosition].storeName == newStoreList[newItemPosition].storeName
    }
}
