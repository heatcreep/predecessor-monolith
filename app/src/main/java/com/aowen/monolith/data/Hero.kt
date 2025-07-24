package com.aowen.monolith.data

import com.aowen.monolith.R


enum class HeroRole(val roleName: String, val drawableId: Int, val simpleDrawableId: Int) {
    Offlane("offlane", R.drawable.offlane, R.drawable.simple_offlane),
    Jungle("jungle", R.drawable.jungle, R.drawable.simple_jungle),
    Midlane("midlane", R.drawable.midlane, R.drawable.simple_mid),
    Carry("carry", R.drawable.carry, R.drawable.simple_carry),
    Support("support", R.drawable.support, R.drawable.simple_support),
    Unknown("unknown", R.drawable.unknown, R.drawable.unknown),
}

fun getHeroName(heroId: Long) = Hero.entries.firstOrNull {
    it.heroId == heroId
}?.heroName ?: "Hero"

fun getHeroRole(roleName: String? = ""): HeroRole? = HeroRole.entries.firstOrNull {
    it.roleName.lowercase() == roleName?.lowercase()
}

fun getHeroImage(heroId: Long) = Hero.entries.firstOrNull {
    it.heroId == heroId
}?.drawableId ?: R.drawable.unknown

enum class Hero(
    val heroName: String,
    val pathName: String,
    val heroId: Long,
    val drawableId: Int
) {
    ARGUS("Argus", "Emerald", 49, R.drawable.argus),
    AURORA("Aurora", "Aurora", 53, R.drawable.aurora),
    BELICA("Lt. Belica", "LtBelica", 13, R.drawable.belica),
    BORIS("Boris", "Boris", 63, R.drawable.boris),
    COUNTESS("Countess", "Countess", 1, R.drawable.countess),
    CRUNCH("Crunch", "Crunch", 2, R.drawable.crunch),
    DEKKER("Dekker", "Dekker", 3, R.drawable.dekker),
    DRONGO("Drongo", "Drongo", 4, R.drawable.drongo),
    FENGMAO("Feng Mao", "FengMao", 5, R.drawable.fengmao),
    FEY("The Fey", "Fey", 6, R.drawable.fey),
    GADGET("Gadget", "Gadget", 7, R.drawable.gadget),
    GIDEON("Gideon", "Gideon", 8, R.drawable.gideon),
    GREYSTONE("Greystone", "Greystone", 29, R.drawable.greystone),
    GRIM("GRIM.exe", "GRIMexe", 52, R.drawable.grim),
    GRUX("Grux", "Grux", 9, R.drawable.grux),
    HOWITZER("Howitzer", "Howitzer", 10, R.drawable.howitzer),
    IGGYANDSCORCH("Iggy & Scorch", "IggyScorch", 42, R.drawable.iggyscorch),
    KALLARI("Kallari", "Kallari", 11, R.drawable.kallari),
    KHAIMERA("Khaimera", "Khaimera", 12, R.drawable.khaimera),
    KIRA("Kira", "Huntress", 24, R.drawable.kira),
    KWANG("Kwang", "Kwang", 44, R.drawable.kwang),
    MORIGESH("Morigesh", "Morigesh", 27, R.drawable.morigesh),
    MOURN("Mourn", "Wood", 60, R.drawable.mourn),
    MURDOCK("Murdock", "Murdock", 14, R.drawable.murdock),
    MURIEL("Muriel", "Muriel", 15, R.drawable.muriel),
    NARBASH("Narbash", "Narbash", 16, R.drawable.narbash),
    PHASE("Phase", "Phase", 25, R.drawable.phase),
    RAMPAGE("Rampage", "Rampage", 17, R.drawable.rampage),
    RENNA("Renna", "Bright", 67, R.drawable.renna),
    REVENANT("Revenant", "Revenant", 22, R.drawable.revenant),
    RIKTOR("Riktor", "Riktor", 18, R.drawable.riktor),
    SERATH("Serath", "Serath", 39, R.drawable.serath),
    SEVAROG("Sevarog", "Sevarog", 19, R.drawable.sevarog),
    SHINBI("Shinbi", "Shinbi", 23, R.drawable.shinbi),
    SPARROW("Sparrow", "Sparrow", 20, R.drawable.sparrow),
    STEEL("Steel", "Steel", 21, R.drawable.steel),
    SKYLAR("Skylar", "Boost", 57, R.drawable.skylar),
    TWINBLAST("TwinBlast", "TwinBlast", 30, R.drawable.twinblast),
    TERRA("Terra", "Terra", 54, R.drawable.terra),
    WRAITH("Wraith", "Wraith", 41, R.drawable.wraith),
    WUKONG("Wukong", "Wukong", 65, R.drawable.wukong),
    YIN("Yin", "Yin", 58, R.drawable.yin),
    ZARUS("Zarus", "Lizard", 31, R.drawable.zarus),
    ZINX("Zinx", "Zinx", 55, R.drawable.zinx),
    YUREI("Yurei", "Tidebinder", 10000000001, R.drawable.yurei),
    UNKNOWN("Unknown", "Unknown", 999, R.drawable.unknown),
}