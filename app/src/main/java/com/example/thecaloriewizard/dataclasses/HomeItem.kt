package com.example.thecaloriewizard.dataclasses

/**
 * [HomeItem]
 * Description: Represents an item displayed on the home screen. This is a sealed class with various subclasses
 * representing different types of items.
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
 * This sealed class is used to model different types of items that can be displayed on the home screen.
 * Each subclass represents a specific type of item.
 *
 * [Notes]
 * - This class is intended to be used as a base class for different types of items displayed on the home screen.
 * - Subclasses of this sealed class represent specific types of home items, such as macronutrient items, BMI items,
 *   and water intake items.
 */
sealed class HomeItem

/**
 * [MacronutrientItem]
 * Description: Represents a macronutrient item displayed on the home screen, including details such as calories,
 * carbohydrates, fats, and proteins.
 */
data class MacronutrientItem(
    val calories: Int,
    val carbs: Int,
    val fats: Int,
    val proteins: Int
) : HomeItem()

/**
 * [BmiItem]
 * Description: Represents a BMI (Body Mass Index) item displayed on the home screen, including BMI value and
 * weight classification.
 */
data class BmiItem(
    val bmi: Double,
    val weightClassification: String
) : HomeItem()

/**
 * [WaterIntakeItem]
 * Description: Represents a water intake item displayed on the home screen, including the current water intake level.
 */
data class WaterIntakeItem(
    var currentWaterIntake: Int
) : HomeItem()

