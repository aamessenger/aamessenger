// Copyright (c) 2023 Andrejs Grišins, Anastasia Petrova. Unauthorized use prohibited.
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.server.application.*
import io.ktor.server.routing.*
import io.ktor.server.response.*
import io.ktor.server.request.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.plugins.contentnegotiation.*

fun main() {
    println("Hello, AAMessenger!")
    
    embeddedServer(Netty, port = 8080) {
        val userRepository = UserRepository(authConfig)
        val authService = AuthService(userRepository, authConfig)

        install(ContentNegotiation) {
            json()
        }

        routing {
            route("/api") {
                authRouting(authService)
            }
        }
    }.start(wait = true)
}