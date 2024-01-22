package com.aowen.monolith.data

import com.aowen.monolith.R

fun getItemImage(itemId: Int) = ItemImage.entries.first {
    it.itemId == itemId
}.drawableId

enum class ItemImage(itemName: String, val itemId: Int, val drawableId: Int) {
    ABSOLUTION("Absolution", 160, R.drawable.absolution),
    ABYSSAL_BRACERS("Abyssal Bracers", 63, R.drawable.abyssalbracers),
    ALCHEMICAL_ROD("Alchemical Rod", 91, R.drawable.alchemicalrod),
    ASHBRINGER("Ashbringer", 105, R.drawable.ashbringer),
    ASSASSIN_CREST("Assassin Crest", 6, R.drawable.assassincrest),
    ASTRAL_CATALYST("Astral Catalyst", 106, R.drawable.astralcatalyst),
    AUGMENTATION("Augmentation", 107, R.drawable.augmentation),
    AZURE_CORE("Azure Core", 108, R.drawable.azurecore),
    BANDED_EMERALD("Banded Emerald", 64, R.drawable.bandedemerald),
    BARBARIC_CLEAVER("Barbaric Cleaver", 65, R.drawable.barbariccleaver),
    BARBED_PAULDRON("Barbed Pauldron", 66, R.drawable.barbedpauldron),
    BASILISK("Basilisk", 109, R.drawable.basilisk),
    BERSERKERS_AXE("Berserker's Axe", 189, R.drawable.berserkersaxe),
    BLOOD_TOME("Blood Tome", 67, R.drawable.bloodtome),
    BLOODLETTER("Bloodletter", 68, R.drawable.bloodlust),
    BONESAW("Bonesaw", 111, R.drawable.bonesaw),
    BONESEEKER("Boneseeker", 190, R.drawable.boneseeker),
    BREACH("Breach", 113, R.drawable.breach),
    BRIMSTONE("Brimstone", 69, R.drawable.brimstone),
    BRUTALLAX("Brutallax", 16, R.drawable.brutallax),
    CAUSTICA("Caustica", 114, R.drawable.caustica),
    CERULEAN_STONE("Cerulean Stone", 80, R.drawable.ceruleanstone),
    CHAMPION_CREST("Champion Crest", 17, R.drawable.championcrest),
    CHAOTIC_CORE("Chaotic Core", 70, R.drawable.chaoticcore),
    CHRONOMATIC_WAND("Chronomatic Wand", 71, R.drawable.chronomaticwand),
    CITADEL("Citadel", 115, R.drawable.citadel),
    CLAYMORE("Claymore", 72, R.drawable.claymore),
    CODEX("Codex", 46, R.drawable.codex),
    COMBUSTION("Combustion", 116, R.drawable.combustion),
    COMPOSITE_BOW("Composite Bow", 73, R.drawable.compositebow),
    CONSORT_CREST("Consort Crest", 31, R.drawable.consortcrest),
    CRIMSON_EDGE("Crimson Edge", 74, R.drawable.crimsonedge),
    CROSSBOW("Crossbow", 47, R.drawable.crossbow),
    CRYSTAL_TEAR("Crystal Tear", 117, R.drawable.crystaltear),
    CRYSTALLINE_CUIRASS("Crystalline Cuirass", 118, R.drawable.crystallinecuirass),
    DEATHSTALKER("Deathstalker", 127, R.drawable.deathstalker),
    DEMOLISHER("Demolisher", 120, R.drawable.demolisher),
    DEMON_EDGE("Demon Edge", 121, R.drawable.demonedge),
    DIFFUSAL_CANE("Diffusal Cane", 75, R.drawable.diffusalcane),
    DIVINE_WREATH("Divine Wreath", 76, R.drawable.divinewreath),
    DRACONUM("Draconum", 122, R.drawable.draconum),
    DREAD("Dread", 136, R.drawable.dread),
    DREAMBINDER("Dreambinder", 124, R.drawable.dreambinder),
    DUSK_STAVE("Dusk Stave", 77, R.drawable.duskstave),
    DUST_DEVIL("Dust Devil", 119, R.drawable.dustdevil),
    DYNAMO("Dynamo", 125, R.drawable.dynamo),
    ECHELON_CLOAK("Echelon Cloak", 191, R.drawable.echeloncloak),
    ELAFROST("Elafrost", 126, R.drawable.elafrost),
    ENERGY_STREAM("Energy Stream", 48, R.drawable.energystream),
    ENVY("Envy", 192, R.drawable.envy),
    EPOCH("Epoch", 21, R.drawable.epoch),
    ESSENCE_RING("Essence Ring", 78, R.drawable.essencering),
    EVISCERATOR("Eviscerator", 11, R.drawable.eviscerator),
    FENIX("Fenix", 18, R.drawable.fenix),
    FIRE_BLOSSOM("Fire Blossom", 128, R.drawable.fireblossom),
    FLUX_MATRIX("Flux Matrix", 129, R.drawable.fluxmatrix),
    FORTIFIED_MANTLE("Fortified Mantle", 79, R.drawable.fortifiedmantle),
    FROSTED_LURE("Frosted Lure", 193, R.drawable.frostedlure),
    FROSTGUARD("Frostguard", 130, R.drawable.frostguard),
    GALAXY_GREAVES("Galaxy Greaves", 131, R.drawable.galaxygreaves),
    GIANTS_RING("Giant's Ring", 194, R.drawable.giantsring),
    GOLEMS_GIFT("Golem's Gift", 180, R.drawable.golemsgift),
    GOLIATH_CREST("Goliath Crest", 41, R.drawable.goliathcrest),
    GREATSWORD("Greatsword", 49, R.drawable.greatsword),
    GUARDIAN_CREST("Guardian Crest", 32, R.drawable.guardiancrest),
    HALLOWED_BRAID("Hallowed Braid", 81, R.drawable.hallowedbraid),
    HEROIC_GUARD("Heroic Guard", 82, R.drawable.heroicguard),
    HEXBOUND_BRACERS("Hexbound Bracers", 134, R.drawable.hexboundbracers),
    HONED_KRIS("Honed Kris", 83, R.drawable.honedkris),
    HORNED_PLATE("Horned Plate", 84, R.drawable.hornedplate),
    HUNT("Hunt", 50, R.drawable.hunt),
    ICESKORN_TALONS("Iceskorn Talons", 19, R.drawable.iceskorntalons),
    IMPERATOR("Imperator", 135, R.drawable.imperator),
    INFERNUM("Infernum", 133, R.drawable.infernum),
    INTELLECT_TONIC("Intellect Tonic", 1, R.drawable.intellecttonic),
    IRONWOOD_WARBOW("Ironwood Warbow", 85, R.drawable.ironwoodwarbow),
    KEEPER_CREST("Keeper Crest", 33, R.drawable.keepercrest),
    KINGSBANE("Kingsbane", 137, R.drawable.kingsbane),
    LEAFSONG("Leafsong", 34, R.drawable.leafsong),
    LEGACY("Legacy", 138, R.drawable.legacy),
    TAINTED_BASTION("Tainted Bastion", 139, R.drawable.leviathan),
    LIBERATOR("Liberator", 12, R.drawable.liberator),
    LIFE_STREAM("Life Stream", 52, R.drawable.lifestream),
    LIFEBINDER("Lifebinder", 140, R.drawable.lifebinder),
    LIGHTNING_HAWK("Lightning Hawk", 141, R.drawable.lightninghawk),
    LOCH_SHAWL("Loch Shawl", 86, R.drawable.lochshawl),
    LONGBOW("Longbow", 53, R.drawable.longbow),
    MAGICIAN_CREST("Magician Crest", 22, R.drawable.magiciancrest),
    MAGNIFY("Magnify", 142, R.drawable.magnify),
    MALADY("Malady", 110, R.drawable.malady),
    RECLAMATION("Reclamation", 35, R.drawable.malediction),
    MARKSMAN_CREST("Marksman Crest", 13, R.drawable.marksmancrest),
    MARSHAL("Marshal", 143, R.drawable.marshal),
    MEGACOSM("Megacosm", 144, R.drawable.megacosm),
    MESMER("Mesmer", 123, R.drawable.mesmer),
    MINDRAZOR("Mindrazor", 145, R.drawable.mindrazor),
    MUTILATOR("Mutilator", 147, R.drawable.mutilator),
    NEX("Nex", 7, R.drawable.nex),
    NIGHTFALL("Nightfall", 148, R.drawable.nightfall),
    NULLIFYING_MASK("Nullifying Mask", 87, R.drawable.nullifyingmask),
    NYR_WARBOOTS("Nyr Warboots", 42, R.drawable.nyrwarboots),
    OATHKEEPER("Oathkeeper", 149, R.drawable.oathkeeper),
    OBELISK("Obelisk", 23, R.drawable.obelisk),
    OBLIVION_CROWN("Oblivion Crown", 150, R.drawable.oblivioncrown),
    OCCULT_CREST("Occult Crest", 24, R.drawable.occultcrest),
    OMEN("Omen", 151, R.drawable.omen),
    ONIXIAN_QUIVER("Onixian Quiver", 195, R.drawable.onixianquiver),
    ORB_OF_ENLIGHTENMENT("Orb Of Enlightenment", 196, R.drawable.orbofenlightenment),
    ORB_OF_GROWTH("Orb Of Growth", 197, R.drawable.orbofgrowth),
    ORTUS("Ortus", 8, R.drawable.ortus),
    OVERLORD("Overlord", 152, R.drawable.overlord),
    PACIFIER("Pacifier", 14, R.drawable.pacifier),
    PAINWEAVER("Painweaver", 153, R.drawable.painweaver),
    POLAR_TREADS("Polar Treads", 88, R.drawable.polartreads),
    POTENT_STAFF("Potent Staff", 54, R.drawable.potentstaff),
    PROPHECY("Prophecy", 155, R.drawable.prophecy),
    PROTECTION_TONIC("Protection Tonic", 2, R.drawable.protectiontonic),
    RAIMENT_OF_RENEWAL("Raiment of Renewal", 156, R.drawable.raimentofrenewal),
    RAPID_RAPIER("Rapid Rapier", 89, R.drawable.ravenousrapier),
    RAZORBACK("Razorback", 43, R.drawable.razorback),
    RESOLUTION("Resolution", 157, R.drawable.reckoning),
    REFILLABLE_POTION("Refillable Potion", 3, R.drawable.refillablepotion),
    REQUIEM("Requiem", 158, R.drawable.requiem),
    RIFT_WALKERS("Rift Walkers", 36, R.drawable.riftwalkers),
    ROBUST_ARBALEST("Robust Arbalest", 90, R.drawable.robustarbalest),
    ROGUE_CREST("Rogue Crest", 9, R.drawable.roguecrest),
    RUNE_BOW("Rune Bow", 92, R.drawable.runebow),
    RUTHLESS_BROADSWORD("Ruthless Broadsword", 93, R.drawable.ruthlessbroadsword),
    SABRE("Sabre", 55, R.drawable.sabre),
    SALVATION("Salvation", 159, R.drawable.salvation),
    SANCTIFICATION("Sanctification", 37, R.drawable.sanctification),
    SAPHIRS_MANTLE("Saphir's Mantle", 44, R.drawable.saphirsmantle),
    SCALDING_SCEPTRE("Scalding Sceptre", 94, R.drawable.scaldingsceptre),
    SCEPTER("Scepter", 56, R.drawable.scepter),
    SENTRY("Sentry", 181, R.drawable.sentry),
    SERRATED_BLADE("Serrated Blade", 95, R.drawable.serratedblade),
    SHARPSHOOTER_CREST("Sharpshooter Crest", 15, R.drawable.sharpshootercrest),
    SHORTSWORD("Shortsword", 57, R.drawable.shortsword),
    SILENTIUM("Silentium", 38, R.drawable.silentium),
    SKY_SPLITTER("Sky Splitter", 161, R.drawable.skysplitter),
    SOLSTONE("Solstone", 183, R.drawable.solstone),
    SOUL_CHALICE("Soul Chalice", 96, R.drawable.soulchalice),
    SOULBEARER("Soulbearer", 25, R.drawable.soulbearer),
    SPELL_SLASHER("Spell Slasher", 58, R.drawable.spellslasher),
    SPELLBREAKER("Spellbreaker", 162, R.drawable.spellbreaker),
    SPIKED_BIRCH("Spiked Birch", 97, R.drawable.spikedbirch),
    SPIRIT_BEADS("Spirit Beads", 59, R.drawable.spiritbeads),
    SPIRIT_OF_AMIR("Spirit Of Amir", 198, R.drawable.spiritofamir),
    STALWART_GAUNTLETS("Stalwart Gauntlets", 98, R.drawable.stalwartgauntlets),
    STAMINA_TONIC("Stamina Tonic", 4, R.drawable.staminatonic),
    STEALTH_WARD("Stealth Ward", 182, R.drawable.stealthward),
    STEEL_MAIL("Steel Mail", 99, R.drawable.steelmail),
    STONE_OF_STRENGTH("Stone Of Strength", 188, R.drawable.stoneofstrength),
    STONEWALL("Stonewall", 163, R.drawable.stonewall),
    STORM_BREAKER("Storm Breaker", 164, R.drawable.stormbreaker),
    STRENGTH_TONIC("Strength Tonic", 5, R.drawable.strengthtonic),
    TAINTED_BLADE("Tainted Blade", 165, R.drawable.taintedblade),
    TAINTED_GUARD("Tainted Guard", 166, R.drawable.taintedguard),
    TAINTED_ROUNDS("Tainted Rounds", 167, R.drawable.taintedrounds),
    TAINTED_SCEPTER("Tainted Scepter", 168, R.drawable.taintedscepter),
    TAINTED_TOTEM("Tainted Totem", 154, R.drawable.taintedtotem),
    TECTONIC_MALLET("Tectonic Mallet", 132, R.drawable.tectonicmallet),
    TEMPEST("Tempest", 26, R.drawable.tempest),
    TEMPORAL_RIPPER("Temporal Ripper", 100, R.drawable.temporalripper),
    TENACIOUS_DRAPE("Tenacious Drape", 101, R.drawable.tenaciousdrape),
    TERMINUS("Terminus", 169, R.drawable.terminus),
    THE_PERFORATOR("The Perforator", 170, R.drawable.theperforator),
    TIMEFLUX_BAND("Time-Flux Band", 27, R.drawable.timefluxband),
    TIMEWARP("Timewarp", 171, R.drawable.timewarp),
    TITAN_CREST("Titan Crest", 45, R.drawable.titancrest),
    TRANQUILITY("Tranquility", 39, R.drawable.tranquility),
    TRUESILVER_BRACELET("Truesilver Bracelet", 172, R.drawable.truesilverbracelet),
    TUNIC("Tunic", 60, R.drawable.tunic),
    TYPHOON("Typhoon", 28, R.drawable.typhoon),
    UNBROKEN_WILL("Unbroken Will", 173, R.drawable.unbrokenwill),
    VANGUARDIAN("Vanguardian", 174, R.drawable.vanguardian),
    VANQUISHER("Vanquisher", 175, R.drawable.vanquisher),
    VIOLET_BROOCH("Violet Brooch", 102, R.drawable.violetbrooch),
    VIPER("Viper", 146, R.drawable.viper),
    VITALITY_BEADS("Vitality Beads", 61, R.drawable.vitalitybeads),
    VOID_CRYSTAL("Void Crystal", 103, R.drawable.voidcrystal),
    VOID_HELM("Void Helm", 176, R.drawable.voidhelm),
    WARDEN_CREST("Warden Crest", 40, R.drawable.wardencrest),
    WARDENS_FAITH("Warden's Faith", 177, R.drawable.wardensfaith),
    WARLOCK_CREST("Warlock Crest", 29, R.drawable.warlockcrest),
    WARRIOR_CREST("Warrior Crest", 20, R.drawable.warriorcrest),
    WELLSPRING("Wellspring", 112, R.drawable.wellspring),
    WILD_HUNT("Wild Hunt", 51, R.drawable.wildhunt),
    WINDCALLER("Windcaller", 199, R.drawable.windcaller),
    WITCHSTALKER("Witchstalker", 10, R.drawable.witchstalker),
    WIZARD_CREST("Wizard Crest", 30, R.drawable.wizardcrest),
    WORLD_BREAKER("World Breaker", 178, R.drawable.worldbreaker),
    WRAITH_LEGGINGS("Wraith Leggings", 179, R.drawable.wraithleggings),
    SOLSTONE_DRONE("Solstone Drone", 186, R.drawable.wraith_solstonedrone),
    SONAR_DRONE("Sonar Drone", 187, R.drawable.wraith_sonardrone),
    WRAPS("Wraps", 62, R.drawable.wraps),
    ZEALOUS_TOMAHAWK("Zealous Tomahawk", 104, R.drawable.zealoustomahawk),
}