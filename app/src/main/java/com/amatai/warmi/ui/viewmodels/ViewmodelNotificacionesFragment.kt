package com.amatai.warmi.ui.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.amatai.warmi.data.repositories.Repository
import com.amatai.warmi.ui.adapters.ApiStatus
import com.amatai.warmi.ui.fragments.MainFragment
import java.lang.Exception

class ViewmodelNotificacionesFragment(private val repository: Repository) : ViewModel() {

    val _apiStatus = MutableLiveData<ApiStatus>()

    val status: LiveData<ApiStatus>
        get() = _apiStatus


    fun obtenerNotificaciones() = liveData {

        try {

            _apiStatus.value = ApiStatus.LOADING
            val notificaciones = repository.listarNotificaciones(MainFragment.sessionLogueo!!)

            _apiStatus.value = ApiStatus.DONE

            emit(notificaciones)


        } catch (e: Exception) {

        }
    }
}