package com.smitpatel.enigmamachine.ui.setting

import com.smitpatel.enigmamachine.models.Reflector
import com.smitpatel.enigmamachine.models.Rotor

data class SettingUiState(
    val muteOption : Boolean,
    val reflectorOption : Reflector,
    val rotorOneOption: Rotor.RotorOption,
    val rotorTwoOption: Rotor.RotorOption,
    val rotorThreeOption: Rotor.RotorOption,
    val rotorOnePosition: Int,
    val rotorTwoPosition: Int,
    val rotorThreePosition: Int,
    val ringOneOption: Int,
    val ringTwoOption: Int,
    val ringThreeOption: Int,
    val plugboardPairs: Set<Pair<Int, Int>>
) {

    override fun equals(other: Any?): Boolean {
        // Note: we don't include the mute option as part of comparison
        return when (other) {
            null, !is SettingUiState -> false
            else -> this.reflectorOption == other.reflectorOption
                    && this.rotorOneOption == other.rotorOneOption
                    && this.rotorTwoOption == other.rotorTwoOption
                    && this.rotorThreeOption == other.rotorThreeOption
                    && this.rotorOnePosition == other.rotorOnePosition
                    && this.rotorTwoPosition == other.rotorTwoPosition
                    && this.rotorThreePosition == other.rotorThreePosition
                    && this.ringOneOption == other.ringOneOption
                    && this.ringTwoOption == other.ringTwoOption
                    && this.ringThreeOption == other.ringThreeOption
                    && this.plugboardPairs == other.plugboardPairs
        }
    }
}
