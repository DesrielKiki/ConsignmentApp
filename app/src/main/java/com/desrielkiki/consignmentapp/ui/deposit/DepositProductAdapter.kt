package com.desrielkiki.consignmentapp.ui.deposit

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.desrielkiki.consignmentapp.data.entity.DepositProductData
import com.desrielkiki.consignmentapp.data.entity.relation.SubDepositWithProductData
import com.desrielkiki.consignmentapp.data.helper.DepositProductDiffUtil
import com.desrielkiki.consignmentapp.databinding.RowDepositProductBinding

class DepositProductAdapter :
    RecyclerView.Adapter<DepositProductAdapter.DepositProductViewHolder>() {
    var productList = emptyList<SubDepositWithProductData>()

    class DepositProductViewHolder(private val binding: RowDepositProductBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(subDepositWithProductData: SubDepositWithProductData) {
            binding.subDepositWithProductData = subDepositWithProductData
            binding.executePendingBindings()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DepositProductViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = RowDepositProductBinding.inflate(layoutInflater, parent, false)

        return DepositProductViewHolder(binding)
    }

    override fun getItemCount(): Int = productList.size

    override fun onBindViewHolder(holder: DepositProductViewHolder, position: Int) {
        val currentItem = productList[position]
        holder.bind(currentItem)
    }

    fun setProductData(subDepositWithProductData: List<SubDepositWithProductData>) {
        val productDiffUtil = DepositProductDiffUtil(productList, subDepositWithProductData)
        val productDiffUtilResult = DiffUtil.calculateDiff(productDiffUtil)
        this.productList = subDepositWithProductData
        productDiffUtilResult.dispatchUpdatesTo(this)
    }
}