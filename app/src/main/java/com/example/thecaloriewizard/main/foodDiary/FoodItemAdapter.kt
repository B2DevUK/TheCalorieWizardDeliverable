package com.example.thecaloriewizard.main.foodDiary

import android.app.AlertDialog
import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.thecaloriewizard.R
import com.example.thecaloriewizard.database.DBHelper
import com.example.thecaloriewizard.dataclasses.FoodItem
import com.example.thecaloriewizard.dataclasses.MealType
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

/**
 * FoodItemAdapter
 * Description: Adapter for displaying food items in a RecyclerView.
 *
 * [Author]
 * Author Name: Brandon Sharp
 *
 * [Parameters/Arguments]
 * - context: The context of the calling activity or fragment.
 * - items: List of food items to display.
 * - mealType: The type of meal associated with the food items.
 * - diaryUpdateListener: Listener interface for updating the diary after adding a food item.
 *
 * [Return Value]
 * None
 *
 * [Usage]
 * Create an instance of this adapter and attach it to a RecyclerView to display food items.
 *
 * [Notes]
 * None
 */
class FoodItemAdapter(
    private val context: Context,
    private var items: List<FoodItem>,
    private val mealType: MealType,
    private val diaryUpdateListener: DiaryUpdateListener?
) : RecyclerView.Adapter<FoodItemAdapter.FoodItemViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FoodItemViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_food, parent, false)
        return FoodItemViewHolder(view, context, diaryUpdateListener)
    }

    override fun onBindViewHolder(holder: FoodItemViewHolder, position: Int) {
        val foodItem = items[position]
        holder.bind(foodItem, mealType)
    }

    override fun getItemCount(): Int = items.size

    /**
     * FoodItemViewHolder
     * Description: ViewHolder for displaying individual food items in the RecyclerView.
     *
     * [Author]
     * Author Name: Brandon Sharp
     *
     * [Parameters/Arguments]
     * - itemView: The view for displaying the food item.
     * - context: The context of the calling activity or fragment.
     * - mealType: The type of meal associated with the food item.
     * - diaryUpdateListener: Listener interface for updating the diary after adding a food item.
     *
     * [Return Value]
     * None
     *
     * [Usage]
     * Used internally by the FoodItemAdapter to manage individual food item views.
     *
     * [Notes]
     * None
     */
    class FoodItemViewHolder(
        itemView: View,
        private val context: Context,
        private val diaryUpdateListener: DiaryUpdateListener?
    ) : RecyclerView.ViewHolder(itemView) {
        private val tvFoodName: TextView = itemView.findViewById(R.id.tvFoodName)
        private val tvCalories: TextView = itemView.findViewById(R.id.tvCalories)
        private val tvCarbs: TextView = itemView.findViewById(R.id.tvCarbs)
        private val tvFats: TextView = itemView.findViewById(R.id.tvFats)
        private val tvProteins: TextView = itemView.findViewById(R.id.tvProteins)

        /**
         * bind
         * Description: Binds the food item data to the view.
         *
         * [Parameters/Arguments]
         * - foodItem: The food item to display.
         * - mealType: The type of meal associated with the food item.
         *
         * [Return Value]
         * None
         *
         * [Notes]
         * None
         */
        fun bind(foodItem: FoodItem, mealType: MealType) {
            tvFoodName.text = foodItem.name
            tvCalories.text = "Calories: ${foodItem.calories} kcal"
            tvCarbs.text = "Carbs: ${foodItem.carbs}g"
            tvFats.text = "Fats: ${foodItem.fats}g"
            tvProteins.text = "Proteins: ${foodItem.proteins}g"

            itemView.setOnClickListener { showDetailDialog(foodItem, mealType) }
        }

        /**
         * showDetailDialog
         * Description: Displays a dialog showing detailed information about the food item.
         *
         * [Parameters/Arguments]
         * - foodItem: The food item to display details for.
         * - mealType: The type of meal associated with the food item.
         *
         * [Return Value]
         * None
         *
         * [Notes]
         * None
         */
        private fun showDetailDialog(foodItem: FoodItem, mealType: MealType) {
            val dialogView = LayoutInflater.from(context).inflate(R.layout.dialog_food_detail, null)
            val tvDetailName: TextView = dialogView.findViewById(R.id.tvDetailName)
            val tvDetailDescription: TextView = dialogView.findViewById(R.id.tvDetailDescription)
            val tvDetailPortionUnit: TextView = dialogView.findViewById(R.id.tvDetailPortionUnit)
            val etPortionSize: EditText = dialogView.findViewById(R.id.tvDetailPortionSize)
            val tvDetailCalories: TextView = dialogView.findViewById(R.id.tvDetailCalories)
            val tvDetailCarbs: TextView = dialogView.findViewById(R.id.tvDetailCarbs)
            val tvDetailFats: TextView = dialogView.findViewById(R.id.tvDetailFats)
            val tvDetailProteins: TextView = dialogView.findViewById(R.id.tvDetailProteins)
            val tvDetailFibre: TextView = dialogView.findViewById(R.id.tvDetailFibre)
            val tvDetailSugar: TextView = dialogView.findViewById(R.id.tvDetailSugar)
            val tvDetailSaturatedFat: TextView = dialogView.findViewById(R.id.tvDetailSaturatedfat)
            val tvDetailPolyunsaturatedfat: TextView = dialogView.findViewById(R.id.tvDetailPolyunsaturatedfat)
            val tvDetailMonounsaturatedfat: TextView = dialogView.findViewById(R.id.tvDetailMonounsaturatedfat)
            val tvDetailCholesterol: TextView = dialogView.findViewById(R.id.tvDetailCholesterol)
            val tvDetailSodium: TextView = dialogView.findViewById(R.id.tvDetailSodium)
            val tvDetailPotassium: TextView = dialogView.findViewById(R.id.tvDetailPotassium)
            var lastScaledFoodItem = foodItem

            tvDetailName.text = foodItem.name
            tvDetailDescription.text = foodItem.description
            tvDetailPortionUnit.text = "Unit: ${foodItem.portionUnit}"
            etPortionSize.setText(foodItem.portionSizeValue.toString())
            tvDetailCalories.text = "Calories: ${foodItem.calories} cal"
            tvDetailCarbs.text = "Carbs: ${foodItem.carbs}g"
            tvDetailFats.text = "Fats: ${foodItem.fats}g"
            tvDetailProteins.text = "Proteins: ${foodItem.proteins}g"
            tvDetailFibre.text = "Fibre: ${foodItem.fibre}g"
            tvDetailSugar.text = "Sugar: ${foodItem.sugar}g"
            tvDetailSaturatedFat.text = "Saturated Fat: ${foodItem.saturatedfat}g"
            tvDetailPolyunsaturatedfat.text = "Polyunsaturated Fat: ${foodItem.polyunsaturatedfat}g"
            tvDetailMonounsaturatedfat.text = "Monounsaturated Fat: ${foodItem.monounsaturatedfat}g"
            tvDetailCholesterol.text = "Cholesterol: ${foodItem.cholesterol}mg"
            tvDetailSodium.text = "Sodium: ${foodItem.sodium}mg"
            tvDetailPotassium.text = "Potassium: ${foodItem.potassium}mg"

            val updateNutrientTexts = fun(scaledFoodItem: FoodItem) {
                lastScaledFoodItem = scaledFoodItem
                tvDetailCalories.text = "Calories: ${scaledFoodItem.calories} cal"
                tvDetailCarbs.text = "Carbs: ${String.format("%.2f", scaledFoodItem.carbs)}g"
                tvDetailFats.text = "Fats: ${String.format("%.2f", scaledFoodItem.fats)}g"
                tvDetailProteins.text = "Proteins: ${String.format("%.2f", scaledFoodItem.proteins)}g"
                tvDetailFibre.text = "Fibre: ${String.format("%.2f", scaledFoodItem.fibre)}g"
                tvDetailSugar.text = "Sugar: ${String.format("%.2f", scaledFoodItem.sugar)}g"
                tvDetailSaturatedFat.text = "Saturated Fat: ${String.format("%.2f", scaledFoodItem.saturatedfat)}g"
                tvDetailPolyunsaturatedfat.text = "Polyunsaturated Fat: ${String.format("%.2f", scaledFoodItem.polyunsaturatedfat)}g"
                tvDetailMonounsaturatedfat.text = "Monounsaturated Fat: ${String.format("%.2f", scaledFoodItem.monounsaturatedfat)}g"
                tvDetailCholesterol.text = "Cholesterol: ${String.format("%.2f", scaledFoodItem.cholesterol)}mg"
                tvDetailSodium.text = "Sodium: ${String.format("%.2f", scaledFoodItem.sodium)}mg"
                tvDetailPotassium.text = "Potassium: ${String.format("%.2f", scaledFoodItem.potassium)}mg"
            }

            updateNutrientTexts(foodItem)

            etPortionSize.addTextChangedListener(object : TextWatcher {
                override fun afterTextChanged(s: Editable?) {
                    s?.toString()?.toDoubleOrNull()?.let {
                        val scale = it / foodItem.portionSizeValue
                        updateNutrientTexts(foodItem.scale(scale))
                    }
                }

                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            })

            AlertDialog.Builder(context)
                .setView(dialogView)
                .setNegativeButton("Cancel", null)
                .setPositiveButton("Add") { dialog, _ ->
                    val activity = context as? AppCompatActivity
                    activity?.let {
                        val sharedPrefs = it.getSharedPreferences("AppPrefs", Context.MODE_PRIVATE)
                        val userId = sharedPrefs.getInt("CurrentUserID", -1)

                        if (userId == -1) {
                            Toast.makeText(it, "Error: User ID not found.", Toast.LENGTH_SHORT).show()
                            return@setPositiveButton
                        }

                        val dbHelper = DBHelper(it)
                        val dateTime = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(Date())
                        val logId = dbHelper.addFoodLog(userId, dateTime)

                        val portionSize = etPortionSize.text.toString().toDouble()
                        val itemId = lastScaledFoodItem.foodId?.let { foodId ->
                            dbHelper.addFoodLogItem(logId, foodId.toLong(), portionSize, mealType.name, lastScaledFoodItem)
                        }

                        if (itemId != -1L) {
                            Toast.makeText(it, "Food item added to diary successfully.", Toast.LENGTH_SHORT).show()
                            diaryUpdateListener?.onDiaryUpdated()

                        } else {
                            Toast.makeText(it, "Failed to add food item to diary.", Toast.LENGTH_SHORT).show()
                        }

                        dialog.dismiss()
                        it.supportFragmentManager.beginTransaction()
                            .replace(R.id.fragmentContainer, DiaryFragment())
                            .commit()
                    } ?: throw IllegalStateException("Context used is not an AppCompatActivity")
                }
                .show()
        }

        /**
         * scale
         * Description: Scales the food item's nutrients based on a given scale factor.
         *
         * [Parameters/Arguments]
         * - scaleFactor: The factor by which to scale the food item's nutrients.
         *
         * [Return Value]
         * FoodItem: The scaled food item.
         *
         * [Notes]
         * None
         */
        private fun FoodItem.scale(scaleFactor: Double): FoodItem {
            return this.copy(
                calories = (this.calories * scaleFactor).toInt(),
                carbs = this.carbs * scaleFactor,
                fats = this.fats * scaleFactor,
                proteins = this.proteins * scaleFactor,
                fibre = this.fibre * scaleFactor,
                sugar = this.sugar * scaleFactor,
                saturatedfat = this.saturatedfat * scaleFactor,
                polyunsaturatedfat = this.polyunsaturatedfat * scaleFactor,
                monounsaturatedfat = this.monounsaturatedfat * scaleFactor,
                cholesterol = this.cholesterol * scaleFactor,
                sodium = this.sodium * scaleFactor,
                potassium = this.sodium * scaleFactor
            )
        }
    }

    /**
     * updateItems
     * Description: Updates the list of food items displayed in the RecyclerView.
     *
     * [Parameters/Arguments]
     * - newItems: The new list of food items to display.
     *
     * [Return Value]
     * None
     *
     * [Notes]
     * None
     */
    fun updateItems(newItems: List<FoodItem>) {
        this.items = newItems
        notifyDataSetChanged()
    }
}