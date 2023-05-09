package com.smitpatel.enigmamachine.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.smitpatel.enigmamachine.events.EnigmaEvent
import com.smitpatel.enigmamachine.models.EnigmaHistoryItem
import com.smitpatel.enigmamachine.models.EnigmaModel
import com.smitpatel.enigmamachine.models.Rotor
import com.smitpatel.enigmamachine.ui.RotorPosition
import com.smitpatel.enigmamachine.ui.main.ClipboardCopyState
import com.smitpatel.enigmamachine.ui.main.EnigmaUiState
import java.util.Stack

class EnigmaViewModel(private val savedState: SavedStateHandle) : ViewModel() {

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
            clipboardCopyState = null,
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
                    rawMessage = enigmaUiState.value?.rawMessage + event.input.numberToLetter(),
                    encodedMessage =  enigmaUiState.value?.encodedMessage + encodedLetter.numberToLetter(),
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
                        // This is to ensure the app doesn't crash if the stack is ever empty
                        // This should not be called if the stack is maintained correctly
                        if (enigma.historyStack.empty()) return

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
                                clipboardCopyState = null,
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
                            clipboardCopyState = null,
                            showSettingsChangedToast = true,
                        )
                }
            }
            is EnigmaEvent.CopyRawText -> enigmaUiState.value?.rawMessage?.let {
                enigmaUiState.value = enigmaUiState.value?.copy(
                    clipboardCopyState = ClipboardCopyState(
                        text = it.formatCopy(),
                        settingsState = null,
                    )
                )
            }
            is EnigmaEvent.CopyEncodedText -> enigmaUiState.value?.encodedMessage?.let {
                enigmaUiState.value = enigmaUiState.value?.copy(
                    clipboardCopyState = ClipboardCopyState(
                        text = it.formatCopy(),
                        settingsState = null,
                    )
                )
            }
            is EnigmaEvent.CopySettings -> enigmaUiState.value?.encodedMessage?.let {
                enigmaUiState.value = enigmaUiState.value?.copy(
                    clipboardCopyState = ClipboardCopyState(
                        text = "",
                        settingsState = ClipboardCopyState.SettingsCopyState(
                            rotorOneLabel = enigma.rotorOne.rotorOption,
                            rotorTwoLabel = enigma.rotorTwo.rotorOption,
                            rotorThreeLabel = enigma.rotorThree.rotorOption,
                            rotorOnePosition = enigma.rotorOne.position,
                            rotorTwoPosition = enigma.rotorTwo.position,
                            rotorThreePosition = enigma.rotorThree.position,
                            rotorOneRing = enigma.rotorOne.ring,
                            rotorTwoRing = enigma.rotorTwo.ring,
                            rotorThreeRing = enigma.rotorThree.ring,
                            reflector = enigma.reflector,
                            plugboardPairs = enigma.plugboard.getAllPairs()
                        ),
                    )
                )
            }
            is EnigmaEvent.ToastMessageDisplayed -> enigmaUiState.value = enigmaUiState.value?.copy(
                showSettingsChangedToast = false,
                clipboardCopyState = null,
            )
            is EnigmaEvent.SaveState -> {
                savedState[ENIGMA_SAVED_STATE_SETTINGS] = enigma.getCurrentSettings()
                savedState[ENIGMA_SAVED_STATE_HISTORY] = enigma.historyStack.toArray()
                enigmaUiState.value?.let {
                    savedState[ENIGMA_SAVED_STATE_RAW_MESSAGE] = it.rawMessage
                    savedState[ENIGMA_SAVED_STATE_ENCODED_MESSAGE] = it.encodedMessage
                }
            }
            is EnigmaEvent.RestoreState -> {
                val settings: EnigmaHistoryItem? = savedState[ENIGMA_SAVED_STATE_SETTINGS]
                val history: Array<Any>? = savedState[ENIGMA_SAVED_STATE_HISTORY]
                val rawMessage: String? = savedState[ENIGMA_SAVED_STATE_RAW_MESSAGE]
                val encodedMessage: String? = savedState[ENIGMA_SAVED_STATE_ENCODED_MESSAGE]
                if (settings != null && history != null && rawMessage != null && encodedMessage != null) {
                    enigma.historyStack.clear()
                    // End of the history array is the top of the stack
                    history.forEach { enigma.historyStack.push(it as EnigmaHistoryItem) }
                    enigmaUiState.value = enigmaUiState.value?.copy(
                        rotorOnePosition = settings.rotorOnePosition,
                        rotorTwoPosition = settings.rotorTwoPosition,
                        rotorThreePosition = settings.rotorThreePosition,
                        rotorOneLabel = settings.rotorOneOption,
                        rotorTwoLabel = settings.rotorTwoOption,
                        rotorThreeLabel = settings.rotorThreeOption,
                        rawMessage = rawMessage,
                        encodedMessage = encodedMessage
                    )
                }
                savedState.remove<EnigmaHistoryItem>(ENIGMA_SAVED_STATE_SETTINGS)
                savedState.remove<Stack<EnigmaHistoryItem>>(ENIGMA_SAVED_STATE_HISTORY)
                savedState.remove<String>(ENIGMA_SAVED_STATE_RAW_MESSAGE)
                savedState.remove<String>(ENIGMA_SAVED_STATE_ENCODED_MESSAGE)
            }
        }
    }

    private companion object {
        const val ENIGMA_SAVED_STATE_SETTINGS = "enigma_saved_state_settings"
        const val ENIGMA_SAVED_STATE_HISTORY = "enigma_saved_state_history"
        const val ENIGMA_SAVED_STATE_RAW_MESSAGE = "enigma_saved_state_raw_message"
        const val ENIGMA_SAVED_STATE_ENCODED_MESSAGE = "enigma_saved_state_encoded_message"
    }

    private fun Int.numberToLetter() = Char(this + 65)

    private fun String.formatCopy() = this.replace(oldChar = space, newChar = ' ')

}