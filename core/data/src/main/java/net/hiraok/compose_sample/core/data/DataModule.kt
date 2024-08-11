package net.hiraok.compose_sample.core.data

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class DataModule {

    @Provides
    @Singleton
    fun provideUserApiClient(retrofit: Retrofit): UserApiClient {
        return UserApiClient(retrofit)
    }

    @Provides
    @Singleton
    fun provideUserRepository(userApiClient: UserApiClient, @IoDispatcher dispatcher: CoroutineDispatcher): UserRepository {
        return DefaultUserRepository(userApiClient, dispatcher)
    }

}