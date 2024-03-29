package com.smitpatel.enigmamachine.models

class Rotor(
    var position: Int,
    var ring: Int,
    val rotorOption: RotorOption,
    private val turnover: Int,
    private val forwardMap: Array<Int>,
    private val reverseMap: Array<Int>,
) {

    enum class RotorOption {
        ROTOR_ONE,
        ROTOR_TWO,
        ROTOR_THREE,
        ROTOR_FOUR,
        ROTOR_FIVE,
    }

    enum class Direction {
        FORWARD,
        REVERSE
    }

    companion object {
        fun makeRotor(rotorOption: RotorOption, position: Int = 0, ring: Int = 0) : Rotor {
            val turnover : Int
            val forwardMap : Array<Int>
            val reverseMap : Array<Int>

            when (rotorOption) {
                RotorOption.ROTOR_ONE -> {
                    turnover = 16
                    forwardMap = arrayOf(4, 10, 12, 5, 11, 6, 3, 16, 21, 25, 13, 19, 14, 22, 24, 7, 23, 20, 18, 15, 0, 8, 1, 17, 2, 9)
                    reverseMap = arrayOf(20, 22, 24, 6, 0, 3, 5, 15, 21, 25, 1, 4, 2, 10, 12, 19, 7, 23, 18, 11, 17, 8, 13, 16, 14, 9)
                }
                RotorOption.ROTOR_TWO -> {
                    turnover = 4
                    forwardMap = arrayOf(0, 9, 3, 10, 18, 8, 17, 20, 23, 1, 11, 7, 22, 19, 12, 2, 16, 6, 25, 13, 15, 24, 5, 21, 14, 4)
                    reverseMap = arrayOf(0, 9, 15, 2, 25, 22, 17, 11, 5, 1, 3, 10, 14, 19, 24, 20, 16, 6, 4, 13, 7, 23, 12, 8, 21, 18)
                }
                RotorOption.ROTOR_THREE -> {
                    turnover = 21
                    forwardMap = arrayOf(1, 3, 5, 7, 9, 11, 2, 15, 17, 19, 23, 21, 25, 13, 24, 4, 8, 22, 6, 0, 10, 12, 20, 18, 16, 14)
                    reverseMap = arrayOf(19, 0, 6, 1, 15, 2, 18, 3, 16, 4, 20, 5, 21, 13, 25, 7, 24, 8, 23, 9, 22, 11, 17, 10, 14, 12)
                }
                RotorOption.ROTOR_FOUR -> {
                    turnover = 9
                    forwardMap = arrayOf(4, 18, 14, 21, 15, 25, 9, 0, 24, 16, 20, 8, 17, 7, 23, 11, 13, 5, 19, 6, 10, 3, 2, 12, 22, 1)
                    reverseMap = arrayOf(7, 25, 22, 21, 0, 17, 19, 13, 11, 6, 20, 15, 23, 16, 2, 4, 9, 12, 1, 18, 10, 3, 24, 14, 8, 5)
                }
                RotorOption.ROTOR_FIVE -> {
                    turnover = 25
                    forwardMap = arrayOf(21, 25, 1, 17, 6, 8, 19, 24, 20, 15, 18, 3, 13, 7, 11, 23, 0, 22, 12, 9, 16, 14, 5, 4, 2, 10)
                    reverseMap = arrayOf(16, 2, 24, 11, 23, 22, 4, 13, 5, 19, 25, 14, 18, 12, 21, 9, 20, 3, 10, 6, 8, 0, 17, 15, 7, 1)
                }
            }

            return Rotor(
                position = position,
                ring = ring,
                rotorOption = rotorOption,
                turnover = turnover,
                forwardMap = forwardMap,
                reverseMap = reverseMap,
            )
        }
    }

    fun mapping(input: Int, direction: Direction): Int {
        val rotorMap = if (direction == Direction.FORWARD) forwardMap else reverseMap
        val mapsTo = rotorMap[floorMod26(input + position - ring)]
        return floorMod26(mapsTo - position + ring)
    }

    fun turn() {
        position = floorMod26(position + 1)
    }

    fun isAtTurnover() = turnover == position

    private fun floorMod26(x: Int): Int {
        var mod = x % 26
        if (mod xor 26 < 0 && mod != 0) {
            mod += 26
        }
        return mod
    }

}