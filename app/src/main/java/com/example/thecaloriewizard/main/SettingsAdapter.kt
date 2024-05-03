package com.example.thecaloriewizard.main

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.thecaloriewizard.R
import com.example.thecaloriewizard.accountcreationlogin.LoginPage
import com.example.thecaloriewizard.dataclasses.SettingItem

/**
 * SettingsAdapter
 * Description: Adapter for handling settings items in a RecyclerView.
 *
 * [Author]
 * Author Name: Brandon Sharp
 *
 * [Parameters/Arguments]
 * - items: List of SettingItem objects to be displayed.
 * - activity: AppCompatActivity instance used for navigation.
 *
 * [Usage]
 * Binds setting items to corresponding views in a RecyclerView and handles click events for each setting item.
 *
 * [Notes]
 * None
 */
class SettingsAdapter(
    private val items: List<SettingItem>,
    private val activity: AppCompatActivity
) : RecyclerView.Adapter<SettingsAdapter.ViewHolder>() {

    /**
     * onCreateViewHolder
     * Description: Called when RecyclerView needs a new ViewHolder.
     *
     * [Parameters/Arguments]
     * - parent: The ViewGroup into which the new View will be added.
     * - viewType: The view type of the new View.
     *
     * [Return Value]
     * ViewHolder: A new ViewHolder that holds a View of the given view type.
     *
     * [Notes]
     * None
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_setting, parent, false)
        return ViewHolder(view, activity)
    }

    /**
     * onBindViewHolder
     * Description: Called by RecyclerView to display the data at the specified position.
     *
     * [Parameters/Arguments]
     * - holder: The ViewHolder which should be updated to represent the contents of the item at the given position.
     * - position: The position of the item within the adapter's data set.
     *
     * [Return Value]
     * None
     *
     * [Notes]
     * None
     */
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(items[position])
    }

    /**
     * getItemCount
     * Description: Returns the total number of items in the data set held by the adapter.
     *
     * [Parameters/Arguments]
     * None
     *
     * [Return Value]
     * Int: The total number of items in the adapter.
     *
     * [Notes]
     * None
     */
    override fun getItemCount(): Int = items.size

    /**
     * ViewHolder
     * Description: ViewHolder class for setting items in the RecyclerView.
     *
     * [Author]
     * Author Name: Brandon Sharp
     *
     * [Parameters/Arguments]
     * - itemView: The view corresponding to the setting item.
     * - activity: AppCompatActivity instance used for navigation.
     *
     * [Usage]
     * Binds setting item data to corresponding views and handles click events.
     *
     * [Notes]
     * None
     */
    class ViewHolder(itemView: View, private val activity: AppCompatActivity) :
        RecyclerView.ViewHolder(itemView) {

        /**
         * bind
         * Description: Binds the setting item data to views and sets click listeners.
         *
         * [Parameters/Arguments]
         * - item: The SettingItem object to bind.
         *
         * [Return Value]
         * None
         *
         * [Notes]
         * None
         */
        fun bind(item: SettingItem) {
            val button: Button = itemView.findViewById(R.id.settingButton)
            button.text = item.title
            button.setOnClickListener {
                Log.d("SettingsAdapter", "${item.title} clicked")
                when (item.title) {
                    "My Account" -> {
                        val transaction = activity.supportFragmentManager.beginTransaction()
                        transaction.replace(R.id.settingsFragmentContainer, MyAccountFragment())
                        transaction.addToBackStack(null)
                        transaction.commit()
                    }

                    "Log Out" -> {
                        val prefs = activity.getSharedPreferences("AppPrefs", Context.MODE_PRIVATE)
                        prefs.edit().clear().apply()

                        val logoutIntent = Intent(activity, LoginPage::class.java)
                        logoutIntent.flags =
                            Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                        activity.startActivity(logoutIntent)
                        activity.finish()
                    }
                }
            }
        }
    }
}