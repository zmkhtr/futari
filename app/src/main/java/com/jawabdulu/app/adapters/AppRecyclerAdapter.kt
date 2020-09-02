package com.jawabdulu.app.adaptersimport

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.jawabdulu.app.R
import com.jawabdulu.app.models.App
import kotlinx.android.synthetic.main.item_app_list.view.*

class AppRecyclerAdapter(private val interaction: Interaction? = null) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    lateinit var context: Context
    val DIFF_CALLBACK = object : DiffUtil.ItemCallback<App>() {
        override fun areItemsTheSame(oldItem: App, newItem: App): Boolean {
            return oldItem.packageName == newItem.packageName
        }

        override fun areContentsTheSame(oldItem: App, newItem: App): Boolean {
            return oldItem == newItem
        }
    }
    private val differ = AsyncListDiffer(this, DIFF_CALLBACK)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val vh = AppViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.item_app_list,
                parent,
                false
            ),
            interaction
        )

        context = parent.context
        return vh
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is AppViewHolder -> {
                holder.bind(differ.currentList.get(position))
            }
        }
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    fun submitList(list: List<App>) {
        differ.submitList(list)
    }

    class AppViewHolder
    constructor(
        itemView: View,
        private val interaction: Interaction?
    ) : RecyclerView.ViewHolder(itemView) {
        fun bind(item: App) = with(itemView) {
            itemView.setOnClickListener {
                interaction?.onItemSelected(adapterPosition, item)
            }

            ivAppItemIcon.setImageDrawable(item.iconAplikasi)
            tvAppItemName.text = item.namaAplikasi

            if (item.locked) {
                ivAppItemLockIcon.setImageResource(R.drawable.ic_lock)
            } else {
                ivAppItemLockIcon.setImageResource(R.drawable.ic_unlock)
            }
        }
    }

    interface Interaction {
        fun onItemSelected(position: Int, item: App)
    }
}
