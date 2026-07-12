package com.aowen.predcompanion.core.database.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import java.math.BigDecimal

private object BigDecimalSerializer : KSerializer<BigDecimal> {
    override val descriptor = PrimitiveSerialDescriptor("BigDecimal", PrimitiveKind.STRING)
    override fun deserialize(decoder: Decoder): BigDecimal = BigDecimal(decoder.decodeString())
    override fun serialize(encoder: Encoder, value: BigDecimal) =
        encoder.encodeString(value.toPlainString())
}

@Serializable
@Entity(tableName = "hero_details")
data class HeroDetailsEntity(
    @PrimaryKey val id: String = "",
    val name: String = "",
    val displayName: String = "",
    val imageUrl: String? = null,
    val posterImageId: String? = null,
    val stats: List<Int> = emptyList(),
    val classes: List<String> = emptyList(),
    val roles: List<String> = emptyList(),
    val abilities: List<AbilityDetailsEntity> = emptyList(),
    val baseStats: HeroBaseStatsEntity = HeroBaseStatsEntity(),
)

@Serializable
data class AbilityDetailsEntity(
    val displayName: String,
    val image: String,
    val gameDescription: String,
    val menuDescription: String? = null,
    val cooldown: List<Float?>,
    val cost: List<Float>,
)

@Serializable
data class HeroBaseStatsEntity(
    val maxHealth: List<@Serializable(with = BigDecimalSerializer::class) BigDecimal> = emptyList(),
    val healthRegen: List<@Serializable(with = BigDecimalSerializer::class) BigDecimal> = emptyList(),
    val maxMana: List<@Serializable(with = BigDecimalSerializer::class) BigDecimal> = emptyList(),
    val manaRegen: List<@Serializable(with = BigDecimalSerializer::class) BigDecimal> = emptyList(),
    val attackSpeed: List<@Serializable(with = BigDecimalSerializer::class) BigDecimal> = emptyList(),
    val physicalArmor: List<@Serializable(with = BigDecimalSerializer::class) BigDecimal> = emptyList(),
    val magicalArmor: List<@Serializable(with = BigDecimalSerializer::class) BigDecimal> = emptyList(),
    val physicalPower: List<@Serializable(with = BigDecimalSerializer::class) BigDecimal> = emptyList(),
    val movementSpeed: Float = 0f,
    val cleave: Float = 0f,
    val attackRange: Float = 0f,
)
