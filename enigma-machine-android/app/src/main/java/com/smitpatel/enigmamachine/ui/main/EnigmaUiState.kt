package com.smitpatel.enigmamachine.ui.main

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
    val showSettingsChangedToast: Boolean,
)
