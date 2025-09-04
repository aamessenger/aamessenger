// Copyright (c) 2023 Andrejs Grišins, Anastasia Petrova. Unauthorized use prohibited.
@kotlinx.serialization.Serializable
data class PreKeyBundle(
    val userId: String, val deviceId: String,
    val identityKey: String, val signedPreKeyId: Int,
    val signedPreKey: String, val signedPreKeySig: String,
    val oneTimePreKeys: List<OneTimePreKey>
)
@kotlinx.serialization.Serializable
data class OneTimePreKey(val keyId: Int, val publicKey: String)
