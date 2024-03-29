package com.smitpatel.enigmamachine.models

import org.junit.After
import org.junit.Test

import org.junit.Assert.*
import java.util.Stack

class EnigmaModelTest {

    private val enigma: EnigmaModel = EnigmaModel

    private fun toIndexArr(text: String) = text.map { it.uppercaseChar().code - 65 }.toTypedArray()

    private fun toIndex(letter: Char) = letter.uppercaseChar().code - 65

    /**
     * Sets the enigma machine to default settings
     * Start: A A A
     * Rotor: I II III
     * Ring: A A A
     * Reflector: UKW-B
     * Plugboard Pairs: /
     */
    @After
    fun cleanUpEnigma() {
        EnigmaModel.rotorOne = Rotor.makeRotor(
            rotorOption = Rotor.RotorOption.ROTOR_ONE,
            position = 0,
            ring = 0,
        )
        EnigmaModel.rotorTwo = Rotor.makeRotor(
            rotorOption = Rotor.RotorOption.ROTOR_TWO,
            position = 0,
            ring = 0,
        )
        EnigmaModel.rotorThree = Rotor.makeRotor(
            rotorOption = Rotor.RotorOption.ROTOR_THREE,
            position = 0,
            ring = 0,
        )
        EnigmaModel.reflector = Reflector.REFLECTOR_UKW_B
        EnigmaModel.plugboard.removeAllPairs()
        EnigmaModel.historyStack.clear()
    }

    @Test
    /**
     * Message with default settings
     * Start: A A A
     * Rotor: I II III
     * Ring: A A A
     * Reflector: UKW-B
     * Plugboard Pairs:
     */
    fun messageDefaultSettings() {
        val inputArr = toIndexArr("SECRET")
        val outputArr = enigma.input(inputArr).toTypedArray()
        val expectedArr = toIndexArr("JLEURR")
        assertArrayEquals(expectedArr, outputArr)
    }

    @Test
    /**
     * Message with different start position
     * Start: X Y Z
     * Rotor: I II III
     * Ring: A A A
     * Reflector: UKW-B
     * Plugboard Pairs:
     */
    fun messageDifferentStart() {
        enigma.rotorOne.position = toIndex('X')
        enigma.rotorTwo.position = toIndex('Y')
        enigma.rotorThree.position = toIndex('Z')

        val inputArr = toIndexArr("SECRET")
        val outputArr = enigma.input(inputArr).toTypedArray()
        val expectedArr = toIndexArr("JMEYTK")
        assertArrayEquals(expectedArr, outputArr)
    }

    @Test
    /**
     * Message with different rotors
     * Start: X Y Z
     * Rotor: V IV II
     * Ring: A A A
     * Reflector: UKW-B
     * Plugboard Pairs:
     */
    fun messageDifferentRotor() {
        enigma.rotorOne = Rotor.makeRotor(
            rotorOption = Rotor.RotorOption.ROTOR_FIVE,
        )
        enigma.rotorTwo = Rotor.makeRotor(
            rotorOption = Rotor.RotorOption.ROTOR_FOUR,
        )
        enigma.rotorThree = Rotor.makeRotor(
            rotorOption = Rotor.RotorOption.ROTOR_TWO,
        )

        enigma.rotorOne.position = toIndex('X')
        enigma.rotorTwo.position = toIndex('Y')
        enigma.rotorThree.position = toIndex('Z')

        val inputArr = toIndexArr("SECRET")
        val outputArr = enigma.input(inputArr).toTypedArray()
        val expectedArr = toIndexArr("XHKYKL")
        assertArrayEquals(expectedArr, outputArr)
    }

    @Test
    /**
     * Message with different rings
     * Start: X Y Z
     * Rotor: V IV II
     * Ring: S M I
     * Reflector: UKW-B
     * Plugboard Pairs:
     */
    fun messageDifferentRings() {
        enigma.rotorOne = Rotor.makeRotor(
            rotorOption = Rotor.RotorOption.ROTOR_FIVE,
        )
        enigma.rotorTwo = Rotor.makeRotor(
            rotorOption = Rotor.RotorOption.ROTOR_FOUR,
        )
        enigma.rotorThree = Rotor.makeRotor(
            rotorOption = Rotor.RotorOption.ROTOR_TWO,
        )

        enigma.rotorOne.position = toIndex('X')
        enigma.rotorTwo.position = toIndex('Y')
        enigma.rotorThree.position = toIndex('Z')

        enigma.rotorOne.ring = toIndex('S')
        enigma.rotorTwo.ring = toIndex('M')
        enigma.rotorThree.ring = toIndex('I')

        val inputArr = toIndexArr("SECRET")
        val outputArr = enigma.input(inputArr).toTypedArray()
        val expectedArr = toIndexArr("KAZVDW")
        assertArrayEquals(expectedArr, outputArr)
    }

