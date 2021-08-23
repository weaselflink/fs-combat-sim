package de.stefanbissell.fscombat

import kotlin.math.pow
import kotlin.math.roundToInt

fun rollD20() = (1..20).random()

infix fun Int.percent(total: Int) = ((this.toDouble() / total) * 100).roundToInt()

fun Double.round(places: Int) =
    10.0.pow(places).toInt()
        .let { factor ->
            (this * factor).roundToInt().toDouble() / factor
        }
