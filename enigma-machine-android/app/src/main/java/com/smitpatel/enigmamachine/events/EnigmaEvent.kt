package com.smitpatel.enigmamachine.events

import com.smitpatel.enigmamachine.ui.RotorPosition


/**
 * Represents actions from enigma activity ui to view model
 */
sealed class EnigmaEvent {
    data class InputKeyPressed(val input: Int) : EnigmaEvent()
    data class InputKeyLifted(val input: Int): EnigmaEvent()
    data class RotorStartPositionChanged(val rotorPosition: RotorPosition, val start: Int): EnigmaEvent()
    data class SettingMenuClosed(val didSettingsChanged : Boolean) : EnigmaEvent()
    object InputSpacePressed : EnigmaEvent()
    object InputDeletePressed : EnigmaEvent()
    object ToastMessageDisplayed : EnigmaEvent()
}
