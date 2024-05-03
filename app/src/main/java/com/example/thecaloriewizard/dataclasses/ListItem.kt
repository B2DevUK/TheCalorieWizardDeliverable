package com.example.thecaloriewizard.dataclasses

/**
 * [ListItem]
 * Description: Represents an item displayed in a list. This is a sealed class with various subclasses
 * representing different types of list items.
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
 * This sealed class is used to model different types of items that can be displayed in a list.
 * Each subclass represents a specific type of list item, such as header items or food items.
 *
 * [Notes]
 * - This class is intended to be used as a base class for different types of items displayed in a list.
 * - Subclasses of this sealed class represent specific types of list items, such as header items or food items.
 */
sealed class ListItem {
    /**
     * [Header]
     * Description: Represents a header item displayed in a list, containing a title.
     */
    data class Header(val title: String) : ListItem()

    /**
     * [FoodItem]
     * Description: Represents a food item displayed in a list, including the consumed food item and its associated log ID.
     */
    data class FoodItem(val consumedFoodItem: ConsumedFoodItem, val logId: Int) : ListItem()
}

