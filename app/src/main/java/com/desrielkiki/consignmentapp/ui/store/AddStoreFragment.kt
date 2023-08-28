package com.desrielkiki.consignmentapp.ui.store

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.desrielkiki.consignmentapp.R
import com.desrielkiki.consignmentapp.data.entity.StoreData
import com.desrielkiki.consignmentapp.databinding.FragmentAddStoreBinding

class AddStoreFragment : Fragment() {

    private val mStoreViewModel: StoreViewModel by viewModels()

    private var _binding: FragmentAddStoreBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentAddStoreBinding.inflate(inflater, container, false)


        binding.btnAddStore.setOnClickListener {
            insertDataToDb()
        }
        return binding.root
    }

    private fun insertDataToDb() {
        val sName = binding.etStoreName.text.toString()
        val sAddress = binding.etStoreAddres.text.toString()
        val sOwner = binding.etStoreOwner.text.toString()
        val sContact = binding.etStoreContact.text.toString()

        val validation = mStoreViewModel.verifyDataFromUser(sName, sAddress, sOwner, sContact)
        if (validation) {
            val newData = StoreData(
                0,
                sName,
                sAddress,
                sOwner,
                sContact
            )
            mStoreViewModel.insertStore(newData)
            Toast.makeText(requireContext(), "Succesfully added!", Toast.LENGTH_SHORT).show()
            findNavController().navigate(R.id.action_addStoreFragment_to_storeFragment)
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