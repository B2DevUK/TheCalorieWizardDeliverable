package com.example.thecaloriewizard.dataclasses

/**
 * WeighInEntry
 * Description: Represents a single weigh-in entry with weight and date.
 *
 * [Author]
 * Author Name: Brandon Sharp
 *
 * [Parameters/Arguments]
 * - weight: The weight recorded in the entry.
 * - date: The date of the weigh-in entry.
 *
 * [Return Value]
 * None
 *
 * [Usage]
 * val entry = WeighInEntry(weight = 150, date = "2024-05-01")
 *
 * [Notes]
 * None
 */
data class WeighInEntry(
    val weight: Int,
    val date: String
)
