package com.example.thecaloriewizard.accountcreationlogin

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.example.thecaloriewizard.R

/**
 * [CreateAccountPage]
 * Description: Activity responsible for displaying the create account page.
 *
 * [Author]
 * Author Name: [Brandon Sharp]
 *
 * [Parameters/Arguments]
 * None
 *
 * [Return Value]
 * None
 *
 * [Usage]
 * This activity is launched when the user wants to create a new account. It displays the create account page
 * with a toolbar and a continue button.
 *
 * [Notes]
 * - This activity uses a toolbar for navigation.
 * - Clicking on the continue button starts the account creation process by launching the AccountCreationActivity.
 */
class CreateAccountPage : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_createaccountpage)

        val toolbar: Toolbar = findViewById(R.id.create_account_page_toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        toolbar.setNavigationOnClickListener {
            onBackPressed()
        }

        val continueButton: Button = findViewById(R.id.create_account_page_continue_button)

        continueButton.setOnClickListener {
            val intent = Intent(this, AccountCreationActivity::class.java)
            startActivity(intent)
        }
    }
}