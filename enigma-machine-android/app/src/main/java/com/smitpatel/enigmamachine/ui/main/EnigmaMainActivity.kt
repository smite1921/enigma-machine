package com.smitpatel.enigmamachine.ui.main

import android.content.ClipData
import android.content.ClipboardManager
import android.os.Build
import android.os.Bundle
import android.view.ContextMenu
import android.view.MenuItem
import android.view.MotionEvent
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.smitpatel.enigmamachine.R
import com.smitpatel.enigmamachine.databinding.ActivityEnigmaMainBinding
import com.smitpatel.enigmamachine.ui.EnigmaSounds
import com.smitpatel.enigmamachine.ui.SoundEffects
import com.smitpatel.enigmamachine.ui.setting.SettingsFragment
import com.smitpatel.enigmamachine.events.EnigmaEvent
import com.smitpatel.enigmamachine.models.Reflector
import com.smitpatel.enigmamachine.models.Rotor
import com.smitpatel.enigmamachine.ui.RotorPosition
import com.smitpatel.enigmamachine.viewmodels.EnigmaViewModel

class EnigmaMainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityEnigmaMainBinding

    private val viewModel : EnigmaViewModel by viewModels()

    private val sounds : SoundEffects = SoundEffects

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEnigmaMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupViews()
        setupRenderer()
        lifecycle.addObserver(sounds)
        SoundEffects.initialize(context = applicationContext)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        viewModel.handleEvent(EnigmaEvent.RestoreState)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        viewModel.handleEvent(EnigmaEvent.SaveState)
        super.onSaveInstanceState(outState)
    }

    private fun setupRenderer() {
        fun getRotorLabelText(rotor: Rotor.RotorOption): String {
            val rotorOptions = resources.getStringArray(R.array.rotor_options)
            return when (rotor) {
                Rotor.RotorOption.ROTOR_ONE -> rotorOptions[0]
                Rotor.RotorOption.ROTOR_TWO -> rotorOptions[1]
                Rotor.RotorOption.ROTOR_THREE -> rotorOptions[2]
                Rotor.RotorOption.ROTOR_FOUR -> rotorOptions[3]
                Rotor.RotorOption.ROTOR_FIVE -> rotorOptions[4]
            }
        }

        fun showToast(text: String) {
            Toast.makeText(applicationContext, text, Toast.LENGTH_SHORT).show()
            viewModel.handleEvent(EnigmaEvent.ToastMessageDisplayed)
        }

        viewModel.uiState.observe(this) {
            binding.rotors.rotor1.value = it.rotorOnePosition + 1
            binding.rotors.rotor2.value = it.rotorTwoPosition + 1
            binding.rotors.rotor3.value = it.rotorThreePosition + 1

            binding.rotors.rotor1Label.text = getRotorLabelText(it.rotorOneLabel)
            binding.rotors.rotor2Label.text = getRotorLabelText(it.rotorTwoLabel)
            binding.rotors.rotor3Label.text = getRotorLabelText(it.rotorThreeLabel)

            binding.textboxes.textRaw.text = it.rawMessage
            binding.textboxes.textCode.text = it.encodedMessage

            binding.textboxes.scrollview.fullScroll(View.FOCUS_RIGHT)

            val lamps = arrayOf(
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

            lamps.forEach { lamp ->
                lamp.isPressed = false
            }

            if (it.activeLampboard != -1) {
                lamps[it.activeLampboard].isPressed = true
            }

            if (it.showSettingsChangedToast) {
                showToast(text = resources.getString(R.string.settings_changed_toast_message))
                sounds.playSound(sound = EnigmaSounds.CHANGES)
                viewModel.handleEvent(
                    event = EnigmaEvent.ToastMessageDisplayed
                )
            }

            if (it.clipboardCopyState != null) {
                val clipboardManager = getSystemService(CLIPBOARD_SERVICE) as ClipboardManager
                when (val settingsState = it.clipboardCopyState.settingsState) {
                    null -> {
                        clipboardManager.setPrimaryClip(ClipData.newPlainText(
                            "",
                            it.clipboardCopyState.text,
                        ))
                    }
                    else -> {

                        fun getReflectorLabelText(reflector: Reflector) = when (reflector) {
                            Reflector.REFLECTOR_UKW_A -> getString(R.string.reflector_a)
                            Reflector.REFLECTOR_UKW_B -> getString(R.string.reflector_b)
                            Reflector.REFLECTOR_UKW_C -> getString(R.string.reflector_c)
                        }

                        val rotorOptions = "${getRotorLabelText(settingsState.rotorOneLabel)} " +
                                "${getRotorLabelText(settingsState.rotorTwoLabel)} " +
                                getRotorLabelText(settingsState.rotorThreeLabel)

                        val rotorPositions = "${settingsState.rotorOnePosition.numberToLetter()} " +
                                "${settingsState.rotorTwoPosition.numberToLetter()} " +
                                settingsState.rotorThreePosition.numberToLetter()

                        val ringPositions = "${settingsState.rotorOneRing.numberToLetter()} " +
                                "${settingsState.rotorTwoRing.numberToLetter()} " +
                                settingsState.rotorThreeRing.numberToLetter()

                        clipboardManager.setPrimaryClip(ClipData.newPlainText(
                            "",
                            getString(R.string.copy_settings_text,
                                rotorOptions,
                                rotorPositions,
                                ringPositions,
                                getReflectorLabelText(settingsState.reflector),
                                settingsState.plugboardPairs,
                            )
                        ))
                    }
                }

                if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.S_V2) {
                    showToast(getString(R.string.text_copied_toast_message))
                } else {
                    viewModel.handleEvent(EnigmaEvent.ToastMessageDisplayed)
                }
            }
        }
    }

    private fun setupViews() {

        binding.rotors.rotor1.displayedValues = resources.getStringArray(R.array.rotor_values)
        binding.rotors.rotor2.displayedValues = resources.getStringArray(R.array.rotor_values)
        binding.rotors.rotor3.displayedValues = resources.getStringArray(R.array.rotor_values)

        binding.rotors.rotor1.setOnValueChangedListener { _, _, newValue ->
            SoundEffects.playSound(sound = EnigmaSounds.ROTOR)
            viewModel.handleEvent(
                event = EnigmaEvent.RotorStartPositionChanged(
                    start = newValue - 1,
                    rotorPosition = RotorPosition.ONE
                )
            )
        }

        binding.rotors.rotor2.setOnValueChangedListener { _, _, newValue ->
            SoundEffects.playSound(sound = EnigmaSounds.ROTOR)
            viewModel.handleEvent(
                event = EnigmaEvent.RotorStartPositionChanged(
                    start = newValue - 1,
                    rotorPosition = RotorPosition.TWO
                )
            )
        }

        binding.rotors.rotor3.setOnValueChangedListener { _, _, newValue ->
            SoundEffects.playSound(sound = EnigmaSounds.ROTOR)
            viewModel.handleEvent(
                event = EnigmaEvent.RotorStartPositionChanged(
                    start = newValue - 1,
                    rotorPosition = RotorPosition.THREE
                )
            )
        }

        binding.rotors.powerSwitch.setOnClickListener {
            SoundEffects.playSound(sound = EnigmaSounds.DEFAULT)
            SettingsFragment().show(supportFragmentManager, "")
        }

        registerForContextMenu(binding.textboxes.textfields)

        val buttons = arrayOf(
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

        buttons.forEach { button ->
            button.setOnTouchListener { _, event ->
                when (event.action) {
                    MotionEvent.ACTION_DOWN -> {
                        button.isPressed = true
                        button.performClick()
                        when (button.id) {
                            R.id.button_delete -> {
                                SoundEffects.playSound(sound = EnigmaSounds.DELETE)
                                viewModel.handleEvent(
                                    event = EnigmaEvent.InputDeletePressed
                                )
                            }
                            R.id.button_spacebar -> {
                                SoundEffects.playSound(sound = EnigmaSounds.SPACE)
                                viewModel.handleEvent(
                                    event = EnigmaEvent.InputSpacePressed
                                )
                            }
                            else -> {
                                SoundEffects.playSound(sound = EnigmaSounds.KEY)
                                viewModel.handleEvent(
                                    event = EnigmaEvent.InputKeyPressed(
                                        input = button.text[0].letterToNumber()
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
                                    input = button.text[0].letterToNumber()
                                )
                            )
                        }
                    }
                }
                true
            }
        }
    }

    override fun onCreateContextMenu(
        menu: ContextMenu?,
        v: View?,
        menuInfo: ContextMenu.ContextMenuInfo?
    ) {
        super.onCreateContextMenu(menu, v, menuInfo)
        menuInflater.inflate(R.menu.enigma_options_menu, menu)
    }

    override fun onContextItemSelected(item: MenuItem): Boolean =
        when (item.itemId) {
            R.id.copy_raw_text -> {
                viewModel.handleEvent(EnigmaEvent.CopyRawText)
                true
            }
            R.id.copy_encoded_text -> {
                viewModel.handleEvent(EnigmaEvent.CopyEncodedText)
                true
            }
            R.id.copy_enigma_settings -> {
                viewModel.handleEvent(EnigmaEvent.CopySettings)
                true
            }
            else -> super.onContextItemSelected(item)
        }


    private fun Char.letterToNumber() = this.code - 65

    private fun Int.numberToLetter() = Char(this + 65)

}