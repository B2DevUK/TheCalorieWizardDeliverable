package com.example.thecaloriewizard.accountcreationlogin.fragments

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.thecaloriewizard.R
import com.example.thecaloriewizard.accountcreationlogin.adapters.ActivityAdapter
import com.example.thecaloriewizard.accountcreationlogin.viewmodels.AccountCreationViewModel

/**
 * [ActivityFragment]
 * Description: Fragment responsible for displaying and selecting activity levels during the account creation process.
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
 * This fragment is used within the account creation process to allow users to select their activity levels.
 * It displays a list of predefined activity levels, and the user can select one or more levels based on their activity.
 *
 * [Notes]
 * - This fragment communicates with the AccountCreationViewModel to update user data with the selected activity levels.
 * - It also notifies the parent activity about the selection change to enable or disable the next button based on the user's selection.
 */
class ActivityFragment : Fragment() {
    private val viewModel: AccountCreationViewModel by activityViewModels()
    private lateinit var activityAdapter: ActivityAdapter

    interface ActivityFragmentListener {
        fun onActivitySelectionChanged(isValidSelection: Boolean)
    }

    private var listener: ActivityFragmentListener? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is ActivityFragmentListener) {
            listener = context
        } else {
            throw RuntimeException("$context must implement ActivityFragmentListener")
        }
    }

    override fun onDetach() {
        super.onDetach()
        listener = null
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_activity_level, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val toolbar: Toolbar = view.findViewById(R.id.toolbar)
        toolbar.title = "Activities"
        toolbar.setNavigationOnClickListener {
            requireActivity().onBackPressed()
        }

        val activityList = listOf(
            "Not active (0 hrs a week)",
            "Lightly Active (2-5 Hrs a week)",
            "Active (5-10 Hrs a week)",
            "Very Active (10+ Hrs a week)"
        )

        activityAdapter = ActivityAdapter(activityList) { _ ->
            val selectedActivity = activityAdapter.getSelectedActivities()
            viewModel.userData.activityLevel = selectedActivity.joinToString(", ")
            val isValidSelection = selectedActivity.isNotEmpty() && selectedActivity.size <= 1
            listener?.onActivitySelectionChanged(isValidSelection)

            Log.d("ActivityFragment", "Updated ViewModel with activity: ${viewModel.userData.activityLevel}")
        }

        view.findViewById<RecyclerView>(R.id.activityLevelRecyclerView).apply {
            layoutManager = LinearLayoutManager(context)
            adapter = activityAdapter
        }

        listener?.onActivitySelectionChanged(false)
    }
}