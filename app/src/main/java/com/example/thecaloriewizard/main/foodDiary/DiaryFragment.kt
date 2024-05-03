package com.example.thecaloriewizard.main.foodDiary

import android.content.Context
import android.icu.text.SimpleDateFormat
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.thecaloriewizard.R
import com.example.thecaloriewizard.database.DBHelper
import com.example.thecaloriewizard.dataclasses.FoodItem
import com.example.thecaloriewizard.dataclasses.ListItem
import com.example.thecaloriewizard.dataclasses.MealType
import java.util.Date
import java.util.Locale

interface DiaryUpdateListener {
    fun onDiaryUpdated()
}

/**
 * DiaryFragment
 * Description: Fragment for displaying and managing food diary entries.
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
 * Replace the existing DiaryFragment with an instance of this fragment in the app.
 *
 * [Notes]
 * None
 */
class DiaryFragment : Fragment(), DiaryUpdateListener {

    override fun onDiaryUpdated() {
        refreshMacroBar()
        Log.d("Add Debug", "onDiaryUpdated executed")
    }

    private lateinit var diaryRecyclerView: RecyclerView
    private var diaryEntries = mutableListOf<ListItem>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_diary, container, false)
        diaryRecyclerView = view.findViewById(R.id.diaryRecyclerView)
        diaryRecyclerView.layoutManager = LinearLayoutManager(activity)

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        loadDiaryEntries()
    }

    /**
     * loadDiaryEntries
     * Description: Loads diary entries from the database and sets up the RecyclerView adapter.
     *
     * [Parameters/Arguments]
     * None
     *
     * [Return Value]
     * None
     *
     * [Notes]
     * None
     */
    private fun loadDiaryEntries() {
        Log.d("DiaryFragment", "Start loading diary entries")
        val dbHelper = DBHelper(requireContext())
        val currentUserId = getCurrentUserId()
        val todayDate = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
        val todayFoodLogs = dbHelper.getTodayFoodLogs(currentUserId)

        diaryEntries.clear()
        val mealTypes = mapOf(
            "Breakfast" to MealType.BREAKFAST,
            "Snack 1" to MealType.SNACK1,
            "Lunch" to MealType.LUNCH,
            "Snack 2" to MealType.SNACK2,
            "Dinner" to MealType.DINNER
        )

        mealTypes.forEach { (mealTypeName, mealTypeValue) ->
            diaryEntries.add(ListItem.Header(mealTypeName))
            todayFoodLogs.flatMap { it.consumedFoodItems }
                .filter { it.mealType == mealTypeValue }
                .forEach { consumedFoodItem ->
                    diaryEntries.add(ListItem.FoodItem(consumedFoodItem, consumedFoodItem.logId))
                }
        }

        Log.d("DiaryFragment", "Data loaded, setting up adapter")
        diaryRecyclerView.adapter = DiaryAdapter(
            diaryEntries = diaryEntries,
            dbHelper = dbHelper,
            mealTypeMap = mealTypes,
            onAddButtonClicked = { mealType ->
                showAddFoodOptions(mealType.name)
            },
            onDeleteButtonClicked = { position, logId ->
                (diaryRecyclerView.adapter as DiaryAdapter).deleteItem(position, logId)
            },
            onItemDeleted = {
                refreshMacroBar()
            },
        )

        Log.d("DiaryFragment", "Adapter set, refreshing macro bar")
        refreshMacroBar()
    }

    /**
     * refreshMacroBar
     * Description: Refreshes the macro bar with updated data.
     *
     * [Parameters/Arguments]
     * None
     *
     * [Return Value]
     * None
     *
     * [Notes]
     * None
     */
    private fun refreshMacroBar() {
        Log.d("DiaryFragment", "Refreshing macro bar")
        val dbHelper = DBHelper(requireContext())
        val currentUserId = getCurrentUserId()
        val todayDate = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
        val todayFoodLogs = dbHelper.getTodayFoodLogs(currentUserId)

        var totalCalories = 0.0
        var totalCarbs = 0.0
        var totalFats = 0.0
        var totalProteins = 0.0

        todayFoodLogs.flatMap { it.consumedFoodItems }.forEach {
            totalCalories += it.foodItem.calories
            totalCarbs += it.foodItem.carbs
            totalFats += it.foodItem.fats
            totalProteins += it.foodItem.proteins
        }

        Log.d("DiaryFragment", "Total Calories: $totalCalories")

        view?.findViewById<TextView>(R.id.textViewCalories)?.text = "Calories\n${totalCalories.toInt()}kcal"
        view?.findViewById<TextView>(R.id.textViewCarbs)?.text = "Carbs\n${totalCarbs.toInt()}g"
        view?.findViewById<TextView>(R.id.textViewFats)?.text = "Fats\n${totalFats.toInt()}g"
        view?.findViewById<TextView>(R.id.textViewProteins)?.text = "Proteins\n${totalProteins.toInt()}g"
    }

    /**
     * showAddFoodOptions
     * Description: Displays options for adding a new food item.
     *
     * [Parameters/Arguments]
     * - mealType: The type of meal to add the food item to.
     *
     * [Return Value]
     * None
     *
     * [Notes]
     * None
     */
    private fun showAddFoodOptions(mealType: String) {
        val options = arrayOf("Search", "Create")
        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle("Add Food Item")
            .setItems(options) { dialog, which ->
                when (which) {
                    0 -> {
                        val mealTypeEnum = MealType.valueOf(mealType.uppercase(Locale.ROOT))
                        showSearchInterface(mealTypeEnum)
                    }
                    1 -> showCreateFoodDialog(mealType)
                }
            }
        builder.create().show()
    }

    /**
     * showSearchInterface
     * Description: Displays the interface for searching for a food item to add.
     *
     * [Parameters/Arguments]
     * - mealType: The type of meal to add the food item to.
     *
     * [Return Value]
     * None
     *
     * [Notes]
     * None
     */
    private fun showSearchInterface(mealType: MealType) {
        val searchFragment = SearchFoodFragment().apply {
            diaryUpdateListener = this@DiaryFragment
            arguments = Bundle().apply {
                putString("mealType", mealType.name)
            }
        }
        parentFragmentManager.beginTransaction()
            .replace(R.id.fragmentContainer, searchFragment)
            .addToBackStack(null)
            .commit()
    }

    /**
     * showCreateFoodDialog
     * Description: Displays the dialog for creating a new food item.
     *
     * [Parameters/Arguments]
     * - mealType: The type of meal to add the food item to.
     *
     * [Return Value]
     * None
     *
     * [Notes]
     * None
     */
    private fun showCreateFoodDialog(mealType: String) {
        val dialogView = LayoutInflater.from(context).inflate(R.layout.dialog_create_food, null)
        val spinnerPortionUnit: Spinner = dialogView.findViewById(R.id.spinnerPortionUnit)

        ArrayAdapter.createFromResource(
            requireContext(),
            R.array.portion_units,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spinnerPortionUnit.adapter = adapter
        }

        AlertDialog.Builder(requireContext())
            .setView(dialogView)
            .setTitle("Create New Food Item")
            .setPositiveButton("Save") { dialog, which ->
                val foodName = dialogView.findViewById<EditText>(R.id.editTextFoodName).text.toString()
                val description = dialogView.findViewById<EditText>(R.id.editTextDescription).text.toString()
                val portionUnit = spinnerPortionUnit.selectedItem.toString()
                val portionSizeValue = dialogView.findViewById<EditText>(R.id.editTextPortionSizeValue).text.toString().toDouble()
                val calories = dialogView.findViewById<EditText>(R.id.editTextCalories).text.toString().toInt()
                val carbs = dialogView.findViewById<EditText>(R.id.editTextCarbs).text.toString().toDouble()
                val fats = dialogView.findViewById<EditText>(R.id.editTextFats).text.toString().toDouble()
                val proteins = dialogView.findViewById<EditText>(R.id.editTextProteins).text.toString().toDouble()
                val fibre = dialogView.findViewById<EditText>(R.id.editTextFibre).text.toString().toDouble()
                val sugar = dialogView.findViewById<EditText>(R.id.editTextSugar).text.toString().toDouble()
                val saturatedfat = dialogView.findViewById<EditText>(R.id.editTextSaturatedFat).text.toString().toDouble()
                val polyunsaturatedfat = dialogView.findViewById<EditText>(R.id.editTextPolyunsaturatedFat).text.toString().toDouble()
                val monounsaturatedfat = dialogView.findViewById<EditText>(R.id.editTextMonounsaturatedFat).text.toString().toDouble()
                val cholesterol = dialogView.findViewById<EditText>(R.id.editTextCholesterol).text.toString().toDouble()
                val sodium = dialogView.findViewById<EditText>(R.id.editTextSodium).text.toString().toDouble()
                val potassium = dialogView.findViewById<EditText>(R.id.editTextPotassium).text.toString().toDouble()

                val dbHelper = DBHelper(requireContext())
                val newFoodItem = FoodItem(
                    name = foodName,
                    description = description,
                    calories = calories,
                    portionUnit = portionUnit,
                    portionSizeValue = portionSizeValue,
                    carbs = carbs,
                    fats = fats,
                    proteins = proteins,
                    fibre = fibre,
                    sugar = sugar,
                    saturatedfat = saturatedfat,
                    polyunsaturatedfat = polyunsaturatedfat,
                    monounsaturatedfat = monounsaturatedfat,
                    cholesterol = cholesterol,
                    sodium = sodium,
                    potassium = potassium
                )
                val result = dbHelper.addFoodItem(newFoodItem)
                if (result != -1L) {
                    val currentUserId = getCurrentUserId()
                    val currentDateTime = getCurrentDateTime()

                    val newLogId = dbHelper.addFoodLog(currentUserId, currentDateTime)

                    if (newLogId != -1L) {
                        val logItemResult = dbHelper.addFoodLogItem(newLogId, result, portionSizeValue, mealType, newFoodItem)
                        if (logItemResult != -1L) {
                            Toast.makeText(context, "Food item logged successfully!", Toast.LENGTH_SHORT).show()
                        } else {
                            Toast.makeText(context, "Failed to log food item.", Toast.LENGTH_SHORT).show()
                        }
                    } else {
                        Toast.makeText(context, "Failed to create food log.", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(context, "Failed to save food item.", Toast.LENGTH_SHORT).show()
                }    
            }
            .setNegativeButton("Cancel", null)
            .show()
    }

    /**
     * getCurrentUserId
     * Description: Retrieves the ID of the current user from shared preferences.
     *
     * [Parameters/Arguments]
     * None
     *
     * [Return Value]
     * Int: The ID of the current user.
     *
     * [Notes]
     * None
     */
    private fun getCurrentUserId(): Int {
        val sharedPref = activity?.getSharedPreferences("AppPrefs", Context.MODE_PRIVATE)
        return sharedPref?.getInt("CurrentUserID", -1) ?: -1
    }

    /**
     * getCurrentDateTime
     * Description: Retrieves the current date and time as a formatted string.
     *
     * [Parameters/Arguments]
     * None
     *
     * [Return Value]
     * String: The formatted current date and time.
     *
     * [Notes]
     * None
     */
    private fun getCurrentDateTime(): String {
        val dateFormat = SimpleDateFormat("dd-mm-yyyy HH:mm:ss", Locale.getDefault())
        return dateFormat.format(Date())
    }
}