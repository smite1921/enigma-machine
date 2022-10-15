package com.smitpatel.enigmamachine.models

object EnigmaModel {

    // Left rotor
    var rotorOne = Rotor.ROTOR_ONE
        set(value) {
            value.position = 0
            value.ring = 0
            field = value
        }

    // Middle rotor
    var rotorTwo = Rotor.ROTOR_TWO
        set(value) {
            value.position = 0
            value.ring = 0
            field = value
        }

    // Right rotor
    var rotorThree = Rotor.ROTOR_THREE
        set(value) {
            value.position = 0
            value.ring = 0
            field = value
        }

    var reflector = Reflector.REFLECTOR_UKW_B

    val plugboard = Plugboard

    fun input(letter: Int): Int {

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
     * Sets the enigma machine to default settings
     * Start: A A A
     * Rotor: I II III
     * Ring: A A A
     * Reflector: UKW-B
     * Plugboard Pairs: /
     */
    fun setDefault() {
        rotorOne = Rotor.ROTOR_ONE
        rotorTwo = Rotor.ROTOR_TWO
        rotorThree = Rotor.ROTOR_THREE
        reflector = Reflector.REFLECTOR_UKW_B
        plugboard.removeAllPairs()
    }

}