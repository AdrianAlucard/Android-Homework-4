package com.example.hw4_favoriteanimal

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class MainViewModel: ViewModel() {

    val animalToRatingFrag = MutableLiveData<AnimalDetails>()
    val animalToImageFrag = MutableLiveData<AnimalDetails>()
}