package de.stefanbissell.fscombat.fs4

import kotlin.random.Random

fun main() {
    val playerA = {
        Fs4Player(
            name = "A",
            strength = (3..10).random(),
            melee = (3..10).random(),
            armor = Fs4Armor.ThickClothing,
            weapon = Fs4Weapon.Dagger,
            shield = Fs4Shield.Standard
        )
    }
    val playerB = {
        Fs4Player(
            name = "B",
            strength = (3..10).random(),
            melee = (3..10).random(),
            armor = Fs4Armor.HalfPlate,
            weapon = Fs4Weapon.Dagger,
            shield = Fs4Shield.None
        )
    }

    Fs4Simulator(playerA, playerB)
        .run(1_000_000)
        .also { println(it) }
}
