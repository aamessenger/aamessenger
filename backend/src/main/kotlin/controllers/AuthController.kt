// Copyright (c) 2023 Andrejs Grišins, Anastasia Petrova. Unauthorized use prohibited.

import io.ktor.server.routing.*
import io.ktor.http.*
import io.ktor.server.response.*
import io.ktor.server.request.*
import io.ktor.server.application.*
import com.example.models.User
import com.example.services.AuthService

fun Route.authRouting(authService: AuthService) {
    post("/register") {
        val params = call.receive<Map<String, String>>()
        val username = params["username"]!!
        val email = params["email"]!!
        val password = params["password"]!!

        try {
            authService.registerUser(username, email, password)
            call.respond(HttpStatusCode.Created, "User registered")
        } catch (e: Exception) {
            call.respond(HttpStatusCode.BadRequest, e.message!!)
        }
    }

    post("/login") {
        val params = call.receive<Map<String, String>>()
        val usernameOrEmail = params["username_or_email"]!!
        val password = params["password"]!!

        if (authService.login(usernameOrEmail, password)) {
            call.respond(HttpStatusCode.OK, "Login successful")
        } else {
            call.respond(HttpStatusCode.Unauthorized, "Invalid credentials")
        }
    }
}