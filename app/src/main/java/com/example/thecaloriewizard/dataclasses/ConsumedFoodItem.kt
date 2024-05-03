package com.example.thecaloriewizard.dataclasses


/**
 * [ConsumedFoodItem]
 * Description: Represents a food item that has been consumed, including details such as the food item itself,
 * portion size, meal type, and associated log ID.
 *
 * [Author]
 * Author Name: Brandon Sharp
 *
 * [Parameters/Arguments]
 * - [foodItem]: The food item that has been consumed.
 * - [portionSizeValue]: The size of the portion consumed.
 * - [mealType]: The type of meal during which the food item was consumed (e.g., breakfast, lunch).
 * - [logId]: The unique identifier associated with the log entry for this consumed food item.
 *
 * [Return Value]
 * None
 *
 * [Usage]
 * This data class can be used to represent consumed food items and store them in logs or databases.
 *
 * [Notes]
 * - The [portionSizeValue] represents the quantity or size of the consumed food item.
 * - The [logId] is used to uniquely identify the log entry associated with this consumed food item.
 */
data class ConsumedFoodItem(
    val foodItem: FoodItem,
    val portionSizeValue: Double,
    val mealType: MealType,
    val logId: Int
)

/**
 * [MealType]
 * Description: Represents the type of meal.
 */
enum class MealType {
    BREAKFAST, LUNCH, DINNER, SNACK1, SNACK2
}
