package de.stefanbissell.fscombat.fs4

class Fs4Combat(
    private val playerTemplateA: Fs4Player,
    private val playerTemplateB: Fs4Player
) {

    fun run(index: Int): Fs4CombatResult {
        var rounds = 0
        val playerA = Fs4PlayerHandler(playerTemplateA)
        val playerB = Fs4PlayerHandler(playerTemplateB)

        while (playerA.isAlive && playerB.isAlive) {
            if (index % 2 == 0) {
                playerA.attack(playerB)
                if (playerB.isAlive) {
                    playerB.attack(playerA)
                }
            } else {
                playerB.attack(playerA)
                if (playerA.isAlive) {
                    playerA.attack(playerB)
                }
            }

            rounds++
        }

        return Fs4CombatResult(
            rounds, playerA, playerB
        )
    }
}

data class Fs4CombatResult(
    val rounds: Int,
    val playerA: Fs4PlayerHandler,
    val playerB: Fs4PlayerHandler
)
