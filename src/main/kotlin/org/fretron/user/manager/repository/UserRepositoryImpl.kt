package org.fretron.user.manager.repository

import com.fasterxml.jackson.databind.ObjectMapper
import com.mongodb.BasicDBObject
import com.mongodb.MongoClient
import com.mongodb.client.MongoDatabase
import com.mongodb.client.model.Filters
import com.mongodb.util.JSON
import org.bson.Document
import org.bson.conversions.Bson
import org.fretron.user.manager.model.User
import java.util.*
import javax.inject.Inject
import javax.inject.Named
import kotlin.collections.ArrayList

class UserRepositoryImpl @Inject constructor(@Named("objectMapper") private var objectMapper: ObjectMapper) :
    UserRepository {

    init {
        println("Repo Init")
    }

    var mongoClient: MongoClient? = null
    var mongoDatabase: MongoDatabase? = null

    private fun createConnection() {
        mongoClient = MongoClient("127.0.0.1", 27017)
        mongoDatabase = mongoClient?.getDatabase("userDB")
    }

    private fun closeConnection() {
        mongoClient?.close()
    }

    private fun generateUserId(): UUID {
        return UUID.randomUUID()
    }

    override fun addUser(user: User): Boolean {
        createConnection()
        val collection = mongoDatabase?.getCollection("user")
        val personToAdd = objectMapper.readValue(user.toString(), User::class.java)
        return if (personToAdd != null) {
            val document = Document.parse(personToAdd.toString())
            document["_id"] = generateUserId().toString()
            println("Add User :: $document")
            collection?.insertOne(document)
            closeConnection()
            true
        } else false
    }

    override fun deleteUser(id: String): Boolean {
        createConnection()
        val collection = mongoDatabase?.getCollection("user")
        println("Delete User With ID :: $id ")
        val mongoCollection = collection?.deleteOne(Filters.eq("_id", id))
        if (mongoCollection != null) println("Delete :: ${mongoCollection.deletedCount}")
        closeConnection()
        return true
    }

    override fun updateUser(id: String, user: User): Boolean {
        createConnection()
        val collection = mongoDatabase?.getCollection("user")
        val query = BasicDBObject()
        query["_id"] = id
        val personDocument = Document.parse(user.toString())
        val update: Bson = Document("\$set", personDocument)
        val mongoCollection = collection?.findOneAndUpdate(query, update)
        println("Updated User :: $mongoCollection")
        closeConnection()
        return true
    }

    override fun getUser(id: String): User? {
        createConnection()
        val collection = mongoDatabase?.getCollection("user")
        var person: User? = null
        val query = BasicDBObject()
        query["_id"] = id
        collection!!.find(query).iterator().use { cur ->
            while (cur.hasNext()) {
                val doc = cur.next()
                doc.remove("_id")
                val json = JSON.serialize(doc)
                person = objectMapper.readValue(json, User::class.java)
            }
        }
        println("Get Users With ID :: $id \n $person")
        closeConnection()
        return person
    }

    override fun getAllUsers(): ArrayList<User> {
        val allUsers: ArrayList<User> = ArrayList()
        createConnection()
        val collection = mongoDatabase?.getCollection("user")
        collection!!.find().iterator().use { cur ->
            while (cur.hasNext()) {
                val doc = cur.next()
                doc.remove("_id")
                val json = JSON.serialize(doc)
                val person = objectMapper.readValue(json, User::class.java)
                allUsers.add(person)
            }
        }
        println("Get All Users \n $allUsers")
        closeConnection()
        return allUsers
    }
}