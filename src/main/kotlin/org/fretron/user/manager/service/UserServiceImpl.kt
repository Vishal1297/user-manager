package org.fretron.user.manager.service

import org.fretron.user.manager.model.User
import org.fretron.user.manager.repository.UserRepository
import javax.inject.Inject
import javax.inject.Named

class UserServiceImpl @Inject constructor(@Named("userRepository") private val userRepository: UserRepository) {

    fun addUser(user: User): Boolean {

        return userRepository.addUser(user)
    }

    fun deleteUser(id: String): Boolean {
        return userRepository.deleteUser(id)
    }

    fun updateUser(id: String, user: User): Boolean {
        return userRepository.updateUser(id, user)
    }

    fun getUser(id: String): User? {
        return userRepository.getUser(id)
    }

    fun getAllUsers(): List<User> {
        return userRepository.getAllUsers()
    }

}