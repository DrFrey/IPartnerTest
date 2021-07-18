package com.example.ipartnertest

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.ipartnertest.databinding.RecordsListFragmentBinding
import com.google.android.material.snackbar.Snackbar
import java.lang.ref.WeakReference

class RecordsListFragment : Fragment() {

    private lateinit var refContext: WeakReference<Context>
    private lateinit var adapter: RecordsAdapter

    private val args: RecordsListFragmentArgs by navArgs()

    private val viewModel: RecordsListViewModel by viewModels {
        RecordsListViewModelFactory(refContext)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = RecordsListFragmentBinding.inflate(inflater)
        refContext = WeakReference(requireContext())

        val sharedPref = requireContext().getSharedPreferences(
            getString(R.string.preferences_file_name),
            Context.MODE_PRIVATE
        )
        viewModel.session = sharedPref.getString(getString(R.string.session_key), "").toString()
        if (viewModel.session.isEmpty()) {
            viewModel.newSession()
        } else {
            viewModel.getEntries()
        }
        viewModel.sessionObtained.observe(viewLifecycleOwner, {
            if (it == true) {
                with(sharedPref.edit()) {
                    putString(getString(R.string.session_key), viewModel.session)
                    apply()
                }
            }
        })

        adapter = RecordsAdapter()
        binding.recyclerview.adapter = adapter
        viewModel.entries.observe(viewLifecycleOwner, {
            adapter.submitList(it)
            if (it.isEmpty()) {
                AlertDialog.Builder(requireContext())
                    .setTitle("No entries yet")
                    .setMessage("Click the button below to add a new entry")
                    .setPositiveButton("Ok") { dialog, _ -> dialog.dismiss() }
                    .show()
            }
        })

        binding.addRecord.setOnClickListener {
            findNavController().navigate(RecordsListFragmentDirections.actionRecordsListFragmentToAddEntryFragment())
        }
        if (!args.newEntryData.isNullOrEmpty()) {
            viewModel.addEntry(args.newEntryData.toString())
        }

        viewModel.error.observe(viewLifecycleOwner, {
            if (it.isNotEmpty()) {
                Snackbar.make(binding.recordsListFragment, it, Snackbar.LENGTH_SHORT).show()
            }
        })

        viewModel.isLoading.observe(viewLifecycleOwner, {
            if (it == true) {
                binding.progressCircular.visibility = View.VISIBLE
            } else {
                binding.progressCircular.visibility = View.GONE
            }
        })

        return binding.root
    }
}