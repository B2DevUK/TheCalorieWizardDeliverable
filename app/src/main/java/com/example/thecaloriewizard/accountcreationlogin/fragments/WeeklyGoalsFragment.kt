package com.example.thecaloriewizard.accountcreationlogin.fragments

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.thecaloriewizard.R
import com.example.thecaloriewizard.accountcreationlogin.adapters.WeeklyGoalsAdapter
import com.example.thecaloriewizard.accountcreationlogin.viewmodels.AccountCreationViewModel

/**
 * [WeeklyGoalsFragment]
 * Description: Fragment responsible for displaying and selecting weekly goals during account creation.
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
 * This fragment is used within the account creation process to allow users to select their weekly goals, such as losing or gaining weight.
 * It provides a list of predefined weekly goals for users to choose from and notifies the parent activity about the selected goals.
 *
 * [Notes]
 * - This fragment communicates with the AccountCreationViewModel to update user data with the selected weekly goals.
 * - It utilizes the WeeklyGoalsAdapter to display the list of weekly goals and handle user selections.
 */
class WeeklyGoalsFragment : Fragment() {

    private val viewModel: AccountCreationViewModel by activityViewModels()
    private lateinit var weeklyGoalsAdapter: WeeklyGoalsAdapter

    interface WeeklyGoalsFragmentListener {
        fun onWeeklyGoalsSelectionChanged(isValidSelection: Boolean)
    }

    private var listener: WeeklyGoalsFragmentListener? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is WeeklyGoalsFragmentListener) {
            listener = context
        } else {
            throw RuntimeException("$context must implement WeeklyGoalsFragmentListener")
        }
    }

    override fun onDetach() {
        super.onDetach()
        listener = null
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_weekly_goals, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val weeklyGoalsList = listOf(
            "Lose 2lb", "Lose 1.5lb", "Lose 1lb", "Lose 0.5lb",
            "Maintain weight",
            "Gain 0.5lb", "Gain 1lb", "Gain 1.5lb", "Gain 2lb"
        )

        // Initialize the WeeklyGoalsAdapter and set up the RecyclerView
        weeklyGoalsAdapter = WeeklyGoalsAdapter(weeklyGoalsList) { _ ->
            // Update ViewModel with selected weekly goals
            val selectedActivity = weeklyGoalsAdapter.getSelectedWeeklyGoals()
            viewModel.userData.activityLevel = selectedActivity.joinToString(", ")
            val isValidSelection = selectedActivity.isNotEmpty() && selectedActivity.size <= 1
            listener?.onWeeklyGoalsSelectionChanged(isValidSelection)

            // Log the current weekly goals for debugging
            Log.d("WeeklyGoalsFragment", "Updated ViewModel with weekly goals: ${viewModel.userData.weeklyGoals}")
        }

        // Set up the RecyclerView
        view.findViewById<RecyclerView>(R.id.weeklyGoalsRecyclerView).apply {
            layoutManager = LinearLayoutManager(context)
            adapter = weeklyGoalsAdapter
        }

        // Initially set the selection state to false until selections are made
        listener?.onWeeklyGoalsSelectionChanged(false)
    }
}