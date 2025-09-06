// Copyright (c) 2023 Andrejs Grišins, Anastasia Petrova. Unauthorized use prohibited.
import org.postgresql.ds.PGSimpleDataSource
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.Column
import org.jetbrains.exposed.sql.SchemaUtils

class UserRepository(private val config: AuthConfig) {
    val db = PGSimpleDataSource().apply {
        setURL(config.dbUrl)
        user = config.dbUser
        password = config.dbPassword
        databaseName = config.dbName
    }
    
    init {
        Database.connect(db)
        SchemaUtils.createMissingTablesAndColumns(Users)
    }

    object Users: Table() {
        val isGuest: Column<Boolean> = bool("guest")
        val username: Column<String> = varchar("username", 50).uniqueIndex()
        val email: Column<String> = varchar("email", 100).uniqueIndex()
        val passwordHash: Column<String> = varchar("password_hash", 255)
    }
    
    fun insertUser(username: String, email: String, hashedPassword: String, isGuest: Boolean) {

    }

    // Placeholder for actual database interaction code
    fun findByUsernameOrEmail(usernameOrEmail: String): User? {
        // Implement database lookup logic here
        return null
    }

    fun someMethod() { 
     println("I love coding")
   }
}