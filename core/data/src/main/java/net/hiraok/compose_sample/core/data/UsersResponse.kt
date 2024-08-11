package net.hiraok.compose_sample.core.data

import kotlinx.serialization.Serializable

@Serializable
data class UsersResponse(
    val users: List<UserResponse>
)

@Serializable
data class UserResponse(
    val id: String,
    val name: String
)