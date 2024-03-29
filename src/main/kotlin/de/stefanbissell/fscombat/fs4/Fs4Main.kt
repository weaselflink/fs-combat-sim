package de.stefanbissell.fscombat.fs4

import de.stefanbissell.fscombat.fs4.BoostBehaviour.*
import de.stefanbissell.fscombat.fs4.Fs4Armor.*
import de.stefanbissell.fscombat.fs4.Fs4Shield.*
import de.stefanbissell.fscombat.fs4.Fs4Weapon.*
import de.stefanbissell.fscombat.fs4.PrecisionBehaviour.*
import kotlin.time.ExperimentalTime
import kotlin.time.measureTime

@ExperimentalTime
fun main() {
    val playerA = {
        Fs4Player(
            strength = (3..10).random(),
            melee = (3..10).random(),
            armor = ThickClothing,
            weapon = Rapier,
            shield = Standard,
            boostBehaviour = OnlyWhenHitAvoided,
            precisionBehaviour = OnlyUseLostCache
        )
    }
    val playerB = {
        Fs4Player(
            strength = (3..10).random(),
            melee = (3..10).random(),
            armor = ScaleMail,
            weapon = UkariPunchBlade,
            shield = NoShield,
            boostBehaviour = NoBoost,
            precisionBehaviour = OnlyUseLostCache
        )
    }

    measureTime {
        Fs4Simulator(playerA, playerB)
            .run(100_000)
            .also { println(it) }
    }.also {
        println("${it.inWholeMilliseconds} ms")
    }
    println()
}
