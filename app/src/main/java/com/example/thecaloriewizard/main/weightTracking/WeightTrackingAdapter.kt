package com.example.thecaloriewizard.main.weightTracking

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.thecaloriewizard.R
import com.example.thecaloriewizard.dataclasses.WeighInEntry

/**
 * WeightTrackingAdapter
 * Description: Adapter for displaying weigh-in entries in a RecyclerView.
 *
 * [Author]
 * Author Name: Brandon Sharp
 *
 * [Parameters/Arguments]
 * - context: The context of the calling activity or fragment.
 * - weighIns: List of WeighInEntry objects representing weigh-in data.
 *
 * [Usage]
 * Used to display weigh-in entries in a RecyclerView.
 *
 * [Notes]
 * None
 */
class WeightTrackingAdapter(
    private val context: Context,
    private var weighIns: List<WeighInEntry>
) : RecyclerView.Adapter<WeightTrackingAdapter.WeighInViewHolder>() {

    /**
     * WeighInViewHolder
     * Description: ViewHolder for individual weigh-in items in the RecyclerView.
     *
     * [Parameters/Arguments]
     * - itemView: The inflated view for the weigh-in item.
     *
     * [Usage]
     * Used to initialize views for individual weigh-in items in the RecyclerView.
     *
     * [Notes]
     * None
     */
    class WeighInViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val textViewWeighIn: TextView = itemView.findViewById(R.id.textViewWeighIn)

        /**
         * bind
         * Description: Binds data to the views in the ViewHolder.
         *
         * [Parameters/Arguments]
         * - weighInEntry: The WeighInEntry object representing a weigh-in record.
         *
         * [Return Value]
         * None
         *
         * [Notes]
         * None
         */
        fun bind(weighInEntry: WeighInEntry) {
            textViewWeighIn.text = "${weighInEntry.weight} lbs - ${weighInEntry.date}"
        }
    }

    /**
     * onCreateViewHolder
     * Description: Creates a new ViewHolder for a weigh-in item.
     *
     * [Parameters/Arguments]
     * - parent: The ViewGroup into which the new View will be added after it is bound to an adapter position.
     * - viewType: The view type of the new View.
     *
     * [Return Value]
     * WeighInViewHolder: The ViewHolder for the newly created View.
     *
     * [Notes]
     * None
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WeighInViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_weigh_in, parent, false)
        return WeighInViewHolder(view)
    }

    /**
     * onBindViewHolder
     * Description: Binds data to the views in the ViewHolder at the specified position.
     *
     * [Parameters/Arguments]
     * - holder: The ViewHolder to bind the data to.
     * - position: The position of the item within the adapter's data set.
     *
     * [Return Value]
     * None
     *
     * [Notes]
     * None
     */
    override fun onBindViewHolder(holder: WeighInViewHolder, position: Int) {
        holder.bind(weighIns[position])
    }

    /**
     * getItemCount
     * Description: Gets the total number of items in the data set held by the adapter.
     *
     * [Parameters/Arguments]
     * None
     *
     * [Return Value]
     * Int: The total number of items in the data set.
     *
     * [Notes]
     * None
     */
    override fun getItemCount(): Int = weighIns.size

}