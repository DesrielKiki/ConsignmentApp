package com.desrielkiki.consignmentapp.ui.category

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.desrielkiki.consignmentapp.R
import com.desrielkiki.consignmentapp.data.entity.CategoryData
import com.desrielkiki.consignmentapp.data.helper.CategoryDiffUtil
import com.desrielkiki.consignmentapp.databinding.RowCategoryBinding

class CategoryAdapter : RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder>() {

    var categoryList = emptyList<CategoryData>()


    class CategoryViewHolder(private val binding: RowCategoryBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(categoryData: CategoryData) {
            binding.categoryData = categoryData
            binding.executePendingBindings()

        }

        companion object {
            fun from(parent: ViewGroup): CategoryAdapter.CategoryViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = RowCategoryBinding.inflate(layoutInflater, parent, false)
                return CategoryViewHolder(
                    binding
                )
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {
        LayoutInflater.from(parent.context).inflate(R.layout.row_category, parent, false)
        return CategoryAdapter.CategoryViewHolder.from(parent)
    }

    override fun getItemCount(): Int = categoryList.size


    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
        val currentItem = categoryList[position]
        holder.bind(currentItem)
    }

    fun setData(categoryData: List<CategoryData>) {
        val categoryDiffUtil = CategoryDiffUtil(categoryList, categoryData)
        val categoryDiffUtilResult = DiffUtil.calculateDiff(categoryDiffUtil)
        this.categoryList = categoryData
        categoryDiffUtilResult.dispatchUpdatesTo(this)
    }
}
