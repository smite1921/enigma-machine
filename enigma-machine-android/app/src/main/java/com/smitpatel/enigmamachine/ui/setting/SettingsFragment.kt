package com.smitpatel.enigmamachine.ui.setting

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager.LayoutParams
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.Spinner
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import com.smitpatel.enigmamachine.R
import com.smitpatel.enigmamachine.databinding.FragmentSettingsBinding
import com.smitpatel.enigmamachine.events.EnigmaEvent
import com.smitpatel.enigmamachine.models.Reflector
import com.smitpatel.enigmamachine.models.Rotor
import com.smitpatel.enigmamachine.ui.SoundEffects
import com.smitpatel.enigmamachine.events.SettingEvent
import com.smitpatel.enigmamachine.ui.RotorPosition
import com.smitpatel.enigmamachine.viewmodels.EnigmaViewModel
import com.smitpatel.enigmamachine.viewmodels.SettingViewModel
import com.smitpatel.enigmamachine.views.Plugboard

class SettingsFragment : DialogFragment() {

    private var _binding: FragmentSettingsBinding? = null

    // Note: This property is only valid between onCreateView and onDestroyView.
    private val binding get() = _binding!!

    private val viewModel : SettingViewModel by viewModels()

    private val reflectorRadioGroup : RadioGroup by lazy {
        binding.reflectorOption
    }

    private val reflectorUkwaRadioButton : RadioButton by lazy {
        binding.reflectorUkwa
    }

    private val reflectorUkwbRadioButton : RadioButton by lazy {
        binding.reflectorUkwb
    }

    private val reflectorUkwcRadioButton : RadioButton by lazy {
        binding.reflectorUkwc
    }

    private val rotorOneSpinner : Spinner by lazy {
        binding.rotor1Option
    }

    private val rotorTwoSpinner : Spinner by lazy {
        binding.rotor2Option
    }

    private val rotorThreeSpinner : Spinner by lazy {
        binding.rotor3Option
    }

    private val positionOneSpinner : Spinner by lazy {
        binding.position1Option
    }

    private val positionTwoSpinner : Spinner by lazy {
        binding.position2Option
    }

    private val positionThreeSpinner : Spinner by lazy {
        binding.position3Option
    }

    private val ringOneSpinner : Spinner by lazy {
        binding.ring1Option
    }

    private val ringTwoSpinner : Spinner by lazy {
        binding.ring2Option
    }

    private val ringThreeSpinner : Spinner by lazy {
        binding.ring3Option
    }

    private val plugboard : Plugboard by lazy {
        binding.plugboard
    }

    private val closeButton : Button by lazy {
        binding.close
    }

    private fun setupInitialValues() {
        viewModel.initialUiState.let {

            binding.muteSwitch.isChecked = !it.muteOption

            when (it.reflectorOption) {
                Reflector.REFLECTOR_UKW_A -> reflectorRadioGroup.check(reflectorUkwaRadioButton.id)
                Reflector.REFLECTOR_UKW_B -> reflectorRadioGroup.check(reflectorUkwbRadioButton.id)
                Reflector.REFLECTOR_UKW_C -> reflectorRadioGroup.check(reflectorUkwcRadioButton.id)
            }

            arrayOf(it.rotorOneOption, it.rotorTwoOption, it.rotorThreeOption).forEachIndexed { index, rotorOption ->

                val rotorSpinner = when (index) {
                    0 -> rotorOneSpinner
                    1 -> rotorTwoSpinner
                    2 -> rotorThreeSpinner
                    else -> rotorThreeSpinner
                }

                rotorSpinner.setSelection(
                    when (rotorOption) {
                        Rotor.RotorOption.ROTOR_ONE -> 0
                        Rotor.RotorOption.ROTOR_TWO -> 1
                        Rotor.RotorOption.ROTOR_THREE -> 2
                        Rotor.RotorOption.ROTOR_FOUR -> 3
                        Rotor.RotorOption.ROTOR_FIVE -> 4
                    }
                )
            }

            positionOneSpinner.setSelection(it.rotorOnePosition)
            positionTwoSpinner.setSelection(it.rotorTwoPosition)
            positionThreeSpinner.setSelection(it.rotorThreePosition)

            ringOneSpinner.setSelection(it.ringOneOption)
            ringTwoSpinner.setSelection(it.ringTwoOption)
            ringThreeSpinner.setSelection(it.ringThreeOption)

            it.plugboardPairs.forEach { pair ->
                plugboard.addPair(pair.first, pair.second)
            }
        }
    }