    @Test
    /**
     * Message with different reflector
     * Start: X Y Z
     * Rotor: V IV II
     * Ring: S M I
     * Reflector: UKW-C
     * Plugboard Pairs:
     */
    fun messageDifferentReflector() {
        enigma.rotorOne = Rotor.makeRotor(
            rotorOption = Rotor.RotorOption.ROTOR_FIVE,
        )
        enigma.rotorTwo = Rotor.makeRotor(
            rotorOption = Rotor.RotorOption.ROTOR_FOUR,
        )
        enigma.rotorThree = Rotor.makeRotor(
            rotorOption = Rotor.RotorOption.ROTOR_TWO,
        )

        enigma.reflector = Reflector.REFLECTOR_UKW_C

        enigma.rotorOne.position = toIndex('X')
        enigma.rotorTwo.position = toIndex('Y')
        enigma.rotorThree.position = toIndex('Z')

        enigma.rotorOne.ring = toIndex('S')
        enigma.rotorTwo.ring = toIndex('M')
        enigma.rotorThree.ring = toIndex('I')

        val inputArr = toIndexArr("SECRET")
        val outputArr = enigma.input(inputArr).toTypedArray()
        val expectedArr = toIndexArr("TTFPXY")
        assertArrayEquals(expectedArr, outputArr)
    }

    @Test
    /**
     * Message with plugboard pairs
     * Start: X Y Z
     * Rotor: V IV II
     * Ring: S M I
     * Reflector: UKW-C
     * Plugboard Pairs: (AE) (DP) (QW) (LK) (SR)
     */
    fun messageDifferentPlugboard() {
        enigma.rotorOne = Rotor.makeRotor(
            rotorOption = Rotor.RotorOption.ROTOR_FIVE,
        )
        enigma.rotorTwo = Rotor.makeRotor(
            rotorOption = Rotor.RotorOption.ROTOR_FOUR,
        )
        enigma.rotorThree = Rotor.makeRotor(
            rotorOption = Rotor.RotorOption.ROTOR_TWO,
        )

        enigma.reflector = Reflector.REFLECTOR_UKW_C

        enigma.rotorOne.position = toIndex('X')
        enigma.rotorTwo.position = toIndex('Y')
        enigma.rotorThree.position = toIndex('Z')

        enigma.rotorOne.ring = toIndex('S')
        enigma.rotorTwo.ring = toIndex('M')
        enigma.rotorThree.ring = toIndex('I')

        enigma.plugboard.addPair(toIndex('A'), toIndex('E'))
        enigma.plugboard.addPair(toIndex('D'), toIndex('P'))
        enigma.plugboard.addPair(toIndex('Q'), toIndex('W'))
        enigma.plugboard.addPair(toIndex('L'), toIndex('K'))
        enigma.plugboard.addPair(toIndex('S'), toIndex('R'))

        val inputArr = toIndexArr("SECRET")
        val outputArr = enigma.input(inputArr).toTypedArray()
        val expectedArr = toIndexArr("EUFYGY")
        assertArrayEquals(expectedArr, outputArr)
    }

    @Test
    /**
     * Testing normal turnover
     * Start: A A U
     * Rotor: I II III
     * Ring: A A A
     * Reflector: UKW-B
     * Plugboard Pairs:
     */
    fun turnOverNormalStep() {
        enigma.rotorThree.position = toIndex('U')
        val positions = mutableListOf<Triple<Int, Int, Int>>()

        repeat(4) {
            positions.add(
                Triple(enigma.rotorOne.position, enigma.rotorTwo.position, enigma.rotorThree.position)
            )
            enigma.input(0)
        }

        val enigmaPositionArr = positions.toTypedArray()
        val expectedArr = arrayOf(
            Triple(toIndex('A'), toIndex('A'), toIndex('U')),
            Triple(toIndex('A'), toIndex('A'), toIndex('V')),
            Triple(toIndex('A'), toIndex('B'), toIndex('W')),
            Triple(toIndex('A'), toIndex('B'), toIndex('X')),
        )
        assertArrayEquals(expectedArr, enigmaPositionArr)
    }

