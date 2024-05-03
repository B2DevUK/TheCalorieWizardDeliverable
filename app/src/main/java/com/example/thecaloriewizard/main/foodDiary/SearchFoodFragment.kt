package com.example.thecaloriewizard.main.foodDiary

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.thecaloriewizard.R
import com.example.thecaloriewizard.database.DBHelper
import com.example.thecaloriewizard.dataclasses.MealType

/**
 * SearchFoodFragment
 * Description: Fragment for searching food items.
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
 * Used to search for food items and display the search results in a RecyclerView.
 *
 * [Notes]
 * None
 */
class SearchFoodFragment : Fragment() {

    private lateinit var searchView: SearchView
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: FoodItemAdapter

    private var mealType: MealType? = null

    var diaryUpdateListener: DiaryUpdateListener? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mealType = arguments?.getString("mealType")?.let { MealType.valueOf(it) }
        val view = inflater.inflate(R.layout.fragment_search_food, container, false)

        searchView = view.findViewById(R.id.searchViewFood)
        recyclerView = view.findViewById(R.id.recyclerViewSearchResults)


        recyclerView.layoutManager = LinearLayoutManager(context)
        mealType?.let {
            adapter = FoodItemAdapter(requireContext(), listOf(), it, diaryUpdateListener)
            recyclerView.adapter = adapter
        } ?: Log.e("SearchFoodFragment", "MealType is null")


        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                Log.d("SearchFoodFragment", "Searching for: $query")
                query?.let { performSearch(it) }
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return true
            }
        })

        return view
    }

    /**
     * performSearch
     * Description: Performs a search for food items based on the provided query.
     *
     * [Parameters/Arguments]
     * - query: The search query entered by the user.
     *
     * [Return Value]
     * None
     *
     * [Notes]
     * None
     */
    private fun performSearch(query: String) {
        val dbHelper = DBHelper(requireContext())
        val results = dbHelper.searchFoodItems(query)

        Log.d("SearchFoodFragment", "Found ${results.size} matching items")

        adapter.updateItems(results)
    }
}