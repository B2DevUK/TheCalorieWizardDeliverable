package com.example.thecaloriewizard.main.home

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.thecaloriewizard.R
import com.example.thecaloriewizard.database.DBHelper
import com.example.thecaloriewizard.dataclasses.BmiItem
import com.example.thecaloriewizard.dataclasses.HomeItem
import com.example.thecaloriewizard.dataclasses.MacronutrientItem
import com.example.thecaloriewizard.dataclasses.WaterIntakeItem
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

/**
 * HomeAdapter
 * Description: Adapter for the home screen RecyclerView displaying macronutrient info, water intake, and BMI.
 *
 * [Author]
 * Author Name: Brandon Sharp
 *
 * [Parameters/Arguments]
 * - context: The context of the calling activity or fragment.
 * - dbHelper: An instance of the database helper class.
 *
 * [Usage]
 * Used to populate the RecyclerView on the home screen with macronutrient info, water intake, and BMI.
 *
 * [Notes]
 * None
 */
class HomeAdapter(
    context: Context,
    private val dbHelper: DBHelper
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var items = mutableListOf<HomeItem>()
    private var userId: Int = getUserIdFromPrefs(context)
    private var currentDate: String = getCurrentDate()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return when (viewType) {
            TYPE_MACRONUTRIENT -> {
                val view = inflater.inflate(R.layout.item_macro_nutrient, parent, false)
                MacronutrientViewHolder(view)
            }
            TYPE_WATER_INTAKE -> {
                val view = inflater.inflate(R.layout.item_water_intake, parent, false)
                WaterIntakeViewHolder(view, this)
            }
            TYPE_BMI -> {
                val view = inflater.inflate(R.layout.item_bmi_calculator, parent, false)
                BMIViewHolder(view)
            }
            else -> throw IllegalArgumentException("Unsupported type")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is MacronutrientViewHolder -> holder.bind(items[position] as MacronutrientItem)
            is WaterIntakeViewHolder -> holder.bind(items[position] as WaterIntakeItem)
            is BMIViewHolder -> holder.bind(items[position] as BmiItem)
        }
    }

    /**
     * increaseWaterIntake
     * Description: Increases the water intake for the current user and updates the UI.
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
    fun increaseWaterIntake() {
        val currentWaterIntake = (items.find { it is WaterIntakeItem } as? WaterIntakeItem)?.currentWaterIntake ?: 0
        dbHelper.updateWaterIntake(userId, currentDate, currentWaterIntake + 1)
        reloadWaterIntake()
    }

    /**
     * decreaseWaterIntake
     * Description: Decreases the water intake for the current user and updates the UI.
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
    fun decreaseWaterIntake() {
        val currentWaterIntake = (items.find { it is WaterIntakeItem } as? WaterIntakeItem)?.currentWaterIntake ?: 0
        if (currentWaterIntake > 0) {
            dbHelper.updateWaterIntake(userId, currentDate, currentWaterIntake - 1)
            reloadWaterIntake()
        }
    }

    /**
     * reloadWaterIntake
     * Description: Reloads the water intake data and updates the UI.
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
    private fun reloadWaterIntake() {
        val newWaterIntake = dbHelper.getWaterIntake(userId, currentDate)
        val waterIntakeIndex = getWaterIntakePosition()
        if (waterIntakeIndex != -1) {
            if (newWaterIntake != null) {
                (items[waterIntakeIndex] as WaterIntakeItem).currentWaterIntake = newWaterIntake.currentWaterIntake
            }
            notifyItemChanged(waterIntakeIndex)
        }
    }

    /**
     * getWaterIntakePosition
     * Description: Gets the position of water intake item in the adapter.
     *
     * [Parameters/Arguments]
     * None
     *
     * [Return Value]
     * Int: The position of water intake item in the adapter.
     *
     * [Notes]
     * None
     */
    private fun getWaterIntakePosition(): Int {
        return items.indexOfFirst { it is WaterIntakeItem }
    }

    override fun getItemCount(): Int = items.size

    override fun getItemViewType(position: Int): Int = when (items[position]) {
        is MacronutrientItem -> TYPE_MACRONUTRIENT
        is WaterIntakeItem -> TYPE_WATER_INTAKE
        is BmiItem -> TYPE_BMI
    }

    /**
     * setItems
     * Description: Sets the items in the adapter.
     *
     * [Parameters/Arguments]
     * - newItems: The list of new items to be set in the adapter.
     *
     * [Return Value]
     * None
     *
     * [Notes]
     * None
     */
    fun setItems(newItems: List<HomeItem>) {
        items.clear()
        items.addAll(newItems)
        notifyDataSetChanged()
    }

    /**
     * MacronutrientViewHolder
     * Description: ViewHolder for macronutrient items.
     *
     * [Parameters/Arguments]
     * - itemView: The inflated view for the ViewHolder.
     * - adapter: The instance of the HomeAdapter.
     *
     * [Usage]
     * Used to bind macronutrient data to the corresponding views.
     *
     * [Notes]
     * None
     */
    class MacronutrientViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val textViewCaloriesValue: TextView = itemView.findViewById(R.id.textViewCaloriesValue)
        private val textViewCarbsValue: TextView = itemView.findViewById(R.id.textViewCarbsValue)
        private val textViewFatsValue: TextView = itemView.findViewById(R.id.textViewFatsValue)
        private val textViewProteinsValue: TextView = itemView.findViewById(R.id.textViewProteinsValue)

        /**
         * MacronutrientViewHolder
         * Description: ViewHolder for macronutrient items.
         *
         * [Parameters/Arguments]
         * - itemView: The inflated view for the ViewHolder.
         * - adapter: The instance of the HomeAdapter.
         *
         * [Usage]
         * Used to bind macronutrient data to the corresponding views.
         *
         * [Notes]
         * None
         */
        fun bind(item: MacronutrientItem) {
            textViewCaloriesValue.text = "${item.calories} cal"
            textViewCarbsValue.text = "${item.carbs}g"
            textViewFatsValue.text = "${item.fats}g"
            textViewProteinsValue.text = "${item.proteins}g"
        }
    }

    /**
     * WaterIntakeViewHolder
     * Description: ViewHolder for water intake items.
     *
     * [Parameters/Arguments]
     * - itemView: The inflated view for the ViewHolder.
     * - adapter: The instance of the HomeAdapter.
     *
     * [Usage]
     * Used to bind water intake data to the corresponding views.
     *
     * [Notes]
     * None
     */
    class WaterIntakeViewHolder(itemView: View, private val adapter: HomeAdapter) : RecyclerView.ViewHolder(itemView) {
        private val textViewWaterAmount: TextView = itemView.findViewById(R.id.textViewWaterAmount)
        private val buttonIncrease: Button = itemView.findViewById(R.id.buttonIncrease)
        private val buttonDecrease: Button = itemView.findViewById(R.id.buttonDecrease)

        /**
         * bind
         * Description: Binds water intake data to the corresponding views.
         *
         * [Parameters/Arguments]
         * - item: The water intake item to be bound.
         *
         * [Return Value]
         * None
         *
         * [Notes]
         * None
         */
        fun bind(item: WaterIntakeItem) {
            textViewWaterAmount.text = "${item.currentWaterIntake} glasses"

            buttonIncrease.setOnClickListener {
                adapter.increaseWaterIntake()
            }

            buttonDecrease.setOnClickListener {
                adapter.decreaseWaterIntake()
            }
        }
    }

    /**
     * BMIViewHolder
     * Description: ViewHolder for BMI items.
     *
     * [Parameters/Arguments]
     * - itemView: The inflated view for the ViewHolder.
     *
     * [Usage]
     * Used to bind BMI data to the corresponding views.
     *
     * [Notes]
     * None
     */
    class BMIViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val textViewBMIValue: TextView = itemView.findViewById(R.id.textViewBMIValue)
        private val textViewBMIClassification: TextView = itemView.findViewById(R.id.textViewBMIWeightClassification)

        /**
         * bind
         * Description: Binds BMI data to the corresponding views.
         *
         * [Parameters/Arguments]
         * - item: The BMI item to be bound.
         *
         * [Return Value]
         * None
         *
         * [Notes]
         * None
         */
        fun bind(item: BmiItem) {
            textViewBMIValue.text = String.format("%.1f", item.bmi)
            textViewBMIClassification.text = item.weightClassification
        }
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

    companion object {
        private const val TYPE_MACRONUTRIENT = 0
        private const val TYPE_WATER_INTAKE = 1
        private const val TYPE_BMI = 2

    }
}