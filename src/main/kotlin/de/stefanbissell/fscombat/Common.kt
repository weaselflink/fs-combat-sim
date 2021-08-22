package de.stefanbissell.fscombat

import kotlin.math.roundToInt
import kotlin.random.Random

fun rollD20() = Random.nextInt(20) + 1

infix fun Int.percent(total: Int) = ((this.toDouble() / total) * 100).roundToInt()
