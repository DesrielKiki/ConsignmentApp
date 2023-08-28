package com.desrielkiki.consignmentapp.ui.deposit

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.desrielkiki.consignmentapp.R
import com.desrielkiki.consignmentapp.data.ConsignmentDao
import com.desrielkiki.consignmentapp.data.ConsignmentDatabase
import com.desrielkiki.consignmentapp.databinding.FragmentDetailDepositBinding
import com.desrielkiki.consignmentapp.ui.SharedViewModel
import com.desrielkiki.consignmentapp.ui.product.ProductViewModel

class DetailDepositFragment : Fragment() {


    private val args by navArgs<DetailDepositFragmentArgs>()
    private var _binding: FragmentDetailDepositBinding? = null
    private val binding get() = _binding!!
    private val depositViewModel: DepositViewModel by viewModels()
    private val productViewModel: ProductViewModel by viewModels()
    private val sharedViewModel: SharedViewModel by viewModels()
    private val productAdapter: DepositProductAdapter by lazy { DepositProductAdapter() }

    private lateinit var consignmentDatabase: ConsignmentDatabase
    private lateinit var consignmentDao: ConsignmentDao
    private var depositId: Long = 0L

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentDetailDepositBinding.inflate(inflater, container, false)
        binding.args = args


        consignmentDatabase = ConsignmentDatabase.getDatabase(requireContext())
        consignmentDao = consignmentDatabase.consignmentDao()


        depositViewModel.getDepositProductData.observe(
            viewLifecycleOwner,
            Observer { depositProduct ->
                productViewModel.getProductData.observe(
                    viewLifecycleOwner,
                    Observer { productData ->
                        if (depositProduct.isNotEmpty() && productData.isNotEmpty()) {
                            val depositRevenue = depositViewModel.calculateDepositRevenue(
                                depositProduct,
                                productData,
                                args.depositWithStoreData.depositData.id
                            )
                            val formattedRevenue =
                                sharedViewModel.formatRupiah(depositRevenue.toInt())
                            binding.tvDepositRevenue.text = formattedRevenue
                        }
                    })
            })
        depositViewModel.getDepositProductData.observe(viewLifecycleOwner, Observer {
            filterProduct()
        })

        setupProductRecyclerView()

        setHasOptionsMenu(true)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        args.depositWithStoreData.depositData.id
        super.onViewCreated(view, savedInstanceState)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.deposit_detail_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_finishDeposit -> navToFinishDeposit()
            R.id.menu_deleteDeposit -> deleteDeposit()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun filterProduct() {
        depositId = args.depositWithStoreData.depositData.id
        depositViewModel.filterProduct(depositId)
            .observe(viewLifecycleOwner, Observer { productList ->
                if (productList.isEmpty()) {
                    depositViewModel.deleteDepositAndProduct(depositId)
                    Toast.makeText(
                        requireContext(),
                        "deposit doesn't have product and automatically deleted",
                        Toast.LENGTH_SHORT
                    ).show()
                    findNavController().navigate(R.id.action_detailDepositFragment_to_depositFragment)
                } else {
                    productList?.let {
                        productAdapter.setProductData(it)
                    }
                }
            })
    }

    private fun depositRevenue() {

    }

    private fun setupProductRecyclerView() {
        val recyclerView = binding.rvDepositedProduct
        recyclerView.adapter = productAdapter
        recyclerView.layoutManager = LinearLayoutManager(requireActivity())
    }

    private fun navToFinishDeposit() {
        val depositData = args.depositWithStoreData
        val action =
            DetailDepositFragmentDirections.actionDetailDepositFragmentToFinishDepositFragment(
                depositData
            )
        view?.findNavController()?.navigate(action)
    }

    private fun deleteDeposit() {
        val builder = AlertDialog.Builder(requireContext())
        builder.setPositiveButton("yes") { _, _ ->
            depositViewModel.deleteDepositAndProduct(depositId)
            Toast.makeText(
                requireContext(),
                "successfully remove deposit'",
                Toast.LENGTH_SHORT
            ).show()
            findNavController().navigate(R.id.action_detailDepositFragment_to_depositFragment)
        }
        builder.setNegativeButton("No") { _, _ -> }
        builder.setIcon(R.drawable.ic_warning)
        builder.setTitle("DELETE DEPOSIT")
        builder.setMessage("are you sure you want to delete this deposit?")
        builder.create().show()
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
}

