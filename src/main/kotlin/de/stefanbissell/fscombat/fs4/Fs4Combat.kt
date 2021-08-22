package de.stefanbissell.fscombat.fs4

import de.stefanbissell.fscombat.rollD20
import kotlin.math.max

class Fs4Combat(
    private val playerTemplateA: Fs4Player,
    private val playerTemplateB: Fs4Player
) {

    fun run(index: Int): Fs4CombatResult {
        var rounds = 0
        val playerA = PlayerHandler(playerTemplateA)
        val playerB = PlayerHandler(playerTemplateB)

        while (playerA.isAlive && playerB.isAlive) {
            if (index % 2 == 0) {
                playerA.attack(playerB)
                if (playerB.isAlive) {
                    playerB.attack(playerA)
                }
            } else {
                playerB.attack(playerA)
                if (playerA.isAlive) {
                    playerA.attack(playerB)
                }
            }

            rounds++
        }

        return Fs4CombatResult(
            rounds, playerA, playerB
        )
    }
}

data class PlayerHandler(
    val player: Fs4Player,
    var currentVitality: Int = player.vitality,
    var currentShieldHits: Int = player.shield.hits
) {
    fun attack(otherPlayer: PlayerHandler) {
        val goal = player.strength + player.melee
        val roll = Fs4Roll(goal, rollD20())
        if (roll.success && roll.victoryPoints > otherPlayer.bodyResistance) {
            val extraVp = roll.victoryPoints - otherPlayer.bodyResistance
            val maxPossibleDamage = weaponDamage + (extraVp / 2)
            val minPossibleDamage = weaponDamage - (extraVp / 2)
            if (otherPlayer.activeShield) {
                val overDamage = max(0, maxPossibleDamage - otherPlayer.player.shield.upper)
                val underShieldDamage = max(0, otherPlayer.player.shield.lower - 1)
                when {
                    overDamage > underShieldDamage -> otherPlayer.takeDamage(maxPossibleDamage)
                    weaponDamage <= underShieldDamage -> otherPlayer.takeDamage(weaponDamage)
                    minPossibleDamage <= underShieldDamage -> otherPlayer.takeDamage(underShieldDamage)
                    else -> otherPlayer.takeDamage(maxPossibleDamage)
                }
            } else {
                otherPlayer.takeDamage(maxPossibleDamage)
            }
        }
    }

    private fun takeDamage(damage: Int) {
        currentVitality = if (activeShield && damage >= player.shield.lower) {
            val damageLeft = max(0, damage - player.shield.upper)
            currentShieldHits--
            max(0, currentVitality - damageLeft)
        } else {
            max(0, currentVitality - damage)
        }

    }

    private val bodyResistance = player.armor.resistance
    private val weaponDamage = player.weapon.damage

    val isAlive
        get() = currentVitality > 0

    private val activeShield
        get() = currentShieldHits > 0
}

data class Fs4CombatResult(
    val rounds: Int,
    val playerA: PlayerHandler,
    val playerB: PlayerHandler
)
