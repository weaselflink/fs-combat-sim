package de.stefanbissell.fscombat

import kotlin.math.max

@Suppress("unused")
sealed class Weapon(
    val damage: Int,
    val init: Int = 0
) {

    object Knife : Weapon(3)
    object Dirk : Weapon(4)
    object Rapier : Weapon(5)
    object BroadSword : Weapon(6)
    object TwoHandedSword : Weapon(8, 1)

}

@Suppress("unused")
sealed class Shield(
    val lowThresh: Int,
    val highThresh: Int,
    val hits: Int
) {

    fun activates(damage: Int) = damage >= lowThresh

    fun absorb(damage: Int) = max(0, damage - highThresh)

    object None : Shield(0, 0, 0)
    object Standard : Shield(5, 10, 10)
    object Dueling : Shield(5, 10, 15)
}

@Suppress("unused")
sealed class Armor(
    val dice: Int,
    val dex: Int = 0
) {

    object None : Armor(0)
    object PaddedClothing : Armor(1)
    object HeavyCloth : Armor(2)
    object LeatherJerkin : Armor(4)
    object SynthSilk : Armor(4)
    object StuddedLeather : Armor(4)
    object HalfPlate : Armor(6, -1)
    object ScaleMail : Armor(7, -1)
    object ChainMail : Armor(8, -1)
    object Plate : Armor(10, -2)
}
