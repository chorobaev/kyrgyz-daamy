package io.flaterlab.kyrgyzdaamy.ui.discount

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.storage.FirebaseStorage
import dagger.hilt.android.lifecycle.HiltViewModel
import io.flaterlab.kyrgyzdaamy.service.ApiResult
import io.flaterlab.kyrgyzdaamy.service.NetworkRepositoryMod
import io.flaterlab.kyrgyzdaamy.service.model.BaseResponse
import io.flaterlab.kyrgyzdaamy.service.response.NewsResponse
import io.flaterlab.kyrgyzdaamy.ui.FirebaseRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject


@ExperimentalCoroutinesApi
@HiltViewModel
class NewsViewModel @Inject constructor(private val firebaseRepository: FirebaseRepository) :
    ViewModel() {

    private var newsFlow: MutableStateFlow<List<NewsResponse>> = MutableStateFlow(arrayListOf())
    val newsStateFlow: StateFlow<List<NewsResponse>>
        get() = newsFlow

    init {
        viewModelScope.launch {
            firebaseRepository.getNews()
                .catch { e ->
                    e.message?.let { Log.d("Exception fetching news", it) }
                }
                .collect {
                    newsFlow.value = it
                }
        }
    }


}