package com.example.paginationexampleproject.modal.webservice

import com.example.paginationexampleproject.modal.PlaceHolderModal

class DataRepository(private var webService: WebService = WebService()) {
    suspend fun getPlaceHolderData() : MutableList<PlaceHolderModal> {
        return webService.getPlaceHolderList()
    }
}