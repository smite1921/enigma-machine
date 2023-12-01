package com.smitpatel.enigmamachine.serializer
//
//import android.content.Context
//import com.smitpatel.enigmamachine.R
//import com.smitpatel.enigmamachine.Serializer
//import com.smitpatel.enigmamachine.Serializer.toNumber
//import com.smitpatel.enigmamachine.Serializer.toReflector
//import com.smitpatel.enigmamachine.Serializer.toRotorOption
//import com.smitpatel.enigmamachine.letterToNumber
//import com.smitpatel.enigmamachine.models.EnigmaHistoryItem
//import com.smitpatel.enigmamachine.models.Reflector
//import com.smitpatel.enigmamachine.models.Rotor
//import io.mockk.every
//import io.mockk.mockk
//
//import org.junit.Test
//
//import org.junit.Assert.*
//
//class SerializerTest {
//
//    @Test
//    fun testStringToNumberSingleLetter() {
//        val actual = "D".toNumber()
//        val expected = 'D'.letterToNumber()
//        assertEquals(actual, expected)
//    }
//
//    @Test
//    fun testStringToNumberWithLowerCaseAndSpace() {
//        val actual = "    d   ".toNumber()
//        val expected = 'D'.letterToNumber()
//        assertEquals(actual, expected)
//    }
//
//    @Test(expected = IllegalArgumentException::class)
//    fun testStringToNumberNotLetter() {
//        "4".toNumber()
//    }
//
//    @Test(expected = IllegalArgumentException::class)
//    fun testStringToNumberTooManyLetters() {
//        "ABC".toNumber()
//    }
//
//    @Test(expected = NoSuchElementException::class)
//    fun testStringToNumberEmptyString() {
//        "".toNumber()
//    }
//
//    @Test
//    fun testStringToRotorOptionRomanNumerals() {
//
//        val mockContext = mockk<Context>()
//
//        val actual = arrayOf("I", "II", "III", "IV", "V").map { it.toRotorOption(mockContext) }.toTypedArray()
//        val expected = arrayOf(Rotor.RotorOption.ROTOR_ONE,
//            Rotor.RotorOption.ROTOR_TWO, Rotor.RotorOption.ROTOR_THREE,
//            Rotor.RotorOption.ROTOR_FOUR, Rotor.RotorOption.ROTOR_FIVE)
//        assertArrayEquals(expected, actual)
//    }
//
//    @Test
//    fun testStringToRotorOptionLowerCaseRomanNumeralsWithSpace() {
//        val actual = arrayOf(" i ", "ii    ", " iii", " iV ", "  v ").map { it.toRotorOption() }.toTypedArray()
//        val expected = arrayOf(Rotor.RotorOption.ROTOR_ONE,
//            Rotor.RotorOption.ROTOR_TWO, Rotor.RotorOption.ROTOR_THREE,
//            Rotor.RotorOption.ROTOR_FOUR, Rotor.RotorOption.ROTOR_FIVE)
//        assertArrayEquals(expected, actual)
//    }
//
//    @Test(expected = IllegalArgumentException::class)
//    fun testStringToRotorOptionInvalidString() {
//        val mockContext = mockk<Context>()
//        every { mockContext.resources.getStringArray(R.array.rotor_options) } returns
//                arrayOf("I", "II", "III", "IV", "V")
//        "rotorOne".toRotorOption(mockContext)
//    }
//
//    @Test
//    fun testStringToReflectorValidString() {
//        val actual = arrayOf("UKW-A", "UKW-B", "UKW-C").map { it.toReflector() }.toTypedArray()
//        val expected = arrayOf(Reflector.REFLECTOR_UKW_A, Reflector.REFLECTOR_UKW_B, Reflector.REFLECTOR_UKW_C)
//        assertArrayEquals(expected, actual)
//
//    }
//
//    @Test
//    fun testStringToReflectorValidStringLowercaseWithSpaces() {
//        val actual = arrayOf("  UKW-a  ", " ukw-b ", "  ukw-C").map { it.toReflector() }.toTypedArray()
//        val expected = arrayOf(Reflector.REFLECTOR_UKW_A, Reflector.REFLECTOR_UKW_B, Reflector.REFLECTOR_UKW_C)
//        assertArrayEquals(expected, actual)
//    }
//
//    @Test(expected = IllegalArgumentException::class)
//    fun testStringToReflectorInvalidString() {
//        "ukw--a".toReflector()
//    }

//    @Test
//    fun testSerialization() {
//        val actual = Serializer.serializeJson(EnigmaHistoryItem(
//            rotorOneOption = Rotor.RotorOption.ROTOR_FIVE,
//            rotorTwoOption = Rotor.RotorOption.ROTOR_FOUR,
//            rotorThreeOption = Rotor.RotorOption.ROTOR_TWO,
//            rotorOnePosition = 'X'.letterToNumber(),
//            rotorTwoPosition = 'Y'.letterToNumber(),
//            rotorThreePosition = 'Z'.letterToNumber(),
//            ringOneOption = 'S'.letterToNumber(),
//            ringTwoOption = 'M'.letterToNumber(),
//            ringThreeOption = 'I'.letterToNumber(),
//            reflectorOption = Reflector.REFLECTOR_UKW_C,
//            plugboardPairs = setOf(
//                Pair('A'.letterToNumber(), 'E'.letterToNumber()),
//                Pair('D'.letterToNumber(), 'P'.letterToNumber()),
//                Pair('Q'.letterToNumber(), 'W'.letterToNumber()),
//                Pair('L'.letterToNumber(), 'K'.letterToNumber()),
//                Pair('S'.letterToNumber(), 'R'.letterToNumber()),
//            )
//        ))
//        val expected = """
//            {
//                "rotorOptions":["V","IV","II"],
//                "rotorPositions":["X","Y","Z"],
//                "ringPositions:["S","M","I"],
//                "reflector":"UKW-C",
//                "plugboardPairs":[["A","E"],["D","E"],["Q","W"],["L","K"],["S","R"]]
//            }
//        """.trimIndent()
//        assertEquals(expected, actual)
//
//    }
//
//}