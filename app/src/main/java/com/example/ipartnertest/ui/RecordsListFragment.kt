package com.example.ipartnertest.ui

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.ipartnertest.R
import com.example.ipartnertest.databinding.RecordsListFragmentBinding
import com.google.android.material.snackbar.Snackbar
import java.lang.ref.WeakReference

class RecordsListFragment : Fragment(), RecordsAdapter.OnItemClickListener {

    private lateinit var refContext: WeakReference<Context>
    private lateinit var adapter: RecordsAdapter

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

        //Check if it is the first launch. If no session is present in shared_prefs, get a new session and save it.
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

        //Assign adapter with the list of records.
        adapter = RecordsAdapter()
        binding.recyclerview.adapter = adapter

        viewModel.entries.observe(viewLifecycleOwner, {
            adapter.submitList(it)
            if (it.isEmpty()) {
                binding.emptyListText.visibility = View.VISIBLE
            } else {
                binding.emptyListText.visibility = View.GONE
            }
        })
        adapter.setOnItemClickListener(this)

        //Add a new entry and check the returned arguments. If the arguments contain a new record, add it to the list.
        //Clear the arguments afterwards.
        binding.addRecord.setOnClickListener {
            findNavController().navigate(RecordsListFragmentDirections.actionRecordsListFragmentToAddEntryFragment())
        }
        val args = RecordsListFragmentArgs.fromBundle(requireArguments())
        if (!args.newEntryData.isNullOrEmpty()) {
            viewModel.addEntry(args.newEntryData.toString())
            arguments?.clear()
        }


        //Check if there are any errors and display them.
        viewModel.error.observe(viewLifecycleOwner, {
            if (it.isNotEmpty()) {
                if (it.equals("No internet")) {
                    AlertDialog.Builder(requireContext())
                        .setTitle("No Internet")
                        .setMessage("Check your internet connection and try again")
                        .setPositiveButton(
                            "Refresh"
                        ) { _, _ ->
                            viewModel.getEntries() }
                        .show()
                } else {
                    Snackbar.make(binding.recordsListFragment, it, Snackbar.LENGTH_SHORT).show()
                }
            }
        })

        //Display progressbar.
        viewModel.isLoading.observe(viewLifecycleOwner, {
            if (it == true) {
                binding.progressCircular.visibility = View.VISIBLE
            } else {
                binding.progressCircular.visibility = View.GONE
            }
        })

        return binding.root
    }

    override fun onItemClick(position: Int) {
        val entry = viewModel.entries.value?.get(position)
        entry?.let {
            findNavController().navigate(
                RecordsListFragmentDirections.actionRecordsListFragmentToRecordFragment(
                    it
                )
            )
        }
    }
}