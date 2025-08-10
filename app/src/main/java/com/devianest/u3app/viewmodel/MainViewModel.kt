package com.devianest.u3app.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.devianest.u3app.domain.SliderModel
import com.devianest.u3app.repository.MainRepository

class MainViewModel():ViewModel(){
    private val repository=MainRepository()

    fun loadBanner():LiveData<MutableList<SliderModel>>{
        return repository.loadBanner()
    }
}