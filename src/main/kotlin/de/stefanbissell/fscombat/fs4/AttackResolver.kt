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
        if (roll.success && roll.victoryPoints > defender.bodyResistance) {
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
                    overDamage > underShieldDamage -> defender.takeDamage(maxPossibleDamage)
                    attacker.weaponDamage <= underShieldDamage -> defender.takeDamage(min(underShieldDamage, maxPossibleDamage))
                    minPossibleDamage <= underShieldDamage -> defender.takeDamage(underShieldDamage)
                    overDamage > 0 -> defender.takeDamage(maxPossibleDamage)
                    else -> defender.takeDamage(attacker.weaponDamage)
                }
            } else {
                defender.takeDamage(maxPossibleDamage)
            }
        }
    }
}
