package com.example.core.utils.data

import com.example.core.utils.result.Result
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map

abstract class BaseRepository {
    protected fun <Local, Remote> networkBoundResource(
        query: () -> Flow<Local>,
        fetch: suspend () -> Result<Remote>,
        saveFetchResult: suspend (Remote) -> Unit,
        shouldFetch: (Local) -> Boolean = { true }
    ): Flow<Result<Local>> = flow {
        val cached = query().first()
        if (shouldFetch(cached)) {
            when (val remote = fetch()) {
                is Result.Success -> saveFetchResult(remote.data)
                is Result.Error   -> emit(remote)
            }
        }
        emitAll(query().map { Result.Success(it) })
    }
}
