package com.amatai.lybra_app.ui.adapters

import android.media.MediaPlayer
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.MediaController
import android.widget.Toast
import androidx.navigation.findNavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.amatai.lybra_app.R
import com.amatai.lybra_app.data.DataSources
import com.amatai.lybra_app.data.repositories.RepositoryImpl
import com.amatai.lybra_app.databasemanager.AppDatabase
import com.amatai.lybra_app.databasemanager.entities.AudioEntity
import com.amatai.lybra_app.databasemanager.entities.VideoEntity
import com.amatai.lybra_app.databinding.ItemaudioBinding
import com.amatai.lybra_app.databinding.VideoitemBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class AudioAdapter:ListAdapter<AudioEntity, AudioAdapter.Viewholder>(AudioDiffUtilsCallback()), MediaPlayer.OnCompletionListener {

    class AudioDiffUtilsCallback :DiffUtil.ItemCallback<AudioEntity>(){
        override fun areItemsTheSame(oldItem: AudioEntity, newItem: AudioEntity): Boolean {
            return oldItem.llavePrimariaLocal == newItem.llavePrimariaLocal
        }

        override fun areContentsTheSame(oldItem: AudioEntity, newItem: AudioEntity): Boolean {
            return oldItem == newItem
        }

    }

    override fun onBindViewHolder(holder: Viewholder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AudioAdapter.Viewholder {
        return Viewholder.from(parent)
    }




    class Viewholder private constructor(private val binding: ItemaudioBinding):RecyclerView.ViewHolder(binding.root){

        val datasource = DataSources(AppDatabase.getDatabase(itemView.context)!!)
        val repository = RepositoryImpl(datasource)


        fun bind(item:AudioEntity){
            val uri = Uri.parse(item.path)
            val mediaPlayer:MediaPlayer = MediaPlayer.create(itemView.context, uri)
            binding.itemaudio = item
            binding.executePendingBindings()

            val datasource = DataSources(AppDatabase.getDatabase(itemView.context)!!)
            val repository = RepositoryImpl(datasource)

            val job = Job()

            val uiScope = CoroutineScope(job + Dispatchers.IO)

            binding.playButton.setOnClickListener {
                Log.d("auddididi", item.path!!)
                mediaPlayer.start()
            }

            binding.pauseButton.setOnClickListener {
                mediaPlayer.stop()

            }
            binding.deleteButton.setOnClickListener {
                Log.d("videoseliminado", item.toString())
                uiScope.launch {

                    repository.actualizarEstadoAudioSqlite(item)
                }
                //it.findNavController().navigateUp()
                Toast.makeText(itemView.context, "Se elimino el audio", Toast.LENGTH_SHORT).show()
                it.findNavController().navigateUp()
            }


        }

        companion object{

            fun from(parent: ViewGroup):Viewholder{
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ItemaudioBinding.inflate(layoutInflater, parent, false)
                return Viewholder(binding)
            }
        }
    }

    override fun onCompletion(mp: MediaPlayer?) {
        TODO("Not yet implemented")
    }


}