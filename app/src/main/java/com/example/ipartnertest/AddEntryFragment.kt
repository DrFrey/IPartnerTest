package com.example.ipartnertest

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.ipartnertest.databinding.AddEntryFragmentBinding

class AddEntryFragment : Fragment() {
    lateinit var binding: AddEntryFragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = AddEntryFragmentBinding.inflate(inflater)

        binding.addButton.setOnClickListener {
            val text = binding.newEntryText.text.toString()
            if (text.isNotEmpty()) {
                findNavController().navigate(AddEntryFragmentDirections.actionAddEntry(text))
            } else {
                AlertDialog.Builder(requireContext())
                    .setTitle("Empty entry")
                    .setMessage("Your entry is empty. Do you wish to go back to the list of entries?")
                    .setPositiveButton(
                        "Yes"
                    ) { _, _ ->
                        findNavController().navigate(
                            AddEntryFragmentDirections.actionCancelEntry(null)
                        )
                    }
                    .setNegativeButton("No") { dialog, _ -> dialog.dismiss() }
                    .show()
            }
        }

        binding.cancelButton.setOnClickListener {
            findNavController().navigate(
                AddEntryFragmentDirections.actionCancelEntry(null)
            )
        }
        return binding.root
    }
}