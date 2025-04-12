package com.hillbeater.bookxpert.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class PdfFragmentViewModel : ViewModel() {

    private val _pdfUrl = MutableLiveData<String>()
    val pdfUrl: LiveData<String> get() = _pdfUrl

    private val _isLoading = MutableLiveData<Boolean>(true)
    val isLoading: LiveData<Boolean> get() = _isLoading

    fun loadPdf(url: String) {
        _pdfUrl.value = url
        _isLoading.value = true
    }

    fun setLoading(isLoading: Boolean) {
        _isLoading.value = isLoading
    }
}