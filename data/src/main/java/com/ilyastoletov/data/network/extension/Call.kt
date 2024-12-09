package com.ilyastoletov.data.network.extension

import com.ilyastoletov.domain.model.exception.ResponseException
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

internal suspend inline fun <T : Any> Call<T>.awaitResult(): Result<T> =
     suspendCoroutine { continuation ->
        this.enqueue(
            object : Callback<T> {

                override fun onFailure(call: Call<T>, error: Throwable) {
                    continuation.resume(Result.failure(error))
                }

                override fun onResponse(call: Call<T>, response: Response<T>) {
                    continuation.resume(
                        if (response.isSuccessful) {
                            Result.success(response.body()!!)
                        } else {
                            Result.failure(
                                ResponseException(
                                    message = response.errorBody()?.string() ?: "Unknown error"
                                )
                            )
                        }
                    )
                }

            }
        )
    }