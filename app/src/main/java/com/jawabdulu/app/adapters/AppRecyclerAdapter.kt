package com.jawabdulu.app.adaptersimport

import androidx.recyclerview.widget.RecyclerView
import com.jawabdulu.app.R
import com.jawabdulu.app.models.AppModel

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import kotlinx.android.synthetic.main.item_app_list.view.*

class AppRecyclerAdapter(private val interaction: Interaction? = null) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    val DIFF_CALLBACK = object : DiffUtil.ItemCallback<AppModel>() {
        override fun areItemsTheSame(oldItem: AppModel, newItem: AppModel): Boolean {
            return oldItem.packageName == newItem.packageName
        }

        override fun areContentsTheSame(oldItem: AppModel, newItem: AppModel): Boolean {
            return oldItem == newItem
        }
    }
    private val differ = AsyncListDiffer(this, DIFF_CALLBACK)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return AppViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.item_app_list,
                parent,
                false
            ),
            interaction
        )
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

    fun submitList(list: List<AppModel>) {
        differ.submitList(list)
    }

    class AppViewHolder
    constructor(
        itemView: View,
        private val interaction: Interaction?
    ) : RecyclerView.ViewHolder(itemView) {
        fun bind(item: AppModel) = with(itemView) {
            itemView.setOnClickListener {
                interaction?.onItemSelected(adapterPosition, item)
            }

            ivAppItemIcon.setImageDrawable(item.iconAplikasi)
            tvAppItemName.text = item.namaAplikasi
        }
    }

    interface Interaction {
        fun onItemSelected(position: Int, item: AppModel)
    }
}
