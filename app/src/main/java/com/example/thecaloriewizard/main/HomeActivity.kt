package com.example.thecaloriewizard.main

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.example.thecaloriewizard.R
import com.example.thecaloriewizard.main.foodDiary.DiaryFragment
import com.example.thecaloriewizard.main.home.HomeFragment
import com.example.thecaloriewizard.main.weightTracking.WeightTrackingFragment
import com.google.android.material.bottomnavigation.BottomNavigationView

/**
 * HomeActivity
 * Description: Responsible for managing the main functionality of the application.
 *
 * [Author]
 * Author Name: Brandon Sharp
 *
 * [Parameters/Arguments]
 * None
 *
 * [Usage]
 * Handles navigation between different fragments of the application using a bottom navigation bar.
 *
 * [Notes]
 * None
 */
class HomeActivity : AppCompatActivity() {

    /**
     * onCreate
     * Description: Called when the activity is starting. Responsible for initializing the activity.
     *
     * [Parameters/Arguments]
     * - savedInstanceState: If the activity is being re-initialized after previously being shut down, this Bundle contains the data it most recently supplied in onSaveInstanceState(Bundle).
     *
     * [Return Value]
     * None
     *
     * [Notes]
     * None
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        val bottomNav: BottomNavigationView = findViewById(R.id.bottomNav)
        bottomNav.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.navigation_home -> {
                    val homeFragment = HomeFragment()
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.fragmentContainer, homeFragment)
                        .commit()
                    true
                }
                R.id.navigation_food_diary -> {
                    val diaryFragment = DiaryFragment()
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.fragmentContainer, diaryFragment)
                        .commit()
                    true
                }
                R.id.navigation_weight_tracking -> {
                    val weightTrackingFragment = WeightTrackingFragment()
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.fragmentContainer, weightTrackingFragment)
                        .commit()
                    true
                }
                else -> false
            }
        }

        if (savedInstanceState == null) {
            bottomNav.selectedItemId = R.id.navigation_home
        }
    }

    /**
     * onCreateOptionsMenu
     * Description: Initialize the contents of the Activity's standard options menu.
     *
     * [Parameters/Arguments]
     * - menu: The options menu in which you place your items.
     *
     * [Return Value]
     * Boolean: You must return true for the menu to be displayed; if you return false it will not be shown.
     *
     * [Notes]
     * None
     */
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }

    /**
     * onOptionsItemSelected
     * Description: This hook is called whenever an item in your options menu is selected.
     *
     * [Parameters/Arguments]
     * - item: The menu item that was selected.
     *
     * [Return Value]
     * Boolean: Return false to allow normal menu processing to proceed, true to consume it here.
     *
     * [Notes]
     * None
     */
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_settings -> {
                val intent = Intent(this, SettingsActivity::class.java)
                startActivity(intent)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}