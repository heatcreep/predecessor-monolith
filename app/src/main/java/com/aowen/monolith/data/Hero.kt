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
    val drawableId: Int,
    val posterDrawableId: Int
) {
    AKERON("Akeron", "DemonKing", 69, R.drawable.akeron, R.drawable.akeron_v2),
    ARGUS("Argus", "Emerald", 49, R.drawable.argus, R.drawable.argus_v2),
    AURORA("Aurora", "Aurora", 53, R.drawable.aurora, R.drawable.aurora_v2),
    BAYLE("Bayle","Cryptmaker", 70, R.drawable.bayle, R.drawable.bayle_v2),
    BELICA("Lt. Belica", "LtBelica", 13, R.drawable.belica, R.drawable.belica_v2),
    BORIS("Boris", "Boris", 63, R.drawable.boris, R.drawable.boris_v2),
    COUNTESS("Countess", "Countess", 1, R.drawable.countess, R.drawable.countess_v2),
    CRUNCH("Crunch", "Crunch", 2, R.drawable.crunch, R.drawable.crunch_v2),
    DEKKER("Dekker", "Dekker", 3, R.drawable.dekker, R.drawable.dekker_v2),
    DRONGO("Drongo", "Drongo", 4, R.drawable.drongo, R.drawable.drongo_v2),
    EDEN("Eden", "Mech", 71, R.drawable.eden, R.drawable.eden_v2),
    FENGMAO("Feng Mao", "FengMao", 5, R.drawable.fengmao, R.drawable.fengmao_v2),
    FEY("The Fey", "Fey", 6, R.drawable.fey, R.drawable.fey_v2),
    GADGET("Gadget", "Gadget", 7, R.drawable.gadget, R.drawable.gadget_v2),
    GIDEON("Gideon", "Gideon", 8, R.drawable.gideon, R.drawable.gideon_v2),
    GREYSTONE("Greystone", "Greystone", 29, R.drawable.greystone, R.drawable.greystone_v2),
    GRIM("GRIM.exe", "GRIMexe", 52, R.drawable.grim, R.drawable.grimm_v2),
    GRUX("Grux", "Grux", 9, R.drawable.grux, R.drawable.grux_v2),
    HOWITZER("Howitzer", "Howitzer", 10, R.drawable.howitzer, R.drawable.howitzer_v2),
    IGGYANDSCORCH("Iggy & Scorch", "IggyScorch", 42, R.drawable.iggyscorch, R.drawable.iggy_v2),
    KALLARI("Kallari", "Kallari", 11, R.drawable.kallari, R.drawable.kallari_v2),
    KHAIMERA("Khaimera", "Khaimera", 12, R.drawable.khaimera, R.drawable.khaimera_v2),
    KIRA("Kira", "Huntress", 24, R.drawable.kira, R.drawable.kira_v2),
    KWANG("Kwang", "Kwang", 44, R.drawable.kwang, R.drawable.kwang_v2),
    MORIGESH("Morigesh", "Morigesh", 27, R.drawable.morigesh, R.drawable.morigesh_v2),
    MOURN("Mourn", "Wood", 60, R.drawable.mourn, R.drawable.mourn_v2),
    MURDOCK("Murdock", "Murdock", 14, R.drawable.murdock, R.drawable.murdock_v2),
    MURIEL("Muriel", "Muriel", 15, R.drawable.muriel, R.drawable.muriel_v2),
    NARBASH("Narbash", "Narbash", 16, R.drawable.narbash, R.drawable.narbash_v2),
    PHASE("Phase", "Phase", 25, R.drawable.phase, R.drawable.phase_v2),
    RAMPAGE("Rampage", "Rampage", 17, R.drawable.rampage, R.drawable.rampage_v2),
    RENNA("Renna", "Bright", 67, R.drawable.renna, R.drawable.renna_v2),
    REVENANT("Revenant", "Revenant", 22, R.drawable.revenant, R.drawable.revenant_v2),
    RIKTOR("Riktor", "Riktor", 18, R.drawable.riktor, R.drawable.riktor_v2),
    SERATH("Serath", "Serath", 39, R.drawable.serath, R.drawable.serath_v2),
    SEVAROG("Sevarog", "Sevarog", 19, R.drawable.sevarog, R.drawable.severog_v2),
    SHINBI("Shinbi", "Shinbi", 23, R.drawable.shinbi, R.drawable.shinbi_v2),
    SPARROW("Sparrow", "Sparrow", 20, R.drawable.sparrow, R.drawable.sparrow_v2),
    STEEL("Steel", "Steel", 21, R.drawable.steel, R.drawable.steel_v2),
    SKYLAR("Skylar", "Boost", 57, R.drawable.skylar, R.drawable.skylar_v2),
    TWINBLAST("TwinBlast", "TwinBlast", 30, R.drawable.twinblast, R.drawable.twinblast_v2),
    TERRA("Terra", "Terra", 54, R.drawable.terra, R.drawable.terra_v2),
    WRAITH("Wraith", "Wraith", 41, R.drawable.wraith, R.drawable.wraith_v2),
    WUKONG("Wukong", "Wukong", 65, R.drawable.wukong, R.drawable.wukong_v2),
    YIN("Yin", "Yin", 58, R.drawable.yin, R.drawable.yin_v2),
    ZARUS("Zarus", "Lizard", 31, R.drawable.zarus, R.drawable.zarus_v2),
    ZINX("Zinx", "Zinx", 55, R.drawable.zinx, R.drawable.zinx_v2),
    YUREI("Yurei", "Tidebinder", 10000000001, R.drawable.yurei, R.drawable.yurei_v2),
    UNKNOWN("Unknown", "Unknown", 999, R.drawable.unknown, R.drawable.question_card);
}