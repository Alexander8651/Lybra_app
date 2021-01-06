package com.amatai.lybra_app.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.amatai.lybra_app.databasemanager.entities.ContactosEntity
import com.amatai.lybra_app.databinding.ContactoitemBinding

class ContactosAdapter ():ListAdapter<ContactosEntity, ContactosAdapter.Viewholder>(ContactosDiffCallback()){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContactosAdapter.Viewholder {
        return Viewholder.from(parent)
    }

    override fun onBindViewHolder(holder: ContactosAdapter.Viewholder, position: Int) {
        val item = getItem(position)
        holder.bind(item)

    }

    class Viewholder private constructor(var bindingAdapter: ContactoitemBinding): RecyclerView.ViewHolder(bindingAdapter.root){


        fun bind(item:ContactosEntity){
            bindingAdapter.item = item
            bindingAdapter.executePendingBindings()
        }
        companion object{
            fun from (parent: ViewGroup):Viewholder{
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ContactoitemBinding.inflate(layoutInflater)
                return Viewholder(binding)
            }
        }
    }

    class ContactosDiffCallback : DiffUtil.ItemCallback<ContactosEntity>(){
        override fun areItemsTheSame(oldItem: ContactosEntity, newItem: ContactosEntity): Boolean {
            return newItem.idLlavePrimaria == oldItem.idLlavePrimaria
        }

        override fun areContentsTheSame(
            oldItem: ContactosEntity,
            newItem: ContactosEntity
        ): Boolean {
            return  newItem == oldItem
        }

    }
}