package de.stefanbissell.fscombat.fs4

import org.junit.jupiter.api.Test
import strikt.api.expectThat
import strikt.assertions.isEqualTo

class AttackResolverTest {

    private var attackerTemplate = Fs4Player()
    private var defenderTemplate = Fs4Player()
    private val attacker by lazy { Fs4PlayerHandler(attackerTemplate) }
    private val defender by lazy { Fs4PlayerHandler(defenderTemplate) }

    @Test
    fun `resolves failure`() {
        AttackResolver.resolve(attacker, defender, 7)

        expectThat(defender.vitality).isEqualTo(defenderTemplate.vitality)
        expectThat(defender.shieldHits).isEqualTo(defenderTemplate.shield.hits)
    }

    @Test
    fun `resolves success beating resistance but triggering shield`() {
        AttackResolver.resolve(attacker, defender, 1)

        expectThat(defender.vitality).isEqualTo(defenderTemplate.vitality)
        expectThat(defender.shieldHits).isEqualTo(defenderTemplate.shield.hits - 1)
    }

    @Test
    fun `resolves success with restraint`() {
        AttackResolver.resolve(attacker, defender, 3)

        expectThat(defender.vitality).isEqualTo(defenderTemplate.vitality - 4)
        expectThat(defender.shieldHits).isEqualTo(defenderTemplate.shield.hits)
    }

    @Test
    fun `resolves success with restraint excess VP`() {
        AttackResolver.resolve(attacker, defender, 5)

        expectThat(defender.vitality).isEqualTo(defenderTemplate.vitality - 4)
        expectThat(defender.shieldHits).isEqualTo(defenderTemplate.shield.hits)
    }

    @Test
    fun `resolves success with restraint if higher damage than over upper threshold`() {
        attackerTemplate = attackerTemplate.copy(
            strength = 7,
            melee = 7,
            weapon = Fs4Weapon.GreatWeapon
        )

        AttackResolver.resolve(attacker, defender, 9)

        expectThat(defender.vitality).isEqualTo(defenderTemplate.vitality - 4)
        expectThat(defender.shieldHits).isEqualTo(defenderTemplate.shield.hits)
    }

    @Test
    fun `resolves success going upper threshold if only option`() {
        attackerTemplate = attackerTemplate.copy(
            strength = 7,
            melee = 7,
            weapon = Fs4Weapon.GreatWeapon
        )

        AttackResolver.resolve(attacker, defender, 7)

        expectThat(defender.vitality).isEqualTo(defenderTemplate.vitality - 1)
        expectThat(defender.shieldHits).isEqualTo(defenderTemplate.shield.hits - 1)
    }

    @Test
    fun `resolves success going over upper threshold if same damage as under`() {
        attackerTemplate = attackerTemplate.copy(
            strength = 7,
            melee = 7,
            weapon = Fs4Weapon.GreatWeapon
        )

        AttackResolver.resolve(attacker, defender, 13)

        expectThat(defender.vitality).isEqualTo(defenderTemplate.vitality - 4)
        expectThat(defender.shieldHits).isEqualTo(defenderTemplate.shield.hits - 1)
    }
}
