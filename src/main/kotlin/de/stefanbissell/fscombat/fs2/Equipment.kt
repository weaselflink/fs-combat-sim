package de.stefanbissell.fscombat.fs2

import kotlin.math.max

@Suppress("unused")
enum class Weapon(
    val damage: Int,
    val init: Int = 0
) {

    Knife(3),
    Dirk(4),
    Rapier(5),
    BroadSword(6),
    TwoHandedSword(8, 1)

}

@Suppress("unused")
enum class Shield(
    val lowThresh: Int,
    val highThresh: Int,
    val hits: Int
) {

    None(0, 0, 0),
    Standard(5, 10, 10),
    Dueling(5, 10, 15);

    fun activates(damage: Int) = damage >= lowThresh

    fun absorb(damage: Int) = max(0, damage - highThresh)
}

@Suppress("unused")
enum class Armor(
    val dice: Int,
    val dex: Int = 0
) {

    None(0),
    PaddedClothing(1),
    HeavyCloth(2),
    LeatherJerkin(4),
    SynthSilk(4),
    StuddedLeather(4),
    HalfPlate(6, -1),
    ScaleMail(7, -1),
    ChainMail(8, -1),
    Plate(10, -2)
}
