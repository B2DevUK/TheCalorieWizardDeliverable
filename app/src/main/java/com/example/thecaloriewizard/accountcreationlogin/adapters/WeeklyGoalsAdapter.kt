package com.example.thecaloriewizard.accountcreationlogin.adapters

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.recyclerview.widget.RecyclerView
import com.example.thecaloriewizard.R

/**
 * [WeeklyGoalsAdapter]
 * Description: Adapter class for managing the weekly goal items displayed in a RecyclerView.
 *
 * [Author]
 * Author Name: Brandon Sharp
 *
 * [Parameters/Arguments]
 * - [weeklyGoalsList]: The list of weekly goals to be displayed in the RecyclerView.
 * - [onSelectionChanged]: A callback function to be invoked when the selection changes.
 *
 * [Return Value]
 * None
 *
 * [Usage]
 * This adapter is used to manage the weekly goal items displayed in a RecyclerView.
 *
 * [Notes]
 * None
 */
class WeeklyGoalsAdapter(
    private val weeklyGoalsList: List<String>,
    private val onSelectionChanged: (selectedCount: Int) -> Unit
) : RecyclerView.Adapter<WeeklyGoalsAdapter.WeeklyGoalsViewHolder>() {

    private val selectedGoals = mutableSetOf<Int>()

    inner class WeeklyGoalsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val itemButton: Button = itemView.findViewById(R.id.itemButton)

        fun bind(position: Int) {
            val goal = weeklyGoalsList[position]
            itemButton.text = goal

            itemButton.isSelected = selectedGoals.contains(position)
            itemButton.setBackgroundColor(if (selectedGoals.contains(position)) Color.LTGRAY else Color.WHITE)

            itemButton.setOnClickListener {
                val isSelected = !selectedGoals.contains(position)
                selectedGoals.clear()
                if (isSelected) {
                    selectedGoals.add(position)
                }
                notifyDataSetChanged()
                onSelectionChanged(selectedGoals.size)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WeeklyGoalsViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_next_button, parent, false)
        return WeeklyGoalsViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: WeeklyGoalsViewHolder, position: Int) {
        holder.bind(position)
    }

    /**
     * [getSelectedWeeklyGoals]
     * Description: Retrieves the list of selected weekly goals.
     *
     * [Return Value]
     * The list of selected weekly goals.
     */
    fun getSelectedWeeklyGoals(): List<String> {
        return selectedGoals.map { weeklyGoalsList[it] }
    }

    override fun getItemCount() = weeklyGoalsList.size
}