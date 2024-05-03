package com.example.thecaloriewizard.main

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.thecaloriewizard.R
import com.example.thecaloriewizard.dataclasses.SettingItem

/**
 * SettingsActivity
 * Description: Activity responsible for displaying user settings.
 *
 * [Author]
 * Author Name: Brandon Sharp
 *
 * [Parameters/Arguments]
 * None
 *
 * [Usage]
 * Handles the display of user settings and navigation between different settings options.
 *
 * [Notes]
 * None
 */
class SettingsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)
        setSupportActionBar(findViewById(R.id.toolbar))
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val settingsList = listOf(
            SettingItem("My Account"),
            SettingItem("Log Out")
        )

        val settingsAdapter = SettingsAdapter(settingsList, this)
        findViewById<RecyclerView>(R.id.settingsRecyclerView).apply {
            layoutManager = LinearLayoutManager(context)
            adapter = settingsAdapter
        }
    }

    /**
     * onOptionsItemSelected
     * Description: Called when a menu item is selected.
     *
     * [Parameters/Arguments]
     * - item: The menu item that was selected.
     *
     * [Return Value]
     * Boolean: True if the menu item selection event has been consumed, false otherwise.
     *
     * [Notes]
     * None
     */
    override fun onOptionsItemSelected(item: android.view.MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                onBackPressed()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }
}