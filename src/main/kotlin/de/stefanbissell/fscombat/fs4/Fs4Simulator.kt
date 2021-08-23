package de.stefanbissell.fscombat.fs4

import de.stefanbissell.fscombat.percent
import kotlin.math.max
import kotlin.math.roundToInt

class Fs4Simulator(
    private val playerAExpr: () -> Fs4Player,
    private val playerBExpr: () -> Fs4Player
) {

    fun run(runs: Int): SimulationResult {
        val vicA = IntStatistic()
        val vicB = IntStatistic()
        val rounds = IntStatistic()
        repeat(runs) { index ->
            Fs4Combat(playerAExpr(), playerBExpr())
                .run(index)
                .also {
                    if (it.playerA.isAlive && !it.playerB.isAlive) {
                        vicA += 1
                        vicB += 0
                    }
                    if (!it.playerA.isAlive && it.playerB.isAlive) {
                        vicA += 0
                        vicB += 1
                    }
                    if (!it.playerA.isAlive && !it.playerB.isAlive) {
                        vicA += 0
                        vicB += 0
                    }
                    rounds += it.rounds
                }
        }

        return SimulationResult(runs, vicA, vicB, rounds)
    }
}

data class SimulationResult(
    val runs: Int,
    val victoriesA: IntStatistic,
    val victoriesB: IntStatistic,
    val rounds: IntStatistic,
    val draws: Int = runs - (victoriesA.sum + victoriesB.sum)
) {

    override fun toString(): String {
        return """
            ${victoriesA.sum} vs ${victoriesB.sum} (draws $draws)
            ${victoriesA.percent}% vs ${victoriesB.percent}% (draws ${draws percent runs}%)
            Average rounds: ${rounds.average}
        """.trimIndent()
    }
}

class IntStatistic {

    private var count: Int = 0
    var sum: Int = 0

    operator fun plusAssign(value: Int) {
        count++
        sum += value
    }

    val average
        get() = sum.toDouble() / max(1, count)

    val percent
        get() = (average * 100).roundToInt()
}
