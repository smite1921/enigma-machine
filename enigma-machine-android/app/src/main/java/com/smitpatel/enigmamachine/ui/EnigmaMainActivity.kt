package com.smitpatel.enigmamachine.ui

import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import android.widget.Button
import android.widget.HorizontalScrollView
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.shawnlin.numberpicker.NumberPicker
import com.smitpatel.enigmamachine.R
import com.smitpatel.enigmamachine.databinding.ActivityEnigmaMainBinding
import com.smitpatel.enigmamachine.viewmodels.EnigmaEvent
import com.smitpatel.enigmamachine.viewmodels.EnigmaViewModel

class EnigmaMainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityEnigmaMainBinding

    private val viewModel : EnigmaViewModel by viewModels()

    private val sounds : SoundEffects = SoundEffects

    private val pickerOne: NumberPicker by lazy {
        binding.rotors.rotor1
    }
    private val pickerTwo: NumberPicker by lazy {
        binding.rotors.rotor2
    }
    private val pickerThree: NumberPicker by lazy {
        binding.rotors.rotor3
    }
    private val powerSwitch: View by lazy {
        binding.rotors.powerSwitch
    }
    private val scrollView: HorizontalScrollView by lazy {
        binding.textboxes.scrollview
    }
    private val rawText: TextView by lazy {
        binding.textboxes.textRaw
    }
    private val encodedText: TextView by lazy {
        binding.textboxes.textCode
    }
    private val buttons: Array<Button> by lazy {
        arrayOf(
            binding.keyboard.buttonA, binding.keyboard.buttonB, binding.keyboard.buttonC,
            binding.keyboard.buttonD, binding.keyboard.buttonE, binding.keyboard.buttonF,
            binding.keyboard.buttonG, binding.keyboard.buttonH, binding.keyboard.buttonI,
            binding.keyboard.buttonJ, binding.keyboard.buttonK, binding.keyboard.buttonL,
            binding.keyboard.buttonM, binding.keyboard.buttonN, binding.keyboard.buttonO,
            binding.keyboard.buttonP, binding.keyboard.buttonQ, binding.keyboard.buttonR,
            binding.keyboard.buttonS, binding.keyboard.buttonT, binding.keyboard.buttonU,
            binding.keyboard.buttonV, binding.keyboard.buttonW, binding.keyboard.buttonX,
            binding.keyboard.buttonY, binding.keyboard.buttonZ, binding.keyboard.buttonDelete,
            binding.keyboard.buttonSpacebar
        )
    }
    private val lamps: Array<Button> by lazy {
        arrayOf(
            binding.lampboard.lampA, binding.lampboard.lampB,
            binding.lampboard.lampC, binding.lampboard.lampD, binding.lampboard.lampE,
            binding.lampboard.lampF, binding.lampboard.lampG, binding.lampboard.lampH,
            binding.lampboard.lampI, binding.lampboard.lampJ, binding.lampboard.lampK,
            binding.lampboard.lampL, binding.lampboard.lampM, binding.lampboard.lampN,
            binding.lampboard.lampO, binding.lampboard.lampP, binding.lampboard.lampQ,
            binding.lampboard.lampR, binding.lampboard.lampS, binding.lampboard.lampT,
            binding.lampboard.lampU, binding.lampboard.lampV, binding.lampboard.lampW,
            binding.lampboard.lampX, binding.lampboard.lampY, binding.lampboard.lampZ,
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEnigmaMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupViews()
        setupRenderer()
        lifecycle.addObserver(sounds)
        sounds.lifecycle = lifecycle
        sounds.initialize(context = this)
    }

    private fun setupRenderer() {
        viewModel.uiState.observe(this) {
            pickerOne.value = it.rotorOnePosition + 1
            pickerTwo.value = it.rotorTwoPosition + 1
            pickerThree.value = it.rotorThreePosition + 1

            rawText.text = it.rawMessage
            encodedText.text = it.encodedMessage

            scrollView.fullScroll(View.FOCUS_RIGHT)

            lamps.forEach { lamp ->
                lamp.isPressed = false
            }

            if (it.activeLampboard != -1) {
                lamps[it.activeLampboard].isPressed = true
            }
        }
    }

    private fun setupViews() {
        pickerOne.displayedValues = resources.getStringArray(R.array.rotor_values)
        pickerTwo.displayedValues = resources.getStringArray(R.array.rotor_values)
        pickerThree.displayedValues = resources.getStringArray(R.array.rotor_values)

        powerSwitch.setOnClickListener {
            sounds.playSound(sound = EnigmaSounds.DEFAULT)
        }

        buttons.forEach { button ->
            button.setOnTouchListener { _, event ->
                when (event.action) {
                    MotionEvent.ACTION_DOWN -> {
                        button.isPressed = true
                        button.performClick()
                        when (button.id) {
                            R.id.button_delete -> {
                                sounds.playSound(sound = EnigmaSounds.DELETE)
                                viewModel.handleEvent(
                                    event = EnigmaEvent.InputDeletePressed
                                )
                            }
                            R.id.button_spacebar -> {
                                sounds.playSound(sound = EnigmaSounds.SPACE)
                                viewModel.handleEvent(
                                    event = EnigmaEvent.InputSpacePressed
                                )
                            }
                            else -> {
                                sounds.playSound(sound = EnigmaSounds.KEY)
                                viewModel.handleEvent(
                                    event = EnigmaEvent.InputKeyPressed(
                                        input = letterToNumber(button.text[0])
                                    )
                                )
                            }
                        }
                    }
                    MotionEvent.ACTION_UP -> {
                        button.isPressed = false
                        if ((button.id != R.id.button_delete) && (button.id !=R.id.button_spacebar)) {
                            viewModel.handleEvent(
                                EnigmaEvent.InputKeyLifted(
                                    input = letterToNumber(button.text[0])
                                )
                            )
                        }
                    }
                }
                true
            }
        }
    }

    private fun letterToNumber(letter: Char) = letter.code - 65

}