package com.smitpatel.enigmamachine.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.smitpatel.enigmamachine.events.EnigmaEvent
import com.smitpatel.enigmamachine.models.EnigmaModel
import com.smitpatel.enigmamachine.models.Rotor
import com.smitpatel.enigmamachine.ui.RotorPosition
import com.smitpatel.enigmamachine.ui.main.EnigmaUiState

class EnigmaViewModel : ViewModel() {

    private val enigma: EnigmaModel = EnigmaModel

    private val enigmaUiState: MutableLiveData<EnigmaUiState> = MutableLiveData(
        EnigmaUiState(
            rotorOnePosition = enigma.rotorOne.position,
            rotorTwoPosition = enigma.rotorTwo.position,
            rotorThreePosition = enigma.rotorThree.position,
            rotorOneLabel = enigma.rotorOne.rotorOption,
            rotorTwoLabel = enigma.rotorTwo.rotorOption,
            rotorThreeLabel = enigma.rotorThree.rotorOption,
            rawMessage = "",
            encodedMessage = "",
            activeLampboard = -1,
            showSettingsChangedToast = false,
        )
    )

    val uiState : LiveData<EnigmaUiState> = enigmaUiState

    private val space: Char = '_'

    /**
     * Handles events sent from screen
     */
    fun handleEvent(event: EnigmaEvent) {
        when (event) {
            is EnigmaEvent.InputKeyPressed -> {
                val encodedLetter = enigma.input(letter = event.input)
                enigmaUiState.value = enigmaUiState.value?.copy(
                    rotorOnePosition = enigma.rotorOne.position,
                    rotorTwoPosition = enigma.rotorTwo.position,
                    rotorThreePosition = enigma.rotorThree.position,
                    rawMessage = enigmaUiState.value?.rawMessage + numberToLetter(event.input),
                    encodedMessage =  enigmaUiState.value?.encodedMessage + numberToLetter(encodedLetter),
                    activeLampboard = encodedLetter,
                )
            }
            is EnigmaEvent.InputKeyLifted -> enigmaUiState.value = enigmaUiState.value?.copy(
                activeLampboard = -1
            )
            is EnigmaEvent.InputSpacePressed -> enigmaUiState.value = enigmaUiState.value?.copy(
                rawMessage = enigmaUiState.value?.rawMessage + space,
                encodedMessage = enigmaUiState.value?.encodedMessage + space,
            )
            is EnigmaEvent.InputDeletePressed -> {
                when(enigmaUiState.value?.rawMessage?.lastOrNull()) {
                    null -> {}
                    space -> enigmaUiState.value = enigmaUiState.value?.let {
                        it.copy(
                            rawMessage = it.rawMessage.dropLast(1),
                            encodedMessage = it.encodedMessage.dropLast(1)
                        )
                    }
                    else -> {
                        val lastSettings = enigma.historyStack.pop()
                        enigma.rotorOne = Rotor.makeRotor(
                            rotorOption = lastSettings.rotorOneOption,
                            position = lastSettings.rotorOnePosition,
                            ring = lastSettings.ringOneOption
                        )
                        enigma.rotorTwo = Rotor.makeRotor(
                            rotorOption = lastSettings.rotorTwoOption,
                            position = lastSettings.rotorTwoPosition,
                            ring = lastSettings.ringTwoOption
                        )
                        enigma.rotorThree = Rotor.makeRotor(
                            rotorOption = lastSettings.rotorThreeOption,
                            position = lastSettings.rotorThreePosition,
                            ring = lastSettings.ringThreeOption
                        )
                        enigma.reflector = lastSettings.reflectorOption
                        lastSettings.plugboardPairs.forEach {
                            enigma.plugboard.addPair(
                                letterOne = it.first,
                                letterTwo = it.second,
                            )
                        }
                        enigmaUiState.value = enigmaUiState.value?.let {
                            EnigmaUiState(
                                rotorOnePosition = enigma.rotorOne.position,
                                rotorTwoPosition = enigma.rotorTwo.position,
                                rotorThreePosition = enigma.rotorThree.position,
                                rotorOneLabel = enigma.rotorOne.rotorOption,
                                rotorTwoLabel = enigma.rotorTwo.rotorOption,
                                rotorThreeLabel = enigma.rotorThree.rotorOption,
                                rawMessage = it.rawMessage.dropLast(1),
                                encodedMessage = it.encodedMessage.dropLast(1),
                                activeLampboard = -1,
                                showSettingsChangedToast = false,
                            )
                        }
                    }
                }
            }
            is EnigmaEvent.RotorStartPositionChanged -> {
                when (event.rotorPosition) {
                    RotorPosition.ONE -> {
                        enigma.rotorOne.position = event.start
                        enigmaUiState.value = enigmaUiState.value?.copy(
                            rotorOnePosition = enigma.rotorOne.position
                        )
                    }
                    RotorPosition.TWO -> {
                        enigma.rotorTwo.position = event.start
                        enigmaUiState.value = enigmaUiState.value?.copy(
                            rotorTwoPosition = enigma.rotorTwo.position
                        )
                    }
                    RotorPosition.THREE -> {
                        enigma.rotorThree.position = event.start
                        enigmaUiState.value = enigmaUiState.value?.copy(
                            rotorThreePosition = enigma.rotorThree.position
                        )
                    }
                }
            }
            is EnigmaEvent.SettingMenuClosed -> {
                if (event.didSettingsChanged) {
                    enigma.historyStack.clear()
                    enigmaUiState.value =
                        EnigmaUiState(
                            rotorOnePosition = enigma.rotorOne.position,
                            rotorTwoPosition = enigma.rotorTwo.position,
                            rotorThreePosition = enigma.rotorThree.position,
                            rotorOneLabel = enigma.rotorOne.rotorOption,
                            rotorTwoLabel = enigma.rotorTwo.rotorOption,
                            rotorThreeLabel = enigma.rotorThree.rotorOption,
                            rawMessage = "",
                            encodedMessage = "",
                            activeLampboard = -1,
                            showSettingsChangedToast = true,
                        )
                }
            }
            is EnigmaEvent.ToastMessageDisplayed -> enigmaUiState.value = enigmaUiState.value?.copy(
                showSettingsChangedToast = false,
            )
        }
    }

    private fun numberToLetter(number: Int) = Char(number + 65)
}