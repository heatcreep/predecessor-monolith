package com.aowen.predcompanion.core.network.model

import android.annotation.SuppressLint
import kotlinx.serialization.KSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import java.util.UUID

object UUIDSerializer : KSerializer<UUID> {
    override val descriptor = PrimitiveSerialDescriptor("UUID", PrimitiveKind.STRING)

    override fun deserialize(decoder: Decoder): UUID {
        return UUID.fromString(decoder.decodeString())
    }

    override fun serialize(encoder: Encoder, value: UUID) {
        encoder.encodeString(value.toString())
    }
}


@SuppressLint("UnsafeOptInUsageError")
@Serializable
data class NetworkUserInfo(
    @Serializable(with = UUIDSerializer::class)
    val id: UUID? = null,
    @SerialName("updated_at")
    val updatedAt: String? = null,
    val email: String = "",
    @SerialName("full_name")
    val fullName: String = "",
    @SerialName("avatar_url")
    val avatarUrl: String = "",
    @SerialName("player_id")
    val playerId: String? = "",
    @SerialName("onboarded")
    val isOnboarded: Boolean = false
)
