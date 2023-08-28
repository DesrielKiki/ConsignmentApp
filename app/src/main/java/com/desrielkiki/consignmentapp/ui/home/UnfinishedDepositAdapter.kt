package com.desrielkiki.consignmentapp.ui.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.desrielkiki.consignmentapp.data.entity.relation.DepositWithStoreData
import com.desrielkiki.consignmentapp.data.helper.DepositDiffUtil
import com.desrielkiki.consignmentapp.data.helper.DepositHistoryDiffUtil
import com.desrielkiki.consignmentapp.databinding.RowUnfinishedDepositBinding

class UnfinishedDepositAdapter :
    RecyclerView.Adapter<UnfinishedDepositAdapter.UnfinishedDepositViewHolder>() {
    var depositWithStoreList = emptyList<DepositWithStoreData>()


    class UnfinishedDepositViewHolder(private val binding: RowUnfinishedDepositBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(depositWithStoreData: DepositWithStoreData) {
            binding.depositWithStoreData = depositWithStoreData
            binding.executePendingBindings()
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UnfinishedDepositViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = RowUnfinishedDepositBinding.inflate(layoutInflater, parent, false)

        return UnfinishedDepositViewHolder(binding)
    }

    override fun getItemCount(): Int = depositWithStoreList.size


    override fun onBindViewHolder(holder: UnfinishedDepositViewHolder, position: Int) {
        val currentItem = depositWithStoreList[position]
        holder.bind(currentItem)
    }

    fun setDepositData(depositWithStoreData: List<DepositWithStoreData>) {
        val depositDiffUtil = DepositDiffUtil(depositWithStoreList, depositWithStoreData)
        val depositDiffUtilResult = DiffUtil.calculateDiff(depositDiffUtil)
        this.depositWithStoreList = depositWithStoreData
        depositDiffUtilResult.dispatchUpdatesTo(this)
    }
}
