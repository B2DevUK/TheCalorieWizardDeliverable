package com.example.thecaloriewizard.dataclasses

/**
 * [UserData]
 * Description: Represents user data including personal information, physical attributes, goals, and activity level.
 *
 * [Author]
 * Author Name: Brandon Sharp
 *
 * [Parameters/Arguments]
 * - [id]: The unique identifier for the user.
 * - [email]: The email address of the user.
 * - [password]: The password of the user.
 * - [phoneNumber]: The phone number of the user.
 * - [sex]: The sex of the user, defaulting to "Male" but can toggle to "Female".
 * - [age]: The age of the user, defaulting to 10000.
 * - [heightFeet]: The feet part of the user's height, defaulting to 10000.
 * - [heightInches]: The inches part of the user's height, defaulting to 10000.
 * - [weightStone]: The stone part of the user's weight, defaulting to 10000.
 * - [weightPounds]: The pounds part of the user's weight, defaulting to 10000.
 * - [goalWeightStone]: The stone part of the user's goal weight, defaulting to 10000.
 * - [goalWeightPounds]: The pounds part of the user's goal weight, defaulting to 10000.
 * - [goals]: A string representation of the user's selected goals.
 * - [weeklyGoals]: A string representation of the user's selected weekly goals.
 * - [barriers]: A string representation of the user's selected barriers.
 * - [activityLevel]: The activity level of the user, defaulting to "Not active".
 *
 * [Return Value]
 * None
 *
 * [Usage]
 * This data class is used to represent user data, including personal information, physical attributes,
 * goals, and activity level. It provides default values for various attributes.
 *
 */
data class UserData(
    var id: String = "",
    var email: String = "",
    var password: String = "",
    var phoneNumber: String = "",
    var sex: String = "Male",
    var age: Int = 10000,
    var heightFeet: Int = 10000,
    var heightInches: Int = 10000,
    var weightStone: Int = 10000,
    var weightPounds: Int = 10000,
    var goalWeightStone: Int = 10000,
    var goalWeightPounds: Int = 10000,
    var goals: String = "",
    var weeklyGoals: String = "",
    var barriers: String = "",
    var activityLevel: String = "Not active"
)



