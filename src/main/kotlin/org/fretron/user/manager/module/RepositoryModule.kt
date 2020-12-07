package org.fretron.user.manager.module

import com.fasterxml.jackson.databind.ObjectMapper
import dagger.Module
import dagger.Provides
import org.fretron.user.manager.repository.UserRepository
import org.fretron.user.manager.repository.UserRepositoryImpl
import javax.inject.Named

@Module
class RepositoryModule {

    @Provides
    @Named("userRepository")
    fun provideUserRepository(@Named("objectMapper") objectMapper: ObjectMapper): UserRepository {
        return UserRepositoryImpl(objectMapper)
    }

}