package com.desrielkiki.consignmentapp.ui

import android.view.View
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.databinding.BindingAdapter
import androidx.lifecycle.MutableLiveData
import androidx.navigation.findNavController
import com.desrielkiki.consignmentapp.R
import com.desrielkiki.consignmentapp.data.entity.CategoryData
import com.desrielkiki.consignmentapp.data.entity.DepositData
import com.desrielkiki.consignmentapp.data.entity.ProductData
import com.desrielkiki.consignmentapp.data.entity.StoreData
import com.desrielkiki.consignmentapp.data.entity.relation.DepositWithStoreData
import com.desrielkiki.consignmentapp.data.entity.relation.ProductWithCategoryData
import com.desrielkiki.consignmentapp.ui.category.CategoryFragmentDirections
import com.desrielkiki.consignmentapp.ui.deposit.DepositFragmentDirections
import com.desrielkiki.consignmentapp.ui.product.ProductFragmentDirections
import com.desrielkiki.consignmentapp.ui.store.StoreFragmentDirections
import com.google.android.material.floatingactionbutton.FloatingActionButton

class BindingAdapter {
    companion object {

        //Store
        @BindingAdapter("android:navigateToAddStoreFragment")
        @JvmStatic
        fun navigateToAddStoreFragment(view: FloatingActionButton, navigate: Boolean) {
            view.setOnClickListener {
                if (navigate) {
                    view.findNavController().navigate(R.id.action_storeFragment_to_addStoreFragment)
                }
            }
        }

        @BindingAdapter("android:sendStoreToDetail")
        @JvmStatic
        fun sendStoreToDetail(view: RelativeLayout, storeData: StoreData) {
            view.setOnClickListener {
                val action =
                    StoreFragmentDirections.actionStoreFragmentToDetailStoreFragment(storeData)
                view.findNavController().navigate(action)
            }
        }

        //category

        @BindingAdapter("android:navigateToUpdateCategory")
        @JvmStatic
        fun navigateToUpdateCategory(view: RelativeLayout, currentCategory: CategoryData) {
            view.setOnClickListener {
                val action =
                    CategoryFragmentDirections.actionCategoryFragmentToUpdateCategoryFragment(
                        currentCategory
                    )
                view.findNavController().navigate(action)
            }
        }

        //product
        @BindingAdapter("android:navigateToUpdateProduct")
        @JvmStatic
        fun navigateToUpdateProduct(view: RelativeLayout, currentProduct: ProductWithCategoryData) {
            view.setOnClickListener {
                val action =
                    ProductFragmentDirections.actionProductFragmentToUpdateProductFragment(
                        currentProduct
                    )
                view.findNavController().navigate(action)
            }
        }

        //deposit
        @BindingAdapter("android:navigateToDetailDeposit")
        @JvmStatic
        fun navigateToDetailDeposit(view: RelativeLayout, depositData: DepositWithStoreData) {
            view.setOnClickListener {
                val action = DepositFragmentDirections.actionDepositFragmentToDetailDepositFragment(
                    depositData
                )
                view.findNavController().navigate(action)
            }
        }

        //util
        @BindingAdapter("android:parseIntToString")
        @JvmStatic
        fun parseIntToString(view: TextView, _intValue: Int) {
            val stringValue = _intValue.toString()
            view.text = stringValue

        }

        @BindingAdapter("android:emptyDatabase")
        @JvmStatic
        fun emptyDatabase(view: View, emptyDatabase: MutableLiveData<Boolean>) {
            when (emptyDatabase.value) {
                true -> view.visibility = View.VISIBLE
                else -> view.visibility = View.INVISIBLE
            }
        }
    }
}

