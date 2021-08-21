package de.stefanbissell.fscombat

import kotlin.math.max
import kotlin.math.roundToInt
import kotlin.random.Random

fun main() {
    var vicA = 0
    var vicB = 0

    val runs = 100_000
    val playerA = {
        Player(
            name = "A",
            dexterity = 8,
            endurance = 4,
            wits = 3,
            melee = 8,
            armor = Armor.ChainMail,
            weapon = Weapon.BroadSword,
            shield = Shield.None
        )
    }
    val playerB = {
        Player(
            name = "B",
            dexterity = 8,
            endurance = 4,
            wits = 3,
            melee = 8,
            armor = Armor.ChainMail,
            weapon = Weapon.BroadSword,
            shield = Shield.None
        )
    }
    val rounds = mutableListOf<Int>()
    repeat(runs) {
        runCombat(playerA, playerB)
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

private fun runCombat(
    playerAExpr: () -> Player = { Player("A") },
    playerBExpr: () -> Player = { Player("B") }
): CombatResult {
    val playerA = playerAExpr()
    val playerB = playerBExpr()
    var rounds = 0

    while (playerA.isAlive && playerB.isAlive) {
        when {
            playerA.initiativeOrder(playerB) > 0 -> {
                playerA.attack(playerB)
                playerB.endOfTurn()
                if (playerB.isAlive) {
                    playerB.attack(playerA)
                    playerA.endOfTurn()
                }
            }
            playerA.initiativeOrder(playerB) < 0 -> {
                playerB.attack(playerA)
                playerA.endOfTurn()
                if (playerA.isAlive) {
                    playerA.attack(playerB)
                    playerB.endOfTurn()
                }
            }
            else -> {
                playerA.attack(playerB)
                playerB.attack(playerA)
                playerA.endOfTurn()
                playerB.endOfTurn()
            }
        }
        rounds++
    }

    return CombatResult(
        rounds = rounds,
        playerA = playerA,
        playerB = playerB
    )
}

data class CombatResult(
    val rounds: Int,
    val playerA: Player,
    val playerB: Player
)

class Player(
    val name: String,
    private val dexterity: Int = 3,
    private val endurance: Int = 3,
    private val wits: Int = 3,
    private val melee: Int = 3,
    private val armor: Armor = Armor.None,
    private val weapon: Weapon = Weapon.Rapier,
    private val shield: Shield = Shield.None,
    private var vitality: Int = 5 + endurance
) {

    private val shieldHandler = ShieldHandler(shield)
    private var pendingDamage = 0

    fun attack(other: Player) {
        val result = rollSkill(goalRoll)
        if (result.success) {
            val damageDice = adjustDamageDiceToShield(result.effectDice, other)
            val damage = rollDamage(damageDice)
            other.applyDamage(damage)
        }
    }

    fun endOfTurn() {
        vitality -= pendingDamage
        pendingDamage = 0
    }

    fun initiativeOrder(other: Player): Int =
        when {
            initiative > other.initiative -> 1
            initiative < other.initiative -> -1
            wits > other.wits -> 1
            wits < other.wits -> -1
            else -> 0
        }

    private fun adjustDamageDiceToShield(effectDice: Int, other: Player): Int {
        val damageDice = weapon.damage + effectDice
        if (!other.shieldHandler.hasCharges) {
            return damageDice
        }
        if (damageDice < other.shield.lowThresh) {
            return damageDice
        }
        if (damageDice > other.shield.highThresh * 1.5) {
            return damageDice
        }
        if (weapon.damage >= other.shield.lowThresh) {
            return weapon.damage
        }
        return other.shield.lowThresh - 1
    }

    private fun applyDamage(damage: Int) {
        val damageAfterShield = shieldHandler.absorb(damage)
        val damageAfterArmor = max(0, damageAfterShield - rollDamage(armor.dice))
        pendingDamage += damageAfterArmor
    }

    private val goalRoll
        get() = dexterity + melee + armor.dex + woundModifier
    private val woundModifier
        get() = if (vitality <= 5) {
            (vitality - 6) * 2
        } else {
            0
        }
    private val initiative
        get() = melee + woundModifier
    val isAlive
        get() = vitality > 0
}

class ShieldHandler(
    private val shield: Shield,
    private var shieldHitsTaken: Int = 0
) {

    val hasCharges
        get() = shield.hits > shieldHitsTaken

    fun absorb(damage: Int) =
        if (hasCharges && shield.activates(damage)) {
            shieldHitsTaken++
            shield.absorb(damage)
        } else {
            damage
        }
}

fun rollD20() = Random.nextInt(20) + 1

fun rollSkill(goal: Int): RollResult {
    val roll = rollD20()
    return when {
        roll == 1 -> RollResult(roll, goal, true)
        roll == 20 -> RollResult(roll, goal)
        roll == goal -> RollResult(roll, goal, true, roll / 3 * 2)
        roll < goal -> RollResult(roll, goal, true, roll / 3)
        else -> RollResult(roll, goal)
    }
}

fun rollDamage(damageDice: Int) =
    (0 until damageDice)
        .map { Random.nextInt(6) + 1 }
        .count { it < 5 }

data class RollResult(
    val roll: Int,
    val goal: Int,
    val success: Boolean = false,
    val effectDice: Int = 0
)
