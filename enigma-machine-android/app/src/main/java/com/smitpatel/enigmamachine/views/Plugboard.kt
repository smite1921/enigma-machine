package com.smitpatel.enigmamachine.views

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.Button
import androidx.appcompat.widget.LinearLayoutCompat
import com.smitpatel.enigmamachine.R
import com.smitpatel.enigmamachine.databinding.ViewPlugboardBinding
import com.smitpatel.enigmamachine.ui.EnigmaSounds
import com.smitpatel.enigmamachine.ui.SoundEffects
import java.util.Stack

class Plugboard(
    context: Context,
    attributeSet: AttributeSet,
) : LinearLayoutCompat(context, attributeSet) {

    private val sounds : SoundEffects = SoundEffects

    var onPairAddedListener: ((Pair<Int, Int>) -> Unit)? = null
    var onPairRemovedListener: ((Pair<Int, Int>) -> Unit)? = null

    private lateinit var binding: ViewPlugboardBinding

    private val pairLookup : MutableMap<Int, Int> = mutableMapOf()
    private val colorLookup : MutableMap<Int, Int> = mutableMapOf()
    private val colorStack : Stack<Int> by lazy {
        val stack = Stack<Int>()
        stack.push(R.drawable.enigma_settings_plugboard_color13)
        stack.push(R.drawable.enigma_settings_plugboard_color12)
        stack.push(R.drawable.enigma_settings_plugboard_color11)
        stack.push(R.drawable.enigma_settings_plugboard_color10)
        stack.push(R.drawable.enigma_settings_plugboard_color09)
        stack.push(R.drawable.enigma_settings_plugboard_color08)
        stack.push(R.drawable.enigma_settings_plugboard_color07)
        stack.push(R.drawable.enigma_settings_plugboard_color06)
        stack.push(R.drawable.enigma_settings_plugboard_color05)
        stack.push(R.drawable.enigma_settings_plugboard_color04)
        stack.push(R.drawable.enigma_settings_plugboard_color03)
        stack.push(R.drawable.enigma_settings_plugboard_color02)
        stack.push(R.drawable.enigma_settings_plugboard_color01)
        stack
    }

    private val lamps: Array<Button> by lazy {
        arrayOf(
            binding.lampA, binding.lampB, binding.lampC, binding.lampD, binding.lampE,
            binding.lampF, binding.lampG, binding.lampH, binding.lampI, binding.lampJ,
            binding.lampK, binding.lampL, binding.lampM, binding.lampN, binding.lampO,
            binding.lampP, binding.lampQ, binding.lampR, binding.lampS, binding.lampT,
            binding.lampU, binding.lampV, binding.lampW, binding.lampX, binding.lampY,
            binding.lampZ,
        )
    }

    init {
        binding = ViewPlugboardBinding.inflate(LayoutInflater.from(context))
        addView(binding.root)

        lamps.forEach { lamp ->
            lamp.setOnClickListener {
                sounds.playSound(EnigmaSounds.PLUGBOARD)
                val hasPairedLamp = pairLookup[it.id] != null
                val isLampColored = colorLookup[it.id] != null
                val isTopColoredUsed = if (colorStack.isEmpty()) true else
                    colorLookup.containsValue(colorStack.peek())

                when {
                    hasPairedLamp && isTopColoredUsed && !colorStack.isEmpty() -> {}
                    hasPairedLamp -> {
                        val pairColor = colorLookup[it.id]
                        colorLookup.remove(it.id)
                        val pairedLamp = colorLookup.getKey(pairColor)
                        colorLookup.remove(pairedLamp)
                        pairLookup.remove(it.id)
                        pairLookup.remove(pairedLamp)
                        colorStack.push(pairColor)
                        val curLamp = it as Button
                        val pairLamp = findViewById<Button>(pairedLamp)
                        curLamp.setDefaultBackground()
                        pairLamp.setDefaultBackground()
                        onPairRemovedListener?.invoke(Pair(curLamp.getNumber(), pairLamp.getNumber()))
                    }
                    isLampColored  -> {
                        colorLookup.remove(it.id)
                        (it as Button).setDefaultBackground()
                    }
                    isTopColoredUsed -> {
                        val topColor = colorStack.pop()
                        val pairedLamp = colorLookup.getKey(topColor)
                        colorLookup[it.id] = topColor
                        pairLookup[it.id] = pairedLamp
                        pairLookup[pairedLamp] = it.id
                        val curLamp = it as Button
                        val pairLamp = findViewById<Button>(pairedLamp)
                        curLamp.setBackgroundResource(topColor)
                        onPairAddedListener?.invoke(Pair(curLamp.getNumber(), pairLamp.getNumber()))
                    }
                    else -> {
                        val topColor = colorStack.peek()
                        colorLookup[it.id] = topColor
                        it.setBackgroundResource(topColor)
                    }
                }
            }
        }
    }

    fun addPair(letter1: Int, letter2: Int) {
        val topColor = colorStack.pop()
        val lampOne = lamps[letter1]
        val lampTwo = lamps[letter2]
        colorLookup[lampOne.id] = topColor
        colorLookup[lampTwo.id] = topColor
        pairLookup[lampOne.id] = lampTwo.id
        pairLookup[lampTwo.id] = lampOne.id
        lampOne.setBackgroundResource(topColor)
        lampTwo.setBackgroundResource(topColor)
    }

    private fun <K, V> Map<K, V>.getKey(value : V) = this.keys.first { value == this[it] }

    private fun Button.getNumber() = this.text[0].code - 65

    private fun Button.setDefaultBackground() =
        this.setBackgroundResource(R.drawable.enigma_settings_plugboard_default)
}
