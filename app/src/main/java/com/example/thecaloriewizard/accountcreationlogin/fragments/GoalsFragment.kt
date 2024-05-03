package com.example.thecaloriewizard.accountcreationlogin.fragments

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.util.Log
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.thecaloriewizard.R
import com.example.thecaloriewizard.accountcreationlogin.adapters.GoalsAdapter
import com.example.thecaloriewizard.accountcreationlogin.viewmodels.AccountCreationViewModel

/**
 * [GoalsFragment]
 * Description: Fragment responsible for displaying and selecting user goals during account creation.
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
 * This fragment is used within the account creation process to allow users to select their goals, such as losing weight, gaining muscle, etc.
 * It provides a list of predefined goals for users to choose from and notifies the parent activity about the selected goals.
 *
 * [Notes]
 * - This fragment communicates with the AccountCreationViewModel to update user data with the selected goals.
 * - It utilizes the GoalsAdapter to display the list of goals and handle user selections.
 */
class GoalsFragment : Fragment() {

    private val viewModel: AccountCreationViewModel by activityViewModels()
    private lateinit var goalsAdapter: GoalsAdapter

    interface GoalsFragmentListener {
        fun onGoalsSelectionChanged(isValidSelection: Boolean)
    }

    private var listener: GoalsFragmentListener? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is GoalsFragmentListener) {
            listener = context
        } else {
            throw RuntimeException("$context must implement GoalsFragmentListener")
        }
    }

    override fun onDetach() {
        super.onDetach()
        listener = null
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_goals, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val toolbar: Toolbar = view.findViewById(R.id.toolbar)
        toolbar.title = "Goals"
        toolbar.setNavigationOnClickListener {
            requireActivity().onBackPressed()
        }

        // Define the list of goals
        val goalsList = listOf(
            "Lose weight",
            "Gain Weight",
            "Maintain Weight",
            "Gain Muscle",
            "Modify My Diet",
            "Manage Stress",
            "Increase activity levels"
        )

        // Initialize the GoalsAdapter and set up the RecyclerView
        goalsAdapter = GoalsAdapter(goalsList) { _ ->
            // Update ViewModel with selected goals
            val selectedGoals = goalsAdapter.getSelectedGoals()
            viewModel.userData.goals = selectedGoals.joinToString(",")

            // Log the update
            Log.d("GoalsFragment", "ViewModel updated with selected goals: ${viewModel.userData.goals}")

            // Check if the selection is valid
            val isValidSelection = selectedGoals.isNotEmpty() && selectedGoals.size <= 5
            Log.d("GoalsFragment", "Selected count: ${selectedGoals.size}, IsValid: $isValidSelection")
            listener?.onGoalsSelectionChanged(isValidSelection)
        }

        // Set up the RecyclerView
        view.findViewById<RecyclerView>(R.id.goalsRecyclerView).apply {
            layoutManager = LinearLayoutManager(context)
            adapter = this@GoalsFragment.goalsAdapter
        }

        // Initial validation check to ensure button state is updated when navigating back to the fragment
        val initialValidSelection = goalsAdapter.getSelectedGoals().size in 1..5
        listener?.onGoalsSelectionChanged(initialValidSelection)
    }
}