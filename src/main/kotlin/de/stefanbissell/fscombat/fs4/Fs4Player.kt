package de.stefanbissell.fscombat.fs4

data class Fs4Player(
    val name: String = "",
    val level: Int = 1,
    val size: Int = 3,
    val strength: Int = 3,
    val endurance: Int = 3,
    val will: Int = 3,
    val faith: Int = 3,
    val melee: Int = 3,
    val vitality: Int = size + endurance + will + faith + level,
    val bank: Int = 5,
    val armor: Fs4Armor = Fs4Armor.None,
    val weapon: Fs4Weapon = Fs4Weapon.Rapier,
    val shield: Fs4Shield = Fs4Shield.Standard,
)
