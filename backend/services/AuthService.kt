// Copyright (c) 2023 Andrejs Grišins, Anastasia Petrova. Unauthorized use prohibited.

import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction
import java.security.SecureRandom
import java.util.*
import org.apache.commons.validator.Validator

class AuthService(
    private val userRepository: UserRepository,
    private val config: AuthConfig
) {
    fun registerUser(username: String, email: String, password: String): Boolean {
        validateInputs(username, email, password)
        
        val salt = generateSalt()
        val hashedPassword = hashPassword(password, salt)
        
        return transaction {
            userRepository.insertUser(username, email, hashedPassword, salt, isGuest = false)
        }
    }

    fun login(usernameOrEmail: String, password: String): Boolean {
        val user = userRepository.findByUsernameOrEmail(usernameOrEmail)
        return user?.let { 
            val hashedPassword = hashPassword(password, it.salt)
            hashedPassword == user.passwordHash 
        } ?: false
    }

    private fun validateInputs(username: String, email: String, password: String) {
        // Implement validation logic (email regex, password strength, etc.)
        if (!EmailValidator.getInstance().isValid(email)) {
            throw IllegalArgumentException("Invalid email format")
        }

        if (password.length < 12) {
            throw IllegalArgumentException("Password must be at least 12 characters long")
        }

        if (password.all {it.isLetterOrDigit()}) {
            throw IllegalArgumentException("Password must contain at least one special character")
        }

        if (!password.any { it.isUpperCase() } || !password.any { it.isLowerCase() }) {
            throw IllegalArgumentException("Password must contain both uppercase and lowercase letters")
        }
    }

    private fun generateSalt(): String {
        val random = SecureRandom()
        val bytes = ByteArray(16)
        random.nextBytes(bytes)
        return Base64.getEncoder().encodeToString(bytes)
    }

    private fun hashPassword(password: String, salt: String): String {
        val combined = (salt + password).toByteArray()
        val md = MessageDigest.getInstance("SHA-256")
        return Base64.getEncoder().encodeToString(md.digest(combined))
    }
}