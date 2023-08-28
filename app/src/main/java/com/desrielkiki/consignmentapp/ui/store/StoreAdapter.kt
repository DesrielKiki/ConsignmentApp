package com.desrielkiki.consignmentapp.ui.store

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.desrielkiki.consignmentapp.R
import com.desrielkiki.consignmentapp.data.entity.StoreData
import com.desrielkiki.consignmentapp.data.entity.relation.DepositWithStoreData
import com.desrielkiki.consignmentapp.data.helper.StoreDiffUtil
import com.desrielkiki.consignmentapp.databinding.RowStoreBinding

class StoreAdapter : RecyclerView.Adapter<StoreAdapter.StoreViewHolder>() {

    private var storeList = emptyList<StoreData>()

    class StoreViewHolder(private val binding: RowStoreBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(storeData: StoreData) {
            binding.storeData = storeData
            binding.executePendingBindings()
        }

        companion object {
            fun from(parent: ViewGroup): StoreViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = RowStoreBinding.inflate(layoutInflater, parent, false)
                return StoreViewHolder(
                    binding
                )
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StoreViewHolder {
        LayoutInflater.from(parent.context).inflate(R.layout.row_store, parent, false)
        return StoreViewHolder.from(parent)
    }

    override fun getItemCount(): Int = storeList.size


    override fun onBindViewHolder(holder: StoreViewHolder, position: Int) {
        val currentItem = storeList[position]
        holder.bind(currentItem)
    }


    fun setData(storeData: List<StoreData>) {
        val storeDiffUtil = StoreDiffUtil(storeList, storeData)
        val toDoDiffResult = DiffUtil.calculateDiff(storeDiffUtil)
        this.storeList = storeData
        toDoDiffResult.dispatchUpdatesTo(this)
    }
}