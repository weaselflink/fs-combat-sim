package de.stefanbissell.fscombat.fs4

import kotlin.math.max
import kotlin.math.min

class Fs4Roll(
    goal: Int,
    roll: Int
) {

    private val adjustedGoal = min(goal, 19)
    val success = roll <= adjustedGoal || roll == 1
    val critical = adjustedGoal == roll
    val victoryPoints = if (success) {
        roll + max(0, goal - 20)
    } else {
        0
    }
}
