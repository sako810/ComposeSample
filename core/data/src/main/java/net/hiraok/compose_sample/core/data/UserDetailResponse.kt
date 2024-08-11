package net.hiraok.compose_sample.core.data

import kotlinx.serialization.Serializable

@Serializable
data class UserDetailResponse(
    val id: String,
    val name: String
)