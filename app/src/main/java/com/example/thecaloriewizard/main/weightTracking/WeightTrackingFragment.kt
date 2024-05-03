package com.example.thecaloriewizard.main.weightTracking

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.thecaloriewizard.R
import com.example.thecaloriewizard.database.DBHelper
import com.example.thecaloriewizard.dataclasses.WeighInEntry
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import kotlin.math.abs

/**
 * WeightTrackingFragment
 * Description: Fragment responsible for weight tracking functionality.
 *
 * [Author]
 * Author Name: Brandon Sharp
 *
 * [Parameters/Arguments]
 * None
 *
 * [Usage]
 * Used to allow users to track their weight and view recent weigh-in entries.
 *
 * [Notes]
 * None
 */
class WeightTrackingFragment : Fragment() {
    private lateinit var recyclerViewRecentWeighIns: RecyclerView
    private lateinit var weightTrackingAdapter: WeightTrackingAdapter
    private lateinit var dbHelper: DBHelper
    private lateinit var editTextWeighIn: EditText
    private lateinit var textViewCurrentWeight: TextView
    private lateinit var textViewGoalWeight: TextView
    private lateinit var textViewWeightRemaining: TextView
    private lateinit var buttonSubmitWeighIn: Button

    /**
     * onCreateView
     * Description: Called to have the fragment instantiate its user interface view.
     *
     * [Parameters/Arguments]
     * - inflater: The LayoutInflater object that can be used to inflate any views in the fragment.
     * - container: If non-null, this is the parent view that the fragment's UI should be attached to.
     * - savedInstanceState: If non-null, this fragment is being re-constructed from a previous saved state as given here.
     *
     * [Return Value]
     * View: The root view of the fragment layout.
     *
     * [Notes]
     * None
     */
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_weight_tracking, container, false)
    }

    /**
     * onViewCreated
     * Description: Called immediately after onCreateView() has returned, but before any saved state has been restored in to the view.
     *
     * [Parameters/Arguments]
     * - view: The view returned by onCreateView().
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

        recyclerViewRecentWeighIns = view.findViewById(R.id.recyclerViewRecentWeighIns)
        recyclerViewRecentWeighIns.layoutManager = LinearLayoutManager(context)
        editTextWeighIn = view.findViewById(R.id.editTextWeighIn)
        textViewCurrentWeight = view.findViewById(R.id.textViewCurrentWeight)
        buttonSubmitWeighIn = view.findViewById(R.id.buttonSubmitWeighIn)
        textViewGoalWeight = view.findViewById(R.id.textViewGoalWeight)
        textViewWeightRemaining = view.findViewById(R.id.textViewWeightRemaining)

        buttonSubmitWeighIn.setOnClickListener {
            submitWeighIn()
        }

        loadRecentWeighIns()
        updateWeightDisplay()
    }

    /**
     * loadRecentWeighIns
     * Description: Loads recent weigh-in entries from the database and updates the RecyclerView.
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
    private fun loadRecentWeighIns() {
        val userId = getUserIdFromPrefs()
        val weighIns = dbHelper.getRecentWeightEntries(userId)
        weightTrackingAdapter = WeightTrackingAdapter(requireContext(), weighIns)
        recyclerViewRecentWeighIns.adapter = weightTrackingAdapter
    }

    /**
     * submitWeighIn
     * Description: Submits a new weigh-in entry to the database and updates the UI accordingly.
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
    private fun submitWeighIn() {
        val weightInPounds = editTextWeighIn.text.toString().toIntOrNull()
        weightInPounds?.let {
            if (dbHelper.addWeightEntry(getUserIdFromPrefs(), it, getCurrentDate())) {
                editTextWeighIn.text.clear()
                updateWeightDisplay()
                loadRecentWeighIns()
                Toast.makeText(context, "Weight submitted successfully!", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(context, "Failed to submit weight.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    /**
     * updateWeightDisplay
     * Description: Updates the UI with the current weight, goal weight, and remaining weight to reach the goal.
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
    private fun updateWeightDisplay() {
        val userId = getUserIdFromPrefs()
        val userData = dbHelper.getUserByIdFull(userId.toString())

        userData?.let {
            val currentWeightPounds = it.weightStone * 14 + it.weightPounds
            val goalWeightPounds = it.goalWeightStone * 14 + it.goalWeightPounds
            val weightDifference = goalWeightPounds - currentWeightPounds

            textViewCurrentWeight.text = "$currentWeightPounds lbs"
            textViewGoalWeight.text = "$goalWeightPounds lbs"

            textViewWeightRemaining.text = when {
                weightDifference > 0 -> "Need to gain ${abs(weightDifference)} lbs"
                weightDifference < 0 -> "Need to lose ${abs(weightDifference)} lbs"
                else -> "Goal reached!"
            }
        } ?: run {
            textViewCurrentWeight.text = "No current weight data available"
            textViewGoalWeight.text = "No goal weight data available"
            textViewWeightRemaining.text = "No data available"
        }
    }

    private fun getUserIdFromPrefs(): Int {
        val sharedPrefs = activity?.getSharedPreferences("AppPrefs", Context.MODE_PRIVATE)
        return sharedPrefs?.getInt("CurrentUserID", -1) ?: -1
    }

    /**
     * getCurrentDate
     * Description: Retrieves the current date in the format "yyyy-MM-dd".
     *
     * [Parameters/Arguments]
     * None
     *
     * [Return Value]
     * String: The current date in the format "yyyy-MM-dd".
     *
     * [Notes]
     * None
     */
    private fun getCurrentDate(): String {
        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        return dateFormat.format(Date())
    }
}