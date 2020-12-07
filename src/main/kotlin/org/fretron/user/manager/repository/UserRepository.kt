package org.fretron.user.manager.repository

import org.fretron.user.manager.model.User


interface UserRepository {

    fun addUser(user: User): Boolean
    fun deleteUser(id: String): Boolean
    fun updateUser(id: String, user: User): Boolean
    fun getUser(id: String): User?
    fun getAllUsers(): List<User>

}