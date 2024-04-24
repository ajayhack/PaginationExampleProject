package com.example.paginationexampleproject.viewmodal
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.paginationexampleproject.modal.PlaceHolderModal
import com.example.paginationexampleproject.modal.webservice.DataRepository
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch

class AppViewModal(private val dataRepository: DataRepository = DataRepository()) : ViewModel() {

    //PlaceHolder List Flow Data:-
    private val placeHolderPageData = MutableSharedFlow<MutableList<PlaceHolderModal>>()
    val placeHolderPageFlowData = placeHolderPageData.asSharedFlow()

    suspend fun getPlaceHolderPageData() {
        viewModelScope.launch {
            placeHolderPageData.emit(dataRepository.getPlaceHolderData())
        }
    }
}