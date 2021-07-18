package com.example.ipartnertest

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.ipartnertest.data.Entry
import com.example.ipartnertest.databinding.RecordListItemBinding

class RecordsAdapter :
    ListAdapter<Entry, RecordsAdapter.ViewHolder>(RecordsAdapterDiffCallback()) {

    private var clickListener: OnItemClickListener? = null
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return ViewHolder(RecordListItemBinding.inflate(inflater, parent, false), clickListener)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    interface OnItemClickListener {
        fun onItemClick(position: Int)
    }

    fun setOnItemClickListener(listener: OnItemClickListener) {
        this.clickListener = listener
    }

    class ViewHolder(
        private val binding: RecordListItemBinding,
        clickListener: OnItemClickListener?
    ) :
        RecyclerView.ViewHolder(binding.root) {

        init {
            binding.card.setOnClickListener {
                if (clickListener != null) {
                    if (adapterPosition != RecyclerView.NO_POSITION) {
                        clickListener.onItemClick(adapterPosition)
                    }
                }
            }
        }

        fun bind(entry: Entry) {
            binding.entry = entry
            binding.executePendingBindings()
        }
    }

    class RecordsAdapterDiffCallback : DiffUtil.ItemCallback<Entry>() {
        override fun areItemsTheSame(
            oldItem: Entry,
            newItem: Entry
        ): Boolean {
            return oldItem === newItem
        }

        override fun areContentsTheSame(
            oldItem: Entry,
            newItem: Entry
        ): Boolean {
            return oldItem == newItem
        }
    }
}



