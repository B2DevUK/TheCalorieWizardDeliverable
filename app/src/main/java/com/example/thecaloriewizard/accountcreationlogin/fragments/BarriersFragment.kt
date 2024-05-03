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
import com.example.thecaloriewizard.accountcreationlogin.adapters.BarriersAdapter
import com.example.thecaloriewizard.accountcreationlogin.viewmodels.AccountCreationViewModel

/**
 * [BarriersFragment]
 * Description: Fragment responsible for displaying and selecting barriers to achieving dietary goals during the account creation process.
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
 * This fragment is used within the account creation process to allow users to select barriers they face in achieving their dietary goals.
 * It displays a list of predefined barriers, and the user can select multiple barriers from the list.
 *
 * [Notes]
 * - This fragment communicates with the AccountCreationViewModel to update user data with the selected barriers.
 * - It also notifies the parent activity about the selection change to enable or disable the next button based on the user's selection.
 */
class BarriersFragment : Fragment() {
    private val viewModel: AccountCreationViewModel by activityViewModels()
    private lateinit var barriersAdapter: BarriersAdapter

    interface BarriersFragmentListener {
        fun onBarriersSelectionChanged(isValidSelection: Boolean)
    }

    private var listener: BarriersFragmentListener? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is BarriersFragmentListener) {
            listener = context
        } else {
            throw RuntimeException("$context must implement BarriersFragmentListener")
        }
    }

    override fun onDetach() {
        super.onDetach()
        listener = null
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_barriers, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val toolbar: Toolbar = view.findViewById(R.id.toolbar)
        toolbar.title = "Barriers"
        toolbar.setNavigationOnClickListener {
            requireActivity().onBackPressed()
        }
        val barriersList = listOf(
            "Lack of time",
            "The regimen was too hard to follow",
            "Did not enjoy eating the food",
            "Difficult to make food choices",
            "Social events and eating",
            "Cravings",
            "Lack of progress",
            "Lack of motivation"
        )

        barriersAdapter = BarriersAdapter(barriersList) { _ ->
            val selectedBarriers = barriersAdapter.getSelectedBarriers()
            viewModel.userData.barriers = selectedBarriers.joinToString(",")
            val isValidSelection = selectedBarriers.isNotEmpty() && selectedBarriers.size <= 5
            listener?.onBarriersSelectionChanged(isValidSelection)

            Log.d("BarriersFragment", "Updated ViewModel with barriers: ${viewModel.userData.barriers}")
        }

        view.findViewById<RecyclerView>(R.id.barriersRecyclerView).apply {
            layoutManager = LinearLayoutManager(context)
            adapter = barriersAdapter
        }

        listener?.onBarriersSelectionChanged(false)
    }
}