package de.stefanbissell.fscombat.fs4

import de.stefanbissell.fscombat.rollD20
import kotlin.math.max
import kotlin.math.min

class Fs4PlayerHandler(
    val player: Fs4Player,
    var vitality: Int = player.vitality,
    var shieldHits: Int = player.shield.hits,
    var cache: Int = 0
) {

    val weaponDamage = player.weapon.damage

    fun bodyResistance(weapon: Fs4Weapon) =
        player.armor.resistanceAgainst(weapon) + player.weapon.resistance

    fun attack(otherPlayer: Fs4PlayerHandler) {
        cache = min(cache, player.bank)
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

    val isAlive
        get() = vitality > 0

    val activeShield
        get() = shieldHits > 0
}
