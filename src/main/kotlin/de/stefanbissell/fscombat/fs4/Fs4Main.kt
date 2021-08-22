package de.stefanbissell.fscombat.fs4

fun main() {
    val playerA = Fs4Player(
        name = "A",
        strength = 5,
        melee = 5,
        armor = Fs4Armor.ThickClothing,
        weapon = Fs4Weapon.Dagger,
        shield = Fs4Shield.Standard
    )
    val playerB = Fs4Player(
        name = "B",
        strength = 5,
        melee = 5,
        armor = Fs4Armor.ScaleMail,
        weapon = Fs4Weapon.Dagger,
        shield = Fs4Shield.None
    )

    Fs4Simulator(playerA, playerB)
        .run()
        .also { println(it) }
}
