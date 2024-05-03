package com.example.thecaloriewizard.dataclasses

/**
 * [FoodLog]
 * Description: Represents an entry in the food log, which can contain multiple consumed food items.
 *
 * [Author]
 * Author Name: Brandon Sharp
 *
 * [Parameters/Arguments]
 * - [logId]: The unique identifier for the food log entry (auto-incremented ID; nullable when creating a new entry).
 * - [userId]: The ID of the user to whom the log belongs.
 * - [dateTime]: The date and time when the food was logged.
 * - [consumedFoodItems]: A list of consumed food items with their serving sizes.
 *
 * [Return Value]
 * None
 *
 * [Usage]
 * This data class is used to represent entries in a food log, providing details such as the user ID, date and time
 * of the log entry, and the list of consumed food items along with their serving sizes.
 *
 * [Notes]
 * - The [logId] can be nullable when creating a new entry as it's often auto-incremented in databases.
 * - The [userId] identifies the user to whom the food log entry belongs.
 * - The [dateTime] represents the timestamp of the food log entry.
 * - The [consumedFoodItems] list contains details of the food items consumed during the logged meal.
 */
data class FoodLog(
    val logId: Int? = null,
    val userId: Int,
    val dateTime: String,
    val consumedFoodItems: List<ConsumedFoodItem>
)

