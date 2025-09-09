// Copyright (c) 2023 Andrejs Grišins, Anastasia Petrova. Unauthorized use prohibited.

data class User(
        val isGuest: Boolean,
        val username: String,
        val email: String?,
        val passwordHash: String?
)