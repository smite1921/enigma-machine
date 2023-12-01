package com.smitpatel.enigmamachine

import android.content.Context
import com.smitpatel.enigmamachine.models.Reflector
import com.smitpatel.enigmamachine.models.Rotor

internal fun Char.letterToNumber() = this.code - 65

internal fun Int.numberToLetter() = Char(this + 65)

internal fun Rotor.RotorOption.toRotorLabelText(context: Context): String {
    val rotorOptions = context.resources.getStringArray(R.array.rotor_options)
    return when (this) {
        Rotor.RotorOption.ROTOR_ONE -> rotorOptions[0]
        Rotor.RotorOption.ROTOR_TWO -> rotorOptions[1]
        Rotor.RotorOption.ROTOR_THREE -> rotorOptions[2]
        Rotor.RotorOption.ROTOR_FOUR -> rotorOptions[3]
        Rotor.RotorOption.ROTOR_FIVE -> rotorOptions[4]
    }
}

internal fun Reflector.toReflectorLabelText(context: Context) = when(this) {
    Reflector.REFLECTOR_UKW_A -> context.getString(R.string.reflector_a)
    Reflector.REFLECTOR_UKW_B -> context.getString(R.string.reflector_b)
    Reflector.REFLECTOR_UKW_C -> context.getString(R.string.reflector_c)
}