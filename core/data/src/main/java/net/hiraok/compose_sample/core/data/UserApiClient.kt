package net.hiraok.compose_sample.core.data

import retrofit2.Retrofit

class UserApiClient(
    private val retrofit: Retrofit
) {

    private val userApiService: UserApiService by lazy {
        retrofit.create(UserApiService::class.java)
    }

    suspend fun users(): UsersResponse {
        return userApiService.users()
    }

    suspend fun userById(userId: String): UserDetailResponse {
        return userApiService.userById(userId)
    }
}