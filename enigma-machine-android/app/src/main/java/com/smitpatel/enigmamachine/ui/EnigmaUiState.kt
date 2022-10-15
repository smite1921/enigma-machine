package com.smitpatel.enigmamachine.ui

data class EnigmaUiState(
    val rotorOnePosition: Int,
    val rotorTwoPosition: Int,
    val rotorThreePosition: Int,
    val activeLampboard: Int,
    val rawMessage: String,
    val encodedMessage: String,
)
