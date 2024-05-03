package com.example.thecaloriewizard.accountcreationlogin.viewmodels

import androidx.lifecycle.ViewModel
import com.example.thecaloriewizard.dataclasses.UserData

/**
 * [AccountCreationViewModel]
 * Description: ViewModel class responsible for managing user data during the account creation process.
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
 * This ViewModel is used to store and manage user data during the account creation process.
 * It contains an instance of UserData, which represents the data collected from the user during account creation.
 *
 * [Notes]
 * None
 */
class AccountCreationViewModel : ViewModel() {
    val userData = UserData()
}