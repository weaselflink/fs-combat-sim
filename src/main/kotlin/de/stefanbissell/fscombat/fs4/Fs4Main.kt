package de.stefanbissell.fscombat.fs4

fun main() {
    val playerA = {
        Fs4Player(
            strength = (3..10).random(),
            melee = (3..10).random(),
            armor = Fs4Armor.ThickClothing,
            weapon = Fs4Weapon.Rapier,
            shield = Fs4Shield.Standard
        )
    }
    val playerB = {
        Fs4Player(
            strength = (3..10).random(),
            melee = (3..10).random(),
            armor = Fs4Armor.ScaleMail,
            weapon = Fs4Weapon.Dagger,
            shield = Fs4Shield.None
        )
    }

    Fs4Simulator(playerA, playerB)
        .run(100_000)
        .also { println(it) }
}
