package com.example.ipartnertest

import android.os.Bundle
import android.text.method.ScrollingMovementMethod
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.example.ipartnertest.databinding.RecordFragmentBinding

class RecordFragment : Fragment() {

    private val args: RecordFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = RecordFragmentBinding.inflate(inflater)
        binding.body.movementMethod = ScrollingMovementMethod()
        binding.entry = args.entryDetails
        return binding.root
    }

}