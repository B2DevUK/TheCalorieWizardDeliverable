package com.example.thecaloriewizard.accountcreationlogin

import android.content.Intent
import android.os.Bundle
import android.util.Base64
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.thecaloriewizard.R
import com.example.thecaloriewizard.database.DBHelper
import com.example.thecaloriewizard.encryption.EncryptionManager
import com.example.thecaloriewizard.main.HomeActivity

/**
 * [LoginPage]
 * Description: Activity responsible for handling user login functionality.
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
 * This activity is launched when the user wants to log in. It handles user authentication
 * by verifying the entered credentials against stored user data in the database.
 *
 * [Notes]
 * - This activity interacts with the DBHelper class to retrieve user data from the database.
 * - It uses the EncryptionManager class for password encryption and decryption.
 */
class LoginPage : AppCompatActivity() {
    private lateinit var dbHelper: DBHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        EncryptionManager.initialize(this)
        setContentView(R.layout.activity_loginpage)

        dbHelper = DBHelper(this)

        val loginButton: Button = findViewById(R.id.login_login_button)
        loginButton.setOnClickListener {
            performLogin()
        }
    }

    /**
     * [performLogin]
     * Description: Performs user login by validating entered credentials against stored user data.
     *
     * [Parameters/Arguments]
     * None
     *
     * [Return Value]
     * None
     *
     * [Usage]
     * This method is called when the user clicks the login button. It retrieves the entered email and password,
     * verifies them against stored user data in the database, and navigates to the home activity if login is successful.
     */
    private fun performLogin() {
        val emailEditText: EditText = findViewById(R.id.login_email_entry)
        val passwordEditText: EditText = findViewById(R.id.login_password_entry)

        val email = emailEditText.text.toString()
        val plaintextPassword = passwordEditText.text.toString()

        val userData = dbHelper.getUser(email)

        if (userData != null) {
            try {
                val encryptedPassword = Base64.decode(userData.password, Base64.DEFAULT)
                val decryptedPassword = EncryptionManager.decrypt(encryptedPassword)
                Log.d("LoginPage", "Decrypted Password: $decryptedPassword")
                if (decryptedPassword == plaintextPassword) {
                    val sharedPrefs = getSharedPreferences("AppPrefs", MODE_PRIVATE)
                    sharedPrefs.edit().putInt("CurrentUserID", userData.id.toInt()).apply()
                    Toast.makeText(this, "Login Successful", Toast.LENGTH_SHORT).show()
                    navigateToHomeActivity()
                } else {
                    Toast.makeText(this, "Login Failed. Please check your credentials.", Toast.LENGTH_LONG).show()
                }
            } catch (e: Exception) {
                Log.e("LoginPage", "Error decrypting password", e)
                Toast.makeText(this, "An error occurred during login. Please try again.", Toast.LENGTH_LONG).show()
            }
        } else {
            Toast.makeText(this, "Login Failed. Please check your credentials.", Toast.LENGTH_LONG).show()
        }
    }

    /**
     * [navigateToHomeActivity]
     * Description: Navigates to the home activity upon successful login.
     *
     * [Parameters/Arguments]
     * None
     *
     * [Return Value]
     * None
     *
     * [Usage]
     * This method is called after successful user login. It starts the home activity and finishes the current activity.
     */
    private fun navigateToHomeActivity() {
        val intent = Intent(this, HomeActivity::class.java)
        startActivity(intent)
        finish()
    }
}
