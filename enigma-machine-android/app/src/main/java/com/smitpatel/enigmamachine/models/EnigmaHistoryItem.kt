package com.smitpatel.enigmamachine.models

data class EnigmaHistoryItem(
    val rotorOneOption: Rotor.RotorOption,
    val rotorTwoOption: Rotor.RotorOption,
    val rotorThreeOption: Rotor.RotorOption,
    val rotorOnePosition: Int,
    val rotorTwoPosition: Int,
    val rotorThreePosition: Int,
    val ringOneOption: Int,
    val ringTwoOption: Int,
    val ringThreeOption: Int,
    val reflectorOption : Reflector,
    val plugboardPairs: Set<Pair<Int, Int>>,
) : java.io.Serializable
