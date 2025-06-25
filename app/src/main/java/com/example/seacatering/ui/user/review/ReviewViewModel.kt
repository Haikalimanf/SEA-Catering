package com.example.seacatering.ui.user.review

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.seacatering.model.DataTestimonial
import com.example.seacatering.repository.AuthRepository
import com.example.seacatering.repository.CateringRepository
import com.google.firebase.Timestamp
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class ReviewViewModel @Inject constructor(
    private val cateringRepository: CateringRepository,
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _postReview = MutableStateFlow<Result<Void?>?>(null)
    val postReview: StateFlow<Result<Void?>?> = _postReview

    private val _getReview = MutableLiveData<List<DataTestimonial>>()
    val getReview: LiveData<List<DataTestimonial>> = _getReview

    init {
        getAllTestimonial()
    }

    fun postReview(reviewText: String, rating: Int) {
        viewModelScope.launch {
            val userId = authRepository.getCurrentUserId()
            val user = authRepository.getCurrentUserData()
            if (user != null) {
                val newReview = DataTestimonial(
                    id = UUID.randomUUID().toString(),
                    userId = userId ?: "",
                    username = user.name,
                    reviewText = reviewText,
                    rating = rating,
                    date = Timestamp.now()
                )

                val result = cateringRepository.addTestimonial(newReview)
                _postReview.value = result

                if (result.isSuccess) {
                    val currentList = _getReview.value?.toMutableList() ?: mutableListOf()
                    currentList.add(0, newReview)
                    _getReview.value = currentList
                }
            }
        }
    }


    fun getAllTestimonial() {
        viewModelScope.launch {
            val result = cateringRepository.getAllTestimonials()
            if (result.isSuccess) {
                _getReview.value = result.getOrNull() ?: emptyList()
            } else {
                _getReview.value = emptyList()
            }
        }
    }

    fun resetResult() {
        _getReview.value = null
    }

}