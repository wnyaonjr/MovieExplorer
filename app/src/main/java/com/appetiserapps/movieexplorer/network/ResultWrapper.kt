package com.appetiserapps.movieexplorer.network

/**
 * Wrapper class for network request status
 */
sealed class ResultWrapper<out T> {
    data class Success<out T>(val value: T) : ResultWrapper<T>()
    object Error : ResultWrapper<Nothing>()
    object Loading : ResultWrapper<Nothing>()
}