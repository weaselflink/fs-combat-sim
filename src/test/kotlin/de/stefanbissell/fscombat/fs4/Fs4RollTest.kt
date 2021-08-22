package de.stefanbissell.fscombat.fs4

import org.junit.jupiter.api.DynamicTest
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestFactory
import strikt.api.expectThat
import strikt.assertions.isEqualTo

class Fs4RollTest {

    @TestFactory
    fun `check rolls`(): List<DynamicTest> {
        return listOf(
            TestRoll(0, 1, 1, true),
            TestRoll(0, 2, 0, false),
            TestRoll(0, 20, 0, false),
            TestRoll(10, 1, 1, true),
            TestRoll(10, 5, 5, true),
            TestRoll(10, 9, 9, true),
            TestRoll(10, 10, 10, success = true, critical = true),
            TestRoll(10, 11, 0, false),
            TestRoll(10, 20, 0, false),
            TestRoll(15, 1, 1, true),
            TestRoll(15, 5, 5, true),
            TestRoll(15, 14, 14, true),
            TestRoll(15, 15, 15, success = true, critical = true),
            TestRoll(15, 16, 0, false),
            TestRoll(15, 20, 0, false),
            TestRoll(20, 1, 1, true),
            TestRoll(20, 5, 5, true),
            TestRoll(20, 19, 19, success = true, critical = true),
            TestRoll(20, 20, 0, false),
            TestRoll(25, 1, 6, true),
            TestRoll(25, 5, 10, true),
            TestRoll(25, 19, 24, success = true, critical = true),
            TestRoll(25, 20, 0, false),
        ).map {
            DynamicTest.dynamicTest(it.display) {
                val roll = Fs4Roll(it.goal, it.roll)
                expectThat(roll.victoryPoints).isEqualTo(it.vp)
                expectThat(roll.success).isEqualTo(it.success)
            }
        }
    }
}

private data class TestRoll(
    val goal: Int,
    val roll: Int,
    val vp: Int,
    val success: Boolean,
    val critical: Boolean = false
) {

    val display = when {
        critical -> "goal: $goal, roll: $roll -> critical success with $vp VP"
        success -> "goal: $goal, roll: $roll -> success with $vp VP"
        else -> "goal: $goal, roll: $roll -> fail"
    }
}
