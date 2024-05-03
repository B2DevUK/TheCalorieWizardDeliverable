package com.example.thecaloriewizard.accountcreationlogin.adapters

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.recyclerview.widget.RecyclerView
import com.example.thecaloriewizard.R

/**
 * [ActivityAdapter]
 * Description: Adapter class for managing the activity items displayed in a RecyclerView.
 *
 * [Author]
 * Author Name: Brandon Sharp
 *
 * [Parameters/Arguments]
 * - [activityList]: The list of activities to be displayed in the RecyclerView.
 * - [onSelectionChanged]: A callback function to be invoked when the selection changes.
 *
 * [Return Value]
 * None
 *
 * [Usage]
 * This adapter is used to manage the activity items displayed in a RecyclerView.
 *
 * [Notes]
 * None
 */
class ActivityAdapter(private val activityList: List<String>, private val onSelectionChanged: (selectedCount: Int) -> Unit) : RecyclerView.Adapter<ActivityAdapter.ActivityViewHolder>() {

    private val selectedActivities = mutableSetOf<Int>()

    inner class ActivityViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val itemButton: Button = itemView.findViewById(R.id.itemButton)

        fun bind(position: Int) {
            val activity = activityList[position]
            itemButton.text = activity

            itemButton.isSelected = selectedActivities.contains(position)
            itemButton.setBackgroundColor(if (selectedActivities.contains(position)) Color.LTGRAY else Color.WHITE)

            itemButton.setOnClickListener {
                val isSelected = !selectedActivities.contains(position)
                if (isSelected && selectedActivities.size < 1) {
                    selectedActivities.add(position)
                } else if (!isSelected) {
                    selectedActivities.remove(position)
                }

                notifyItemChanged(position)
                onSelectionChanged(selectedActivities.size)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ActivityViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_next_button, parent, false)
        return ActivityViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ActivityViewHolder, position: Int) {
        holder.bind(position)
    }

    override fun getItemCount() = activityList.size

    /**
     * [getSelectedActivities]
     * Description: Retrieves the list of selected activities.
     *
     * [Return Value]
     * The list of selected activities.
     */
    fun getSelectedActivities(): List<String> {
        return selectedActivities.map { activityList[it] }
    }
}