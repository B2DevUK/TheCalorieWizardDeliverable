package com.example.thecaloriewizard.accountcreationlogin

import android.content.Intent
import android.os.Bundle
import android.util.Base64
import android.util.Log
import android.widget.Button
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2
import com.example.thecaloriewizard.R
import com.example.thecaloriewizard.accountcreationlogin.adapters.AccountCreationAdapter
import com.example.thecaloriewizard.accountcreationlogin.fragments.AboutFragment
import com.example.thecaloriewizard.accountcreationlogin.fragments.ActivityFragment
import com.example.thecaloriewizard.accountcreationlogin.fragments.BarriersFragment
import com.example.thecaloriewizard.accountcreationlogin.fragments.CreateAccountFragment
import com.example.thecaloriewizard.accountcreationlogin.fragments.GoalsFragment
import com.example.thecaloriewizard.accountcreationlogin.fragments.WeeklyGoalsFragment
import com.example.thecaloriewizard.accountcreationlogin.viewmodels.AccountCreationViewModel
import com.example.thecaloriewizard.database.DBHelper
import com.example.thecaloriewizard.encryption.EncryptionManager
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class AccountCreationActivity : AppCompatActivity(),
    GoalsFragment.GoalsFragmentListener,
    BarriersFragment.BarriersFragmentListener,
    ActivityFragment.ActivityFragmentListener,
    AboutFragment.AboutFragmentListener,
    WeeklyGoalsFragment.WeeklyGoalsFragmentListener,
    CreateAccountFragment.CreateAccountFragmentListener {

    private lateinit var dbHelper: DBHelper
    private val viewModel: AccountCreationViewModel by viewModels()
    private lateinit var viewPager: ViewPager2

    /**
     * [onCreate]
     * Description: Lifecycle method called when the activity is starting. Initializes the activity,
     * sets the content view, initializes the database helper, sets up the viewpager and button click listener.
     *
     * [Parameters/Arguments]
     * - [savedInstanceState]: The previously saved instance state, if available.
     *
     * [Return Value]
     * None
     *
     * [Usage]
     * This method is called when the activity is created, typically during the app startup process.
     * It sets up the necessary components for the account creation process.
     *
     * [Notes]
     * - This method initializes various components of the activity, including the database helper,
     *   viewpager, and button click listener.
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        EncryptionManager.initialize(this)
        setContentView(R.layout.activity_accountcreation)

        dbHelper = DBHelper(this)

        viewPager = findViewById(R.id.viewpager)
        viewPager.adapter = AccountCreationAdapter(this)

        val btnNext: Button = findViewById(R.id.btnNext)
        btnNext.setOnClickListener {
            val currentItem = viewPager.currentItem
            val isLastPage = currentItem == viewPager.adapter!!.itemCount - 1

            if (isLastPage) {
                createUserAccount()

                navigateToLoginPage()
            } else {
                viewPager.currentItem = currentItem + 1
            }
        }

    }

    /**
     * [createUserAccount]
     * Description: Creates a user account using data from the view model, encrypts the password,
     * serializes lists, and inserts user data into the database.
     *
     * [Parameters/Arguments]
     * None
     *
     * [Return Value]
     * None
     *
     * [Usage]
     * This method is called when the user proceeds to the final step of the account creation process.
     * It encrypts the password, serializes lists, and inserts user data into the database.
     *
     * [Notes]
     * - The method relies on the view model to access user data.
     */
    private fun createUserAccount() {
        val userData = viewModel.userData
        Log.d("AccountCreationActivity", "Creating user account with: $userData")

        try {
            val encryptedPassword = EncryptionManager.encrypt(userData.password)
            userData.password = Base64.encodeToString(encryptedPassword, Base64.DEFAULT)
        } catch (e: Exception) {
            Log.e("AccountCreationActivity", "Error encrypting password", e)
            return
        }

        userData.goals = serializeList(viewModel.userData.goals)
        userData.weeklyGoals = serializeList(viewModel.userData.weeklyGoals)
        userData.barriers = serializeList(viewModel.userData.barriers)

        dbHelper.addUser(userData)

        navigateToLoginPage()
    }

    /**
     * [navigateToLoginPage]
     * Description: Navigates to the login page.
     *
     * [Parameters/Arguments]
     * None
     *
     * [Return Value]
     * None
     *
     * [Usage]
     * This method is called after the user account is created successfully to navigate to the login page.
     *
     * [Notes]
     * - This method finishes the current activity and starts the login activity.
     */
    private fun navigateToLoginPage() {
        val intent = Intent(this, LoginPage::class.java)
        startActivity(intent)
        finish()
    }

    /**
     * [onGoalsSelectionChanged]
     * Description: Callback method called when the user changes their selection in the GoalsFragment.
     * Enables or disables the "Next" button based on the validity of the selection.
     *
     * [Parameters/Arguments]
     * - [isValidSelection]: Boolean indicating whether the selection is valid.
     *
     * [Return Value]
     * None
     *
     * [Usage]
     * This method is called when the user changes their selection in the GoalsFragment.
     * It updates the state of the "Next" button based on the validity of the selection.
     *
     * [Notes]
     * - This method is a callback from the GoalsFragment.
     */
    override fun onGoalsSelectionChanged(isValidSelection: Boolean) {
        val btnNext: Button = findViewById(R.id.btnNext)
        Log.d("AccountCreationActivity", "onGoalsSelectionChanged called with isValidSelection = $isValidSelection")
        btnNext.isEnabled = isValidSelection
    }

    /**
     * [onBarriersSelectionChanged]
     * Description: Callback method called when the user changes their selection in the BarriersFragment.
     * Enables or disables the "Next" button based on the validity of the selection.
     *
     * [Parameters/Arguments]
     * - [isValidSelection]: Boolean indicating whether the selection is valid.
     *
     * [Return Value]
     * None
     *
     * [Usage]
     * This method is called when the user changes their selection in the BarriersFragment.
     * It updates the state of the "Next" button based on the validity of the selection.
     *
     * [Notes]
     * - This method is a callback from the BarriersFragment.
     */
    override fun onBarriersSelectionChanged(isValidSelection: Boolean) {
        findViewById<Button>(R.id.btnNext).isEnabled = isValidSelection
    }

    /**
     * [onActivitySelectionChanged]
     * Description: Callback method called when the user changes their selection in the ActivityFragment.
     * Enables or disables the "Next" button based on the validity of the selection.
     *
     * [Parameters/Arguments]
     * - [isValidSelection]: Boolean indicating whether the selection is valid.
     *
     * [Return Value]
     * None
     *
     * [Usage]
     * This method is called when the user changes their selection in the ActivityFragment.
     * It updates the state of the "Next" button based on the validity of the selection.
     *
     * [Notes]
     * - This method is a callback from the ActivityFragment.
     */
    override fun onActivitySelectionChanged(isValidSelection: Boolean) {
        findViewById<Button>(R.id.btnNext).isEnabled = isValidSelection
    }

    /**
     * [onAboutInfoValidated]
     * Description: Callback method called when the user validates their about information.
     * Enables or disables the "Next" button based on the validity of the information.
     * Logs the validation result for debugging purposes.
     *
     * [Parameters/Arguments]
     * - [isValid]: Boolean indicating whether the information is valid.
     *
     * [Return Value]
     * None
     *
     * [Usage]
     * This method is called when the user validates their about information.
     * It updates the state of the "Next" button based on the validity of the information and logs the result.
     *
     * [Notes]
     * - This method is a callback from the AboutFragment.
     */
    override fun onAboutInfoValidated(isValid: Boolean) {
        val btnNext: Button = findViewById(R.id.btnNext)
        btnNext.isEnabled = isValid
        Log.d("AccountCreationActivity", "onAboutInfoValidated called with isValid = $isValid")
    }

    /**
     * [onWeeklyGoalsSelectionChanged]
     * Description: Callback method called when the user changes their selection in the WeeklyGoalsFragment.
     * Enables or disables the "Next" button based on the validity of the selection.
     * Logs the selection validity for debugging purposes.
     *
     * [Parameters/Arguments]
     * - [isValid]: Boolean indicating whether the selection is valid.
     *
     * [Return Value]
     * None
     *
     * [Usage]
     * This method is called when the user changes their selection in the WeeklyGoalsFragment.
     * It updates the state of the "Next" button based on the validity of the selection and logs the result.
     *
     * [Notes]
     * - This method is a callback from the WeeklyGoalsFragment.
     */
    override fun onWeeklyGoalsSelectionChanged(isValid: Boolean) {
        val btnNext: Button = findViewById(R.id.btnNext)
        btnNext.isEnabled = isValid
        Log.d("AccountCreationActivity", "onWeeklyGoalsSelectionChanged called with isValid = $isValid")
    }

    /**
     * [onCreateAccountInfoValidated]
     * Description: Callback method called when the user validates their account creation information.
     * Enables or disables the "Next" button based on the validity of the information.
     * Logs the validation result for debugging purposes.
     *
     * [Parameters/Arguments]
     * - [isValid]: Boolean indicating whether the information is valid.
     *
     * [Return Value]
     * None
     *
     * [Usage]
     * This method is called when the user validates their account creation information.
     * It updates the state of the "Next" button based on the validity of the information and logs the result.
     *
     * [Notes]
     * - This method is a callback from the CreateAccountFragment.
     */
    override fun onCreateAccountInfoValidated(isValid: Boolean) {
        val btnNext: Button = findViewById(R.id.btnNext)
        btnNext.isEnabled = isValid
        Log.d("AccountCreationActivity", "onCreateAccountInfoValidated called with isValid = $isValid")
    }

    /**
     * [serializeList]
     * Description: Serializes a list of strings into a JSON string using Gson.
     *
     * [Parameters/Arguments]
     * - [list]: The list of strings to be serialized.
     *
     * [Return Value]
     * A JSON string representing the serialized list.
     *
     * [Usage]
     * This method is used to serialize a list of strings into a JSON string for storage or transmission.
     *
     * [Notes]
     * - This method uses Gson library for serialization.
     */
    private fun serializeList(list: String): String {
        val gson = Gson()
        return gson.toJson(list)
    }

}