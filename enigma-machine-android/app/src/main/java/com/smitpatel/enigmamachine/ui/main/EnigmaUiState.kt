package com.smitpatel.enigmamachine.ui.main

import com.smitpatel.enigmamachine.models.EnigmaHistoryItem
import com.smitpatel.enigmamachine.models.Rotor

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
    val settingsState: EnigmaHistoryItem?,
)
