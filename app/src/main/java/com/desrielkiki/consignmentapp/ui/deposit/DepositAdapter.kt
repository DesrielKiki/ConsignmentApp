package com.desrielkiki.consignmentapp.ui.deposit

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.desrielkiki.consignmentapp.data.entity.relation.DepositWithStoreData
import com.desrielkiki.consignmentapp.data.helper.DepositDiffUtil
import com.desrielkiki.consignmentapp.data.helper.DepositHistoryDiffUtil
import com.desrielkiki.consignmentapp.databinding.RowDepositBinding

class DepositAdapter : RecyclerView.Adapter<DepositAdapter.DepositViewHolder>() {
    var depositWithStoreDataList = emptyList<DepositWithStoreData>()


    class DepositViewHolder(private val binding: RowDepositBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(depositWithStoreData: DepositWithStoreData) {
            binding.depositWithStore = depositWithStoreData
            binding.executePendingBindings()
        }

        companion object {
            fun from(
                parent: ViewGroup,
                depositViewModel: DepositViewModel,
            ): DepositAdapter.DepositViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = RowDepositBinding.inflate(layoutInflater, parent, false)
                return DepositAdapter.DepositViewHolder(
                    binding

                )
            }
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DepositViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = RowDepositBinding.inflate(layoutInflater, parent, false)

        return DepositViewHolder(binding)
    }

    override fun getItemCount(): Int = depositWithStoreDataList.size


    override fun onBindViewHolder(holder: DepositViewHolder, position: Int) {
        val currentItem = depositWithStoreDataList[position]
        holder.bind(currentItem)
    }

    fun setDepositData(depositWithStoreData: List<DepositWithStoreData>) {
        val depositDiffUtil = DepositDiffUtil(depositWithStoreDataList, depositWithStoreData)
        val depositDiffUtilResult = DiffUtil.calculateDiff(depositDiffUtil)
        this.depositWithStoreDataList = depositWithStoreData
        depositDiffUtilResult.dispatchUpdatesTo(this)
    }
}