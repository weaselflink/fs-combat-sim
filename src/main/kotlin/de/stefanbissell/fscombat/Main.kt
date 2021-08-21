package de.stefanbissell.fscombat

import kotlin.math.roundToInt

fun main() {
    var vicA = 0
    var vicB = 0

    val runs = 100_000
    val playerA = {
        Player(
            name = "A",
            dexterity = 6,
            endurance = 4,
            wits = 3,
            melee = 6,
            armor = Armor.HeavyCloth,
            weapon = Weapon.Rapier,
            shield = Shield.Dueling
        )
    }
    val playerB = {
        Player(
            name = "B",
            dexterity = 6,
            endurance = 4,
            wits = 3,
            melee = 6,
            armor = Armor.ChainMail,
            weapon = Weapon.TwoHandedSword,
            shield = Shield.None
        )
    }
    val rounds = mutableListOf<Int>()
    repeat(runs) {
        Combat(playerA(), playerB())
            .run()
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

private infix fun Int.percent(total: Int) = ((this.toDouble() / total) * 100).roundToInt()
