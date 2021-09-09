package de.stefanbissell.fscombat.fs4

import de.stefanbissell.fscombat.fs4.Fs4Armor.*
import de.stefanbissell.fscombat.fs4.Fs4Shield.*
import de.stefanbissell.fscombat.fs4.Fs4Weapon.*
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

        expectMiss()
    }

    @Test
    fun `takes weapon goal modifier into account`() {
        attackerTemplate = attackerTemplate.copy(
            strength = 7,
            melee = 7,
            weapon = Quarterstaff
        )
        AttackResolver.resolve(attacker, defender, 14)

        expectMiss()
    }

    @Test
    fun `takes precision into account`() {
        defenderTemplate = defenderTemplate.copy(
            shield = NoShield
        )
        AttackResolver.resolve(attacker, defender, 8, 3)

        expectThat(defender.vitality).isEqualTo(defenderTemplate.vitality - 8)
        expectThat(attacker.cache).isEqualTo(1)
    }

    @Test
    fun `takes weapon resistance modifier into account`() {
        defenderTemplate = defenderTemplate.copy(
            armor = PolymerKnit,
            weapon = MainGauche,
            shield = NoShield
        )
        AttackResolver.resolve(attacker, defender, 4)

        expectThat(defender.vitality).isEqualTo(defenderTemplate.vitality - 5)
        expectThat(attacker.cache).isEqualTo(1)
    }

    @Test
    fun `resolves success beating resistance but triggering shield`() {
        attackerTemplate = attackerTemplate.copy(
            strength = 7,
            melee = 7,
            weapon = GreatWeapon
        )

        AttackResolver.resolve(attacker, defender, 5)

        expectThat(defender.vitality).isEqualTo(defenderTemplate.vitality)
        expectThat(defender.shieldHits).isEqualTo(defenderTemplate.shield.hits - 1)
        expectThat(attacker.cache).isEqualTo(5)
    }

    @Test
    fun `resolves success with restraint`() {
        AttackResolver.resolve(attacker, defender, 3)

        expectThat(defender.vitality).isEqualTo(defenderTemplate.vitality - 4)
        expectThat(defender.shieldHits).isEqualTo(defenderTemplate.shield.hits)
        expectThat(attacker.cache).isEqualTo(0)
    }

    @Test
    fun `resolves success with restraint excess VP`() {
        AttackResolver.resolve(attacker, defender, 5)

        expectThat(defender.vitality).isEqualTo(defenderTemplate.vitality - 4)
        expectThat(defender.shieldHits).isEqualTo(defenderTemplate.shield.hits)
        expectThat(attacker.cache).isEqualTo(2)
    }

    @Test
    fun `resolves success with restraint if higher damage than over upper threshold`() {
        attackerTemplate = attackerTemplate.copy(
            strength = 7,
            melee = 7,
            weapon = GreatWeapon
        )

        AttackResolver.resolve(attacker, defender, 9)

        expectThat(defender.vitality).isEqualTo(defenderTemplate.vitality - 4)
        expectThat(defender.shieldHits).isEqualTo(defenderTemplate.shield.hits)
        expectThat(attacker.cache).isEqualTo(0)
    }

    @Test
    fun `resolves success going upper threshold if only option`() {
        attackerTemplate = attackerTemplate.copy(
            strength = 7,
            melee = 7,
            weapon = GreatWeapon
        )

        AttackResolver.resolve(attacker, defender, 8)

        expectThat(defender.vitality).isEqualTo(defenderTemplate.vitality - 1)
        expectThat(defender.shieldHits).isEqualTo(defenderTemplate.shield.hits - 1)
        expectThat(attacker.cache).isEqualTo(1)
    }

    @Test
    fun `resolves success going over upper threshold if same damage as under`() {
        attackerTemplate = attackerTemplate.copy(
            strength = 7,
            melee = 7,
            weapon = GreatWeapon
        )

        AttackResolver.resolve(attacker, defender, 13)

        expectThat(defender.vitality).isEqualTo(defenderTemplate.vitality - 4)
        expectThat(defender.shieldHits).isEqualTo(defenderTemplate.shield.hits - 1)
        expectThat(attacker.cache).isEqualTo(0)
    }

    @Test
    fun `defender boosts resistance with full VP`() {
        defenderTemplate = defenderTemplate.copy(
            boostBehaviour = BoostBehaviour.MaximumBoost
        )
        defender.cache = 2
        defender.shieldHits = 0

        AttackResolver.resolve(attacker, defender, 4)

        expectThat(defender.vitality).isEqualTo(defenderTemplate.vitality - 5)
        expectThat(defender.cache).isEqualTo(0)
        expectThat(attacker.cache).isEqualTo(1)
    }

    @Test
    fun `boost cost double with hindering armor`() {
        defenderTemplate = defenderTemplate.copy(
            armor = ScaleMail,
            boostBehaviour = BoostBehaviour.MaximumBoost
        )
        defender.cache = 2
        defender.shieldHits = 0
        attackerTemplate = attackerTemplate.copy(
            strength = 7,
            melee = 7
        )

        AttackResolver.resolve(attacker, defender, 8)

        expectThat(defender.vitality).isEqualTo(defenderTemplate.vitality - 5)
        expectThat(defender.cache).isEqualTo(0)
        expectThat(attacker.cache).isEqualTo(1)
    }

    private fun expectMiss() {
        expectThat(defender.vitality).isEqualTo(defenderTemplate.vitality)
        expectThat(defender.shieldHits).isEqualTo(defenderTemplate.shield.hits)
        expectThat(attacker.cache).isEqualTo(0)
    }
}
