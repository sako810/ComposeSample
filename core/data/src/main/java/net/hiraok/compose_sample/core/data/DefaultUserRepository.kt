package net.hiraok.compose_sample.core.data

import android.util.LruCache
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import net.hiraok.compose_sample.core.model.User

interface UserRepository {
    suspend fun users(): List<User>
    suspend fun userById(userId: String): User
}

class DefaultUserRepository(
    private val apiClient: UserApiClient,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher
) : UserRepository {

    private val cache = LruCache<Int, User>(5 * 1024 * 1024)

    override suspend fun users(): List<User> {
        return listOf(User("1", "hoge"), User("2", "fuga"))
    }

    override suspend fun userById(userId: String): User {
        return withContext(ioDispatcher) {
            if (cache[userId.toInt()] != null) {
                return@withContext cache[userId.toInt()]
            } else {
                val response = apiClient.userById(userId)
                val user = User(
                    id = response.id,
                    name = response.name
                )
                cache.put(userId.toInt(), user)
                return@withContext user
            }
        }
    }
}