package com.appetiserapps.movieexplorer.repository.handler

import com.appetiserapps.movieexplorer.network.ResultWrapper
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import timber.log.Timber

/**
 * Class for handling network requests that uses flow
 */
interface FlowRequestHandler {
    fun <T> safeApiCall(
        apiCall: suspend () -> T
    ): Flow<ResultWrapper<T>>
}

/**
 * Definition of handling flow network requests, used through delegate
 */
object DefaultFlowRequestHandler : FlowRequestHandler {
    override fun <T> safeApiCall(
        apiCall: suspend () -> T
    ): Flow<ResultWrapper<T>> = flow {
        emit(ResultWrapper.Loading)
        val response = apiCall.invoke()
        emit(
            ResultWrapper.Success(response)
        )
    }.catch { throwable ->
        throwable.printStackTrace()
        Timber.d(throwable)
        emit(ResultWrapper.Error)
    }
}
