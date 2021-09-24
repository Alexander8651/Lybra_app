package com.amatai.warmi.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.amatai.warmi.data.repositories.Repository

class VMFactory ( private val repository:Repository):ViewModelProvider.Factory{
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return  modelClass.getConstructor(Repository::class.java).newInstance(repository)
    }

}