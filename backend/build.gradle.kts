plugins {
    kotlin("jvm") version "2.2.10"
    application
}

group = "com.example"
version = "1.0"

repositories {
    mavenCentral()

    maven {
        url = uri("https://maven.pkg.jetbrains.space/public/p/ktor/eap")
    }
}

sourceSets {
    main {
        kotlin {
            srcDirs("src/main/kotlin", "services") // Include both directories
        }
        resources {
            srcDirs("src/main/resources")
        }
    }
}

val exposedVersion = "0.41.1"
val ktorVersion = "3.2.0"

dependencies {
    implementation("org.jetbrains.exposed:exposed-core:$exposedVersion")
    implementation("org.jetbrains.exposed:exposed-jdbc:$exposedVersion")
    implementation("org.jetbrains.exposed:exposed-java-time:$exposedVersion")
    implementation("org.postgresql:postgresql:42.5.4")
    implementation("com.zaxxer:HikariCP:5.0.1")
    implementation("io.ktor:ktor-server-core:$ktorVersion")
    implementation("io.ktor:ktor-server-netty:$ktorVersion")
    implementation("io.ktor:ktor-server-content-negotiation:$ktorVersion")
    implementation("io.ktor:ktor-serialization-kotlinx-json:$ktorVersion")
    implementation("commons-validator:commons-validator:1.7")
    implementation("org.mindrot:jbcrypt:0.4")
}

application {
    mainClass.set("ApplicationKt")
}