package com.example.thecaloriewizard.accountcreationlogin.fragments

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.NumberPicker
import android.widget.Switch
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.example.thecaloriewizard.R
import com.example.thecaloriewizard.accountcreationlogin.viewmodels.AccountCreationViewModel
import com.example.thecaloriewizard.dataclasses.UserData
import com.google.android.material.textfield.TextInputEditText

/**
 * [AboutFragment]
 * Description: Fragment responsible for collecting user's personal information such as age, height, weight, and goals.
 *
 * [Author]
 * Author Name: Brandon Sharp
 *
 * [Parameters/Arguments]
 * None
 *
 * [Return Value]
 * None
 *
 * [Usage]
 * This fragment is used within the account creation process to collect information about the user's profile.
 * It allows the user to input their age, height, weight, and goal weight.
 *
 * [Notes]
 * - This fragment communicates with the AccountCreationViewModel to update user data.
 * - It also interacts with the parent activity to notify about the validation status of the information collected.
 */
class AboutFragment : Fragment() {
    private lateinit var userData: UserData
    private val viewModel: AccountCreationViewModel by activityViewModels()

    interface AboutFragmentListener {
        fun onAboutInfoValidated(isValid: Boolean)
    }

    private var listener: AboutFragmentListener? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        listener = if (context is AboutFragmentListener) {
            context
        } else {
            throw RuntimeException("$context must implement AboutFragmentListener")
        }
    }

    override fun onDetach() {
        super.onDetach()
        listener = null
    }

    /**
     * [showNumberPickerDialog]
     * Description: Shows a number picker dialog to select a numeric value within a specified range.
     *
     * [Parameters/Arguments]
     * - [context]: The context in which the dialog should be shown.
     * - [title]: The title of the dialog.
     * - [minValue]: The minimum value allowed to be selected.
     * - [maxValue]: The maximum value allowed to be selected.
     * - [currentValue]: The current value set in the number picker.
     * - [onValueSelected]: Callback function invoked when a value is selected in the number picker.
     *
     * [Return Value]
     * None
     *
     * [Usage]
     * This method is used to display a number picker dialog to allow the user to select a numeric value within a specified range.
     * The selected value is returned via the callback function.
     */
    private fun showNumberPickerDialog(context: Context, title: String, minValue: Int, maxValue: Int, currentValue: Int, onValueSelected: (Int) -> Unit) {
        val numberPicker = NumberPicker(context).apply {
            this.minValue = minValue
            this.maxValue = maxValue
            this.value = currentValue
        }

        AlertDialog.Builder(context).apply {
            setTitle(title)
            setView(numberPicker)
            setPositiveButton(android.R.string.ok) { _, _ ->
                onValueSelected(numberPicker.value)
            }
            setNegativeButton(android.R.string.cancel, null)
        }.show()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        userData = UserData()
        return inflater.inflate(R.layout.fragment_about, container, false)
    }

    @SuppressLint("UseSwitchCompatOrMaterialCode")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val toolbar: Toolbar = view.findViewById(R.id.toolbar)
        toolbar.title = "About You"
        toolbar.setNavigationOnClickListener {
            requireActivity().onBackPressed()
        }

        val sexSwitch: Switch = view.findViewById(R.id.sexSwitch)
        sexSwitch.setOnCheckedChangeListener { _, isChecked ->
            viewModel.userData.sex = if (isChecked) "Female" else "Male"
            validateAboutInfo()

            Log.d("AboutFragment", "Updated ViewModel with sex: ${viewModel.userData.sex}")
        }

        val ageInput: TextInputEditText = view.findViewById(R.id.ageInput)
        ageInput.setOnClickListener {
            val currentValue = userData.age
            showNumberPickerDialog(requireContext(), "Select Age", 13, 100, currentValue) { selectedValue ->
                viewModel.userData.age = selectedValue
                ageInput.setText("$selectedValue")

                validateAboutInfo()
                Log.d("AboutFragment", "Updated ViewModel with age: ${viewModel.userData.age}")
            }
        }

        val heightFeetInput: TextInputEditText = view.findViewById(R.id.heightFeet)
        heightFeetInput.setOnClickListener {
            val currentValue = userData.heightFeet
            showNumberPickerDialog(requireContext(), "Select Feet", 3, 8, currentValue) { selectedValue ->
                viewModel.userData.heightFeet = selectedValue
                heightFeetInput.setText("$selectedValue")

                validateAboutInfo()
                Log.d("AboutFragment", "Updated ViewModel with height(ft): ${viewModel.userData.heightFeet}")
            }
        }

        val heightInchesInput: TextInputEditText = view.findViewById(R.id.heightInches)
        heightInchesInput.setOnClickListener {
            val currentValue = userData.heightInches
            showNumberPickerDialog(requireContext(), "Select Inches", 0, 11, currentValue) { selectedValue ->
                viewModel.userData.heightInches = selectedValue
                heightInchesInput.setText("$selectedValue")

                validateAboutInfo()
                Log.d("AboutFragment", "Updated ViewModel with height(in): ${viewModel.userData.heightInches}")
            }
        }

        val weightStoneInput: TextInputEditText = view.findViewById(R.id.weightStone)
        weightStoneInput.setOnClickListener {
            val currentValue = userData.weightStone
            showNumberPickerDialog(requireContext(), "Select Stone", 2, 40, currentValue) { selectedValue ->
                viewModel.userData.weightStone = selectedValue
                weightStoneInput.setText("$selectedValue")

                validateAboutInfo()
                Log.d("AboutFragment", "Updated ViewModel with weight(st): ${viewModel.userData.weightStone}")
            }
        }

        val weightPoundsInput: TextInputEditText = view.findViewById(R.id.weightPounds)
        weightPoundsInput.setOnClickListener {
            val currentValue = userData.weightPounds
            showNumberPickerDialog(requireContext(), "Select Pounds", 0, 13, currentValue) { selectedValue ->
                viewModel.userData.weightPounds = selectedValue
                weightPoundsInput.setText("$selectedValue")

                validateAboutInfo()
                Log.d("AboutFragment", "Updated ViewModel with weight(lb): ${viewModel.userData.weightPounds}")
            }
        }

        val goalWeightStoneInput: TextInputEditText = view.findViewById(R.id.goalWeightStone)
        goalWeightStoneInput.setOnClickListener {
            val currentValue = userData.goalWeightStone
            showNumberPickerDialog(requireContext(), "Select Stone", 0, 30, currentValue) { selectedValue ->
                viewModel.userData.goalWeightStone = selectedValue
                goalWeightStoneInput.setText("$selectedValue")

                validateAboutInfo()
                Log.d("AboutFragment", "Updated ViewModel with goal weight(st): ${viewModel.userData.goalWeightStone}")
            }
        }

        val goalWeightPoundsInput: TextInputEditText = view.findViewById(R.id.goalWeightPounds)
        goalWeightPoundsInput.setOnClickListener {
            val currentValue = userData.goalWeightPounds
            showNumberPickerDialog(requireContext(), "Select Pounds", 0, 13, currentValue) { selectedValue ->
                viewModel.userData.goalWeightPounds = selectedValue
                goalWeightPoundsInput.setText("$selectedValue")

                validateAboutInfo()
                Log.d("AboutFragment", "Updated ViewModel with goal weight(lb): ${viewModel.userData.goalWeightPounds}")
            }
        }
        listener?.onAboutInfoValidated(false)
    }

    /**
     * [validateAboutInfo]
     * Description: Validates the user's about information and notifies the parent activity about the validation status.
     *
     * [Parameters/Arguments]
     * None
     *
     * [Return Value]
     * None
     *
     * [Usage]
     * This method is called to validate the user's entered about information.
     * It checks if the age, height, weight, and goal weight are within acceptable ranges.
     * It then notifies the parent activity about the overall validation status.
     */
    private fun validateAboutInfo() {
        val isAgeValid = viewModel.userData.age in 13..100
        val isHeightValid = viewModel.userData.heightFeet in 3..8 && viewModel.userData.heightInches in 0..11
        val isWeightValid = viewModel.userData.weightStone in 2..40 && viewModel.userData.weightPounds in 0..13
        val isGoalWeightValid = viewModel.userData.goalWeightStone in 2..40 && viewModel.userData.goalWeightPounds in 0..13

        Log.d("AboutFragment", "Age valid: $isAgeValid")
        Log.d("AboutFragment", "Height valid: $isHeightValid")
        Log.d("AboutFragment", "Weight valid: $isWeightValid")
        Log.d("AboutFragment", "Goal Weight valid: $isGoalWeightValid")

        val isValid = isAgeValid && isHeightValid && isWeightValid && isGoalWeightValid

        Log.d("AboutFragment", "Overall Validation status: $isValid")

        listener?.onAboutInfoValidated(isValid)
    }
}
