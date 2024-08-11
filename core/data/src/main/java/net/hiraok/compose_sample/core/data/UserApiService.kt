package net.hiraok.compose_sample.core.data

import retrofit2.http.GET
import retrofit2.http.Path

interface UserApiService {
    @GET("/users")
    suspend fun users(): UsersResponse

    @GET("users/{userId}")
    suspend fun userById(@Path("userId") userId: String): UserDetailResponse
}