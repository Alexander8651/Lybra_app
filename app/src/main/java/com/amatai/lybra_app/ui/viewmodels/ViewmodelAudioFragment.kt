package com.amatai.lybra_app.ui.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.amatai.lybra_app.data.repositories.Repository
import com.amatai.lybra_app.databasemanager.entities.AudioEntity
import com.amatai.lybra_app.databasemanager.entities.VideoEntity
import com.amatai.lybra_app.databasemanager.toVideoVisible

class ViewmodelAudioFragment(private val repository: Repository) :ViewModel() {

    fun obtenerAudios ()= liveData {
        try {

            val audio = repository.obtenerAudios()

            val audios = mutableListOf<AudioEntity>()



            for (i in audio) {

                Log.d("videos", i.toString())
                if (i.estado == 1) {
                    audios.add(i)
                }

                if (i.estado == 2) {

                    repository.actualizarEstadoAudioSqlite(i)
                }

                if (i.estado == 3) {
                    audios.remove(i)
                }
            }

            emit(audios)

        }catch (e:Exception){

        }
    }
}