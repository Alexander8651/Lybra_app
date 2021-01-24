package com.amatai.lybra_app.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.amatai.lybra_app.data.repositories.Repository

class ViewmodelReporteFragment(private val repository: Repository):ViewModel() {

    val reportes = liveData {
        try {
            emit(repository.obtenerReportesSqlite())
        }catch (e:Exception){

        }
    }
}