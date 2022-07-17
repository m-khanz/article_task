package com.task.nytimesarticles.data.repository

import androidx.annotation.MainThread
import androidx.annotation.WorkerThread
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import retrofit2.Response

//DATABASE represents the type for database -> to get data from the database
//REMOTE represents the type for network -> to get data from the network
abstract class NetworkRepository<DATABASE, REMOTE> {

    fun asFlow() = flow<Resource<DATABASE>> {
        // Emit Database content first
        emit(Resource.Success(fetchFromLocal().first()))

        // Fetch latest posts from remote
        val apiResponse = fetchFromRemote()

        // Parse body
        val remotePosts = apiResponse.body()

        if (apiResponse.isSuccessful && remotePosts != null) {
            // save posts into the local storage
            saveRemoteData(remotePosts)
        } else {
            // Something went wrong! Emit Error state.
            emit(Resource.Failed(apiResponse.message()))
        }

        // retrieve posts from local storage and emit
        emitAll(
            fetchFromLocal().map {
                Resource.Success<DATABASE>(it)
            }
        )
    }.catch { e ->
        e.printStackTrace()
        emit(Resource.Failed("Network error!"))
    }

    //save retrieved data from remote into the local storage.
    @WorkerThread
    protected abstract suspend fun saveRemoteData(response: REMOTE)

    //retrieve all data from local storage.
    @MainThread
    protected abstract fun fetchFromLocal(): Flow<DATABASE>

    //fetch Response from the remote
    @MainThread
    protected abstract suspend fun fetchFromRemote(): Response<REMOTE>
}