    @Test
    /**
     * Testing double turnover
     * Start: A D U
     * Rotor: I II III
     * Ring: A A A
     * Reflector: UKW-B
     * Plugboard Pairs:
     */
    fun turnOverDoubleStep() {
        enigma.rotorTwo.position = toIndex('D')
        enigma.rotorThree.position = toIndex('U')

        val positions = mutableListOf<Triple<Int, Int, Int>>()

        repeat(5) {
            positions.add(
                Triple(enigma.rotorOne.position, enigma.rotorTwo.position, enigma.rotorThree.position)
            )
            enigma.input(0)
        }

        val enigmaPositionArr = positions.toTypedArray()
        val expectedArr = arrayOf(
            Triple(toIndex('A'), toIndex('D'), toIndex('U')),
            Triple(toIndex('A'), toIndex('D'), toIndex('V')),
            Triple(toIndex('A'), toIndex('E'), toIndex('W')),
            Triple(toIndex('B'), toIndex('F'), toIndex('X')),
            Triple(toIndex('B'), toIndex('F'), toIndex('Y')),
        )
        assertArrayEquals(expectedArr, enigmaPositionArr)
    }

    @Test
    /**
     * Testing history stack
     * Start: X Y Z
     * Rotor: V IV II
     * Ring: S M I
     * Reflector: UKW-C
     * Plugboard Pairs: (AE) (DP)
     */
    fun testHistoryStack() {
        enigma.rotorOne = Rotor.makeRotor(
            rotorOption = Rotor.RotorOption.ROTOR_FIVE,
        )
        enigma.rotorTwo = Rotor.makeRotor(
            rotorOption = Rotor.RotorOption.ROTOR_FOUR,
        )
        enigma.rotorThree = Rotor.makeRotor(
            rotorOption = Rotor.RotorOption.ROTOR_TWO,
        )

        enigma.reflector = Reflector.REFLECTOR_UKW_C

        enigma.rotorOne.position = toIndex('X')
        enigma.rotorTwo.position = toIndex('Y')
        enigma.rotorThree.position = toIndex('Z')

        enigma.rotorOne.ring = toIndex('S')
        enigma.rotorTwo.ring = toIndex('M')
        enigma.rotorThree.ring = toIndex('I')

        enigma.plugboard.addPair(toIndex('A'), toIndex('E'))
        enigma.plugboard.addPair(toIndex('D'), toIndex('P'))

        val inputArr = toIndexArr("ABC")
        enigma.input(inputArr)

        val expectedHistoryStackItems = arrayOf(
            EnigmaHistoryItem(
                rotorOneOption = Rotor.RotorOption.ROTOR_FIVE,
                rotorTwoOption = Rotor.RotorOption.ROTOR_FOUR,
                rotorThreeOption = Rotor.RotorOption.ROTOR_TWO,
                rotorOnePosition = toIndex('X'),
                rotorTwoPosition = toIndex('Y'),
                rotorThreePosition = toIndex('B'),
                ringOneOption = toIndex('S'),
                ringTwoOption = toIndex('M'),
                ringThreeOption = toIndex('I'),
                reflectorOption = Reflector.REFLECTOR_UKW_C,
                plugboardPairs = setOf(
                    Pair(toIndex('A'), toIndex('E')),
                    Pair(toIndex('D'), toIndex('P')),
                ),
            ),
            EnigmaHistoryItem(
                rotorOneOption = Rotor.RotorOption.ROTOR_FIVE,
                rotorTwoOption = Rotor.RotorOption.ROTOR_FOUR,
                rotorThreeOption = Rotor.RotorOption.ROTOR_TWO,
                rotorOnePosition = toIndex('X'),
                rotorTwoPosition = toIndex('Y'),
                rotorThreePosition = toIndex('A'),
                ringOneOption = toIndex('S'),
                ringTwoOption = toIndex('M'),
                ringThreeOption = toIndex('I'),
                reflectorOption = Reflector.REFLECTOR_UKW_C,
                plugboardPairs = setOf(
                    Pair(toIndex('A'), toIndex('E')),
                    Pair(toIndex('D'), toIndex('P')),
                ),
            ),
            EnigmaHistoryItem(
                rotorOneOption = Rotor.RotorOption.ROTOR_FIVE,
                rotorTwoOption = Rotor.RotorOption.ROTOR_FOUR,
                rotorThreeOption = Rotor.RotorOption.ROTOR_TWO,
                rotorOnePosition = toIndex('X'),
                rotorTwoPosition = toIndex('Y'),
                rotorThreePosition = toIndex('Z'),
                ringOneOption = toIndex('S'),
                ringTwoOption = toIndex('M'),
                ringThreeOption = toIndex('I'),
                reflectorOption = Reflector.REFLECTOR_UKW_C,
                plugboardPairs = setOf(
                    Pair(toIndex('A'), toIndex('E')),
                    Pair(toIndex('D'), toIndex('P')),
                ),
            )
        )

        for (expectedHistoryItem in expectedHistoryStackItems) {
            assertEquals(expectedHistoryItem, enigma.historyStack.pop())
        }
    }

