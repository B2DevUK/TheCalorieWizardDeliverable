package com.example.thecaloriewizard.main.foodDiary

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.thecaloriewizard.R
import com.example.thecaloriewizard.database.DBHelper
import com.example.thecaloriewizard.dataclasses.ConsumedFoodItem
import com.example.thecaloriewizard.dataclasses.ListItem
import com.example.thecaloriewizard.dataclasses.MealType

/**
 * DiaryAdapter
 * Description: RecyclerView adapter for displaying food diary entries.
 *
 * [Author]
 * Author Name: Brandon Sharp
 *
 * [Parameters/Arguments]
 * - diaryEntries: List of diary entries to display.
 * - dbHelper: Database helper for managing data.
 * - mealTypeMap: Map of meal types.
 * - onAddButtonClicked: Callback for adding a food item.
 * - onDeleteButtonClicked: Callback for deleting a food item.
 * - onItemDeleted: Callback for notifying when an item is deleted.
 *
 * [Return Value]
 * None
 *
 * [Usage]
 * val adapter = DiaryAdapter(diaryEntries, dbHelper, mealTypeMap, onAddButtonClicked, onDeleteButtonClicked, onItemDeleted)
 *
 * [Notes]
 * None
 */
class DiaryAdapter(
    private var diaryEntries: MutableList<ListItem>,
    private val dbHelper: DBHelper,
    private val mealTypeMap: Map<String, MealType>,
    private val onAddButtonClicked: (MealType) -> Unit,
    private val onDeleteButtonClicked: (Int, Int) -> Unit,
    private val onItemDeleted: () -> Unit
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        private const val TYPE_MEAL_HEADER = 0
        private const val TYPE_FOOD_ITEM = 1
    }

    override fun getItemViewType(position: Int): Int {
        return when (diaryEntries[position]) {
            is ListItem.Header -> TYPE_MEAL_HEADER
            is ListItem.FoodItem -> TYPE_FOOD_ITEM
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return when (viewType) {
            TYPE_MEAL_HEADER -> {
                val view = inflater.inflate(R.layout.item_meal_header, parent, false)
                MealHeaderViewHolder(view, mealTypeMap, onAddButtonClicked)
            }
            TYPE_FOOD_ITEM -> {
                val view = inflater.inflate(R.layout.item_food_entry, parent, false)
                FoodItemViewHolder(view)
            }
            else -> throw IllegalArgumentException("Invalid view type")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (val entry = diaryEntries[position]) {
            is ListItem.Header -> (holder as MealHeaderViewHolder).bind(entry.title)
            is ListItem.FoodItem -> (holder as FoodItemViewHolder).bind(
                entry.consumedFoodItem,
                entry.logId,
                onDeleteButtonClicked
            )
        }
    }

    override fun getItemCount(): Int = diaryEntries.size

    /**
     * MealHeaderViewHolder
     * Description: ViewHolder for meal header items in the diary.
     *
     * [Parameters/Arguments]
     * - view: The inflated view.
     * - mealTypeMap: Map of meal types.
     * - onAddButtonClicked: Callback for adding a food item.
     *
     * [Return Value]
     * None
     *
     * [Notes]
     * None
     */
    class MealHeaderViewHolder(view: View, private val mealTypeMap: Map<String, MealType>, private val onAddButtonClicked: (MealType) -> Unit) : RecyclerView.ViewHolder(view) {
        private val textViewMealName: TextView = view.findViewById(R.id.textViewMealName)
        private val addFoodButton: ImageView = view.findViewById(R.id.addFoodButton)

        fun bind(title: String) {
            textViewMealName.text = title
            addFoodButton.setOnClickListener {
                mealTypeMap[title]?.let { mealType ->
                    onAddButtonClicked(mealType)
                }
            }
        }
    }

    /**
     * FoodItemViewHolder
     * Description: ViewHolder for food item entries in the diary.
     *
     * [Parameters/Arguments]
     * - view: The inflated view.
     *
     * [Return Value]
     * None
     *
     * [Notes]
     * None
     */
    class FoodItemViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val textViewFoodItem: TextView = view.findViewById(R.id.textViewFoodItem)
        private val deleteButton: ImageView = view.findViewById(R.id.imageViewRemove)
        private val textViewCalories: TextView = view.findViewById(R.id.textViewCalories)
        private val textViewCarbs: TextView = view.findViewById(R.id.textViewCarbs)
        private val textViewFats: TextView = view.findViewById(R.id.textViewFats)
        private val textViewProteins: TextView = view.findViewById(R.id.textViewProteins)

        fun bind(consumedFoodItem: ConsumedFoodItem, logId: Int, deleteAction: (Int, Int) -> Unit) {
            textViewFoodItem.text = consumedFoodItem.foodItem.name
            textViewCalories.text = "${consumedFoodItem.foodItem.calories} Calories"
            textViewCarbs.text = "Carb: ${consumedFoodItem.foodItem.carbs}g"
            textViewFats.text = "Fat: ${consumedFoodItem.foodItem.fats}g"
            textViewProteins.text = "Prot: ${consumedFoodItem.foodItem.proteins}g"
            deleteButton.setOnClickListener {
                val adapterPosition = adapterPosition
                deleteAction(adapterPosition, logId)
            }
        }
    }

    /**
     * deleteItem
     * Description: Deletes an item from the diary and notifies the adapter.
     *
     * [Parameters/Arguments]
     * - position: The position of the item to delete.
     * - logId: The ID of the log entry to delete.
     *
     * [Return Value]
     * None
     *
     * [Notes]
     * None
     */
    fun deleteItem(position: Int, logId: Int) {
        dbHelper.deleteFoodLog(logId)
        diaryEntries.removeAt(position)
        notifyItemRemoved(position)
        notifyItemRangeChanged(position, diaryEntries.size - position)
        onItemDeleted()
    }
}