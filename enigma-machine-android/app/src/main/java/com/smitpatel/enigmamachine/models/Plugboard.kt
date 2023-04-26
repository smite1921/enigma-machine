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

    fun getAllPairs() : Set<Pair<Int, Int>> {
        val pairs = mutableSetOf<Pair<Int, Int>>()
        val visitedLetters = mutableSetOf<Int>()
        plugboardMap.forEachIndexed{ index, element ->
            if (element != -1 && !visitedLetters.contains(element) && !visitedLetters.contains(index)) {
                pairs.add(Pair(index, element))
                visitedLetters.add(element)
                visitedLetters.add(index)
            }
        }
        return pairs
    }

    fun getPair(letter: Int) = if (hasPair(letter)) plugboardMap[letter] else letter

    private fun hasPair(letter: Int) = plugboardMap[letter] != -1

}
