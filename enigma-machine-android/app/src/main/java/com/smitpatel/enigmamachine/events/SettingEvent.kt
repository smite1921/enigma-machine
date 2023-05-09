package com.smitpatel.enigmamachine.events

import com.smitpatel.enigmamachine.models.Reflector
import com.smitpatel.enigmamachine.models.Rotor
import com.smitpatel.enigmamachine.ui.RotorPosition

/**
 * Represents actions from settings fragment ui to view model
 */
sealed class SettingEvent {
    data class ReflectorOptionSelected(val reflector: Reflector) : SettingEvent()
    data class RotorOptionSelected(val rotorPosition: RotorPosition, val rotor: Rotor.RotorOption, val position: Int, val ring: Int): SettingEvent()
    data class PositionOptionSelected(val rotorPosition: RotorPosition, val position: Int) : SettingEvent()
    data class RingOptionSelected(val rotorPosition: RotorPosition, val ring: Int) : SettingEvent()
    data class MuteSwitchToggled(val muteStatus : Boolean) : SettingEvent()
    data class PlugboardPairAdded(val pair: Pair<Int, Int>) : SettingEvent()
    data class PlugboardPairRemoved(val pair: Pair<Int, Int>) : SettingEvent()
    object SaveState : SettingEvent()
    object RestoreState : SettingEvent()
}
