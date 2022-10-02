package com.example.fitpho.Database

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class ViewModel(application: Application): AndroidViewModel(application)   {

    val readAllData: LiveData<List<GuideEntity>>
    private val repository: Repository

    init {
        val guideDao = AppDatabase.getDatabase(application)!!.guideDao()
        repository = Repository(guideDao)
        readAllData = repository.readAllData
    }

    class Factory(val application: Application): ViewModelProvider.Factory{
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return com.example.fitpho.Database.ViewModel(application) as T
        }
    }



}