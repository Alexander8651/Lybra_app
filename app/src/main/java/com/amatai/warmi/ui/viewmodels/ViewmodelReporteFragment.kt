package com.amatai.warmi.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.amatai.warmi.data.repositories.Repository

class ViewmodelReporteFragment(private val repository: Repository):ViewModel() {

    val reportes = liveData {
        try {
            emit(repository.obtenerReportesSqlite())
        }catch (e:Exception){

        }
    }
}