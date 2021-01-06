package com.amatai.lybra_app.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.amatai.lybra_app.data.repositories.Repository
import java.lang.Exception

class ViewmodelDirectorioFragment(private val repository: Repository):ViewModel() {

    fun obtenerDirecotioSqlite() = repository.obtenerDirectorioSqlite()
}