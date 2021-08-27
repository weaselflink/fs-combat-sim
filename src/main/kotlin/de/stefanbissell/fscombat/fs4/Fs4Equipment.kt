package de.stefanbissell.fscombat.fs4

import de.stefanbissell.fscombat.fs4.Fs4DamageType.*
import de.stefanbissell.fscombat.fs4.Fs4Shield.*

@Suppress("unused")
enum class Fs4Armor(
    private val resistance: Int,
    private val proofing: List<Fs4DamageType> = emptyList(),
    val allowedShield: Fs4Shield = Standard,
    val hindering: Boolean = false,
) {
    None(0),
    ThickClothing(1),
    LeatherJerkin(2, listOf(Shock, Slam), Assault),
    PolymerKnit(2, listOf(Hard, Shock, Slam)),
    SynthSilk(3, listOf(Shock)),
    StiffSynth(4, listOf(Shock, Slam), Assault),
    SmartSynth(4, listOf(Hard, Shock, Slam), Assault),

    BattlePadding(1, listOf(Shock, Slam)),
    StuddedLeatherJerkin(3, listOf(Slam), Assault),
    StuddedLeatherJerkinPlastic(3, listOf(Shock, Slam), Assault),
    StuddedLeatherJerkinPlasteel(3, listOf(Hard, Shock, Slam), Assault),
    HalfPlate(5, listOf(Hard, Slam)),
    HalfPlatePlastic(5, listOf(Shock, Slam)),
    HalfPlatePlasteel(5, listOf(Hard, Shock, Slam)),

    CeramsteelExoframe(10, listOf(Hard, Laser, Shock, Slam), Battle, true),
    ScaleMail(6, listOf(Hard, Slam), Battle, true),
    ScaleMailPlastic(6, listOf(Shock, Slam), Assault, true),
    ScaleMailPlasteel(6, listOf(Hard, Shock, Slam), Assault, true),
    Mail(7, listOf(Hard, Slam), Assault, true),
    MailPlastic(7, listOf(Shock, Slam), Assault, true),
    MailPlasteel(7, listOf(Hard, Shock, Slam), Assault, true),
    FullPlate(8, listOf(Hard, Slam), Battle, true),
    FullPlatePlastic(8, listOf(Shock, Slam), Battle, true),
    FullPlatePlasteel(8, listOf(Hard, Shock, Slam), Battle, true);

    fun resistanceAgainst(weapon: Fs4Weapon) =
        if (weapon.types.any { it !in proofing }) {
            resistance / 2
        } else {
            resistance
        }
}

@Suppress("unused")
enum class Fs4DamageType {
    Blaster,
    Flame,
    Hard,
    Laser,
    Shock,
    Slam,
}

@Suppress("unused")
enum class Fs4Weapon(
    val damage: Int,
    val types: List<Fs4DamageType> = emptyList(),
    val resistance: Int = 0
) {
    Hatchet(6),
    BattleAxe(7),
    Club(4),
    Mace(5, listOf(Hard, Slam)),
    Dagger(4),
    UkariPunchBlade(4, listOf(Hard)),
    MainGauche(4, resistance = 1),
    GreatWeapon(8),
    Knife(3),
    Rapier(5),
    Sword(6),
}

@Suppress("unused")
enum class Fs4Shield(
    val hits: Int,
    val lower: Int = 5,
    val upper: Int = 10
) {
    NoShield(0),
    Antique(8, lower = 6, upper = 9),
    Standard(10),
    Dueling(15),
    Assault(20, upper = 15),
    Battle(30, upper = 20),
}
