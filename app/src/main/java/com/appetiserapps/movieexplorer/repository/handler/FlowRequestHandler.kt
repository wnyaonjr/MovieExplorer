package com.appetiserapps.movieexplorer.repository.handler

import com.appetiserapps.movieexplorer.network.ResultWrapper
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import timber.log.Timber

interface FlowRequestHandler {
    fun <T> safeApiCall(
        apiCall: suspend () -> T
    ): Flow<ResultWrapper<T>>
}

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
