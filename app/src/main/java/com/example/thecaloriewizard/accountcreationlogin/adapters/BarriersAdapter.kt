package com.example.thecaloriewizard.accountcreationlogin.adapters

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.recyclerview.widget.RecyclerView
import com.example.thecaloriewizard.R

/**
 * [BarriersAdapter]
 * Description: Adapter class for managing the barrier items displayed in a RecyclerView.
 *
 * [Author]
 * Author Name: Brandon Sharp
 *
 * [Parameters/Arguments]
 * - [barriersList]: The list of barriers to be displayed in the RecyclerView.
 * - [onSelectionChanged]: A callback function to be invoked when the selection changes.
 *
 * [Return Value]
 * None
 *
 * [Usage]
 * This adapter is used to manage the barrier items displayed in a RecyclerView.
 *
 * [Notes]
 * None
 */
class BarriersAdapter(private val barriersList: List<String>, private val onSelectionChanged: (selectedCount: Int) -> Unit) : RecyclerView.Adapter<BarriersAdapter.BarriersViewHolder>() {

    private val selectedBarriers = mutableSetOf<Int>()

    inner class BarriersViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val itemButton: Button = itemView.findViewById(R.id.itemButton)

        fun bind(position: Int) {
            val barrier = barriersList[position]
            itemButton.text = barrier

            itemButton.isSelected = selectedBarriers.contains(position)
            itemButton.setBackgroundColor(if (selectedBarriers.contains(position)) Color.LTGRAY else Color.WHITE)

            itemButton.setOnClickListener {
                val isSelected = !selectedBarriers.contains(position)
                if (isSelected && selectedBarriers.size < 5) {
                    selectedBarriers.add(position)
                } else if (!isSelected) {
                    selectedBarriers.remove(position)
                }
                notifyItemChanged(position)
                onSelectionChanged(selectedBarriers.size)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BarriersViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_next_button, parent, false)
        return BarriersViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: BarriersViewHolder, position: Int) {
        holder.bind(position)
    }

    /**
     * [getSelectedBarriers]
     * Description: Retrieves the list of selected barriers.
     *
     * [Return Value]
     * The list of selected barriers.
     */
    fun getSelectedBarriers(): List<String> {
        return selectedBarriers.map { barriersList[it] }
    }

    override fun getItemCount() = barriersList.size
}