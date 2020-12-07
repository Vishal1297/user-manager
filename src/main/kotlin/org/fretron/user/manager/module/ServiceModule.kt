package org.fretron.user.manager.module

import dagger.Module
import dagger.Provides
import org.fretron.user.manager.repository.UserRepository
import org.fretron.user.manager.service.UserServiceImpl
import javax.inject.Named

@Module
class ServiceModule {

    @Provides
    @Named("userService")
    fun provideService(@Named("userRepository") userRepository: UserRepository): UserServiceImpl =
        UserServiceImpl(userRepository)

}