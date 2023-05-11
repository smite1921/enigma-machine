package com.smitpatel.enigmamachine.views

import android.content.Context
import android.os.Parcelable
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.Button
import androidx.appcompat.widget.LinearLayoutCompat
import com.smitpatel.enigmamachine.R
import com.smitpatel.enigmamachine.databinding.ViewPlugboardBinding
import com.smitpatel.enigmamachine.ui.EnigmaSounds
import com.smitpatel.enigmamachine.ui.SoundEffects
import kotlinx.parcelize.Parcelize
import java.util.Stack

class Plugboard(
    context: Context,
    attributeSet: AttributeSet,
) : LinearLayoutCompat(context, attributeSet) {

    private val sounds : SoundEffects = SoundEffects

    var onPairAddedListener: ((Pair<Int, Int>) -> Unit)? = null
    var onPairRemovedListener: ((Pair<Int, Int>) -> Unit)? = null

    private lateinit var binding: ViewPlugboardBinding

    private var pairLookup : MutableMap<Int, Int> = mutableMapOf()
    private var colorLookup : MutableMap<Int, Int> = mutableMapOf()
    private var colorStack : Stack<Int> = run {
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

    override fun onSaveInstanceState(): Parcelable? {
        return when (val superState = super.onSaveInstanceState()) {
            null -> null
            else -> return PlugboardSaveState(superState, pairLookup, colorLookup, colorStack)
        }
    }

    override fun onRestoreInstanceState(state: Parcelable) {
        super.onRestoreInstanceState(state)
        when (state) {
            is PlugboardSaveState -> {
                pairLookup = state.pairLookup
                colorLookup = state.colorLookup
                colorStack = state.colorStack
                val colouredLamps = mutableSetOf<Int>()
                pairLookup.forEach {
                    val lampOne = findViewById<Button>(it.value)
                    val lampTwo = findViewById<Button>(it.key)
                    val lampColour = colorLookup[it.key]
                    if (!colouredLamps.contains(it.value) && !colouredLamps.contains(it.key) && lampColour != null) {
                        lampOne.setBackgroundResource(lampColour)
                        lampTwo.setBackgroundResource(lampColour)
                        colouredLamps.add(it.key)
                        colouredLamps.add(it.value)
                    }
                }
            }
        }
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

    @Parcelize
    private data class PlugboardSaveState(
        val ss : Parcelable,
        val pairLookup : MutableMap<Int, Int>,
        val colorLookup : MutableMap<Int, Int>,
        val colorStack : Stack<Int>,
    ) : BaseSavedState(ss)
}
