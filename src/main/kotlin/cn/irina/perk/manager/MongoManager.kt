package cn.irina.perk.manager

import cn.irina.perk.Main
import cn.irina.perk.data.PlayerData
import cn.irina.perk.perks.AbstractPerk
import com.mongodb.ConnectionString
import com.mongodb.MongoClientSettings
import com.mongodb.client.model.Filters
import com.mongodb.client.model.UpdateOptions
import com.mongodb.client.result.UpdateResult
import com.mongodb.kotlin.client.coroutine.MongoClient
import com.mongodb.kotlin.client.coroutine.MongoCollection
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.withContext
import org.bson.Document
import org.bukkit.Bukkit
import java.net.URLEncoder
import java.util.UUID
import java.util.concurrent.TimeUnit

/**
 * @Author Irina
 * @Date 2025/10/27 19:46
 */

class MongoManager {
    companion object {
        private val config = Main.cfg
        
        private const val URL = "Database"
        private val IP = config.getString("$URL.ip", "localhost")
        private val PORT = config.getInt("$URL.port", 27017)
        private val USER = config.getString("$URL.user", "")
        private val PASSWORD = config.getString("$URL.password", "")
        private val DATABASE_NAME = config.getString("$URL.db", "Perk")
        
        private const val COLLECTION_NAME = "players"
    }
    
    private val connectionUrl = buildMongoUri(IP, PORT, USER, PASSWORD, DATABASE_NAME)
    
    private val settings  = MongoClientSettings.builder()
        .applyConnectionString(ConnectionString(connectionUrl))
        .applyToConnectionPoolSettings { pool ->
            pool.maxSize(50)
            pool.minSize(5)
            pool.maxWaitTime(30, TimeUnit.SECONDS)
        }
        .build()
    private val client: MongoClient by lazy { MongoClient.create(settings) }
    private val database by lazy { client.getDatabase(DATABASE_NAME) }
    private val collection: MongoCollection<Document> by lazy {
        database.getCollection<Document>(COLLECTION_NAME)
    }
    
    fun buildMongoUri(host: String? = null, port: Int? = null, user: String? = null, password: String? = null, database: String? = null): String {
        val credentials = if (user != null && password != null) "$user:${password.encodeURL()}@" else ""
        return "mongodb://$credentials$host:$port/${database ?: ""}"
    }
    
    private fun String.encodeURL(): String = URLEncoder.encode(this, "UTF-8")
    
    suspend fun getData(uuid: UUID): Document {
        return withContext(Dispatchers.IO) {
            try {
                collection.find(Filters.eq("uuid", uuid.toString())).first()
            } catch (_: NoSuchElementException) {
                createData(uuid)
            }
        }
    }
    
    suspend fun saveData(pd: PlayerData): UpdateResult {
        return withContext(Dispatchers.IO) {
            collection.updateOne(
                filter = Filters.eq("uuid", pd.uuid.toString()),
                update = Document(
                    $$"$set", Document()
                    .append("currentPerks", pd.currentPerks)
                ),
                options = UpdateOptions().upsert(true)
            )
        }
    }
    
    suspend fun createData(uuid: UUID): Document {
        return withContext(Dispatchers.IO) {
            val doc = Document()
                .append("uuid", uuid.toString())
                .append("name", Bukkit.getPlayer(uuid)?.name)
                .append("currentPerks", listOf<AbstractPerk>())
                .append("createdAt", System.currentTimeMillis())
            collection.insertOne(doc)
            return@withContext doc
        }
    }
    
    suspend fun load() {
        withContext(Dispatchers.IO) {
            val exists = database.listCollectionNames()
                .toList()
                .contains(COLLECTION_NAME)
            
            if (exists) database.createCollection(COLLECTION_NAME)
        }
    }
    
    suspend fun disable() {
        withContext(Dispatchers.IO) {
            client.close()
        }
    }
}