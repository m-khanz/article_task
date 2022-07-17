package com.task.nytimesarticles.model

import com.task.nytimesarticles.data.repository.Resource

sealed class States<T> {
    data class Success<T>(val data: T) : States<T>()
    class Loading<T> : States<T>()//during data fetching-> show refresh loader
    data class Error<T>(val message: String) : States<T>()

    companion object {
        fun <T> loading() = Loading<T>()

        private fun <T> success(data: T) =
            Success(data)

        private fun <T> error(message: String) =
            Error<T>(message)

        fun <T> fromResource(resource: Resource<T>): States<T> = when (resource) {
            is Resource.Success -> success(resource.data)
            is Resource.Failed -> error(resource.message)
        }
    }
}
