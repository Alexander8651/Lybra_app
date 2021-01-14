package com.amatai.lybra_app.ui.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import com.amatai.lybra_app.data.repositories.Repository
import com.amatai.lybra_app.databasemanager.entities.VideoEntity
import com.amatai.lybra_app.databasemanager.toVideoVisible
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat

class ViewmodelArchivosFragment(private val repository: Repository) : ViewModel() {

    private val FILENAME_FORMAT = "yyyy-MM-dd"


    fun obtenerVideosSqlite() = liveData {
        try {
            val video = repository.obtenerVideosSqlite()

            val videos = mutableListOf<VideoEntity>()



            for (i in video) {

                Log.d("videos", i.toString())
                if (i.estado == 1) {
                    videos.add(i)
                }

                val nombre = i.path!!.subSequence(55..64)
                // Log.d("substring", nombre.toString())

                val fecha = SimpleDateFormat(FILENAME_FORMAT).format(System.currentTimeMillis())
                //Log.d("substring", fecha)

                if (nombre != fecha) {
                    repository.actualizarEstadoVideoSqlite(i.toVideoVisible())
                }



                if (i.estado == 3) {
                    videos.remove(i)
                }


            }
            emit(videos)
        } catch (e: Exception) {

        }
    }

    fun actualizarEstadoVideo(videoEntity: VideoEntity) {
        viewModelScope.launch {
            repository.actualizarEstadoVideoSqlite(videoEntity)
        }
    }
}