package com.example.ipartnertest

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.ipartnertest.data.EntriesResponse
import com.example.ipartnertest.databinding.RecordListItemBinding

class RecordsAdapter : ListAdapter<EntriesResponse.Entry, ViewHolder>(RecordsAdapterDiffCallback()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return ViewHolder(RecordListItemBinding.inflate(inflater, parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}

class RecordsAdapterDiffCallback : DiffUtil.ItemCallback<EntriesResponse.Entry>() {
    override fun areItemsTheSame(
        oldItem: EntriesResponse.Entry,
        newItem: EntriesResponse.Entry
    ): Boolean {
        return oldItem === newItem
    }

    override fun areContentsTheSame(
        oldItem: EntriesResponse.Entry,
        newItem: EntriesResponse.Entry
    ): Boolean {
        return oldItem == newItem
    }

}

class ViewHolder(private val binding: RecordListItemBinding) : RecyclerView.ViewHolder(binding.root) {
    fun bind(entry: EntriesResponse.Entry) {
        binding.entry = entry
        binding.executePendingBindings()
    }
}
