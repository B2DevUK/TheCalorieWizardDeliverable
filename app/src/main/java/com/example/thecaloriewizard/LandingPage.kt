package com.example.thecaloriewizard

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.example.thecaloriewizard.accountcreationlogin.CreateAccountPage
import com.example.thecaloriewizard.accountcreationlogin.LoginPage
import com.example.thecaloriewizard.main.HomeActivity

/**
 * LandingPage
 * Description: Landing page activity for handling user authentication and navigation.
 *
 * [Author]
 * Author Name: Brandon Sharp
 *
 * [Parameters/Arguments]
 * None
 *
 * [Usage]
 * Handles user authentication and navigation to different parts of the application.
 *
 * [Notes]
 * None
 */
class LandingPage : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (isUserLoggedIn()) {
            navigateToHomeActivity()
            return
        }

        setContentView(R.layout.activity_landingpage)

        val createAccountButton: Button = findViewById(R.id.landing_create_account_button)
        val loginButton: Button = findViewById(R.id.landing_log_in_button)

        createAccountButton.setOnClickListener {
            val intent = Intent(this, CreateAccountPage::class.java)
            startActivity(intent)
        }

        loginButton.setOnClickListener {
            val intent = Intent(this, LoginPage::class.java)
            startActivity(intent)
        }
    }

    /**
     * isUserLoggedIn
     * Description: Checks if the user is logged in by verifying the presence of a user ID in SharedPreferences.
     *
     * [Parameters/Arguments]
     * None
     *
     * [Return Value]
     * Boolean: True if the user is logged in, false otherwise.
     *
     * [Notes]
     * None
     */
    private fun isUserLoggedIn(): Boolean {
        val sharedPrefs = getSharedPreferences("AppPrefs", MODE_PRIVATE)
        val userId = sharedPrefs.getInt("CurrentUserID", -1)
        return userId != -1
    }

    /**
     * isUserLoggedIn
     * Description: Checks if the user is logged in by verifying the presence of a user ID in SharedPreferences.
     *
     * [Parameters/Arguments]
     * None
     *
     * [Return Value]
     * Boolean: True if the user is logged in, false otherwise.
     *
     * [Notes]
     * None
     */
    private fun navigateToHomeActivity() {
        val intent = Intent(this, HomeActivity::class.java)
        startActivity(intent)
        finish()
    }
}