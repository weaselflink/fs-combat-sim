package de.stefanbissell.fscombat.fs4

import de.stefanbissell.fscombat.percent

fun main() {
    var vicA = 0
    var vicB = 0

    val runs = 100_000
    val playerA = Fs4Player(
        name = "A",
        strength = 6,
        melee = 7,
        armor = Fs4Armor.ThickClothing,
        weapon = Fs4Weapon.Rapier,
        shield = Fs4Shield.Standard
    )
    val playerB = Fs4Player(
        name = "B",
        strength = 6,
        melee = 6,
        armor = Fs4Armor.ThickClothing,
        weapon = Fs4Weapon.Rapier,
        shield = Fs4Shield.Standard
    )
    val rounds = mutableListOf<Int>()
    repeat(runs) { index ->
        Fs4Combat(playerA, playerB)
            .run(index)
            .also {
                if (it.playerA.isAlive && !it.playerB.isAlive) {
                    vicA++
                }
                if (!it.playerA.isAlive && it.playerB.isAlive) {
                    vicB++
                }
                rounds += it.rounds
            }
    }

    val draws = runs - vicA - vicB
    println("$vicA vs $vicB (draws $draws)")
    println("${vicA percent runs}% vs ${vicB percent runs}% (draws ${draws percent runs}%)")
    println("Average rounds: ${rounds.average()}")
}
