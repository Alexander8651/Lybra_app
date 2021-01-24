package com.amatai.lybra_app.ui.adapters

import android.location.Address
import android.location.Geocoder
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.amatai.lybra_app.databasemanager.entities.ReportesEntity
import com.amatai.lybra_app.databinding.ItemreporteBinding
import com.amatai.lybra_app.ui.fragments.ReporteEnviadosFragment
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import java.lang.Exception
import java.util.*

class ReporteAdapter : ListAdapter<ReportesEntity, ReporteAdapter.Viewholder>(RepoteDiffCallback()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReporteAdapter.Viewholder {
        return Viewholder.from(parent)
    }

    override fun onBindViewHolder(holder: ReporteAdapter.Viewholder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
    }

    class Viewholder private constructor(private val binding: ItemreporteBinding):RecyclerView.ViewHolder(binding.root){

        fun bind(item:ReportesEntity){
            binding.item = item
            binding.executePendingBindings()
        }

        companion object{
            fun from(parent: ViewGroup):Viewholder{
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ItemreporteBinding.inflate(layoutInflater, parent, false)
                return Viewholder(binding)
            }
        }

    }

    class RepoteDiffCallback :DiffUtil.ItemCallback<ReportesEntity>(){
        override fun areItemsTheSame(oldItem: ReportesEntity, newItem: ReportesEntity): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: ReportesEntity, newItem: ReportesEntity): Boolean {
            return oldItem== newItem
        }

    }
}