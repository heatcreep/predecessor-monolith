package com.aowen.monolith.data

import com.aowen.monolith.R

enum class RoleImage(val roleName: String, val drawableId: Int) {
    CARRY("carry", R.drawable.carry),
    SUPPORT("support", R.drawable.support),
    OFFLANE("offlane", R.drawable.offlane),
    JUNGLE("jungle", R.drawable.jungle),
    MID("midlane", R.drawable.midlane),
    UNKNOWN("unknown", R.drawable.unknown),
}

fun getRoleImage(roleName: String) = RoleImage.values().first {
    it.roleName == roleName
}

enum class HeroImage(val heroName: String, val drawableId: Int) {
    BELICA("Lt. Belica", R.drawable.belica),
    COUNTESS("Countess", R.drawable.countess),
    CRUNCH("Crunch", R.drawable.crunch),
    DEKKER("Dekker", R.drawable.dekker),
    DRONGO("Drongo", R.drawable.drongo),
    FENGMAO("Feng Mao", R.drawable.fengmao),
    FEY("The Fey", R.drawable.fey),
    GADGET("Gadget", R.drawable.gadget),
    GIDEON("Gideon", R.drawable.gideon),
    GREYSTONE("Greystone", R.drawable.greystone),
    GRUX("Grux", R.drawable.grux),
    HOWITZER("Howitzer", R.drawable.howitzer),
    IGGYANDSCORCH("Iggy & Scorch", R.drawable.iggyscorch),
    KALLARI("Kallari", R.drawable.kallari),
    KHAIMERA("Khaimera", R.drawable.khaimera),
    KIRA("Kira", R.drawable.kira),
    MORIGESH("Morigesh", R.drawable.morigesh),
    MURDOCK("Murdock", R.drawable.murdock),
    MURIEL("Muriel", R.drawable.muriel),
    NARBASH("Narbash", R.drawable.narbash),
    PHASE("Phase", R.drawable.phase),
    RAMPAGE("Rampage", R.drawable.rampage),
    REVENANT("Revenant", R.drawable.revenant),
    RIKTOR("Riktor", R.drawable.riktor),
    SERATH("Serath", R.drawable.serath),
    SEVAROG("Sevarog", R.drawable.sevarog),
    SHINBI("Shinbi", R.drawable.shinbi),
    SPARROW("Sparrow", R.drawable.sparrow),
    STEEL("Steel", R.drawable.steel),
    TWINBLAST("TwinBlast", R.drawable.twinblast),
    WRAITH("Wraith", R.drawable.wraith),
    ZARUS("Zarus", R.drawable.zarus),
    UNKNOWN("Unknown", R.drawable.unknown),
}

fun getHeroImage(heroName: String?) = HeroImage.values().firstOrNull {
    it.heroName == heroName
} ?: HeroImage.UNKNOWN