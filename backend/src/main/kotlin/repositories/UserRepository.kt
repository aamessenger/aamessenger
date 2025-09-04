// Copyright (c) 2023 Andrejs Grišins, Anastasia Petrova. Unauthorized use prohibited.
import org.postgresql.ds.PGSimpleDataSource
import org.jetbrains.exposed.sql.Database

class UserRepository(private val config: AuthConfig) {
    val db = Database.connect(
        PGSimpleDataSource().apply {
            setURL(config.dbUrl)
            user = config.dbUser
            password = config.dbPassword
            databaseName = config.dbName
        }
    )
    
    fun insertUser(username: String, email: String, hashedPassword: String, salt: String, isGuest: Boolean) {

    }

    // Placeholder for actual database interaction code
    fun findByUsernameOrEmail(usernameOrEmail: String): User? {
        // Implement database lookup logic here
        return null
    }
}