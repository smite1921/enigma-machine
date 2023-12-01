package com.smitpatel.enigmamachine.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.smitpatel.enigmamachine.events.EnigmaEvent
import com.smitpatel.enigmamachine.letterToNumber
import com.smitpatel.enigmamachine.models.EnigmaHistoryItem
import com.smitpatel.enigmamachine.models.EnigmaModel
import com.smitpatel.enigmamachine.numberToLetter
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
            showSettingsErrorToast = false,
            pasteError = null,
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

                        enigma.applySettings(settings = enigma.historyStack.pop())
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
                                showSettingsErrorToast = false,
                                pasteError = null,
                            )
                        }
                    }
                }
            }
            is EnigmaEvent.InputLongDeletePressed -> {
                var firstItem: EnigmaHistoryItem? = null

                while (enigma.historyStack.isNotEmpty()) { firstItem = enigma.historyStack.pop() }

                if (firstItem != null) {
                    enigma.applySettings(settings = firstItem)
                    enigmaUiState.value = enigmaUiState.value?.let {
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
                            showSettingsErrorToast = false,
                            pasteError = null,
                        )
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
                            showSettingsErrorToast = false,
                            pasteError = null,
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
                        settingsState = EnigmaHistoryItem(
                            rotorOneOption = enigma.rotorOne.rotorOption,
                            rotorTwoOption = enigma.rotorTwo.rotorOption,
                            rotorThreeOption = enigma.rotorThree.rotorOption,
                            rotorOnePosition = enigma.rotorOne.position,
                            rotorTwoPosition = enigma.rotorTwo.position,
                            rotorThreePosition = enigma.rotorThree.position,
                            ringOneOption = enigma.rotorOne.ring,
                            ringTwoOption = enigma.rotorTwo.ring,
                            ringThreeOption = enigma.rotorThree.ring,
                            reflectorOption = enigma.reflector,
                            plugboardPairs = enigma.plugboard.getAllPairs()
                        ),
                    )
                )
            }
            is EnigmaEvent.ClosePasteError -> enigmaUiState.value = enigmaUiState.value?.copy(
                pasteError = null
            )
            is EnigmaEvent.PasteRawText -> {
                fun containsOnlyLettersSpacesAndUnderscores(input: String) =
                    Regex("[a-zA-Z_\\s]*").matches(input)

                when (containsOnlyLettersSpacesAndUnderscores(event.rawText)) {
                    true -> {
                        val rawText = event.rawText.uppercase().replace(
                            regex = Regex("\\s"),
                            replacement = "_"
                        )
                        var encodedText = ""
                        rawText.forEach {
                            encodedText += when (it) {
                                '_' -> "_"
                                else -> enigma.input(letter = it.letterToNumber()).numberToLetter()
                            }
                        }
                        enigmaUiState.value = enigmaUiState.value?.copy(
                            rotorOnePosition = enigma.rotorOne.position,
                            rotorTwoPosition = enigma.rotorTwo.position,
                            rotorThreePosition = enigma.rotorThree.position,
                            rawMessage = enigmaUiState.value?.rawMessage + rawText,
                            encodedMessage =  enigmaUiState.value?.encodedMessage + encodedText,
                        )
                    }
                    false -> {
                        enigmaUiState.value = enigmaUiState.value?.copy(
                            pasteError = event.rawText,
                        )
                    }
                }
            }
            is EnigmaEvent.PasteEnigmaSettings -> {
                when (val enigmaSettings = event.enigmaSettings) {
                    null -> enigmaUiState.value = enigmaUiState.value?.copy(showSettingsErrorToast = true)
                    else -> {
                        enigma.applySettings(
                            settings = enigmaSettings,
                            historyStack = emptyList()
                        )

                        enigmaUiState.value = enigmaUiState.value?.copy(
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
                            pasteError = null,
                        )

                    }
                }
            }
            is EnigmaEvent.ToastMessageDisplayed -> enigmaUiState.value = enigmaUiState.value?.copy(
                showSettingsChangedToast = false,
                showSettingsErrorToast = false,
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
                    enigma.applySettings(
                        settings = settings,
                        historyStack = history.map { it as EnigmaHistoryItem }
                    )
                    enigmaUiState.value = enigmaUiState.value?.copy(
                        rotorOnePosition = enigma.rotorOne.position,
                        rotorTwoPosition = enigma.rotorTwo.position,
                        rotorThreePosition = enigma.rotorThree.position,
                        rotorOneLabel = enigma.rotorOne.rotorOption,
                        rotorTwoLabel = enigma.rotorTwo.rotorOption,
                        rotorThreeLabel = enigma.rotorThree.rotorOption,
                        rawMessage = rawMessage,
                        encodedMessage = encodedMessage,
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

    private fun String.formatCopy() = this.replace(oldChar = space, newChar = ' ')

}