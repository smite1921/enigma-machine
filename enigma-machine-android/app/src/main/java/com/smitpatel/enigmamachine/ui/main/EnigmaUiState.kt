package com.smitpatel.enigmamachine.ui.main

import com.smitpatel.enigmamachine.models.Reflector
import com.smitpatel.enigmamachine.models.Rotor
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.Serializer
import kotlinx.serialization.builtins.IntArraySerializer
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder

data class EnigmaUiState(
    val rotorOnePosition: Int,
    val rotorTwoPosition: Int,
    val rotorThreePosition: Int,
    val rotorOneLabel: Rotor.RotorOption,
    val rotorTwoLabel: Rotor.RotorOption,
    val rotorThreeLabel: Rotor.RotorOption,
    val activeLampboard: Int,
    val rawMessage: String,
    val encodedMessage: String,
    val clipboardCopyState: ClipboardCopyState?,
    val showSettingsChangedToast: Boolean,
    val showSettingsErrorToast: Boolean,
    val pasteError: String?,
)

data class ClipboardCopyState(
    val text: String,
    val settingsState: SettingsCopyState?,
    val json: Boolean,
) {

    data class SettingsCopyState(
        val rotorOneLabel: Rotor.RotorOption,
        val rotorTwoLabel: Rotor.RotorOption,
        val rotorThreeLabel: Rotor.RotorOption,
        val rotorOnePosition: Int,
        val rotorTwoPosition: Int,
        val rotorThreePosition: Int,
        val rotorOneRing: Int,
        val rotorTwoRing: Int,
        val rotorThreeRing: Int,
        val reflector: Reflector,
        val plugboardPairs: Set<Pair<Int, Int>>,
    )
}