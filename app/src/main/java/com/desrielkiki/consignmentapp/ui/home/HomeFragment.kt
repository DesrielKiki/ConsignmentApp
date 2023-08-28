package com.desrielkiki.consignmentapp.ui.home

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.desrielkiki.consignmentapp.data.entity.DepositStatus
import com.desrielkiki.consignmentapp.databinding.FragmentHomeBinding
import com.desrielkiki.consignmentapp.ui.SharedViewModel
import com.desrielkiki.consignmentapp.ui.deposit.DepositViewModel
import com.desrielkiki.consignmentapp.ui.product.ProductViewModel

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private val adapter: UnfinishedDepositAdapter by lazy { UnfinishedDepositAdapter() }
    private val productViewModel: ProductViewModel by viewModels()
    private val homeViewModel: HomeViewModel by viewModels()
    private val depositViewModel: DepositViewModel by viewModels()
    private val sharedViewModel: SharedViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        depositViewModel.getDepositProductData.observe(
            viewLifecycleOwner,
            Observer { depositProducts ->
                productViewModel.getProductData.observe(
                    viewLifecycleOwner,
                    Observer { productDataList ->
                        if (depositProducts.isNotEmpty() && productDataList.isNotEmpty()) {
                            val totalDepositRevenue =
                                homeViewModel.calculateTotalFinishDepositRevenue(
                                )
                            val formattedRevenue =
                                sharedViewModel.formatRupiah(totalDepositRevenue.toInt())
                            binding.tvTotalRevenue.text = formattedRevenue
                        } else {
                            binding.tvTotalRevenue.text = "N/A"
                        }
                        homeViewModel.calculateRevenueForCurrentMonth()
                        homeViewModel.revenueForCurrentMonth.observe(
                            viewLifecycleOwner,
                            Observer { revenue ->
                                // Display the total deposit revenue in the TextView with the currency format
                                binding.tvRevenueThisMonth.text =
                                    sharedViewModel.formatCurrency(revenue.toInt())
                            })
                    })
            })
        _binding = FragmentHomeBinding.inflate(inflater, container, false)



        homeViewModel.getDepositData.observe(viewLifecycleOwner, Observer {
            getUnfinishedDepositData()
        })


        setupUnfinishedDepositRecyclerView()
        return binding.root
    }

    private fun getUnfinishedDepositData() {
        homeViewModel.filterUnfinishedDeposit(DepositStatus.Deposit)
            .observe(viewLifecycleOwner, Observer { depositList ->
                if (depositList.isEmpty()) {
                    binding.tvNoDeposit.visibility = View.VISIBLE
                } else {
                    binding.tvNoDeposit.visibility = View.INVISIBLE
                }
                depositList?.let {
                    adapter.setDepositData(it)
                }
            })
    }

    private fun setupUnfinishedDepositRecyclerView() {
        val recyclerView = binding.rvUnfinishedDeposit
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(requireActivity())
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}