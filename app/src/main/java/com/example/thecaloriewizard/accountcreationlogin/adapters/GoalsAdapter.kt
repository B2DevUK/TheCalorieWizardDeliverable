package com.example.thecaloriewizard.accountcreationlogin.adapters

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.recyclerview.widget.RecyclerView
import com.example.thecaloriewizard.R

/**
 * [GoalsAdapter]
 * Description: Adapter class for managing the goal items displayed in a RecyclerView.
 *
 * [Author]
 * Author Name: Brandon Sharp
 *
 * [Parameters/Arguments]
 * - [goalsList]: The list of goals to be displayed in the RecyclerView.
 * - [onSelectionChanged]: A callback function to be invoked when the selection changes.
 *
 * [Return Value]
 * None
 *
 * [Usage]
 * This adapter is used to manage the goal items displayed in a RecyclerView.
 *
 * [Notes]
 * None
 */
class GoalsAdapter(private val goalsList: List<String>, private val onSelectionChanged: (selectedCount: Int) -> Unit) : RecyclerView.Adapter<GoalsAdapter.GoalsViewHolder>() {

    private val selectedGoals = mutableSetOf<Int>()

    inner class GoalsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val itemButton: Button = itemView.findViewById(R.id.itemButton)

        fun bind(position: Int) {
            val goal = goalsList[position]
            itemButton.text = goal

            itemButton.isSelected = selectedGoals.contains(position)
            itemButton.setBackgroundColor(if (selectedGoals.contains(position)) Color.LTGRAY else Color.WHITE)

            itemButton.setOnClickListener {
                val isSelected = !selectedGoals.contains(position)
                if (isSelected && selectedGoals.size < 5) {
                    selectedGoals.add(position)
                    notifyItemChanged(position)
                } else if (!isSelected) {
                    selectedGoals.remove(position)
                    notifyItemChanged(position)
                }

                onSelectionChanged(selectedGoals.size)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GoalsViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_next_button, parent, false)
        return GoalsViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: GoalsViewHolder, position: Int) {
        holder.bind(position)
    }

    /**
     * [getSelectedGoals]
     * Description: Retrieves the list of selected goals.
     *
     * [Return Value]
     * The list of selected goals.
     */
    fun getSelectedGoals(): List<String> {
        return selectedGoals.map { goalsList[it] }
    }

    override fun getItemCount() = goalsList.size
}