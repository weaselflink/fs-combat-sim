package de.stefanbissell.fscombat.fs4

import kotlin.math.max
import kotlin.math.min

object AttackResolver {

    fun resolve(
        attacker: Fs4PlayerHandler,
        defender: Fs4PlayerHandler,
        diceRoll: Int
    ) {
        val goal = attacker.player.strength + attacker.player.melee
        val roll = Fs4Roll(goal, diceRoll)
        if (roll.success) {
            resolveSuccess(roll, attacker, defender)
        }
    }

    private fun resolveSuccess(
        roll: Fs4Roll,
        attacker: Fs4PlayerHandler,
        defender: Fs4PlayerHandler
    ) {
        if (roll.victoryPoints > defender.bodyResistance) {
            val resistance = if (roll.critical) {
                0
            } else {
                defender.bodyResistance
            }
            val extraVp = roll.victoryPoints - resistance
            val maxPossibleDamage = attacker.weaponDamage + (extraVp / 2)
            if (defender.activeShield) {
                val minPossibleDamage = attacker.weaponDamage - (extraVp / 2)
                val overDamage = max(0, maxPossibleDamage - defender.player.shield.upper)
                val underShieldDamage = max(0, defender.player.shield.lower - 1)
                when {
                    overDamage >= underShieldDamage -> inflictMaxDamage(attacker, defender, extraVp)
                    minPossibleDamage <= underShieldDamage -> goUnderShield(attacker, defender, extraVp)
                    overDamage > 0 -> inflictMaxDamage(attacker, defender, extraVp)
                    else -> inflictRegularDamage(attacker, defender)
                }
            } else {
                inflictMaxDamage(attacker, defender, extraVp)
            }
        }
    }

    private fun inflictMaxDamage(
        attacker: Fs4PlayerHandler,
        defender: Fs4PlayerHandler,
        extraVp: Int
    ) {
        val maxPossibleDamage = attacker.weaponDamage + (extraVp / 2)
        defender.takeDamage(maxPossibleDamage)
    }

    private fun inflictRegularDamage(
        attacker: Fs4PlayerHandler,
        defender: Fs4PlayerHandler
    ) {
        defender.takeDamage(attacker.weaponDamage)
    }

    private fun goUnderShield(
        attacker: Fs4PlayerHandler,
        defender: Fs4PlayerHandler,
        extraVp: Int
    ) {
        val maxPossibleDamage = attacker.weaponDamage + (extraVp / 2)
        val underShieldDamage = max(0, defender.player.shield.lower - 1)
        defender.takeDamage(min(underShieldDamage, maxPossibleDamage))
    }
}
