package de.stefanbissell.fscombat.fs4

import de.stefanbissell.fscombat.rollD20
import kotlin.math.max
import kotlin.math.min

data class Fs4PlayerHandler(
    val player: Fs4Player,
    var vitality: Int = player.vitality,
    var shieldHits: Int = player.shield.hits
) {

    fun attack(otherPlayer: Fs4PlayerHandler) {
        attack(otherPlayer, rollD20())
    }

    fun attack(otherPlayer: Fs4PlayerHandler, diceRoll: Int) {
        val goal = player.strength + player.melee
        val roll = Fs4Roll(goal, diceRoll)
        if (roll.success && roll.victoryPoints > otherPlayer.bodyResistance) {
            val extraVp = roll.victoryPoints - otherPlayer.bodyResistance
            val maxPossibleDamage = weaponDamage + (extraVp / 2)
            val minPossibleDamage = weaponDamage - (extraVp / 2)
            if (otherPlayer.activeShield) {
                val overDamage = max(0, maxPossibleDamage - otherPlayer.player.shield.upper)
                val underShieldDamage = max(0, otherPlayer.player.shield.lower - 1)
                when {
                    overDamage > underShieldDamage -> otherPlayer.takeDamage(maxPossibleDamage)
                    weaponDamage <= underShieldDamage -> otherPlayer.takeDamage(min(underShieldDamage, maxPossibleDamage))
                    minPossibleDamage <= underShieldDamage -> otherPlayer.takeDamage(underShieldDamage)
                    overDamage > 0 -> otherPlayer.takeDamage(maxPossibleDamage)
                    else -> otherPlayer.takeDamage(weaponDamage)
                }
            } else {
                otherPlayer.takeDamage(maxPossibleDamage)
            }
        }
    }

    fun takeDamage(damage: Int) {
        vitality = if (activeShield && damage >= player.shield.lower) {
            val damageLeft = max(0, damage - player.shield.upper)
            shieldHits--
            max(0, vitality - damageLeft)
        } else {
            max(0, vitality - damage)
        }

    }

    private val bodyResistance = player.armor.resistance
    private val weaponDamage = player.weapon.damage

    val isAlive
        get() = vitality > 0

    private val activeShield
        get() = shieldHits > 0
}
