package com.aowen.monolith.data

import com.aowen.monolith.R

enum class HeroRole(val roleName: String, val drawableId: Int) {
    Offlane("offlane", R.drawable.offlane),
    Jungle("jungle", R.drawable.jungle),
    Midlane("midlane", R.drawable.midlane),
    Carry("carry", R.drawable.carry),
    Support("support", R.drawable.support),
    Unknown("unknown", R.drawable.unknown),
}

fun getHeroRole(roleName: String? = "") : HeroRole = HeroRole.entries.firstOrNull {
    it.roleName == roleName?.lowercase()
} ?: HeroRole.Unknown

fun getHeroImage(heroId: Int) = Hero.entries.firstOrNull {
    it.heroId == heroId
}?.drawableId ?: R.drawable.unknown

enum class Hero(val heroName: String, val heroId: Int, val drawableId: Int) {
    ARGUS("Argus", 49, R.drawable.argus),
    AURORA("Aurora", 53, R.drawable.aurora),
    BELICA("Lt. Belica",  13, R.drawable.belica),
    COUNTESS("Countess", 1, R.drawable.countess),
    CRUNCH("Crunch", 2, R.drawable.crunch),
    DEKKER("Dekker", 3, R.drawable.dekker),
    DRONGO("Drongo", 4, R.drawable.drongo),
    FENGMAO("Feng Mao", 5, R.drawable.fengmao),
    FEY("The Fey", 6, R.drawable.fey),
    GADGET("Gadget", 7, R.drawable.gadget),
    GIDEON("Gideon", 8, R.drawable.gideon),
    GREYSTONE("Greystone", 29, R.drawable.greystone),
    GRIM("GRIM.exe", 52, R.drawable.grim),
    GRUX("Grux", 9, R.drawable.grux),
    HOWITZER("Howitzer", 10, R.drawable.howitzer),
    IGGYANDSCORCH("Iggy & Scorch", 42, R.drawable.iggyscorch),
    KALLARI("Kallari", 11, R.drawable.kallari),
    KHAIMERA("Khaimera", 12, R.drawable.khaimera),
    KIRA("Kira", 24, R.drawable.kira),
    KWANG("Kwang", 44, R.drawable.kwang),
    MORIGESH("Morigesh", 27, R.drawable.morigesh),
    MURDOCK("Murdock", 14, R.drawable.murdock),
    MURIEL("Muriel", 15, R.drawable.muriel),
    NARBASH("Narbash", 16, R.drawable.narbash),
    PHASE("Phase",25, R.drawable.phase),
    RAMPAGE("Rampage", 17, R.drawable.rampage),
    REVENANT("Revenant", 22, R.drawable.revenant),
    RIKTOR("Riktor", 18, R.drawable.riktor),
    SERATH("Serath", 39, R.drawable.serath),
    SEVAROG("Sevarog", 19, R.drawable.sevarog),
    SHINBI("Shinbi", 23, R.drawable.shinbi),
    SPARROW("Sparrow", 20, R.drawable.sparrow),
    STEEL("Steel", 21, R.drawable.steel),
    TWINBLAST("TwinBlast", 30, R.drawable.twinblast),
    WRAITH("Wraith", 41, R.drawable.wraith),
    ZARUS("Zarus", 31, R.drawable.zarus),
    UNKNOWN("Unknown", 999, R.drawable.unknown),
}

fun getHeroRole(heroId: Int?) = Hero.entries.firstOrNull {
    it.heroId == heroId
} ?: Hero.UNKNOWN