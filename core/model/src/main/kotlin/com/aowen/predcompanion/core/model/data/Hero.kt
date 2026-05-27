package com.aowen.predcompanion.core.model.data

import com.aowen.predcompanion.core.resources.R as coreResources


enum class HeroRole(val roleName: String, val drawableId: Int, val simpleDrawableId: Int) {
    Offlane("offlane", coreResources.drawable.offlane, coreResources.drawable.simple_offlane),
    Jungle("jungle", coreResources.drawable.jungle, coreResources.drawable.simple_jungle),
    Midlane("midlane", coreResources.drawable.midlane, coreResources.drawable.simple_mid),
    Carry("carry", coreResources.drawable.carry, coreResources.drawable.simple_carry),
    Support("support", coreResources.drawable.support, coreResources.drawable.simple_support),
    Unknown("unknown", coreResources.drawable.unknown, coreResources.drawable.unknown),
}

enum class Hero(
    val heroName: String,
    val pathName: String,
    val heroId: Long,
    val drawableId: Int,
    val posterDrawableId: Int
) {
    AKERON(
        "Akeron",
        "DemonKing",
        69,
        coreResources.drawable.akeron,
        coreResources.drawable.akeron_v2
    ),
    ARGUS("Argus", "Emerald", 49, coreResources.drawable.argus, coreResources.drawable.argus_v2),
    AURORA("Aurora", "Aurora", 53, coreResources.drawable.aurora, coreResources.drawable.aurora_v2),
    BAYLE("Bayle", "Cryptmaker", 70, coreResources.drawable.bayle, coreResources.drawable.bayle_v2),
    BELICA(
        "Lt. Belica",
        "LtBelica",
        13,
        coreResources.drawable.belica,
        coreResources.drawable.belica_v2
    ),
    BORIS("Boris", "Boris", 63, coreResources.drawable.boris, coreResources.drawable.boris_v2),
    COUNTESS(
        "Countess",
        "Countess",
        1,
        coreResources.drawable.countess,
        coreResources.drawable.countess_v2
    ),
    CRUNCH("Crunch", "Crunch", 2, coreResources.drawable.crunch, coreResources.drawable.crunch_v2),
    DEKKER("Dekker", "Dekker", 3, coreResources.drawable.dekker, coreResources.drawable.dekker_v2),
    DRONGO("Drongo", "Drongo", 4, coreResources.drawable.drongo, coreResources.drawable.drongo_v2),
    EDEN("Eden", "Mech", 71, coreResources.drawable.eden, coreResources.drawable.eden_v2),
    FENGMAO(
        "Feng Mao",
        "FengMao",
        5,
        coreResources.drawable.fengmao,
        coreResources.drawable.fengmao_v2
    ),
    FEY("The Fey", "Fey", 6, coreResources.drawable.fey, coreResources.drawable.fey_v2),
    GADGET("Gadget", "Gadget", 7, coreResources.drawable.gadget, coreResources.drawable.gadget_v2),
    GIDEON("Gideon", "Gideon", 8, coreResources.drawable.gideon, coreResources.drawable.gideon_v2),
    GREYSTONE(
        "Greystone",
        "Greystone",
        29,
        coreResources.drawable.greystone,
        coreResources.drawable.greystone_v2
    ),
    GRIM("GRIM.exe", "GRIMexe", 52, coreResources.drawable.grim, coreResources.drawable.grimm_v2),
    GRUX("Grux", "Grux", 9, coreResources.drawable.grux, coreResources.drawable.grux_v2),
    HOWITZER(
        "Howitzer",
        "Howitzer",
        10,
        coreResources.drawable.howitzer,
        coreResources.drawable.howitzer_v2
    ),
    IGGYANDSCORCH(
        "Iggy & Scorch",
        "IggyScorch",
        42,
        coreResources.drawable.iggyscorch,
        coreResources.drawable.iggy_v2
    ),
    KALLARI(
        "Kallari",
        "Kallari",
        11,
        coreResources.drawable.kallari,
        coreResources.drawable.kallari_v2
    ),
    KHAIMERA(
        "Khaimera",
        "Khaimera",
        12,
        coreResources.drawable.khaimera,
        coreResources.drawable.khaimera_v2
    ),
    KIRA("Kira", "Huntress", 24, coreResources.drawable.kira, coreResources.drawable.kira_v2),
    KWANG("Kwang", "Kwang", 44, coreResources.drawable.kwang, coreResources.drawable.kwang_v2),
    MORIGESH(
        "Morigesh",
        "Morigesh",
        27,
        coreResources.drawable.morigesh,
        coreResources.drawable.morigesh_v2
    ),
    MOURN("Mourn", "Wood", 60, coreResources.drawable.mourn, coreResources.drawable.mourn_v2),
    MURDOCK(
        "Murdock",
        "Murdock",
        14,
        coreResources.drawable.murdock,
        coreResources.drawable.murdock_v2
    ),
    MURIEL("Muriel", "Muriel", 15, coreResources.drawable.muriel, coreResources.drawable.muriel_v2),
    NARBASH(
        "Narbash",
        "Narbash",
        16,
        coreResources.drawable.narbash,
        coreResources.drawable.narbash_v2
    ),
    PHASE("Phase", "Phase", 25, coreResources.drawable.phase, coreResources.drawable.phase_v2),
    RAMPAGE(
        "Rampage",
        "Rampage",
        17,
        coreResources.drawable.rampage,
        coreResources.drawable.rampage_v2
    ),
    RENNA("Renna", "Bright", 67, coreResources.drawable.renna, coreResources.drawable.renna_v2),
    REVENANT(
        "Revenant",
        "Revenant",
        22,
        coreResources.drawable.revenant,
        coreResources.drawable.revenant_v2
    ),
    RIKTOR("Riktor", "Riktor", 18, coreResources.drawable.riktor, coreResources.drawable.riktor_v2),
    SERATH("Serath", "Serath", 39, coreResources.drawable.serath, coreResources.drawable.serath_v2),
    SEVAROG(
        "Sevarog",
        "Sevarog",
        19,
        coreResources.drawable.sevarog,
        coreResources.drawable.severog_v2
    ),
    SHINBI("Shinbi", "Shinbi", 23, coreResources.drawable.shinbi, coreResources.drawable.shinbi_v2),
    SPARROW(
        "Sparrow",
        "Sparrow",
        20,
        coreResources.drawable.sparrow,
        coreResources.drawable.sparrow_v2
    ),
    STEEL("Steel", "Steel", 21, coreResources.drawable.steel, coreResources.drawable.steel_v2),
    SKYLAR("Skylar", "Boost", 57, coreResources.drawable.skylar, coreResources.drawable.skylar_v2),
    TWINBLAST(
        "TwinBlast",
        "TwinBlast",
        30,
        coreResources.drawable.twinblast,
        coreResources.drawable.twinblast_v2
    ),
    TERRA("Terra", "Terra", 54, coreResources.drawable.terra, coreResources.drawable.terra_v2),
    WRAITH("Wraith", "Wraith", 41, coreResources.drawable.wraith, coreResources.drawable.wraith_v2),
    WUKONG("Wukong", "Wukong", 65, coreResources.drawable.wukong, coreResources.drawable.wukong_v2),
    YIN("Yin", "Yin", 58, coreResources.drawable.yin, coreResources.drawable.yin_v2),
    ZARUS("Zarus", "Lizard", 31, coreResources.drawable.zarus, coreResources.drawable.zarus_v2),
    ZINX("Zinx", "Zinx", 55, coreResources.drawable.zinx, coreResources.drawable.zinx_v2),
    YUREI(
        "Yurei",
        "Tidebinder",
        10000000001,
        coreResources.drawable.yurei,
        coreResources.drawable.yurei_v2
    ),
    UNKNOWN(
        "Unknown",
        "Unknown",
        999,
        coreResources.drawable.unknown,
        coreResources.drawable.question_card
    );
}