    private fun setupViews() {

        binding.muteSwitch.setOnCheckedChangeListener { _, isChecked ->
            viewModel.handleEvent(
                event = SettingEvent.MuteSwitchToggled(
                    muteStatus = !isChecked
                )
            )
        }

        reflectorRadioGroup.setOnCheckedChangeListener { _, checkedId ->
            when (checkedId) {
                reflectorUkwaRadioButton.id -> viewModel.handleEvent(
                    event = SettingEvent.ReflectorOptionSelected(
                        reflector = Reflector.REFLECTOR_UKW_A
                    )
                )
                reflectorUkwbRadioButton.id -> viewModel.handleEvent(
                    event = SettingEvent.ReflectorOptionSelected(
                        reflector = Reflector.REFLECTOR_UKW_B
                    )
                )
                reflectorUkwcRadioButton.id -> viewModel.handleEvent(
                    event = SettingEvent.ReflectorOptionSelected(
                        reflector = Reflector.REFLECTOR_UKW_C
                    )
                )
            }
        }

        val rotorSpinnerListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {

                val selectedRotor = when (position) {
                    0 -> Rotor.RotorOption.ROTOR_ONE
                    1 -> Rotor.RotorOption.ROTOR_TWO
                    2 -> Rotor.RotorOption.ROTOR_THREE
                    3 -> Rotor.RotorOption.ROTOR_FOUR
                    4 -> Rotor.RotorOption.ROTOR_FIVE
                    // to make expression exhaustive
                    else -> Rotor.RotorOption.ROTOR_ONE
                }

                when (parent?.id) {
                    rotorOneSpinner.id -> viewModel.handleEvent(
                        event = SettingEvent.RotorOptionSelected(
                            rotor = selectedRotor,
                            ring = binding.ring1Option.selectedItemPosition,
                                    position = binding.position1Option.selectedItemPosition,
                            rotorPosition = RotorPosition.ONE,
                        )
                    )
                    rotorTwoSpinner.id -> viewModel.handleEvent(
                        event = SettingEvent.RotorOptionSelected(
                            rotor = selectedRotor,
                            ring = binding.ring2Option.selectedItemPosition,
                            position = binding.position2Option.selectedItemPosition,
                            rotorPosition = RotorPosition.TWO,
                        )
                    )
                    rotorThreeSpinner.id -> viewModel.handleEvent(
                        event = SettingEvent.RotorOptionSelected(
                            rotor = selectedRotor,
                            ring = binding.ring3Option.selectedItemPosition,
                            position = binding.position3Option.selectedItemPosition,
                            rotorPosition = RotorPosition.THREE,
                        )
                    )
                }
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
                // do nothing
            }

        }
        rotorOneSpinner.onItemSelectedListener = rotorSpinnerListener
        rotorTwoSpinner.onItemSelectedListener = rotorSpinnerListener
        rotorThreeSpinner.onItemSelectedListener = rotorSpinnerListener

        val ringSpinnerListener = object : AdapterView.OnItemSelectedListener {

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {

                when (parent?.id) {
                    ringOneSpinner.id -> viewModel.handleEvent(
                        event = SettingEvent.RingOptionSelected(
                            ring = position,
                            rotorPosition = RotorPosition.ONE,
                        )
                    )
                    ringTwoSpinner.id -> viewModel.handleEvent(
                        event = SettingEvent.RingOptionSelected(
                            ring = position,
                            rotorPosition = RotorPosition.TWO,
                        )
                    )
                    ringThreeSpinner.id -> viewModel.handleEvent(
                        event = SettingEvent.RingOptionSelected(
                            ring = position,
                            rotorPosition = RotorPosition.THREE,
                        )
                    )
                }
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
                // do nothing
            }
        }
        ringOneSpinner.onItemSelectedListener = ringSpinnerListener
        ringTwoSpinner.onItemSelectedListener = ringSpinnerListener
        ringThreeSpinner.onItemSelectedListener = ringSpinnerListener

        val positionSpinnerListener = object : AdapterView.OnItemSelectedListener {

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {

                when (parent?.id) {
                    positionOneSpinner.id -> viewModel.handleEvent(
                        event = SettingEvent.PositionOptionSelected(
                            position = position,
                            rotorPosition = RotorPosition.ONE,
                        )
                    )
                    positionTwoSpinner.id -> viewModel.handleEvent(
                        event = SettingEvent.PositionOptionSelected(
                            position = position,
                            rotorPosition = RotorPosition.TWO,
                        )
                    )
                    positionThreeSpinner.id -> viewModel.handleEvent(
                        event = SettingEvent.PositionOptionSelected(
                            position = position,
                            rotorPosition = RotorPosition.THREE,
                        )
                    )
                }
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
                // do nothing
            }
        }
        positionOneSpinner.onItemSelectedListener = positionSpinnerListener
        positionTwoSpinner.onItemSelectedListener = positionSpinnerListener
        positionThreeSpinner.onItemSelectedListener = positionSpinnerListener

        plugboard.onPairAddedListener = {
            viewModel.handleEvent(event = SettingEvent.PlugboardPairAdded(pair = it))
        }

        plugboard.onPairRemovedListener = {
            viewModel.handleEvent(event = SettingEvent.PlugboardPairRemoved(pair = it))
        }

        context?.let {
            ArrayAdapter.createFromResource(it, R.array.rotor_options, android.R.layout.simple_spinner_item).also { adapter ->
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                rotorOneSpinner.adapter = adapter
                rotorTwoSpinner.adapter = adapter
                rotorThreeSpinner.adapter = adapter
            }

            ArrayAdapter.createFromResource(it, R.array.rotor_values, android.R.layout.simple_spinner_item).also { adapter ->
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                positionOneSpinner.adapter = adapter
                positionTwoSpinner.adapter = adapter
                positionThreeSpinner.adapter = adapter
                ringOneSpinner.adapter = adapter
                ringTwoSpinner.adapter = adapter
                ringThreeSpinner.adapter = adapter
            }
        }

        closeButton.setOnClickListener {
            val enigmaViewModel : EnigmaViewModel by activityViewModels()
            enigmaViewModel.handleEvent(
                event = EnigmaEvent.SettingMenuClosed(
                    didSettingsChanged = viewModel.didSettingsChange
                )
            )
            dialog?.dismiss()
        }
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSettingsBinding.inflate(inflater, container, false)
        setupViews()
        setupInitialValues()
        return binding.root
    }

    override fun onStart() {
        super.onStart()
        dialog?.window?.setLayout(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}