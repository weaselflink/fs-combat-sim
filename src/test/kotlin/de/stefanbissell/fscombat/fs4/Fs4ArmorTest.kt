package de.stefanbissell.fscombat.fs4

import org.junit.jupiter.api.DynamicTest
import org.junit.jupiter.api.TestFactory
import strikt.api.expectThat
import strikt.assertions.isEqualTo

class Fs4ArmorTest {

    @TestFactory
    fun `calculates resistance against weapon`(): List<DynamicTest> {
        return listOf(
            Triple(Fs4Armor.ThickClothing, Fs4Weapon.Knife, 1),
            Triple(Fs4Armor.ThickClothing, Fs4Weapon.Mace, 0),
            Triple(Fs4Armor.ThickClothing, Fs4Weapon.UkariPunchBlade, 0),
            Triple(Fs4Armor.LeatherJerkin, Fs4Weapon.Mace, 1),
            Triple(Fs4Armor.PolymerKnit, Fs4Weapon.Mace, 2),
            Triple(Fs4Armor.FullPlatePlastic, Fs4Weapon.UkariPunchBlade, 4),
            Triple(Fs4Armor.FullPlatePlasteel, Fs4Weapon.UkariPunchBlade, 8),
        ).map { (armor, weapon, resistance) ->
            DynamicTest.dynamicTest("$armor offers $resistance resistance against $weapon") {
                expectThat(armor.resistanceAgainst(weapon)).isEqualTo(resistance)
            }
        }
    }
}
