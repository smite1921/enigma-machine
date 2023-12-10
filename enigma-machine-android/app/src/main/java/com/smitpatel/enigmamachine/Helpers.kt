package com.smitpatel.enigmamachine

import android.content.Context
import com.smitpatel.enigmamachine.models.Reflector
import com.smitpatel.enigmamachine.models.Rotor
import com.smitpatel.enigmamachine.ui.main.ClipboardCopyState
import org.json.JSONArray
import org.json.JSONObject

internal fun Char.letterToNumber() = this.code - 65

internal fun Int.numberToLetter() = Char(this + 65)

internal fun Rotor.RotorOption.getRotorLabelText(context: Context): String {
    val rotorOptions = context.resources.getStringArray(R.array.rotor_options)
    return when (this) {
        Rotor.RotorOption.ROTOR_ONE -> rotorOptions[0]
        Rotor.RotorOption.ROTOR_TWO -> rotorOptions[1]
        Rotor.RotorOption.ROTOR_THREE -> rotorOptions[2]
        Rotor.RotorOption.ROTOR_FOUR -> rotorOptions[3]
        Rotor.RotorOption.ROTOR_FIVE -> rotorOptions[4]
    }
}

internal fun String.toRotorOption(context: Context): Rotor.RotorOption {
    val rotorOptions = context.resources.getStringArray(R.array.rotor_options)
    return when (this.uppercase().trim()) {
        rotorOptions[0] -> Rotor.RotorOption.ROTOR_ONE
        rotorOptions[1]  -> Rotor.RotorOption.ROTOR_TWO
        rotorOptions[2]  -> Rotor.RotorOption.ROTOR_THREE
        rotorOptions[3]  -> Rotor.RotorOption.ROTOR_FOUR
        rotorOptions[4]  -> Rotor.RotorOption.ROTOR_FIVE
        else -> throw IllegalArgumentException("Invalid Roman numeral for RotorOption: $this")
    }
}

internal fun String.toNumber(): Int {
    val char = this.single().uppercaseChar()
    if (!char.isLetter()) {
        throw IllegalArgumentException("Input must be a letter: $this")
    }
    return char.letterToNumber()
}

internal fun Reflector.toStringRepresentation(context: Context) = when(this) {
    Reflector.REFLECTOR_UKW_A -> context.getString(R.string.reflector_a)
    Reflector.REFLECTOR_UKW_B -> context.getString(R.string.reflector_b)
    Reflector.REFLECTOR_UKW_C -> context.getString(R.string.reflector_c)
}

internal fun String.toReflector(context: Context) = when (this.uppercase().trim()) {
    context.getString(R.string.reflector_a) -> Reflector.REFLECTOR_UKW_A
    context.getString(R.string.reflector_b) -> Reflector.REFLECTOR_UKW_B
    context.getString(R.string.reflector_c) -> Reflector.REFLECTOR_UKW_C
    else -> throw IllegalArgumentException("Invalid reflector string: $this")
}

internal fun ClipboardCopyState.SettingsCopyState.toSettingsJson(context: Context): String {
    val json = JSONObject()

    json.put("rotorOptions", JSONArray(arrayOf(
        this.rotorOneLabel.getRotorLabelText(context),
        this.rotorTwoLabel.getRotorLabelText(context),
        this.rotorThreeLabel.getRotorLabelText(context),
    )))

    json.put("rotorPositions", JSONArray(arrayOf(
        this.rotorOnePosition.numberToLetter(),
        this.rotorTwoPosition.numberToLetter(),
        this.rotorThreePosition.numberToLetter(),
    )))

    json.put("ringPositions", JSONArray(arrayOf(
        this.rotorOnePosition.numberToLetter(),
        this.rotorTwoPosition.numberToLetter(),
        this.rotorThreePosition.numberToLetter(),
    )))

    json.put("reflector", this.reflector.toStringRepresentation(context))
    json.put("plugboardPairs", JSONArray(this.plugboardPairs.map { JSONArray(arrayOf(it.first, it.second)) }))

    return json.toString()
}

internal fun ClipboardCopyState.SettingsCopyState.toSettingsString(context: Context): String {

    val rotorOptions = "${this.rotorOneLabel.getRotorLabelText(context)} " +
            "${this.rotorTwoLabel.getRotorLabelText(context)} " +
            this.rotorThreeLabel.getRotorLabelText(context)

    val rotorPositions = "${this.rotorOnePosition.numberToLetter()} " +
            "${this.rotorTwoPosition.numberToLetter()} " +
            this.rotorThreePosition.numberToLetter()

    val ringPositions = "${this.rotorOneRing.numberToLetter()} " +
            "${this.rotorTwoRing.numberToLetter()} " +
            this.rotorThreeRing.numberToLetter()

    val plugboardPairs = this.plugboardPairs.map {
        Pair(
            first = it.first.numberToLetter(),
            second = it.second.numberToLetter()
        )
    }

    return context.getString(R.string.copy_settings_text,
        rotorOptions,
        rotorPositions,
        ringPositions,
        this.reflector.toStringRepresentation(context),
        plugboardPairs,
    )
}