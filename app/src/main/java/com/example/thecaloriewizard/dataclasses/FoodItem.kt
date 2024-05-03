package com.example.thecaloriewizard.dataclasses

/**
 * [FoodItem]
 * Description: Represents a food item with nutritional information, including ID (if available), name,
 * description, portion unit, portion size, calories, macronutrients, fiber, sugar, and additional properties.
 *
 * [Author]
 * Author Name: Brandon Sharp
 *
 * [Parameters/Arguments]
 * - [foodId]: The unique identifier for the food item (auto-incremented ID; nullable when creating a new entry).
 * - [name]: The name of the food item.
 * - [description]: A brief description of the food item.
 * - [portionUnit]: The unit of measurement for the portion size (e.g., grams, millilitres).
 * - [portionSizeValue]: The size of the portion.
 * - [calories]: The amount of calories in the food item.
 * - [carbs]: The amount of carbohydrates in grams.
 * - [fats]: The amount of fats in grams.
 * - [proteins]: The amount of proteins in grams.
 * - [fibre]: The amount of fiber in grams.
 * - [sugar]: The amount of sugar in grams.
 * - [saturatedfat]: The amount of saturated fat in grams.
 * - [polyunsaturatedfat]: The amount of polyunsaturated fat in grams.
 * - [monounsaturatedfat]: The amount of monounsaturated fat in grams.
 * - [cholesterol]: The amount of cholesterol in milligrams.
 * - [sodium]: The amount of sodium in milligrams.
 * - [potassium]: The amount of potassium in milligrams.
 * - [mealType]: The type of meal associated with the food item (e.g., breakfast, lunch). Defaults to an empty string.
 *
 * [Return Value]
 * None
 *
 * [Usage]
 * This data class can be used to represent various food items with their nutritional information for tracking
 * and management purposes.
 *
 * [Notes]
 * - The [foodId] can be nullable when creating a new entry as it's auto-incremented.
 * - All nutritional values are represented in standard units (e.g., grams for macronutrients, milligrams for others).
 * - Additional properties can be added as needed to represent specific nutritional or dietary information.
 */
data class FoodItem(
    val foodId: Int? = null,
    val name: String,
    val description: String,
    val portionUnit: String,
    val portionSizeValue: Double,
    val calories: Int,
    val carbs: Double,
    val fats: Double,
    val proteins: Double,
    val fibre: Double,
    val sugar: Double,
    val saturatedfat: Double,
    val polyunsaturatedfat: Double,
    val monounsaturatedfat: Double,
    val cholesterol: Double,
    val sodium: Double,
    val potassium: Double,
    val mealType: String = ""
)
