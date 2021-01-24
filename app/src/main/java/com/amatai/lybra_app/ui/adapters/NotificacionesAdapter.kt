package com.amatai.lybra_app.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.amatai.lybra_app.databinding.NotificacionesitemBinding
import com.amatai.lybra_app.requestmanager.apiresponses.Notification

class NotificacionesAdapter : ListAdapter<Notification, NotificacionesAdapter.Viewholder> (NotificacionDiffCallback()){
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): NotificacionesAdapter.Viewholder {
        return  Viewholder.from(parent)
    }

    override fun onBindViewHolder(holder: NotificacionesAdapter.Viewholder, position: Int) {
        val item = getItem(position)
        holder.bind(item)


    }

    class Viewholder private constructor(private val binding:NotificacionesitemBinding):RecyclerView.ViewHolder(binding.root){

        fun bind(item:Notification){
            binding.notificacion = item
            binding.executePendingBindings()
        }



        companion object{
            fun from(parent: ViewGroup):Viewholder{
                val layoutInflater = LayoutInflater.from(parent.context)
                val binbing = NotificacionesitemBinding.inflate(layoutInflater,parent, false)
                return Viewholder(binbing)
            }
        }

    }

    class NotificacionDiffCallback : DiffUtil.ItemCallback<Notification>(){
        override fun areItemsTheSame(oldItem: Notification, newItem: Notification): Boolean {
            return oldItem.created_at == newItem.created_at
        }

        override fun areContentsTheSame(oldItem: Notification, newItem: Notification): Boolean {
            return oldItem == newItem
        }

    }
}