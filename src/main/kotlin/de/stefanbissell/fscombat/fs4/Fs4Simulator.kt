package de.stefanbissell.fscombat.fs4

import de.stefanbissell.fscombat.percent

class Fs4Simulator(
    private val playerA: Fs4Player,
    private val playerB: Fs4Player
) {

    fun run(runs: Int = 100_000): SimulationResult {
        var vicA = 0
        var vicB = 0
        val rounds = mutableListOf<Int>()
        repeat(runs) { index ->
            Fs4Combat(playerA, playerB)
                .run(index)
                .also {
                    if (it.playerA.isAlive && !it.playerB.isAlive) {
                        vicA++
                    }
                    if (!it.playerA.isAlive && it.playerB.isAlive) {
                        vicB++
                    }
                    rounds += it.rounds
                }
        }

        return SimulationResult(runs, vicA, vicB, rounds)
    }
}

data class SimulationResult(
    val runs: Int,
    val victoriesA: Int,
    val victoriesB: Int,
    val rounds: List<Int>,
    val draws: Int = runs - (victoriesA + victoriesB)
) {

    override fun toString(): String {
        return """
            $victoriesA vs $victoriesB (draws $draws)
            ${victoriesA percent runs}% vs ${victoriesB percent runs}% (draws ${draws percent runs}%)
            Average rounds: ${rounds.average()}
        """.trimIndent()
    }
}
