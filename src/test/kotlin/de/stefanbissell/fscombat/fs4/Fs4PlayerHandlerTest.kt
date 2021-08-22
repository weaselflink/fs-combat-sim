package de.stefanbissell.fscombat.fs4

import de.stefanbissell.fscombat.fs4.Fs4Shield.*
import org.junit.jupiter.api.DynamicTest
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestFactory
import strikt.api.expectThat
import strikt.assertions.isEqualTo

class Fs4PlayerHandlerTest {

    @Test
    fun `take damage without shield`() {
        val handler = Fs4PlayerHandler(
            player = Fs4Player(shield = None),
            vitality = 20
        )

        val damage = 6
        handler.takeDamage(damage)

        expectThat(20 - handler.vitality).isEqualTo(damage)
    }

    @TestFactory
    fun `take damage with shield`(): List<DynamicTest> {
        return listOf(
            ShieldTest(Antique, 1, false, 1),
            ShieldTest(Antique, 5, false, 5),
            ShieldTest(Antique, 6, true, 0),
            ShieldTest(Antique, 9, true, 0),
            ShieldTest(Antique, 10, true, 1),
            ShieldTest(Antique, 15, true, 6),
            ShieldTest(Standard, 1, false, 1),
            ShieldTest(Standard, 4, false, 4),
            ShieldTest(Standard, 5, true, 0),
            ShieldTest(Standard, 10, true, 0),
            ShieldTest(Standard, 11, true, 1),
            ShieldTest(Standard, 15, true, 5),
            ShieldTest(Assault, 4, false, 4),
            ShieldTest(Assault, 5, true, 0),
            ShieldTest(Assault, 15, true, 0),
            ShieldTest(Assault, 16, true, 1),
            ShieldTest(Assault, 20, true, 5),
            ShieldTest(Battle, 4, false, 4),
            ShieldTest(Battle, 5, true, 0),
            ShieldTest(Battle, 20, true, 0),
            ShieldTest(Battle, 21, true, 1),
            ShieldTest(Battle, 25, true, 5),
        ).map {
            DynamicTest.dynamicTest(it.display) {
                val handler = Fs4PlayerHandler(
                    player = Fs4Player(shield = it.shield),
                    vitality = 20
                )

                handler.takeDamage(it.damage)

                expectThat(20 - handler.vitality)
                    .isEqualTo(it.effectiveDamage)
                expectThat(handler.shieldHits < handler.player.shield.hits)
                    .isEqualTo(it.activated)
            }
        }
    }
}

private data class ShieldTest(
    val shield: Fs4Shield,
    val damage: Int,
    val activated: Boolean,
    val effectiveDamage: Int
) {

    val display
        get() = if (activated) {
            "$shield takes $damage -> activates and blocks ${damage - effectiveDamage}"
        } else {
            "$shield takes $damage -> does not activate, $damage damage taken"
        }
}
