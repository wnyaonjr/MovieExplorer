package com.appetiserapps.movieexplorer.network

sealed class ResultWrapper<out T> {
    data class Success<out T>(val value: T) : ResultWrapper<T>()
    object Error : ResultWrapper<Nothing>()
    object Loading : ResultWrapper<Nothing>()
}