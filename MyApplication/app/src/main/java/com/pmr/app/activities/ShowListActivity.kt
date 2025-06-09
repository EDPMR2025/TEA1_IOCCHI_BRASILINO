package com.pmr.app.activities

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.pmr.app.R
import com.pmr.app.adapters.ShowListAdapter

class ShowListActivity : AppCompatActivity() {

    var currentUser: String = ""
    var currentList: String = ""
    lateinit var recyclerView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_show_list)
        setSupportActionBar(findViewById(R.id.toolbar))
        setClickListeners()
        setupRecyclerView()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_settings -> {
                val intent = Intent(this, SettingsActivity::class.java)
                startActivity(intent)
                Log.d("MainActivity", "Settings button clicked")
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onResume() {
        super.onResume()
        // get current user from shared preference
        val sharedPref = this.getSharedPreferences("com.pmr.tea1", MODE_PRIVATE)
        val spCurrentUser = sharedPref.getString("currentUser", String())
        if (spCurrentUser != null) {
            currentUser = spCurrentUser
        }
        val spCurrentList = sharedPref.getString("currentList", String())
        if (spCurrentList != null) {
            currentList = spCurrentList
            // get list for that pseudo
            val keySP = "list_${currentList}_$currentUser"
            val items = sharedPref.getString(keySP, String())
            if (items != null) {
                // steup recycler view with elements of the list
                val listItems = items.split(";")
                // setup recycler view
                if (listItems.isNotEmpty()) {
                    if (!listItems.first().isEmpty() && listItems.first() != "") {
                        recyclerView.adapter?.let {
                            (it as ShowListAdapter).setList(listItems)
                        }
                    }
                }
            }
        }
    }


    private fun setClickListeners() {
        findViewById<View>(R.id.btnValider)?.setOnClickListener {
            //add element to the list of item and update adapter
            val currentItem = findViewById<EditText>(R.id.etItem).text.toString()
            val sharedPref = this.getSharedPreferences("com.pmr.tea1", MODE_PRIVATE)
            val keySP = "list_${currentList}_$currentUser"
            val listOfLists = sharedPref.getString(keySP, String())
            if (listOfLists != null) {
                val list = listOfLists.split(";").toMutableList()
                if (list.first().isEmpty() || list.first() == "") {
                    list.clear()
                }
                list.add("0_$currentItem")
                val newListOfLists = list.joinToString(";")
                with(sharedPref.edit()) {
                    putString(keySP, newListOfLists)
                    apply()
                }
                // update recycler view
                recyclerView.adapter?.let {
                    (it as ShowListAdapter).setList(list)
                }
            }
            Log.d("ShowListActivity", "Item added $currentItem to list")
        }
    }

    private fun setupRecyclerView() {
        // setup recycler view
        recyclerView = findViewById<RecyclerView>(R.id.rvItem)
        recyclerView.layoutManager = androidx.recyclerview.widget.LinearLayoutManager(this)

        // setup adapter
        val adapter = ShowListAdapter(this)
        recyclerView.adapter = adapter
    }
}