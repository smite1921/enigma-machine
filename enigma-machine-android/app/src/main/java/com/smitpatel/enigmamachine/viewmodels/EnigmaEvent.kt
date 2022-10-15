package com.smitpatel.enigmamachine.viewmodels

/**
 * Represents actions from ui to view model
 */
sealed class EnigmaEvent {
    data class InputKeyPressed(val input: Int) : EnigmaEvent()
    data class InputKeyLifted(val input: Int): EnigmaEvent()
    object InputSpacePressed : EnigmaEvent()
    object InputDeletePressed : EnigmaEvent()
}
