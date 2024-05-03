package com.example.thecaloriewizard.accountcreationlogin.adapters

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.example.thecaloriewizard.R
import com.example.thecaloriewizard.accountcreationlogin.fragments.AboutFragment
import com.example.thecaloriewizard.accountcreationlogin.fragments.ActivityFragment
import com.example.thecaloriewizard.accountcreationlogin.fragments.BarriersFragment
import com.example.thecaloriewizard.accountcreationlogin.fragments.CreateAccountFragment
import com.example.thecaloriewizard.accountcreationlogin.fragments.GoalsFragment
import com.example.thecaloriewizard.accountcreationlogin.fragments.WeeklyGoalsFragment
import com.google.android.material.progressindicator.LinearProgressIndicator

/**
 * [AccountCreationAdapter]
 * Description: Adapter class for managing the fragments displayed in the ViewPager for account creation.
 *
 * [Author]
 * Author Name: Brandon Sharp
 *
 * [Parameters/Arguments]
 * - [activity]: The AppCompatActivity instance to which this adapter is associated.
 *
 * [Return Value]
 * None
 *
 * [Usage]
 * This adapter is used to manage the fragments displayed in the ViewPager for account creation.
 *
 * [Notes]
 * None
 */
class AccountCreationAdapter(activity: AppCompatActivity) : FragmentStateAdapter(activity) {
    override fun getItemCount(): Int = 6

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> GoalsFragment()
            1 -> BarriersFragment()
            2 -> ActivityFragment()
            3 -> AboutFragment()
            4 -> WeeklyGoalsFragment()
            5 -> CreateAccountFragment()
            else -> throw IllegalStateException("Unexpected position $position")
        }
    }
}

/**
 * [AccountCreationActivity]
 * Description: Activity class responsible for managing the account creation process.
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
 * This activity is used to manage the account creation process, including handling ViewPager and progress indicator updates.
 *
 * [Notes]
 * None
 */
class AccountCreationActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_accountcreation)

        val viewPager: ViewPager2 = findViewById(R.id.viewpager)
        viewPager.adapter = AccountCreationAdapter(this)

        val progressIndicator: LinearProgressIndicator = findViewById(R.id.progress_indicator)
        viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                val progress = ((position + 1) / viewPager.adapter!!.itemCount.toFloat()) * 100
                progressIndicator.progress = progress.toInt()
            }
        })
    }
}