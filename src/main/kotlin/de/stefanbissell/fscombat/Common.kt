package de.stefanbissell.fscombat

import kotlin.math.roundToInt

fun rollD20() = (1..20).random()

infix fun Int.percent(total: Int) = ((this.toDouble() / total) * 100).roundToInt()
