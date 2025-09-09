// Copyright (c) 2023 Andrejs Grišins, Anastasia Petrova. Unauthorized use prohibited.
import org.apache.commons.validator.routines.EmailValidator
import java.security.MessageDigest
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction
import java.security.SecureRandom
import java.util.*
import org.mindrot.jbcrypt.BCrypt

class AuthService(
    private val userRepository: UserRepository,
    private val config: AuthConfig
) {
    fun registerUser(username: String, email: String, password: String): Boolean {
        validateInputs(username, email, password)
        
        val hashedPassword = hashPassword(password)
        
        return try {
            transaction {
                userRepository.insertUser(username, email, hashedPassword, isGuest = false)
            }
            true
        } catch (e: Exception) {
            if (e.message?.contains("duplicate key") == true) {
                throw IllegalArgumentException("Username or email already exists")
            }
            throw e
        }
    }

    fun login(usernameOrEmail: String, password: String): Boolean {
        val user = userRepository.findByUsernameOrEmail(usernameOrEmail)
        return if (user != null) { 
            verifyPassword(user.passwordHash, password)
        } else {
            false
        }
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

    private fun hashPassword(password: String): String {
        return BCrypt.hashpw(password, BCrypt.gensalt(12))
    }

    private fun verifyPassword(storedHash: String, inputPassword: String): Boolean {
        return BCrypt.checkpw(inputPassword, storedHash)
    }
}