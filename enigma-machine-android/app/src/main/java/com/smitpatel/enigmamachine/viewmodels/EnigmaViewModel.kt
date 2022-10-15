package com.smitpatel.enigmamachine.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.smitpatel.enigmamachine.models.EnigmaModel
import com.smitpatel.enigmamachine.ui.EnigmaUiState

class EnigmaViewModel : ViewModel() {

    private val enigma: EnigmaModel = EnigmaModel

    private val enigmaUiState: MutableLiveData<EnigmaUiState> = MutableLiveData(
        EnigmaUiState(
            rotorOnePosition = enigma.rotorOne.position,
            rotorTwoPosition = enigma.rotorTwo.position,
            rotorThreePosition = enigma.rotorThree.position,
            rawMessage = "",
            encodedMessage = "",
            activeLampboard = -1,
        )
    )

    val uiState : LiveData<EnigmaUiState> = enigmaUiState

    /**
     * Handles events sent from screen
     */
    fun handleEvent(event: EnigmaEvent) {
        when (event) {
            is EnigmaEvent.InputKeyPressed -> {
                val encodedLetter = enigma.input(letter = event.input)
                enigmaUiState.value = enigmaUiState.value?.let {
                    EnigmaUiState(
                        rotorOnePosition = enigma.rotorOne.position,
                        rotorTwoPosition = enigma.rotorTwo.position,
                        rotorThreePosition = enigma.rotorThree.position,
                        rawMessage = it.rawMessage + numberToLetter(event.input),
                        encodedMessage =  it.encodedMessage + numberToLetter(encodedLetter),
                        activeLampboard = encodedLetter
                    )
                }
            }
            is EnigmaEvent.InputKeyLifted -> enigmaUiState.value = enigmaUiState.value?.let {
                EnigmaUiState(
                    rotorOnePosition = enigma.rotorOne.position,
                    rotorTwoPosition = enigma.rotorTwo.position,
                    rotorThreePosition = enigma.rotorThree.position,
                    rawMessage = it.rawMessage,
                    encodedMessage = it.encodedMessage,
                    activeLampboard = -1,
                )
            }
            is EnigmaEvent.InputSpacePressed -> enigmaUiState.value = enigmaUiState.value?.let {
                EnigmaUiState(
                    rotorOnePosition = enigma.rotorOne.position,
                    rotorTwoPosition = enigma.rotorTwo.position,
                    rotorThreePosition = enigma.rotorThree.position,
                    rawMessage = it.rawMessage + "_",
                    encodedMessage = it.encodedMessage + "_",
                    activeLampboard = -1,
                )
            }
            is EnigmaEvent.InputDeletePressed -> {}
        }
    }

    private fun numberToLetter(number: Int) = Char(number + 65)


}