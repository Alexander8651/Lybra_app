package com.amatai.lybra_app.ui.adapters

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.findNavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.amatai.lybra_app.R
import com.amatai.lybra_app.data.DataSources
import com.amatai.lybra_app.data.repositories.RepositoryImpl
import com.amatai.lybra_app.databasemanager.AppDatabase
import com.amatai.lybra_app.databasemanager.entities.VideoEntity
import com.amatai.lybra_app.databinding.VideoitemBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class VideoAdapter:ListAdapter<VideoEntity, VideoAdapter.Viewholder>(VideoDiffUtilsCallback()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VideoAdapter.Viewholder {
        return Viewholder.from(parent)
    }

    override fun onBindViewHolder(holder: VideoAdapter.Viewholder, position: Int) {
        val item = getItem(position)
        holder.bind(item)


    }

    class Viewholder private constructor(private val binding: VideoitemBinding):RecyclerView.ViewHolder(binding.root){

        val datasource = DataSources(AppDatabase.getDatabase(itemView.context)!!)
        val repository = RepositoryImpl(datasource)

        val job = Job()

        val uiScope = CoroutineScope(job + Dispatchers.IO)

        fun bind(item:VideoEntity){
            binding.itemvideo = item
            binding.executePendingBindings()

            binding.playButton.setOnClickListener {
                val bundle = Bundle()
                bundle.putParcelable("archivo", item)
                it.findNavController().navigate(R.id.action_archivosFragment_to_archivosDetalleFragment, bundle)
            }

            binding.deleteButton.setOnClickListener {
                Log.d("videoseliminado", item.toString())
                uiScope.launch {
                    repository.actualizarEstadoVideoSqlite(item)
                }
                it.findNavController().navigateUp()
                Toast.makeText(itemView.context, "Se elimino el video", Toast.LENGTH_SHORT).show()

            }
        }

        companion object{

            fun from(parent: ViewGroup):Viewholder{
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = VideoitemBinding.inflate(layoutInflater, parent, false)
                return Viewholder(binding)
            }
        }
    }



    class VideoDiffUtilsCallback :DiffUtil.ItemCallback<VideoEntity>(){
        override fun areItemsTheSame(oldItem: VideoEntity, newItem: VideoEntity): Boolean {
            return oldItem.llavePrimariaLocal == newItem.llavePrimariaLocal
        }

        override fun areContentsTheSame(oldItem: VideoEntity, newItem: VideoEntity): Boolean {
            return oldItem == newItem
        }

    }
}