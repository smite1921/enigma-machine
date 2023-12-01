package com.smitpatel.enigmamachine

import android.content.Context
import com.smitpatel.enigmamachine.models.EnigmaHistoryItem
import com.smitpatel.enigmamachine.models.Reflector
import com.smitpatel.enigmamachine.models.Rotor
import org.json.JSONArray
import org.json.JSONObject
import kotlin.Exception

class DeserializeException : Exception()

object Serializer {

    private const val rotorOptionsKey: String = "rotorOptions"
    private const val rotorPositionsKey: String = "rotorPositions"
    private const val ringPositionsKey: String = "ringPositions"
    private const val reflectorKey: String = "reflector"
    private const val plugboardPairsKey: String = "plugboardPairs"


    fun serializeJson(context: Context, settings: EnigmaHistoryItem): String {
        val json = JSONObject()

        json.put(rotorOptionsKey, JSONArray(arrayOf(
            settings.rotorOneOption.toRotorLabelText(context),
            settings.rotorTwoOption.toRotorLabelText(context),
            settings.rotorThreeOption.toRotorLabelText(context),
        )))

        json.put(rotorPositionsKey, JSONArray(arrayOf(
            settings.rotorOnePosition.numberToLetter(),
            settings.rotorTwoPosition.numberToLetter(),
            settings.rotorThreePosition.numberToLetter(),
        )))

        json.put(ringPositionsKey, JSONArray(arrayOf(
            settings.ringOneOption.numberToLetter(),
            settings.ringTwoOption.numberToLetter(),
            settings.ringThreeOption.numberToLetter(),
        )))

        json.put(reflectorKey, settings.reflectorOption.toReflectorLabelText(context))

        json.put(plugboardPairsKey, JSONArray(settings.plugboardPairs.map {
            JSONArray(arrayOf(it.first.numberToLetter(), it.second.numberToLetter()))
        }))

        return json.toString()
    }

    @Throws(DeserializeException::class)
    fun deserializeJson(context: Context, settings: String): EnigmaHistoryItem {
        try {
            val json = JSONObject(settings)

            val jsonRotorOptions = json.getJSONArray(rotorOptionsKey)
            val rotorOptions = (0 until 3).map {
                jsonRotorOptions.getString(it).toRotorOption(context)
            }

            val jsonRotorPositions = json.getJSONArray(rotorPositionsKey)
            val rotorPositions = (0 until 3).map {
                jsonRotorPositions.getString(it).toNumber()
            }

            val jsonRingPosition = json.getJSONArray(ringPositionsKey)
            val ringPositions = (0 until 3).map {
                jsonRingPosition.getString(it).toNumber()
            }

            val reflector = json.getString(reflectorKey).toReflector(context)

            val jsonPlugboardPairs = json.getJSONArray(plugboardPairsKey)
            val plugboardPairs = mutableSetOf<Pair<Int, Int>>()
            for (i in 0 until jsonPlugboardPairs.length()) {
                val jsonPair = jsonPlugboardPairs.getJSONArray(i)
                plugboardPairs.add(
                    Pair(
                        first = jsonPair.getString(0).toNumber(),
                        second = jsonPair.getString(1).toNumber(),
                    )
                )
            }

            return EnigmaHistoryItem(
                rotorOneOption = rotorOptions[0],
                rotorTwoOption = rotorOptions[1],
                rotorThreeOption = rotorOptions[2],
                rotorOnePosition = rotorPositions[0],
                rotorTwoPosition = rotorPositions[1],
                rotorThreePosition = rotorPositions[2],
                ringOneOption = ringPositions[0],
                ringTwoOption = ringPositions[1],
                ringThreeOption = ringPositions[2],
                reflectorOption = reflector,
                plugboardPairs = plugboardPairs,
            )
        } catch (exception: Exception) {
            throw DeserializeException()
        }
    }

    @Throws(IllegalArgumentException::class, NoSuchElementException::class)
    internal fun String.toNumber(): Int {
        val char = this.trim().single().uppercaseChar()
        if (!char.isLetter()) {
            throw IllegalArgumentException("Input must be a letter: $this")
        }
        return char.letterToNumber()
    }

    @Throws(IllegalArgumentException::class)
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

    @Throws(IllegalArgumentException::class)
    internal fun String.toReflector(context: Context) = when (this.uppercase().trim()) {
        context.getString(R.string.reflector_a) -> Reflector.REFLECTOR_UKW_A
        context.getString(R.string.reflector_b) -> Reflector.REFLECTOR_UKW_B
        context.getString(R.string.reflector_c) -> Reflector.REFLECTOR_UKW_C
        else -> throw IllegalArgumentException("Invalid reflector string: $this")
    }
}

