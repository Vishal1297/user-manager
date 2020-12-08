package org.fretron.user.manager.repository

import com.fasterxml.jackson.databind.ObjectMapper
import com.mongodb.BasicDBObject
import com.mongodb.MongoClient
import com.mongodb.client.MongoCollection
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

    var mongoClient: MongoClient? = null
    var mongoDatabase: MongoDatabase? = null

    private fun createConnection() {
        mongoClient = MongoClient("127.0.0.1", 27017)
        mongoDatabase = mongoClient?.getDatabase("userDB")
    }

    private fun String.getCollection(): MongoCollection<Document>? {
        return mongoDatabase?.getCollection(this)
    }

    private fun closeConnection() {
        mongoDatabase = null
        mongoClient?.close()
    }

    private fun generateUserId(): UUID {
        return UUID.randomUUID()
    }

    override fun addUser(user: User): Boolean {
        createConnection()
        val collection = "user".getCollection()
        val personToAdd = objectMapper.readValue(user.toString(), User::class.java)
        return if (personToAdd != null) {
            val document = Document.parse(personToAdd.toString())
            document["_id"] = generateUserId().toString()
            collection?.insertOne(document)
            closeConnection()
            true
        } else false
    }

    override fun deleteUser(id: String): Boolean {
        createConnection()
        val collection = "user".getCollection()
        val mongoCollection = collection?.deleteOne(Filters.eq("_id", id))
        closeConnection()
        return mongoCollection?.deletedCount == "1".toLong()
    }

    override fun updateUser(id: String, user: User): Boolean {
        createConnection()
        val collection = "user".getCollection()
        val query = BasicDBObject()
        query["_id"] = id
        val personDocument = Document.parse(user.toString())
        val update: Bson = Document("\$set", personDocument)
        val mongoCollection = collection?.findOneAndUpdate(query, update)
        closeConnection()
        return mongoCollection!=null
    }

    override fun getUser(id: String): User? {
        createConnection()
        val collection = "user".getCollection()
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
        closeConnection()
        return person
    }

    override fun getAllUsers(): ArrayList<User> {
        val allUsers: ArrayList<User> = ArrayList()
        createConnection()
        val collection = "user".getCollection()
        collection!!.find().iterator().use { cur ->
            while (cur.hasNext()) {
                val doc = cur.next()
                doc.remove("_id")
                val json = JSON.serialize(doc)
                val person = objectMapper.readValue(json, User::class.java)
                allUsers.add(person)
            }
        }
        closeConnection()
        return allUsers
    }
}