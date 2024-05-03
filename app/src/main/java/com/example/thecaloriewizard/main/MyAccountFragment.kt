package com.example.thecaloriewizard.main

import android.content.Context
import android.os.Bundle
import android.util.Base64
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.thecaloriewizard.R
import com.example.thecaloriewizard.database.DBHelper
import com.example.thecaloriewizard.dataclasses.UserData
import com.example.thecaloriewizard.encryption.EncryptionManager
import com.google.android.material.textfield.TextInputEditText

/**
 * MyAccountFragment
 * Description: Fragment responsible for managing user account information.
 *
 * [Author]
 * Author Name: Brandon Sharp
 *
 * [Parameters/Arguments]
 * None
 *
 * [Usage]
 * Handles updating and validating user account information.
 *
 * [Notes]
 * None
 */
class MyAccountFragment : Fragment() {
    private lateinit var emailEditText: TextInputEditText
    private lateinit var passwordEditText: TextInputEditText
    private lateinit var phoneEditText: TextInputEditText
    private lateinit var ageEditText: TextInputEditText
    private lateinit var goalWeightStoneEditText: TextInputEditText
    private lateinit var goalWeightPoundsEditText: TextInputEditText
    private lateinit var updateAccountButton: Button
    private lateinit var dbHelper: DBHelper

