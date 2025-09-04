// Copyright (c) 2023 Andrejs Grišins, Anastasia Petrova. Unauthorized use prohibited.
@kotlinx.serialization.Serializable
data class Envelope(
    val id: String,
    val senderUserId: String,
    val senderDeviceId: String,
    val recipientUserId: String,
    val receivedAt: Long,
    val ciphertext: String,           // Base64
    val contentType: String = "text"
)
