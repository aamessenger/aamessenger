// Copyright (c) 2023 Andrejs Grišins, Anastasia Petrova. Unauthorized use prohibited.

data class AuthConfig(
    val dbUrl: String,
    val dbUser: String,
    val dbPassword: String,
    val dbName: String = "postgres",
    val saltLength: Int = 16,
    val tokenExpiration: Long = 86400 // 1 day in seconds
)

val authConfig = AuthConfig(
    dbUrl = System.getenv("DATABASE_URL") 
        ?: "jdbc:postgresql://localhost:5432/postgres",
    dbUser = System.getenv("DB_USER") ?: "postgres",
    dbPassword = System.getenv("DB_PASSWORD") ?: "databasepassword"
)