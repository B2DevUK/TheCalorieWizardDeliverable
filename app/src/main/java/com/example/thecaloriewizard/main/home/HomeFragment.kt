package com.example.thecaloriewizard.main.home

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.thecaloriewizard.R
import com.example.thecaloriewizard.database.DBHelper
import com.example.thecaloriewizard.dataclasses.BmiItem
import com.example.thecaloriewizard.dataclasses.HomeItem
import com.example.thecaloriewizard.dataclasses.MacronutrientItem
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

/**
 * HomeFragment
 * Description: Fragment for displaying home screen content including macronutrient info, water intake, and BMI.
 *
 * [Author]
 * Author Name: Brandon Sharp
 *
 * [Parameters/Arguments]
 * None
 *
 * [Usage]
 * Used to display home screen content including macronutrient info, water intake, and BMI.
 *
 * [Notes]
 * None
 */
class HomeFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var homeAdapter: HomeAdapter
    private lateinit var dbHelper: DBHelper

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_home, container, false)

        dbHelper = DBHelper(requireContext())

        recyclerView = view.findViewById(R.id.homeRecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(context)
        homeAdapter = HomeAdapter(requireContext(), dbHelper)
        recyclerView.adapter = homeAdapter

        loadHomeData(requireContext())

        return view
    }

    /**
     * getCurrentDate
     * Description: Gets the current date in yyyy-MM-dd format.
     *
     * [Parameters/Arguments]
     * None
     *
     * [Return Value]
     * String: The current date in yyyy-MM-dd format.
     *
     * [Notes]
     * None
     */
    private fun getCurrentDate(): String {
        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        return dateFormat.format(Date())
    }

    /**
     * getUserIdFromPrefs
     * Description: Retrieves the user ID from shared preferences.
     *
     * [Parameters/Arguments]
     * - context: The context of the calling activity or fragment.
     *
     * [Return Value]
     * Int: The user ID retrieved from shared preferences.
     *
     * [Notes]
     * None
     */
    private fun getUserIdFromPrefs(context: Context): Int {
        val sharedPrefs = context.getSharedPreferences("AppPrefs", Context.MODE_PRIVATE)
        return sharedPrefs.getInt("CurrentUserID", -1)
    }

    /**
     * loadHomeData
     * Description: Loads macronutrient info, water intake, and BMI data and updates the adapter.
     *
     * [Parameters/Arguments]
     * - context: The context of the calling activity or fragment.
     *
     * [Return Value]
     * None
     *
     * [Notes]
     * None
     */
    private fun loadHomeData(context: Context) {
        val userId = getUserIdFromPrefs(context)
        val currentDate = getCurrentDate()

        val (bmi, classification) = dbHelper.calculateBMI(userId.toString())

        val todayFoodLogs = dbHelper.getTodayFoodLogs(userId)
        var totalCalories = 0
        var totalCarbs = 0.0
        var totalFats = 0.0
        var totalProteins = 0.0
        for (foodLog in todayFoodLogs) {
            for (consumedFoodItem in foodLog.consumedFoodItems) {
                totalCalories += consumedFoodItem.foodItem.calories
                totalCarbs += consumedFoodItem.foodItem.carbs
                totalFats += consumedFoodItem.foodItem.fats
                totalProteins += consumedFoodItem.foodItem.proteins
            }
        }
        val macronutrientItem = MacronutrientItem(totalCalories, totalCarbs.toInt(), totalFats.toInt(), totalProteins.toInt())

        val waterIntake = dbHelper.getWaterIntake(userId, currentDate)

        val bmiItem = BmiItem(bmi, classification)

        val items = mutableListOf<HomeItem>()
        macronutrientItem.let { items.add(it) }
        waterIntake?.let { items.add(it) }
        bmiItem.let { items.add(it) }
        homeAdapter.setItems(items)
    }
}