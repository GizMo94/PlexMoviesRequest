package com.fredrikbogg.movie_app.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.fredrikbogg.movie_app.data.model.GoToCredit
import com.fredrikbogg.movie_app.data.model.entity.Credit
import com.fredrikbogg.movie_app.data.model.entity.Movie
import com.fredrikbogg.movie_app.data.model.entity.Torrent
import com.fredrikbogg.movie_app.databinding.ListItemCreditBinding
import com.fredrikbogg.movie_app.databinding.ListItemTorrentBinding

class TorrentListAdapter internal constructor() :
    ListAdapter<(Torrent), TorrentListAdapter.ViewHolder>(TorrentsDiffCallback()) {

    class ViewHolder(private val binding: ListItemTorrentBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Torrent) {
            binding.torrent = item
            binding.executePendingBindings()
        }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = ListItemTorrentBinding.inflate(layoutInflater, parent, false)
        return ViewHolder(binding)
    }

    private class TorrentsDiffCallback : DiffUtil.ItemCallback<Torrent>() {
        override fun areItemsTheSame(oldItem: Torrent, newItem: Torrent): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: Torrent, newItem: Torrent): Boolean {
            return oldItem.name == newItem.name
        }
    }
}
