package com.example.composeskt.viewmodel

import android.util.Log
import android.widget.Toast
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import com.example.composeskt.MainActivity
import com.example.composeskt.data.models.CurrencyData
import com.example.composeskt.repository.CurrencyRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class CurrencyViewModel(private val currencyRepository : CurrencyRepository) : ViewModel() {
    private val _apiResult: MutableLiveData<CurrencyData> by lazy {
        MutableLiveData<CurrencyData>()
    }
    val apiResult: LiveData<CurrencyData> get() =
        _apiResult
    fun getCurrencyData(currencyCode : String) {
        CoroutineScope(Dispatchers.IO).launch {
            val defrredResult = async { currencyRepository.getCurrencyRates(currencyCode) }.await()
            when(defrredResult.code()){
                200,201 -> {
                    _apiResult.postValue(defrredResult.body())
                }
            }
        }

    }
}

class CurrencyViewModelFactory(private val currencyRepository: CurrencyRepository): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>, extras: CreationExtras): T {
        return CurrencyViewModel(currencyRepository) as T
    }
}