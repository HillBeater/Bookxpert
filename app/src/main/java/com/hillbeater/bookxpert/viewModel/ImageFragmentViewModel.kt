package com.hillbeater.bookxpert.viewModel

import android.graphics.Bitmap
import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class ImageFragmentViewModel : ViewModel() {

    private val _selectedImageUri = MutableLiveData<Uri?>()
    val selectedImageUri: LiveData<Uri?> = _selectedImageUri

    private val _capturedImage = MutableLiveData<Bitmap?>()
    val capturedImage: LiveData<Bitmap?> = _capturedImage

    fun setSelectedImage(uri: Uri?) {
        _selectedImageUri.value = uri
    }

    fun setCapturedImage(bitmap: Bitmap?) {
        _capturedImage.value = bitmap
    }
}