package com.smitpatel.enigmamachine.models

import java.util.Stack

object EnigmaModel {

    val historyStack : Stack<EnigmaHistoryItem> = Stack()

    // Left rotor
    var rotorOne = Rotor.makeRotor(
        rotorOption = Rotor.RotorOption.ROTOR_ONE,
        position = 0,
        ring = 0,
    )

    // Middle rotor
    var rotorTwo = Rotor.makeRotor(
        rotorOption = Rotor.RotorOption.ROTOR_TWO,
        position = 0,
        ring = 0,
    )

    // Right rotor
    var rotorThree = Rotor.makeRotor(
        rotorOption = Rotor.RotorOption.ROTOR_THREE,
        position = 0,
        ring = 0,
    )

    var reflector = Reflector.REFLECTOR_UKW_B

    val plugboard = Plugboard

    fun input(letter: Int): Int {

        historyStack.addHistoryItem()

        // Rotor Shifts
        when {
            // Case 1: Middle rotor at turnover -> all three rotors shift
            rotorTwo.isAtTurnover() -> {
                rotorThree.turn()
                rotorTwo.turn()
                rotorOne.turn()
            }
            // Case 2: Right rotor turnover -> right and middle rotor shift
            rotorThree.isAtTurnover() -> {
                rotorThree.turn()
                rotorTwo.turn()
            }
            // Case 3: Left rotor turnover or no rotors at turnover -> right rotor shifts
            else -> {
                rotorThree.turn()
            }
        }

        // Forward direction
        val pbfOutput = plugboard.getPair(letter)
        val r3fOutput = rotorThree.mapping(pbfOutput, Rotor.Direction.FORWARD)
        val r2fOutput = rotorTwo.mapping(r3fOutput, Rotor.Direction.FORWARD)
        val r1fOutput = rotorOne.mapping(r2fOutput, Rotor.Direction.FORWARD)

        // Reflector
        val reflectorOutput = reflector.input(r1fOutput)

        // Reverse direction
        val r1rOutput = rotorOne.mapping(reflectorOutput, Rotor.Direction.REVERSE)
        val r2rOutput = rotorTwo.mapping(r1rOutput, Rotor.Direction.REVERSE)
        val r3rOutput  = rotorThree.mapping(r2rOutput, Rotor.Direction.REVERSE)
        val pbrOutput = plugboard.getPair(r3rOutput)

        return pbrOutput
    }

    fun input(letters: Array<Int>) = letters.map { input(it) }

    /**
     * Returns the current settings
     */
    fun getCurrentSettings() : EnigmaHistoryItem =
        EnigmaHistoryItem(
            rotorOneOption = rotorOne.rotorOption,
            rotorTwoOption = rotorTwo.rotorOption,
            rotorThreeOption = rotorThree.rotorOption,
            rotorOnePosition = rotorOne.position,
            rotorTwoPosition = rotorTwo.position,
            rotorThreePosition = rotorThree.position,
            ringOneOption = rotorOne.ring,
            ringTwoOption = rotorTwo.ring,
            ringThreeOption = rotorThree.ring,
            reflectorOption = reflector,
            plugboardPairs = plugboard.getAllPairs()
        )

    /**
     * Applies the settings given
     */
    fun applySettings(settings: EnigmaHistoryItem, historyStack: List<EnigmaHistoryItem>? = null) {
        if (historyStack != null) {
            this.historyStack.clear()
            historyStack.forEach { this.historyStack.push(it) }
        }

        rotorOne = Rotor.makeRotor(
            rotorOption = settings.rotorOneOption,
            position = settings.rotorOnePosition,
            ring = settings.ringOneOption,
        )

        rotorTwo = Rotor.makeRotor(
            rotorOption = settings.rotorTwoOption,
            position = settings.rotorTwoPosition,
            ring = settings.ringTwoOption,
        )

        rotorThree = Rotor.makeRotor(
            rotorOption = settings.rotorThreeOption,
            position = settings.rotorThreePosition,
            ring = settings.ringThreeOption,
        )

        reflector = settings.reflectorOption

        settings.plugboardPairs.forEach {
            plugboard.addPair(
                letterOne = it.first,
                letterTwo = it.second,
            )
        }
    }

    private fun Stack<EnigmaHistoryItem>.addHistoryItem() {
        this.push(
            EnigmaHistoryItem(
                rotorOneOption = rotorOne.rotorOption,
                rotorTwoOption = rotorTwo.rotorOption,
                rotorThreeOption = rotorThree.rotorOption,
                rotorOnePosition = rotorOne.position,
                rotorTwoPosition = rotorTwo.position,
                rotorThreePosition = rotorThree.position,
                ringOneOption = rotorOne.ring,
                ringTwoOption = rotorTwo.ring,
                ringThreeOption = rotorThree.ring,
                reflectorOption = reflector,
                plugboardPairs = plugboard.getAllPairs()
            )
        )
    }


}