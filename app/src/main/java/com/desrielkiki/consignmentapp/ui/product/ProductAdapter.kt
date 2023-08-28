package com.desrielkiki.consignmentapp.ui.product

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.desrielkiki.consignmentapp.R
import com.desrielkiki.consignmentapp.data.entity.relation.ProductWithCategoryData
import com.desrielkiki.consignmentapp.data.helper.ProductDiffUtil
import com.desrielkiki.consignmentapp.databinding.RowProductBinding
import java.text.NumberFormat
import java.util.Locale


class ProductAdapter: RecyclerView.Adapter<ProductAdapter.ProductViewHolder>() {

    private var productWithCategoryDataList = emptyList<ProductWithCategoryData>()

    class ProductViewHolder(private val binding: RowProductBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(data: ProductWithCategoryData) {
            binding.productWithCategory = data
            val price = data.productData.productPrice
            val formattedPrice = formatRupiah(price)
            binding.tvProductPrice.text = formattedPrice.toString()
            Log.d("ProductAdapter", "Category Name: ${data.categoryData?.categoryName}")
            binding.tvProductCategory.text = data.categoryData?.categoryName.toString()
            binding.executePendingBindings()
        }

        private fun formatRupiah(amount: Long): String {
            val numberFormat = NumberFormat.getCurrencyInstance(
                Locale(
                    "id",
                    "ID"
                )
            ) // Set locale to Indonesia (ID)
            return numberFormat.format(amount)
        }
        companion object {
            fun from(parent: ViewGroup): ProductAdapter.ProductViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = RowProductBinding.inflate(layoutInflater, parent, false)
                return ProductAdapter.ProductViewHolder(
                    binding
                )
            }
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        LayoutInflater.from(parent.context).inflate(R.layout.row_product, parent, false)
        return ProductAdapter.ProductViewHolder.from(parent)
    }

    override fun getItemCount(): Int = productWithCategoryDataList.size


    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        val currentItem = productWithCategoryDataList[position]
        Log.d("ProductAdapter", "Binding item at position $position: $currentItem")
        holder.bind(currentItem)
    }

    fun setData(productWithCategoryData: List<ProductWithCategoryData>) {
        Log.d("ProductAdapter", "Setting new data: $productWithCategoryData")
        val productDiffUtil = ProductDiffUtil(productWithCategoryDataList, productWithCategoryData)
        val productDiffResult = DiffUtil.calculateDiff(productDiffUtil)
        this.productWithCategoryDataList = productWithCategoryData
        productDiffResult.dispatchUpdatesTo(this)
    }
}