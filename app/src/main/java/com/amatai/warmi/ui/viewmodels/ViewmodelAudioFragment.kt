package com.amatai.warmi.ui.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.amatai.warmi.data.repositories.Repository
import com.amatai.warmi.databasemanager.entities.AudioEntity

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