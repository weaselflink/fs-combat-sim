package de.stefanbissell.fscombat.fs4

fun main() {
    val playerA = Fs4Player(
        name = "A",
        strength = 6,
        melee = 7,
        armor = Fs4Armor.ThickClothing,
        weapon = Fs4Weapon.Rapier,
        shield = Fs4Shield.Standard
    )
    val playerB = Fs4Player(
        name = "B",
        strength = 6,
        melee = 6,
        armor = Fs4Armor.ThickClothing,
        weapon = Fs4Weapon.Rapier,
        shield = Fs4Shield.Standard
    )

    Fs4Simulator(playerA, playerB)
        .run()
        .also { println(it) }
}
