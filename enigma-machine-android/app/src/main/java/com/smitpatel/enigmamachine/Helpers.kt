package com.smitpatel.enigmamachine

internal fun Char.letterToNumber() = this.code - 65

internal fun Int.numberToLetter() = Char(this + 65)
