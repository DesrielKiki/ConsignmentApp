    package com.desrielkiki.consignmentapp.ui.store

    import android.os.Bundle
    import android.util.Log
    import android.view.LayoutInflater
    import android.view.Menu
    import android.view.MenuInflater
    import android.view.MenuItem
    import android.view.View
    import android.view.ViewGroup
    import androidx.fragment.app.Fragment
    import androidx.fragment.app.viewModels
    import androidx.lifecycle.Observer
    import androidx.navigation.findNavController
    import androidx.navigation.fragment.navArgs
    import androidx.recyclerview.widget.LinearLayoutManager
    import com.desrielkiki.consignmentapp.R
    import com.desrielkiki.consignmentapp.data.entity.StoreData
    import com.desrielkiki.consignmentapp.databinding.FragmentDetailStoreBinding
    import com.desrielkiki.consignmentapp.ui.deposit.DepositViewModel

    class DetailStoreFragment : Fragment() {

        private val args by navArgs<DetailStoreFragmentArgs>()
        private var _binding: FragmentDetailStoreBinding? = null
        private val binding get() = _binding!!

        private val storeViewModel: StoreViewModel by viewModels()
        private val depositViewModel: DepositViewModel by viewModels()
        private val historyAdapter: HistoryAdapter by lazy { HistoryAdapter() }

        private var storeId: Int = 0

        override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?,
        ): View? {
            _binding = FragmentDetailStoreBinding.inflate(inflater, container, false)
            binding.args = args
            storeViewModel.getStoreData.observe(viewLifecycleOwner, Observer {
                filterDeposit()
            })
            setupHistoryRecyclerView()

            setHasOptionsMenu(true)
            return binding.root
        }

        override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
            inflater.inflate(R.menu.detail_menu, menu)
        }

        override fun onOptionsItemSelected(item: MenuItem): Boolean {
            when (item.itemId) {
                R.id.menu_editStore -> navToEdit()
            }
            return super.onOptionsItemSelected(item)
        }

        private fun filterDeposit() {
            storeId = args.storeData.id
            depositViewModel.filterDeposit(storeId)
                .observe(viewLifecycleOwner, Observer { depositList ->
                    if (depositList.isEmpty()) {
                        binding.tvNoHistory.visibility = View.VISIBLE
                    } else {
                        binding.tvNoHistory.visibility = View.INVISIBLE
                    }
                    depositList?.let {
                        historyAdapter.setHistoryData(it)
                    }
                })
        }
        private fun setupHistoryRecyclerView() {
            val recyclerView = binding.rvDepositHistory
            recyclerView.adapter = historyAdapter
            recyclerView.layoutManager = LinearLayoutManager(requireActivity())
        }

        private fun navToEdit() {
            val sName = binding.tvStoreName.text.toString()
            val sAddress = binding.tvStoreAddress.text.toString()
            val sOwner = binding.tvStoreOwner.text.toString()
            val sContact = binding.tvStoreContact.text.toString()
            val editStoreData =
                StoreData(args.storeData.id, sName, sAddress, sOwner, sContact)
            val action =
                DetailStoreFragmentDirections.actionDetailStoreFragmentToUpdateStoreFragment(
                    editStoreData
                )
            view?.findNavController()?.navigate(action)
        }

        override fun onDestroyView() {
            super.onDestroyView()
            _binding = null
        }
    }

