package de.stefanbissell.fscombat.fs4

import de.stefanbissell.fscombat.rollD20
import kotlin.math.max

data class Fs4PlayerHandler(
    val player: Fs4Player,
    var vitality: Int = player.vitality,
    var shieldHits: Int = player.shield.hits
) {

    fun attack(otherPlayer: Fs4PlayerHandler) {
        AttackResolver.resolve(this, otherPlayer, rollD20())
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

    val bodyResistance = player.armor.resistance + player.weapon.resistance
    val weaponDamage = player.weapon.damage

    val isAlive
        get() = vitality > 0

    val activeShield
        get() = shieldHits > 0
}
