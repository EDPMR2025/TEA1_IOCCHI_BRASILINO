package com.pmr.app.activities

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
import com.pmr.app.adapters.ChoixListAdapter

class ChoixListActivity : AppCompatActivity() {

    var currentUser: String = "" ;
    lateinit var recyclerView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_choix_list)
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
            // get list for that pseudo
            val keySP = "list_$currentUser"
            val listOfLists = sharedPref.getString(keySP, String())
            if (listOfLists != null) {
                // steup recycler view with elements of the list
                val list = listOfLists.split(";")
                // setup recycler view
                recyclerView.adapter?.let {
                    (it as ChoixListAdapter).setList(list)
                }
            }
        }
    }

    private fun setClickListeners() {
        findViewById<View>(R.id.btnValider)?.setOnClickListener {
            //add element to the list of list and update adapte
            val currentChoix = findViewById<EditText>(R.id.etList).text.toString()
            val sharedPref = this.getSharedPreferences("com.pmr.tea1", MODE_PRIVATE)
            val keySP = "list_$currentUser"
            val listOfLists = sharedPref.getString(keySP, String())
            if (listOfLists != null) {
                val list = listOfLists.split(";").toMutableList()
                list.add(currentChoix)
                val newListOfLists = list.joinToString(";")
                with(sharedPref.edit()) {
                    putString(keySP, newListOfLists)
                    apply()
                }
                // update recycler view
                recyclerView.adapter?.let {
                    (it as ChoixListAdapter).setList(list)
                }
            }
            Log.d("ChoixListActivity", "Choix added $currentChoix to list")
        }
    }

    private fun setupRecyclerView() {
        // setup recycler view
        recyclerView = findViewById<RecyclerView>(R.id.rvList)
        recyclerView.layoutManager = androidx.recyclerview.widget.LinearLayoutManager(this)

        // setup adapter
        val adapter = ChoixListAdapter(this)
        recyclerView.adapter = adapter
    }

    fun goToDetailActivity(choix: String) {
        // set currentList to the list of the current user
        val sharedPref = this.getSharedPreferences("com.pmr.tea1", MODE_PRIVATE)
        with(sharedPref.edit()) {
            putString("currentList", choix)
            apply()
        }

        val intent = Intent(this, ShowListActivity::class.java)
        intent.putExtra("choix", choix)
        startActivity(intent)

        Log.d("ChoixListActivity", "Choix clicked: $choix")
    }
}