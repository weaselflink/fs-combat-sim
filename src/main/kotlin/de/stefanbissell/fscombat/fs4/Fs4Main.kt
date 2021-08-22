package de.stefanbissell.fscombat.fs4

fun main() {
    val playerA = Fs4Player(
        name = "A",
        strength = 8,
        melee = 8,
        armor = Fs4Armor.ThickClothing,
        weapon = Fs4Weapon.Knife,
        shield = Fs4Shield.Standard
    )
    val playerB = Fs4Player(
        name = "B",
        strength = 8,
        melee = 8,
        armor = Fs4Armor.ThickClothing,
        weapon = Fs4Weapon.GreatWeapon,
        shield = Fs4Shield.Standard
    )

    Fs4Simulator(playerA, playerB)
        .run()
        .also { println(it) }
}
