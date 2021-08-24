package de.stefanbissell.fscombat.fs4

import kotlin.math.max
import kotlin.math.min

object AttackResolver {

    fun resolve(
        attacker: Fs4PlayerHandler,
        defender: Fs4PlayerHandler,
        diceRoll: Int
    ) {
        AttackResolverInstance(attacker, defender, diceRoll).resolve()
    }
}

private class AttackResolverInstance(
    private val attacker: Fs4PlayerHandler,
    private val defender: Fs4PlayerHandler,
    private val diceRoll: Int
) {

    fun resolve() {
        val goal = attacker.player.strength + attacker.player.melee
        val roll = Fs4Roll(goal, diceRoll)
        if (roll.success) {
            resolveSuccess(roll)
        }
    }

    private fun resolveSuccess(roll: Fs4Roll) {
        attacker.cache += roll.victoryPoints
        val boostedResistance = if (roll.critical) {
            0
        } else {
            boostedResistance()
        }
        if (attacker.cache >= boostedResistance) {
            val extraVp = attacker.cache - boostedResistance
            val maxPossibleDamage = attacker.weaponDamage + (extraVp / 2)
            if (defender.activeShield) {
                val minPossibleDamage = attacker.weaponDamage - (extraVp / 2)
                val overDamage = max(0, maxPossibleDamage - defender.player.shield.upper)
                val underShieldDamage = max(0, defender.player.shield.lower - 1)
                when {
                    overDamage >= underShieldDamage -> inflictMaxDamage(extraVp, boostedResistance)
                    minPossibleDamage <= underShieldDamage -> goUnderShield(extraVp, boostedResistance)
                    overDamage > 0 -> inflictMaxDamage(extraVp, boostedResistance)
                    else -> inflictRegularDamage()
                }
            } else {
                inflictMaxDamage(extraVp, boostedResistance)
            }
        }
    }

    private fun inflictMaxDamage(extraVp: Int, resistance: Int) {
        val damageIncrease = extraVp / 2
        attacker.cache -= resistance
        attacker.cache -= damageIncrease * 2
        val maxPossibleDamage = attacker.weaponDamage + damageIncrease
        defender.takeDamage(maxPossibleDamage)
    }

    private fun inflictRegularDamage() {
        defender.takeDamage(attacker.weaponDamage)
    }

    private fun goUnderShield(extraVp: Int, resistance: Int) {
        attacker.cache -= resistance
        if (attacker.weaponDamage < defender.player.shield.lower) {
            val boostNeeded = (defender.player.shield.lower - 1) - attacker.weaponDamage
            val boostAvailable = extraVp / 2
            val boost = min(boostNeeded, boostAvailable)
            attacker.cache -= boost * 2
            defender.takeDamage(attacker.weaponDamage + boost)
        } else {
            val restraint = attacker.weaponDamage - (defender.player.shield.lower - 1)
            attacker.cache -= restraint * 2
            defender.takeDamage(attacker.weaponDamage - restraint)
        }
    }

    private fun boostedResistance(): Int {
        val boost = BoostDecider(attacker, defender).decide()
        payBoostCost(defender, boost)
        return defender.bodyResistance + boost
    }

    private fun payBoostCost(defender: Fs4PlayerHandler, boost: Int) {
        if (defender.player.armor.hindering) {
            defender.cache -= boost * 2
        } else {
            defender.cache -= boost
        }
    }
}

class BoostDecider(
    private val attacker: Fs4PlayerHandler,
    private val defender: Fs4PlayerHandler
) {

    fun decide(): Int {
        val invincibleResistance = attacker.cache + 1
        val boostToInvincible = invincibleResistance - defender.bodyResistance
        val maxBoost = if (defender.player.armor.hindering) {
            defender.cache / 2
        } else {
            defender.cache
        }
        return if (maxBoost >= boostToInvincible) {
            boostToInvincible
        } else {
            maxBoost
        }
    }
}
