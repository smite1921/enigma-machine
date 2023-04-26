package com.smitpatel.enigmamachine.viewmodels

import androidx.lifecycle.ViewModel
import com.smitpatel.enigmamachine.events.SettingEvent
import com.smitpatel.enigmamachine.models.EnigmaModel
import com.smitpatel.enigmamachine.models.Rotor
import com.smitpatel.enigmamachine.ui.RotorPosition
import com.smitpatel.enigmamachine.ui.SoundEffects
import com.smitpatel.enigmamachine.ui.setting.SettingUiState

class SettingViewModel : ViewModel() {

    private val enigma: EnigmaModel = EnigmaModel

    val initialUiState : SettingUiState = SettingUiState(
        muteOption = SoundEffects.isMuteOn,
        reflectorOption = enigma.reflector,
        rotorOneOption = enigma.rotorOne.rotorOption,
        rotorTwoOption = enigma.rotorTwo.rotorOption,
        rotorThreeOption = enigma.rotorThree.rotorOption,
        rotorOnePosition = enigma.rotorOne.position,
        rotorTwoPosition = enigma.rotorTwo.position,
        rotorThreePosition = enigma.rotorThree.position,
        ringOneOption = enigma.rotorOne.ring,
        ringTwoOption = enigma.rotorTwo.ring,
        ringThreeOption = enigma.rotorThree.ring,
        plugboardPairs = enigma.plugboard.getAllPairs(),
    )

    val didSettingsChange : Boolean
        get() = initialUiState != SettingUiState(
            muteOption = SoundEffects.isMuteOn,
            reflectorOption = enigma.reflector,
            rotorOneOption = enigma.rotorOne.rotorOption,
            rotorTwoOption = enigma.rotorTwo.rotorOption,
            rotorThreeOption = enigma.rotorThree.rotorOption,
            rotorOnePosition = enigma.rotorOne.position,
            rotorTwoPosition = enigma.rotorTwo.position,
            rotorThreePosition = enigma.rotorThree.position,
            ringOneOption = enigma.rotorOne.ring,
            ringTwoOption = enigma.rotorTwo.ring,
            ringThreeOption = enigma.rotorThree.ring,
            plugboardPairs = enigma.plugboard.getAllPairs(),
        )

    fun handleEvent(event: SettingEvent) {
        when (event) {
            is SettingEvent.ReflectorOptionSelected -> enigma.reflector = event.reflector
            is SettingEvent.RotorOptionSelected -> when (event.rotorPosition) {
                RotorPosition.ONE -> enigma.rotorOne = Rotor.makeRotor(
                    rotorOption = event.rotor,
                    ring = event.ring,
                    position = event.position,
                )
                RotorPosition.TWO -> enigma.rotorTwo = Rotor.makeRotor(
                    rotorOption = event.rotor,
                    ring = event.ring,
                    position = event.position,
                )
                RotorPosition.THREE -> enigma.rotorThree = Rotor.makeRotor(
                    rotorOption = event.rotor,
                    ring = event.ring,
                    position = event.position,
                )
            }
            is SettingEvent.PositionOptionSelected -> when (event.rotorPosition) {
                    RotorPosition.ONE -> enigma.rotorOne.position = event.position
                    RotorPosition.TWO -> enigma.rotorTwo.position = event.position
                    RotorPosition.THREE -> enigma.rotorThree.position = event.position
                }
            is SettingEvent.RingOptionSelected -> when (event.rotorPosition) {
                RotorPosition.ONE -> enigma.rotorOne.ring = event.ring
                RotorPosition.TWO -> enigma.rotorTwo.ring = event.ring
                RotorPosition.THREE -> enigma.rotorThree.ring = event.ring
            }
            is SettingEvent.PlugboardPairAdded -> enigma.plugboard.addPair(
                letterOne = event.pair.first,
                letterTwo = event.pair.second,
            )
            is SettingEvent.PlugboardPairRemoved -> enigma.plugboard.removePair(
                letter = event.pair.first,
            )
            is SettingEvent.MuteSwitchToggled -> SoundEffects.isMuteOn = event.muteStatus
        }
    }
}