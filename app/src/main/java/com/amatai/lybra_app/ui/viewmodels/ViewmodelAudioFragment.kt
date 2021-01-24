package com.amatai.lybra_app.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.amatai.lybra_app.data.repositories.Repository

class ViewmodelAudioFragment(private val repository: Repository) :ViewModel() {

    fun obtenerAudios ()= liveData {
        try {
            emit(repository.obtenerAudios())

        }catch (e:Exception){

        }
    }
}