    @Test
    /**
     * Test the applySettings() function
     */
    fun testApplySettings() {
        val newSettings = EnigmaHistoryItem(
            rotorOneOption = Rotor.RotorOption.ROTOR_FIVE,
            rotorTwoOption = Rotor.RotorOption.ROTOR_FOUR,
            rotorThreeOption = Rotor.RotorOption.ROTOR_TWO,
            rotorOnePosition = toIndex('X'),
            rotorTwoPosition = toIndex('Y'),
            rotorThreePosition = toIndex('B'),
            ringOneOption = toIndex('S'),
            ringTwoOption = toIndex('M'),
            ringThreeOption = toIndex('I'),
            reflectorOption = Reflector.REFLECTOR_UKW_C,
            plugboardPairs = setOf(
                Pair(toIndex('A'), toIndex('E')),
                Pair(toIndex('D'), toIndex('P')),
            ),
        )
        enigma.applySettings(settings = newSettings)
        assertEquals(enigma.getCurrentSettings(), newSettings)
        assertTrue(enigma.historyStack.empty())
    }

    @Test
    /**
     * Test the applySettings() function with history stack argument
     */
    fun testApplySettingsWithHistoryStack() {
        val newSettings = EnigmaHistoryItem(
            rotorOneOption = Rotor.RotorOption.ROTOR_FIVE,
            rotorTwoOption = Rotor.RotorOption.ROTOR_FOUR,
            rotorThreeOption = Rotor.RotorOption.ROTOR_TWO,
            rotorOnePosition = toIndex('X'),
            rotorTwoPosition = toIndex('Y'),
            rotorThreePosition = toIndex('C'),
            ringOneOption = toIndex('S'),
            ringTwoOption = toIndex('M'),
            ringThreeOption = toIndex('I'),
            reflectorOption = Reflector.REFLECTOR_UKW_C,
            plugboardPairs = setOf(
                Pair(toIndex('A'), toIndex('E')),
                Pair(toIndex('D'), toIndex('P')),
            )
        )
        val newHistoryStack = listOf(
            EnigmaHistoryItem(
                rotorOneOption = Rotor.RotorOption.ROTOR_FIVE,
                rotorTwoOption = Rotor.RotorOption.ROTOR_FOUR,
                rotorThreeOption = Rotor.RotorOption.ROTOR_TWO,
                rotorOnePosition = toIndex('X'),
                rotorTwoPosition = toIndex('Y'),
                rotorThreePosition = toIndex('Z'),
                ringOneOption = toIndex('S'),
                ringTwoOption = toIndex('M'),
                ringThreeOption = toIndex('I'),
                reflectorOption = Reflector.REFLECTOR_UKW_C,
                plugboardPairs = setOf(
                    Pair(toIndex('A'), toIndex('E')),
                    Pair(toIndex('D'), toIndex('P')),
                ),
            ),
            EnigmaHistoryItem(
                rotorOneOption = Rotor.RotorOption.ROTOR_FIVE,
                rotorTwoOption = Rotor.RotorOption.ROTOR_FOUR,
                rotorThreeOption = Rotor.RotorOption.ROTOR_TWO,
                rotorOnePosition = toIndex('X'),
                rotorTwoPosition = toIndex('Y'),
                rotorThreePosition = toIndex('A'),
                ringOneOption = toIndex('S'),
                ringTwoOption = toIndex('M'),
                ringThreeOption = toIndex('I'),
                reflectorOption = Reflector.REFLECTOR_UKW_C,
                plugboardPairs = setOf(
                    Pair(toIndex('A'), toIndex('E')),
                    Pair(toIndex('D'), toIndex('P')),
                ),
            ),
            EnigmaHistoryItem(
                rotorOneOption = Rotor.RotorOption.ROTOR_FIVE,
                rotorTwoOption = Rotor.RotorOption.ROTOR_FOUR,
                rotorThreeOption = Rotor.RotorOption.ROTOR_TWO,
                rotorOnePosition = toIndex('X'),
                rotorTwoPosition = toIndex('Y'),
                rotorThreePosition = toIndex('B'),
                ringOneOption = toIndex('S'),
                ringTwoOption = toIndex('M'),
                ringThreeOption = toIndex('I'),
                reflectorOption = Reflector.REFLECTOR_UKW_C,
                plugboardPairs = setOf(
                    Pair(toIndex('A'), toIndex('E')),
                    Pair(toIndex('D'), toIndex('P')),
                ),
            ),
        )

        enigma.applySettings(settings = newSettings, historyStack = newHistoryStack)

        val expectedHistoryStack = Stack<EnigmaHistoryItem>()
        for (historyItem in newHistoryStack) {
            expectedHistoryStack.push(historyItem)
        }

        assertEquals(newSettings, enigma.getCurrentSettings())
        assertEquals(expectedHistoryStack, enigma.historyStack)
    }

}