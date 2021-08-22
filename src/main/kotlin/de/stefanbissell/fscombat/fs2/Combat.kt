package de.stefanbissell.fscombat.fs2

class Combat(
    private val playerA: Player,
    private val playerB: Player
) {

    fun run(): CombatResult {
        var rounds = 0

        while (playerA.isAlive && playerB.isAlive) {
            when {
                playerA.initiativeOrder(playerB) > 0 -> {
                    playerA.attack(playerB)
                    playerB.endOfTurn()
                    if (playerB.isAlive) {
                        playerB.attack(playerA)
                        playerA.endOfTurn()
                    }
                }
                playerA.initiativeOrder(playerB) < 0 -> {
                    playerB.attack(playerA)
                    playerA.endOfTurn()
                    if (playerA.isAlive) {
                        playerA.attack(playerB)
                        playerB.endOfTurn()
                    }
                }
                else -> {
                    playerA.attack(playerB)
                    playerB.attack(playerA)
                    playerA.endOfTurn()
                    playerB.endOfTurn()
                }
            }
            rounds++
        }

        return CombatResult(
            rounds = rounds,
            playerA = playerA,
            playerB = playerB
        )
    }
}

data class CombatResult(
    val rounds: Int,
    val playerA: Player,
    val playerB: Player
)
