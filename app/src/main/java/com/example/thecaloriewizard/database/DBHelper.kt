package com.example.thecaloriewizard.database

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log
import com.example.thecaloriewizard.dataclasses.ConsumedFoodItem
import com.example.thecaloriewizard.dataclasses.FoodItem
import com.example.thecaloriewizard.dataclasses.FoodLog
import com.example.thecaloriewizard.dataclasses.MealType
import com.example.thecaloriewizard.dataclasses.UserData
import com.example.thecaloriewizard.dataclasses.WaterIntakeItem
import com.example.thecaloriewizard.dataclasses.WeighInEntry
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class DBHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_VERSION = 12
        private const val DATABASE_NAME = "TheCalorieWizard.db"

        private const val TABLE_USER_DATA = "TUserData"
        private const val COLUMN_ID = "id"
        private const val COLUMN_EMAIL = "email"
        private const val COLUMN_PASSWORD = "password"
        private const val COLUMN_PHONE_NUMBER = "phoneNumber"
        private const val COLUMN_SEX = "sex"
        private const val COLUMN_AGE = "age"
        private const val COLUMN_HEIGHT_FEET = "heightFeet"
        private const val COLUMN_HEIGHT_INCHES = "heightInches"
        private const val COLUMN_WEIGHT_STONE = "weightStone"
        private const val COLUMN_WEIGHT_POUNDS = "weightPounds"
        private const val COLUMN_GOAL_WEIGHT_STONE = "goalWeightStone"
        private const val COLUMN_GOAL_WEIGHT_POUNDS = "goalWeightPounds"
        private const val COLUMN_GOALS = "goals"
        private const val COLUMN_WEEKLY_GOALS = "weeklyGoals"
        private const val COLUMN_BARRIERS = "barriers"
        private const val COLUMN_ACTIVITY_LEVEL = "activityLevel"

        // Food Items Table
        private const val TABLE_FOOD_ITEMS = "TFoodItems"
        private const val COLUMN_FOOD_ID = "foodId"
        private const val COLUMN_FOOD_NAME = "name"
        private const val COLUMN_FOOD_PORTION_UNIT = "portionUnit"
        private const val COLUMN_FOOD_PORTION_SIZE_VALUE = "portionSizeValue"
        private const val COLUMN_DESCRIPTION = "description"
        private const val COLUMN_CALORIES = "calories"
        private const val COLUMN_CARBS = "carbs"
        private const val COLUMN_FATS = "fats"
        private const val COLUMN_PROTEINS = "proteins"
        private const val COLUMN_FIBRES = "fibre"
        private const val COLUMN_SUGARS = "sugar"
        private const val COLUMN_SATURATED_FATS = "saturatedfat"
        private const val COLUMN_POLYUNSATURATED_FATS = "polyunsaturatedfat"
        private const val COLUMN_MONOUNSATURATED_FATS = "monounsaturatedfat"
        private const val COLUMN_CHOLESTEROL = "cholesterol"
        private const val COLUMN_SODIUM = "sodium"
        private const val COLUMN_POTASSIUM = "potassium"

        // Food Logs Table
        private const val TABLE_FOOD_LOGS = "TFoodLogs"
        private const val COLUMN_LOG_ID = "logId"
        private const val COLUMN_USER_ID_FK = "userId"
        private const val COLUMN_DATE_TIME = "dateTime"

        // Food Log Items Junction Table (Linking Food Logs with Food Items)
        private const val TABLE_FOOD_LOG_ITEMS = "TFoodLogItems"
        private const val COLUMN_FOOD_ID_FK = "foodId"
        private const val COLUMN_LOG_ID_FK = "logId"
        private const val COLUMN_SERVING_SIZE = "servingSize"
        private const val COLUMN_MEAL_TYPE = "mealType"
        private const val COLUMN_FLI_CALORIES = "calories"
        private const val COLUMN_FLI_CARBS = "carbs"
        private const val COLUMN_FLI_FATS = "fats"
        private const val COLUMN_FLI_PROTEINS = "proteins"
        private const val COLUMN_FLI_FIBRES = "fibre"
        private const val COLUMN_FLI_SUGARS = "sugar"
        private const val COLUMN_FLI_SATURATED_FATS = "saturatedfat"
        private const val COLUMN_FLI_POLYUNSATURATED_FATS = "polyunsaturatedfat"
        private const val COLUMN_FLI_MONOUNSATURATED_FATS = "monounsaturatedfat"
        private const val COLUMN_FLI_CHOLESTEROL = "cholesterol"
        private const val COLUMN_FLI_SODIUM = "sodium"
        private const val COLUMN_FLI_POTASSIUM = "potassium"

        // Water Intake Logs Table
        private const val TABLE_WATER_LOGS = "TWaterLogs"
        private const val COLUMN_WATER_LOG_ID = "waterLogId"
        private const val COLUMN_USER_ID_FK_W = "userId"
        private const val COLUMN_DATE = "date"
        private const val COLUMN_WATER_AMOUNT = "waterAmount"

        // Weight Entries Table
        private const val TABLE_WEIGHT_ENTRIES = "TWeightEntries"
        private const val COLUMN_WEIGHT_ID = "weightId"
        private const val COLUMN_USER_ID_FK_WE = "userId"
        private const val COLUMN_WEIGHT = "weight"
        private const val COLUMN_WEIGHT_DATE = "date"

    }

    @Suppress("LocalVariableName")
    override fun onCreate(db: SQLiteDatabase?) {
        val CREATE_USER_DATA_TABLE = ("CREATE TABLE $TABLE_USER_DATA(" +
                "$COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT," +
                "$COLUMN_EMAIL TEXT," +
                "$COLUMN_PASSWORD TEXT," +
                "$COLUMN_PHONE_NUMBER TEXT," +
                "$COLUMN_SEX TEXT," +
                "$COLUMN_AGE INTEGER," +
                "$COLUMN_HEIGHT_FEET INTEGER," +
                "$COLUMN_HEIGHT_INCHES INTEGER," +
                "$COLUMN_WEIGHT_STONE INTEGER," +
                "$COLUMN_WEIGHT_POUNDS INTEGER," +
                "$COLUMN_GOAL_WEIGHT_STONE INTEGER," +
                "$COLUMN_GOAL_WEIGHT_POUNDS INTEGER," +
                "$COLUMN_GOALS TEXT," +
                "$COLUMN_WEEKLY_GOALS TEXT," +
                "$COLUMN_BARRIERS TEXT," +
                "$COLUMN_ACTIVITY_LEVEL TEXT)")

        // For TFoodItems
        val CREATE_FOOD_ITEMS_TABLE = ("CREATE TABLE $TABLE_FOOD_ITEMS (" +
                "$COLUMN_FOOD_ID INTEGER PRIMARY KEY AUTOINCREMENT," +
                "$COLUMN_FOOD_NAME TEXT," +
                "$COLUMN_DESCRIPTION TEXT," +
                "$COLUMN_FOOD_PORTION_UNIT TEXT," +
                "$COLUMN_FOOD_PORTION_SIZE_VALUE TEXT," +
                "$COLUMN_CALORIES INTEGER," +
                "$COLUMN_CARBS REAL," +
                "$COLUMN_FATS REAL," +
                "$COLUMN_PROTEINS REAL," +
                "$COLUMN_FIBRES REAL," +
                "$COLUMN_SUGARS REAL," +
                "$COLUMN_SATURATED_FATS REAL," +
                "$COLUMN_POLYUNSATURATED_FATS REAL," +
                "$COLUMN_MONOUNSATURATED_FATS REAL," +
                "$COLUMN_CHOLESTEROL REAL," +
                "$COLUMN_SODIUM REAL," +
                "$COLUMN_POTASSIUM REAL)")

        // For TFoodLogs
        val CREATE_FOOD_LOGS_TABLE = ("CREATE TABLE $TABLE_FOOD_LOGS (" +
                "$COLUMN_LOG_ID INTEGER PRIMARY KEY AUTOINCREMENT," +
                "$COLUMN_USER_ID_FK INTEGER," +
                "$COLUMN_DATE_TIME TEXT," +
                "FOREIGN KEY($COLUMN_USER_ID_FK) REFERENCES TUserData($COLUMN_ID))")

        // For TFoodLogItems
        val CREATE_FOOD_LOG_ITEMS_TABLE = ("CREATE TABLE $TABLE_FOOD_LOG_ITEMS (" +
                "$COLUMN_LOG_ID_FK INTEGER," +
                "$COLUMN_FOOD_ID_FK INTEGER," +
                "$COLUMN_SERVING_SIZE REAL," +
                "$COLUMN_MEAL_TYPE TEXT," +
                "$COLUMN_FLI_CALORIES REAL," +
                "$COLUMN_FLI_CARBS REAL," +
                "$COLUMN_FLI_FATS REAL," +
                "$COLUMN_FLI_PROTEINS REAL," +
                "$COLUMN_FLI_FIBRES REAL," +
                "$COLUMN_FLI_SUGARS REAL," +
                "$COLUMN_FLI_SATURATED_FATS REAL," +
                "$COLUMN_FLI_POLYUNSATURATED_FATS REAL," +
                "$COLUMN_FLI_MONOUNSATURATED_FATS REAL," +
                "$COLUMN_FLI_CHOLESTEROL REAL," +
                "$COLUMN_FLI_SODIUM REAL," +
                "$COLUMN_FLI_POTASSIUM REAL," +
                "FOREIGN KEY($COLUMN_LOG_ID_FK) REFERENCES $TABLE_FOOD_LOGS($COLUMN_LOG_ID)," +
                "FOREIGN KEY($COLUMN_FOOD_ID_FK) REFERENCES $TABLE_FOOD_ITEMS($COLUMN_FOOD_ID))")

        val CREATE_WATER_LOGS_TABLE = ("CREATE TABLE $TABLE_WATER_LOGS(" +
                "$COLUMN_WATER_LOG_ID INTEGER PRIMARY KEY AUTOINCREMENT," +
                "$COLUMN_USER_ID_FK_W INTEGER," +
                "$COLUMN_DATE TEXT," +
                "$COLUMN_WATER_AMOUNT INTEGER," +
                "FOREIGN KEY ($COLUMN_USER_ID_FK) REFERENCES $TABLE_USER_DATA($COLUMN_USER_ID_FK)" +
                ");")

        val CREATE_WEIGHT_ENTRIES_TABLE = ("CREATE TABLE $TABLE_WEIGHT_ENTRIES(" +
                "$COLUMN_WEIGHT_ID INTEGER PRIMARY KEY AUTOINCREMENT," +
                "$COLUMN_USER_ID_FK_WE INTEGER," +
                "$COLUMN_WEIGHT REAL," +
                "$COLUMN_WEIGHT_DATE TEXT," +
                "FOREIGN KEY($COLUMN_USER_ID_FK) REFERENCES $TABLE_USER_DATA($COLUMN_USER_ID_FK)" +
                ");")

        db?.execSQL(CREATE_USER_DATA_TABLE)
        db?.execSQL(CREATE_FOOD_ITEMS_TABLE)
        db?.execSQL(CREATE_FOOD_LOGS_TABLE)
        db?.execSQL(CREATE_FOOD_LOG_ITEMS_TABLE)
        db?.execSQL(CREATE_WATER_LOGS_TABLE)
        db?.execSQL(CREATE_WEIGHT_ENTRIES_TABLE)

    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL("DROP TABLE IF EXISTS $TABLE_USER_DATA")
        db?.execSQL("DROP TABLE IF EXISTS $TABLE_FOOD_ITEMS")
        db?.execSQL("DROP TABLE IF EXISTS $TABLE_FOOD_LOGS")
        db?.execSQL("DROP TABLE IF EXISTS $TABLE_FOOD_LOG_ITEMS")
        db?.execSQL("DROP TABLE IF EXISTS $TABLE_WATER_LOGS")
        db?.execSQL("DROP TABLE IF EXISTS $TABLE_WEIGHT_ENTRIES")

        onCreate(db)
    }

    // USER CRUD OPERATIONS
    fun addUser(userData: UserData) {
        val db = this.writableDatabase

        val values = ContentValues().apply {
            put(COLUMN_EMAIL, userData.email)
            put(COLUMN_PASSWORD, userData.password)
            put(COLUMN_PHONE_NUMBER, userData.phoneNumber)
            put(COLUMN_SEX, userData.sex)
            put(COLUMN_AGE, userData.age)
            put(COLUMN_HEIGHT_FEET, userData.heightFeet)
            put(COLUMN_HEIGHT_INCHES, userData.heightInches)
            put(COLUMN_WEIGHT_STONE, userData.weightStone)
            put(COLUMN_WEIGHT_POUNDS, userData.weightPounds)
            put(COLUMN_GOAL_WEIGHT_STONE, userData.goalWeightStone)
            put(COLUMN_GOAL_WEIGHT_POUNDS, userData.goalWeightPounds)
            put(COLUMN_GOALS, userData.goals)
            put(COLUMN_WEEKLY_GOALS, userData.weeklyGoals)
            put(COLUMN_BARRIERS, userData.barriers)
            put(COLUMN_ACTIVITY_LEVEL, userData.activityLevel)
        }

        db.insert(TABLE_USER_DATA, null, values)
        db.close()
    }

    fun getUser(email: String): UserData? {
        val db = this.readableDatabase

        val cursor = db.query(
            TABLE_USER_DATA,
            arrayOf(COLUMN_ID, COLUMN_EMAIL, COLUMN_PASSWORD, COLUMN_PHONE_NUMBER, COLUMN_SEX, COLUMN_AGE, COLUMN_HEIGHT_FEET, COLUMN_HEIGHT_INCHES, COLUMN_WEIGHT_STONE, COLUMN_WEIGHT_POUNDS, COLUMN_GOAL_WEIGHT_STONE, COLUMN_GOAL_WEIGHT_POUNDS, COLUMN_GOALS, COLUMN_WEEKLY_GOALS, COLUMN_BARRIERS, COLUMN_ACTIVITY_LEVEL),
            "$COLUMN_EMAIL=?",
            arrayOf(email),
            null,
            null,
            null
        )

        var userData: UserData? = null
        if (cursor.moveToFirst()) {
            userData = UserData(
                id = cursor.getString(0),
                email = cursor.getString(1),
                password = cursor.getString(2),
                phoneNumber = cursor.getString(3),
                sex = cursor.getString(4),
                age = cursor.getInt(5),
                heightFeet = cursor.getInt(6),
                heightInches = cursor.getInt(7),
                weightStone = cursor.getInt(8),
                weightPounds = cursor.getInt(9),
                goalWeightStone = cursor.getInt(10),
                goalWeightPounds = cursor.getInt(11),
                goals = cursor.getString(12),
                weeklyGoals = cursor.getString(13),
                barriers = cursor.getString(14),
                activityLevel = cursor.getString(15)
            )
        }
        cursor.close()
        db.close()
        return userData
    }

    fun getUserByIdFull(userId: String): UserData? {
        val db = this.readableDatabase
        val projection = arrayOf(
            COLUMN_ID, COLUMN_EMAIL, COLUMN_PASSWORD, COLUMN_PHONE_NUMBER, COLUMN_SEX, COLUMN_AGE,
            COLUMN_HEIGHT_FEET, COLUMN_HEIGHT_INCHES, COLUMN_WEIGHT_STONE, COLUMN_WEIGHT_POUNDS,
            COLUMN_GOAL_WEIGHT_STONE, COLUMN_GOAL_WEIGHT_POUNDS, COLUMN_GOALS, COLUMN_WEEKLY_GOALS,
            COLUMN_BARRIERS, COLUMN_ACTIVITY_LEVEL
        )

        val cursor = db.query(
            TABLE_USER_DATA,
            projection,
            "$COLUMN_ID = ?",
            arrayOf(userId),
            null, null, null
        )

        var userData: UserData? = null
        if (cursor.moveToFirst()) {
            userData = UserData(
                id = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_ID)),
                email = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_EMAIL)),
                password = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_PASSWORD)),
                phoneNumber = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_PHONE_NUMBER)),
                sex = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_SEX)),
                age = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_AGE)),
                heightFeet = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_HEIGHT_FEET)),
                heightInches = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_HEIGHT_INCHES)),
                weightStone = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_WEIGHT_STONE)),
                weightPounds = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_WEIGHT_POUNDS)),
                goalWeightStone = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_GOAL_WEIGHT_STONE)),
                goalWeightPounds = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_GOAL_WEIGHT_POUNDS)),
                goals = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_GOALS)),
                weeklyGoals = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_WEEKLY_GOALS)),
                barriers = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_BARRIERS)),
                activityLevel = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_ACTIVITY_LEVEL))
            )
        }
        cursor.close()
        db.close()
        return userData
    }

    fun updateUser(userData: UserData): Boolean {
        val db = this.writableDatabase
        try {
            val values = ContentValues().apply {
                put(COLUMN_EMAIL, userData.email)
                put(COLUMN_PASSWORD, userData.password)
                put(COLUMN_PHONE_NUMBER, userData.phoneNumber)
                put(COLUMN_GOAL_WEIGHT_STONE, userData.goalWeightStone)
                put(COLUMN_GOAL_WEIGHT_POUNDS, userData.goalWeightPounds)
            }

            val rowsAffected = db.update(TABLE_USER_DATA, values, "$COLUMN_ID = ?", arrayOf(userData.id))
            return rowsAffected > 0
        } catch (e: Exception) {
            Log.e("DBHelper", "Error updating user: ${e.message}")
            return false
        } finally {
            db.close()
        }
    }


    // FOODITEM CRUD OPERATIONS

    fun addFoodItem(foodItem: FoodItem): Long {
        val db = this.writableDatabase
        val values = ContentValues()

        values.put(COLUMN_FOOD_NAME, foodItem.name)
        values.put(COLUMN_DESCRIPTION, foodItem.description)
        values.put(COLUMN_FOOD_PORTION_UNIT, foodItem.portionUnit)
        values.put(COLUMN_FOOD_PORTION_SIZE_VALUE, foodItem.portionSizeValue)
        values.put(COLUMN_CALORIES, foodItem.calories)
        values.put(COLUMN_CARBS, foodItem.carbs)
        values.put(COLUMN_FATS, foodItem.fats)
        values.put(COLUMN_PROTEINS, foodItem.proteins)
        values.put(COLUMN_FIBRES, foodItem.fibre)
        values.put(COLUMN_SUGARS, foodItem.sugar)
        values.put(COLUMN_SATURATED_FATS, foodItem.saturatedfat)
        values.put(COLUMN_POLYUNSATURATED_FATS, foodItem.polyunsaturatedfat)
        values.put(COLUMN_MONOUNSATURATED_FATS, foodItem.monounsaturatedfat)
        values.put(COLUMN_CHOLESTEROL, foodItem.cholesterol)
        values.put(COLUMN_SODIUM, foodItem.sodium)
        values.put(COLUMN_POTASSIUM, foodItem.potassium)


        val success = db.insert(TABLE_FOOD_ITEMS, null, values)
        db.close()
        return success
    }

    fun searchFoodItems(query: String): List<FoodItem> {
        val matchedItems = mutableListOf<FoodItem>()
        val db = this.readableDatabase

        val cursor = db.query(
            TABLE_FOOD_ITEMS,
            arrayOf(COLUMN_FOOD_ID, COLUMN_FOOD_NAME, COLUMN_FOOD_PORTION_UNIT, COLUMN_FOOD_PORTION_SIZE_VALUE, COLUMN_DESCRIPTION, COLUMN_CALORIES, COLUMN_CARBS, COLUMN_FATS, COLUMN_PROTEINS, COLUMN_FIBRES, COLUMN_SUGARS, COLUMN_SATURATED_FATS, COLUMN_POLYUNSATURATED_FATS, COLUMN_MONOUNSATURATED_FATS, COLUMN_CHOLESTEROL, COLUMN_SODIUM, COLUMN_POTASSIUM),
            "$COLUMN_FOOD_NAME LIKE ?",
            arrayOf("%$query%"),
            null,
            null,
            null
        )

        while (cursor.moveToNext()) {
            val foodItem = FoodItem(
                foodId = cursor.getInt(0),
                name = cursor.getString(1),
                portionUnit = cursor.getString(2),
                portionSizeValue = cursor.getDouble(3),
                description = cursor.getString(4),
                calories = cursor.getInt(5),
                carbs = cursor.getDouble(6),
                fats = cursor.getDouble(7),
                proteins = cursor.getDouble(8),
                fibre = cursor.getDouble(9),
                sugar = cursor.getDouble(10),
                saturatedfat = cursor.getDouble(11),
                polyunsaturatedfat = cursor.getDouble(12),
                monounsaturatedfat = cursor.getDouble(13),
                cholesterol = cursor.getDouble(14),
                sodium = cursor.getDouble(15),
                potassium = cursor.getDouble(16)
            )
            matchedItems.add(foodItem)
        }
        cursor.close()
        db.close()

        return matchedItems
    }


    // FOODLOGENTRY CRUD OPERATIONS

    fun addFoodLog(userId: Int, dateTime: String): Long {
        val db = this.writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_USER_ID_FK, userId)
            put(COLUMN_DATE_TIME, dateTime)
        }
        val logId = db.insert(TABLE_FOOD_LOGS, null, values)
        db.close()
        return logId
    }

    fun addFoodLogItem(logId: Long, foodId: Long, servingSize: Double, mealType: String, foodItem: FoodItem): Long {
        val db = this.writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_LOG_ID_FK, logId)
            put(COLUMN_FOOD_ID_FK, foodId)
            put(COLUMN_SERVING_SIZE, servingSize)
            put(COLUMN_MEAL_TYPE, mealType)
            put(COLUMN_FLI_CALORIES, foodItem.calories)
            put(COLUMN_FLI_CARBS, foodItem.carbs)
            put(COLUMN_FLI_FATS, foodItem.fats)
            put(COLUMN_FLI_PROTEINS, foodItem.proteins)
            put(COLUMN_FLI_FIBRES, foodItem.fibre)
            put(COLUMN_FLI_SUGARS, foodItem.sugar)
            put(COLUMN_FLI_SATURATED_FATS, foodItem.saturatedfat)
            put(COLUMN_FLI_POLYUNSATURATED_FATS, foodItem.polyunsaturatedfat)
            put(COLUMN_FLI_MONOUNSATURATED_FATS, foodItem.monounsaturatedfat)
            put(COLUMN_FLI_CHOLESTEROL, foodItem.cholesterol)
            put(COLUMN_FLI_SODIUM, foodItem.sodium)
            put(COLUMN_FLI_POTASSIUM, foodItem.potassium)
        }
        val itemId = db.insert(TABLE_FOOD_LOG_ITEMS, null, values)
        db.close()
        return itemId
    }

    fun getTodayFoodLogs(userId: Int): List<FoodLog> {
        val foodLogs = mutableListOf<FoodLog>()
        val db = this.readableDatabase
        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val todayDate = dateFormat.format(Date())

        val query = """
        SELECT * FROM $TABLE_FOOD_LOGS 
        WHERE $COLUMN_USER_ID_FK = ? AND strftime('%Y-%m-%d', $COLUMN_DATE_TIME) = ?
    """.trimIndent()

        val cursor = db.rawQuery(query, arrayOf(userId.toString(), todayDate))

        while (cursor.moveToNext()) {
            val logId = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_LOG_ID))
            val dateTime = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DATE_TIME))
            val consumedFoodItems = getFoodItemsForLog(logId)
            foodLogs.add(FoodLog(logId, userId, dateTime, consumedFoodItems))
        }
        cursor.close()
        db.close()
        return foodLogs
    }

    private fun getFoodItemsForLog(logId: Int): List<ConsumedFoodItem> {
        val consumedFoodItems = mutableListOf<ConsumedFoodItem>()
        val db = this.readableDatabase
        val query = """
        SELECT FLI.*, FI.foodId, FI.name, FI.description, FI.portionUnit, FLI.servingSize AS portionSizeValue,
        FLI.calories AS calories, FLI.carbs AS carbs, FLI.fats AS fats, FLI.proteins AS proteins,
        FLI.fibre AS fibre, FLI.sugar AS sugar, FLI.saturatedfat AS saturatedfat,
        FLI.polyunsaturatedfat AS polyunsaturatedfat, FLI.monounsaturatedfat AS monounsaturatedfat,
        FLI.cholesterol AS cholesterol, FLI.sodium AS sodium, FLI.potassium AS potassium
        FROM $TABLE_FOOD_LOG_ITEMS FLI
        JOIN $TABLE_FOOD_ITEMS FI ON FLI.$COLUMN_FOOD_ID_FK = FI.$COLUMN_FOOD_ID
        WHERE FLI.$COLUMN_LOG_ID_FK = ?
        """.trimIndent()

        val cursor = db.rawQuery(query, arrayOf(logId.toString()))

        while (cursor.moveToNext()) {
            val calories = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_FLI_CALORIES))
            Log.d("DiaryFragment", "Calories fetched: $calories")
            val foodItem = FoodItem(
                foodId = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_FOOD_ID)),
                name = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_FOOD_NAME)),
                description = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DESCRIPTION)),
                portionUnit = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_FOOD_PORTION_UNIT)),
                portionSizeValue = cursor.getDouble(cursor.getColumnIndexOrThrow(COLUMN_SERVING_SIZE)),
                calories = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_FLI_CALORIES)),
                carbs = cursor.getDouble(cursor.getColumnIndexOrThrow(COLUMN_FLI_CARBS)),
                fats = cursor.getDouble(cursor.getColumnIndexOrThrow(COLUMN_FLI_FATS)),
                proteins = cursor.getDouble(cursor.getColumnIndexOrThrow(COLUMN_FLI_PROTEINS)),
                fibre = cursor.getDouble(cursor.getColumnIndexOrThrow(COLUMN_FLI_FIBRES)),
                sugar = cursor.getDouble(cursor.getColumnIndexOrThrow(COLUMN_FLI_SUGARS)),
                saturatedfat = cursor.getDouble(cursor.getColumnIndexOrThrow(COLUMN_FLI_SATURATED_FATS)),
                polyunsaturatedfat = cursor.getDouble(cursor.getColumnIndexOrThrow(COLUMN_FLI_POLYUNSATURATED_FATS)),
                monounsaturatedfat = cursor.getDouble(cursor.getColumnIndexOrThrow(COLUMN_FLI_MONOUNSATURATED_FATS)),
                cholesterol = cursor.getDouble(cursor.getColumnIndexOrThrow(COLUMN_FLI_CHOLESTEROL)),
                sodium = cursor.getDouble(cursor.getColumnIndexOrThrow(COLUMN_FLI_SODIUM)),
                potassium = cursor.getDouble(cursor.getColumnIndexOrThrow(COLUMN_FLI_POTASSIUM))
            )
            val mealTypeString = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_MEAL_TYPE))
            val mealType = MealType.valueOf(mealTypeString)
            val portionSizeValue = cursor.getDouble(cursor.getColumnIndexOrThrow(COLUMN_SERVING_SIZE))
            consumedFoodItems.add(ConsumedFoodItem(foodItem, portionSizeValue, mealType, logId))
        }
        cursor.close()
        db.close()
        return consumedFoodItems
    }

    fun deleteFoodLog(logId: Int): Boolean {
        val db = writableDatabase
        val deleteSuccess = db.delete(TABLE_FOOD_LOGS, "$COLUMN_LOG_ID = ?", arrayOf(logId.toString()))
        db.close()
        return deleteSuccess > 0
    }

    fun getWaterIntake(userId: Int, date: String): WaterIntakeItem? {
        val db = this.readableDatabase
        var waterIntakeItem: WaterIntakeItem? = null

        val selectQuery = "SELECT * FROM $TABLE_WATER_LOGS WHERE $COLUMN_USER_ID_FK_W = $userId AND $COLUMN_DATE = '$date'"
        val cursor = db.rawQuery(selectQuery, null)

        cursor.use { cursor ->
            if (cursor.moveToFirst()) {
                val waterIntakeIndex = cursor.getColumnIndex(COLUMN_WATER_AMOUNT)
                if (waterIntakeIndex != -1) {
                    val waterIntake = cursor.getInt(waterIntakeIndex)
                    if (waterIntake >= 0) {
                        waterIntakeItem = WaterIntakeItem(waterIntake)
                    }
                }
            } else {
                val values = ContentValues().apply {
                    put(COLUMN_USER_ID_FK_W, userId)
                    put(COLUMN_DATE, date)
                    put(COLUMN_WATER_AMOUNT, 0)
                }
                val newRowId = db.insert(TABLE_WATER_LOGS, null, values)
                if (newRowId != -1L) {
                    waterIntakeItem = WaterIntakeItem(0)
                }
            }
        }

        cursor.close()
        db.close()

        return waterIntakeItem
    }

    fun updateWaterIntake(userId: Int, date: String, newWaterIntake: Int): Boolean {
        val db = this.writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_WATER_AMOUNT, newWaterIntake)
        }
        val whereClause = "$COLUMN_USER_ID_FK_W = ? AND $COLUMN_DATE = ?"
        val whereArgs = arrayOf(userId.toString(), date)
        val rowsAffected = db.update(TABLE_WATER_LOGS, values, whereClause, whereArgs)
        db.close()
        return rowsAffected > 0
    }

    fun calculateBMI(userId: String): Pair<Double, String> {
        val userData = getUserByIdFull(userId)
        Log.d("UserData", "Fetching data for userID: $userId")
        if (userData == null) {
            Log.d("UserData", "No data found for userID: $userId")
            return Pair(0.0, "Invalid user data")
        } else {
            Log.d("UserData", "Data for userID $userId: $userData")
        }

        Log.d("BMI Calculation", "User Data: $userData")

        val totalHeightInMeters = (userData.heightFeet * 0.3048) + (userData.heightInches * 0.0254)
        val totalWeightInKg = (userData.weightStone * 6.35029) + (userData.weightPounds * 0.453592)

        Log.d("BMI Calculation", "Height in meters: $totalHeightInMeters")
        Log.d("BMI Calculation", "Weight in kg: $totalWeightInKg")

        val bmi = if (totalHeightInMeters > 0) totalWeightInKg / (totalHeightInMeters * totalHeightInMeters) else 0.0
        Log.d("BMI Calculation", "Calculated BMI: $bmi")


        val classification = when {
            bmi < 18.5 -> "Underweight"
            bmi in 18.5..24.9 -> "Normal weight"
            bmi in 25.0..29.9 -> "Overweight"
            else -> "Obesity"
        }

        return Pair(bmi, classification)
    }

    fun addWeightEntry(userId: Int, weightPounds: Int, date: String): Boolean {
        val db = this.writableDatabase
        db.beginTransaction()
        try {
            val contentValues = ContentValues().apply {
                put(COLUMN_USER_ID_FK_W, userId)
                put(COLUMN_WEIGHT, weightPounds)
                put(COLUMN_WEIGHT_DATE, date)
            }

            val insertResult = db.insert(TABLE_WEIGHT_ENTRIES, null, contentValues)

            if (insertResult == -1L) {
                return false
            }

            val poundsPart = weightPounds % 14
            val stonePart = weightPounds / 14

            val updateValues = ContentValues().apply {
                put(COLUMN_WEIGHT_STONE, stonePart)
                put(COLUMN_WEIGHT_POUNDS, poundsPart)
            }

            val rowsAffected = db.update(
                TABLE_USER_DATA,
                updateValues,
                "$COLUMN_ID = ?",
                arrayOf(userId.toString())
            )

            if (rowsAffected == 1) {
                db.setTransactionSuccessful()
                return true
            }
        } catch (_: Exception) {
        } finally {
            db.endTransaction()
        }
        return false
    }

    fun getRecentWeightEntries(userId: Int): List<WeighInEntry> {
        val weighIns = mutableListOf<WeighInEntry>()
        val db = this.readableDatabase
        val cursor = db.query(
            TABLE_WEIGHT_ENTRIES,
            arrayOf(COLUMN_WEIGHT, COLUMN_WEIGHT_DATE),
            "$COLUMN_USER_ID_FK_W = ?",
            arrayOf(userId.toString()),
            null, null, "$COLUMN_WEIGHT_DATE DESC"
        )

        while (cursor.moveToNext()) {
            val weight = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_WEIGHT))
            val date = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_WEIGHT_DATE))
            weighIns.add(WeighInEntry(weight, date))
        }
        cursor.close()
        return weighIns
    }

}