    /**
     * onCreateView
     * Description: Called to create the view hierarchy associated with the fragment.
     *
     * [Parameters/Arguments]
     * - inflater: The LayoutInflater object that can be used to inflate any views in the fragment,
     * - container: If non-null, this is the parent view that the fragment's UI should be attached to.
     * - savedInstanceState: If non-null, this fragment is being re-constructed from a previous saved state as given here.
     *
     * [Return Value]
     * View: Return the View for the fragment's UI, or null.
     *
     * [Notes]
     * None
     */
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_my_account, container, false)
    }

    /**
     * onViewCreated
     * Description: Called immediately after onCreateView() has returned, but before any saved state has been restored in to the view.
     *
     * [Parameters/Arguments]
     * - view: The View returned by onCreateView().
     * - savedInstanceState: If non-null, this fragment is being re-constructed from a previous saved state as given here.
     *
     * [Return Value]
     * None
     *
     * [Notes]
     * None
     */
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        dbHelper = DBHelper(requireContext())
        emailEditText = view.findViewById(R.id.emailEditText)
        passwordEditText = view.findViewById(R.id.passwordEditText)
        phoneEditText = view.findViewById(R.id.phoneEditText)
        ageEditText = view.findViewById(R.id.ageEditText)
        goalWeightStoneEditText = view.findViewById(R.id.goalWeightStoneEditText)
        goalWeightPoundsEditText = view.findViewById(R.id.goalWeightPoundsEditText)
        updateAccountButton = view.findViewById(R.id.updateAccountButton)

        populateAccountInfo()
        setupUpdateButton()
    }

    /**
     * populateAccountInfo
     * Description: Populate user account information into respective input fields.
     *
     * [Parameters/Arguments]
     * None
     *
     * [Return Value]
     * None
     *
     * [Notes]
     * None
     */
    private fun populateAccountInfo() {
        val userId = getUserIdFromPrefs()
        dbHelper.getUserByIdFull(userId.toString())?.let { userData ->
            emailEditText.setText(userData.email)
            passwordEditText.setText(EncryptionManager.decrypt(Base64.decode(userData.password, Base64.DEFAULT)))
            phoneEditText.setText(userData.phoneNumber)
            ageEditText.setText(userData.age.toString())
            goalWeightStoneEditText.setText(userData.goalWeightStone.toString())
            goalWeightPoundsEditText.setText(userData.goalWeightPounds.toString())
        }
    }

    /**
     * setupUpdateButton
     * Description: Set up functionality for the update account button.
     *
     * [Parameters/Arguments]
     * None
     *
     * [Return Value]
     * None
     *
     * [Notes]
     * None
     */
    private fun setupUpdateButton() {
        updateAccountButton.setOnClickListener {
            if (validateAccountInfo()) {
                updateAccount()
            }
        }
    }

    /**
     * validateAccountInfo
     * Description: Validate user account information.
     *
     * [Parameters/Arguments]
     * None
     *
     * [Return Value]
     * Boolean: True if all account information is valid, false otherwise.
     *
     * [Notes]
     * None
     */
    private fun validateAccountInfo(): Boolean {
        val email = emailEditText.text.toString().trim()
        val password = passwordEditText.text.toString().trim()
        val phone = phoneEditText.text.toString().trim()
        val age = ageEditText.text.toString().trim()
        val goalWeightStone = goalWeightStoneEditText.text.toString().trim()
        val goalWeightPounds = goalWeightPoundsEditText.text.toString().trim()

        return when {
            !isValidEmail(email) -> false.also { Toast.makeText(context, "Invalid email format", Toast.LENGTH_SHORT).show() }
            !isValidPassword(password) -> false.also { Toast.makeText(context, "Invalid password format", Toast.LENGTH_SHORT).show() }
            !isValidPhone(phone) -> false.also { Toast.makeText(context, "Invalid phone format", Toast.LENGTH_SHORT).show() }
            !isValidAge(age) -> false.also { Toast.makeText(context, "Invalid age", Toast.LENGTH_SHORT).show() }
            !isValidWeight(goalWeightStone, goalWeightPounds) -> false.also { Toast.makeText(context, "Invalid goal weight", Toast.LENGTH_SHORT).show() }
            else -> true
        }
    }

    /**
     * validateAccountInfo
     * Description: Validate user account information.
     *
     * [Parameters/Arguments]
     * None
     *
     * [Return Value]
     * Boolean: True if all account information is valid, false otherwise.
     *
     * [Notes]
     * None
     */
    private fun updateAccount() {
        val newEmail = emailEditText.text.toString().trim()
        val newPassword = EncryptionManager.encrypt(passwordEditText.text.toString().trim())
        val newPhone = phoneEditText.text.toString().trim()
        val newAge = ageEditText.text.toString().trim().toInt()
        val newGoalWeightStone = goalWeightStoneEditText.text.toString().trim().toInt()
        val newGoalWeightPounds = goalWeightPoundsEditText.text.toString().trim().toInt()
        val userId = getUserIdFromPrefs()

        val userData = UserData(
            id = userId.toString(),
            email = newEmail,
            password = Base64.encodeToString(newPassword, Base64.DEFAULT),
            phoneNumber = newPhone,
            age = newAge,
            goalWeightStone = newGoalWeightStone,
            goalWeightPounds = newGoalWeightPounds
        )

        if (dbHelper.updateUser(userData)) {
            Toast.makeText(context, "Account updated successfully", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(context, "Failed to update account", Toast.LENGTH_SHORT).show()
        }
    }

    /**
     * getUserIdFromPrefs
     * Description: Retrieve user ID from shared preferences.
     *
     * [Parameters/Arguments]
     * None
     *
     * [Return Value]
     * Int: User ID
     *
     * [Notes]
     * None
     */
    private fun getUserIdFromPrefs(): Int {
        val sharedPrefs = requireActivity().getSharedPreferences("AppPrefs", Context.MODE_PRIVATE)
        return sharedPrefs.getInt("CurrentUserID", -1)
    }

    /**
     * isValidEmail
     * Description: Validate email format.
     *
     * [Parameters/Arguments]
     * - email: Email string to be validated.
     *
     * [Return Value]
     * Boolean: True if email is valid, false otherwise.
     *
     * [Notes]
     * None
     */
    private fun isValidEmail(email: String): Boolean {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    /**
     * isValidPassword
     * Description: Validate password format.
     *
     * [Parameters/Arguments]
     * - password: Password string to be validated.
     *
     * [Return Value]
     * Boolean: True if password is valid, false otherwise.
     *
     * [Notes]
     * None
     */
    private fun isValidPassword(password: String): Boolean {
        val passwordPattern = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[!@#$%^&+=])(?=\\S+$).{8,16}$"
        return password.matches(passwordPattern.toRegex())
    }

    /**
     * isValidPhone
     * Description: Validate phone format.
     *
     * [Parameters/Arguments]
     * - phone: Phone string to be validated.
     *
     * [Return Value]
     * Boolean: True if phone is valid, false otherwise.
     *
     * [Notes]
     * None
     */
    private fun isValidPhone(phone: String): Boolean {
        val phonePattern = "^07\\d{9}$"
        return phone.matches(phonePattern.toRegex())
    }

    /**
     * isValidAge
     * Description: Validate age format.
     *
     * [Parameters/Arguments]
     * - age: Age string to be validated.
     *
     * [Return Value]
     * Boolean: True if age is valid, false otherwise.
     *
     * [Notes]
     * None
     */
    private fun isValidAge(age: String): Boolean {
        return age.toIntOrNull() in 13..99
    }

    /**
     * isValidWeight
     * Description: Validate weight format.
     *
     * [Parameters/Arguments]
     * - stone: Stone part of weight.
     * - pounds: Pound part of weight.
     *
     * [Return Value]
     * Boolean: True if weight is valid, false otherwise.
     *
     * [Notes]
     * None
     */
    private fun isValidWeight(stone: String, pounds: String): Boolean {
        return stone.toIntOrNull() != null && pounds.toIntOrNull() in 0..13
    }

}