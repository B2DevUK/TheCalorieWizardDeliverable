package com.example.thecaloriewizard.accountcreationlogin.fragments

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.example.thecaloriewizard.R
import com.example.thecaloriewizard.accountcreationlogin.viewmodels.AccountCreationViewModel
import com.google.android.material.textfield.TextInputEditText

/**
 * [CreateAccountFragment]
 * Description: Fragment responsible for collecting user account creation information, such as email, password, and phone number.
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
 * This fragment is used within the account creation process to collect user account information.
 * It provides input fields for the user to enter their email, password, and phone number.
 * It also performs validation on the entered information and notifies the parent activity about the validation status.
 *
 * [Notes]
 * - This fragment communicates with the AccountCreationViewModel to update user data with the entered account information.
 * - It utilizes text change listeners to validate the entered information dynamically as the user types.
 */
class CreateAccountFragment : Fragment() {
    private val viewModel: AccountCreationViewModel by activityViewModels()

    interface CreateAccountFragmentListener {
        fun onCreateAccountInfoValidated(isValid: Boolean)
    }

    private var listener: CreateAccountFragmentListener? = null

    private lateinit var emailEditText: TextInputEditText
    private lateinit var passwordEditText: TextInputEditText
    private lateinit var phoneEditText: TextInputEditText

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is CreateAccountFragmentListener) {
            listener = context
        } else {
            throw RuntimeException("$context must implement CreateAccountFragmentListener")
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_create_account, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Initialize UI components and perform initial validation
        emailEditText = view.findViewById(R.id.emailEditText)
        passwordEditText = view.findViewById(R.id.passwordEditText)
        phoneEditText = view.findViewById(R.id.phoneEditText)
        validateAccountInfo()

        // Setup text change listeners to validate input dynamically
        setupTextChangeListeners()
    }

    // Function to set up text change listeners on input fields
    private fun setupTextChangeListeners() {
        // Define a TextWatcher for each input field
        val textWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable?) {
                // Update ViewModel with the entered information and validate
                viewModel.userData.email = emailEditText.text.toString()
                viewModel.userData.password = passwordEditText.text.toString()
                viewModel.userData.phoneNumber = phoneEditText.text.toString()
                validateAccountInfo()
            }
        }

        // Attach the TextWatcher to each input field
        emailEditText.addTextChangedListener(textWatcher)
        passwordEditText.addTextChangedListener(textWatcher)
        phoneEditText.addTextChangedListener(textWatcher)
    }

    // Function to perform validation on account information
    private fun validateAccountInfo() {
        // Validate email, password, and phone number
        val isEmailValid = isValidEmail(emailEditText.text.toString())
        val isPasswordValid = isValidPassword(passwordEditText.text.toString())
        val isPhoneValid = isValidPhone(phoneEditText.text.toString())

        // Log validation status for each field
        Log.d("CreateAccountFragment", "Email valid: $isEmailValid")
        Log.d("CreateAccountFragment", "Password valid: $isPasswordValid")
        Log.d("CreateAccountFragment", "Phone valid: $isPhoneValid")

        // Check overall validation status
        val isValid = isEmailValid && isPasswordValid && isPhoneValid
        Log.d("CreateAccountFragment", "Overall Validation status: $isValid")

        // Notify the parent activity about the validation status
        listener?.onCreateAccountInfoValidated(isValid)
    }

    // Function to validate email format
    private fun isValidEmail(email: String): Boolean {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    // Function to validate password format
    private fun isValidPassword(password: String): Boolean {
        val passwordPattern = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[!@#$%^&+=])(?=\\S+$).{8,16}$"
        return password.matches(passwordPattern.toRegex())
    }

    // Function to validate phone number format
    private fun isValidPhone(phone: String): Boolean {
        val phonePattern = "^07\\d{9}$"
        return phone.matches(phonePattern.toRegex())
    }

    override fun onDetach() {
        super.onDetach()
        listener = null
    }
}