package com.smitpatel.enigmamachine.models

object Plugboard {

    private val plugboardMap: Array<Int> = arrayOf(-1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1)

    fun addPair(letterOne: Int, letterTwo: Int) {
        plugboardMap[letterOne] = letterTwo
        plugboardMap[letterTwo] = letterOne
    }

    fun removePair(letter: Int) {
        val letterTwo = plugboardMap[letter]
        plugboardMap[letter] = -1
        plugboardMap[letterTwo] = -1
    }

    fun removeAllPairs() {
        plugboardMap.fill(-1, 0, plugboardMap.size)
    }

    fun getPair(letter: Int) = if (hasPair(letter)) plugboardMap[letter] else letter

    private fun hasPair(letter: Int) = plugboardMap[letter] != -1

}
