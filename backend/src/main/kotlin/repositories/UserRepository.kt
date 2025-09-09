// Copyright (c) 2023 Andrejs Grišins, Anastasia Petrova. Unauthorized use prohibited.
import org.postgresql.ds.PGSimpleDataSource
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction

class UserRepository(private val config: AuthConfig) {
    val db = PGSimpleDataSource().apply {
        setURL(config.dbUrl)
        user = config.dbUser
        password = config.dbPassword
    }
    
    init {
        Database.connect(db)
        transaction {
            SchemaUtils.createMissingTablesAndColumns(Users)
        }
    }

    object Users: Table() {
        val isGuest: Column<Boolean> = bool("guest")
        val username: Column<String> = varchar("username", 50).uniqueIndex()
        val email: Column<String> = varchar("email", 100).uniqueIndex()
        val passwordHash: Column<String> = varchar("password_hash", 255)
    }
    
    fun insertUser(username: String, email: String, hashedPassword: String, isGuest: Boolean) {
        transaction {
            Users.insert {
                it[this.isGuest] = isGuest
                it[this.username] = username
                it[this.email] = email
                it[this.passwordHash] = hashedPassword
            }
        }
    }

    fun findByUsernameOrEmail(usernameOrEmail: String): User? {
        return transaction {
            Users.select { (Users.username eq usernameOrEmail) or (Users.email eq usernameOrEmail) }
                .map {
                    User(
                        isGuest = it[Users.isGuest],
                        username = it[Users.username],
                        email = it[Users.email],
                        passwordHash = it[Users.passwordHash]
                    )
                }.singleOrNull()
        }
    }
}