package com.desrielkiki.consignmentapp.ui.store

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
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.desrielkiki.consignmentapp.R
import com.desrielkiki.consignmentapp.data.entity.StoreData
import com.desrielkiki.consignmentapp.data.entity.relation.DepositWithStoreData
import com.desrielkiki.consignmentapp.databinding.FragmentUpdateStoreBinding

class UpdateStoreFragment : Fragment() {

    private val args by navArgs<UpdateStoreFragmentArgs>()
    private var _binding: FragmentUpdateStoreBinding? = null
    private val binding get() = _binding!!
    private val sViewModel: StoreViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentUpdateStoreBinding.inflate(inflater, container, false)
        binding.args = args
        setHasOptionsMenu(true)

        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.update_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_delete -> confirmStoreDelete()
            R.id.menu_save -> updateStore()

        }
        return super.onOptionsItemSelected(item)
    }

    private fun confirmStoreDelete() {
        val builder = AlertDialog.Builder(requireContext())
        builder.setPositiveButton("yes") { _, _ ->
            sViewModel.deleteStore(args.storeData)
            Toast.makeText(
                requireContext(),
                "succesfully removed: '${args.storeData.storeName}'",
                Toast.LENGTH_SHORT
            ).show()
            findNavController().navigate(R.id.action_updateStoreFragment_to_storeFragment)
        }
        builder.setNegativeButton("No") { _, _ -> }
        builder.setIcon(R.drawable.ic_warning)
        builder.setTitle("DELETE '${args.storeData.storeName}'")
        builder.setMessage("are you sure you want to delete this store?")
        builder.create().show()
    }

    private fun updateStore() {
        val storeName = binding.etStoreName.text.toString()
        val storeAddress = binding.etStoreAddress.text.toString()
        val storeOwner = binding.etStoreOwner.text.toString()
        val storeContact = binding.etStoreContact.text.toString()

        val validation =
            sViewModel.verifyDataFromUser(storeName, storeAddress, storeOwner, storeContact)
        if (validation) {
            val updatedStore = args.storeData.copy(
                id = args.storeData.id,
                storeName = storeName,
                storeAddress = storeAddress,
                storeOwner = storeOwner,
                storeContact = storeContact
            )
            sViewModel.updateStore(updatedStore)
            Toast.makeText(requireContext(), "succesfully update!", Toast.LENGTH_SHORT).show()
            val action =
                UpdateStoreFragmentDirections.actionUpdateStoreFragmentToStoreFragment(
                )
            view?.findNavController()?.navigate(action)
        } else {
            Toast.makeText(requireContext(), "Please fill in all the fields", Toast.LENGTH_SHORT)
                .show()
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}