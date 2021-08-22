package de.stefanbissell.fscombat.fs2

import de.stefanbissell.fscombat.rollD20
import kotlin.math.max
import kotlin.random.Random

data class Player(
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
