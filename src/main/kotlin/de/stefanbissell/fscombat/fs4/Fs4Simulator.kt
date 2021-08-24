package de.stefanbissell.fscombat.fs4

import de.stefanbissell.fscombat.percent

class Fs4Simulator(
    private val playerAExpr: () -> Fs4Player,
    private val playerBExpr: () -> Fs4Player
) {

    fun run(runs: Int): SimulationResult {
        return sequence {
            repeat(runs) {
                yield(singleRun(it))
            }
        }.fold(RunResult()) { acc, run ->
            acc + run
        }.let {
            SimulationResult(runs, it.victoriesA, it.victoriesB, it.draws, it.rounds)
        }
    }

    private fun singleRun(index: Int): RunResult {
        return Fs4Combat(playerAExpr(), playerBExpr())
            .run(index)
            .toRunResult()
    }

    private fun Fs4CombatResult.toRunResult() =
        when {
            playerA.isAlive && !playerB.isAlive -> {
                RunResult(1, 0, 0, rounds)
            }
            !playerA.isAlive && playerB.isAlive -> {
                RunResult(0, 1, 0, rounds)
            }
            else -> {
                RunResult(0, 0, 1, rounds)
            }
        }
}

private data class RunResult(
    val victoriesA: Int = 0,
    val victoriesB: Int = 0,
    val draws: Int = 0,
    val rounds: Int = 0,
) {

    operator fun plus(other: RunResult) =
        RunResult(
            victoriesA = victoriesA + other.victoriesA,
            victoriesB = victoriesB + other.victoriesB,
            draws = draws + other.draws,
            rounds = rounds + other.rounds
        )
}

data class SimulationResult(
    val runs: Int,
    val victoriesA: Int,
    val victoriesB: Int,
    val draws: Int,
    val rounds: Int
) {

    override fun toString(): String {
        return """
            $victoriesA vs $victoriesB (draws $draws)
            ${victoriesA percent runs}% vs ${victoriesB percent runs}% (draws ${draws percent runs}%)
            Average rounds: ${rounds.toDouble() / runs}
        """.trimIndent()
    }
}
