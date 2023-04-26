package com.smitpatel.enigmamachine.models

enum class Reflector(
    private val reflectorMap: Array<Int>,
) {
    REFLECTOR_UKW_A(
        reflectorMap = arrayOf(4, 9, 12, 25, 0, 11, 24, 23, 21, 1, 22, 5, 2, 17, 16, 20, 14, 13, 19, 18, 15, 8, 10, 7, 6, 3)
    ),
    REFLECTOR_UKW_B(
        reflectorMap = arrayOf(24, 17, 20, 7, 16, 18, 11, 3, 15, 23, 13, 6, 14, 10, 12, 8, 4, 1, 5, 25, 2, 22, 21, 9, 0, 19)
    ),
    REFLECTOR_UKW_C(
        reflectorMap = arrayOf(5, 21, 15, 9, 8, 0, 14, 24, 4, 3, 17, 25, 23, 22, 6, 2, 19, 10, 20, 16, 18, 1, 13, 12, 7, 11)
    );

    fun input(input: Int) = reflectorMap[input]
}