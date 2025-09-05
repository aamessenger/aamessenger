// Copyright (c) 2023 Andrejs Grišins, Anastasia Petrova. Unauthorized use prohibited.

class User(
    val id: Int,
    val username: String,
    val email: String,
    val passwordHash: String,
    val salt: String,
    val isGuest: Boolean
)