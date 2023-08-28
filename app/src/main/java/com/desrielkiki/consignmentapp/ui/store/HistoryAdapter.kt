package com.desrielkiki.consignmentapp.ui.store

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.desrielkiki.consignmentapp.data.entity.DepositData
import com.desrielkiki.consignmentapp.data.entity.relation.DepositWithStoreData
import com.desrielkiki.consignmentapp.data.helper.DepositHistoryDiffUtil
import com.desrielkiki.consignmentapp.databinding.RowDepositHistoryBinding

class HistoryAdapter : RecyclerView.Adapter<HistoryAdapter.HistoryViewHolder>() {

    private var depositList = emptyList<DepositWithStoreData>()


    class HistoryViewHolder(private val binding: RowDepositHistoryBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(depositWithStoreData: DepositWithStoreData) {
            binding.depositWithStoreData = depositWithStoreData
            binding.executePendingBindings()
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HistoryViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = RowDepositHistoryBinding.inflate(layoutInflater, parent, false)

        return HistoryViewHolder(binding)
    }

    override fun getItemCount(): Int = depositList.size


    override fun onBindViewHolder(holder: HistoryViewHolder, position: Int) {
        val currentItem = depositList[position]
        holder.bind(currentItem)
    }

    fun setHistoryData(depositWithStoreData: List<DepositWithStoreData>) {
        val depositHistoryDiffUtil = DepositHistoryDiffUtil(depositList, depositWithStoreData)
        val depositDiffUtilResult = DiffUtil.calculateDiff(depositHistoryDiffUtil)
        this.depositList = depositWithStoreData
        depositDiffUtilResult.dispatchUpdatesTo(this)
    }
}