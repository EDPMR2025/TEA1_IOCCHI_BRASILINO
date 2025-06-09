package com.pmr.app.activities

import android.content.Intent
import android.os.Bundle
import android.preference.PreferenceManager
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import com.pmr.app.R

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(findViewById(R.id.toolbar))
        setClickListeners()
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
        // get default pseudo to fill text input if preference is checked
        val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this)
        val isChecked = sharedPreferences.getBoolean("DefaultPseudoShow", false)
        if (isChecked) {
            val sharedPref = this.getSharedPreferences("com.pmr.tea1", MODE_PRIVATE)
            val pseudo = sharedPref.getString("defaultPseudo", String())
            if (pseudo != null) {
                findViewById<EditText>(R.id.etPseudo).setText(pseudo)
            }
        }
    }

    private fun setClickListeners() {
        findViewById<View>(R.id.btnValider)?.setOnClickListener {
            // write pseudo on shared preference as currentUser
            // go to next activity
            val currentPseudo = findViewById<EditText>(R.id.etPseudo).text.toString()
            val sharedPref = this.getSharedPreferences("com.pmr.tea1", MODE_PRIVATE)
            with(sharedPref.edit()) {
                putString("currentUser", currentPseudo)
                putString("defaultPseudo", currentPseudo)
                apply()
            }

            // get list of pseudos
            val listPseudos = sharedPref.getString("listPseudos", String())
            // check if currentPseudo is already in the list
            if (listPseudos != null) {
                val pseudos = listPseudos.split(";")
                if (!pseudos.contains(currentPseudo)) {
                    val newPseudos = listPseudos + ";" + currentPseudo
                    with(sharedPref.edit()) {
                        putString("listPseudos", newPseudos)
                        apply()
                    }
                } else {
                    var newPseudos : List<String> = pseudos
                        .filter { it != currentPseudo }
                        .toMutableList()
                    newPseudos += currentPseudo
                    val newPseudosString = newPseudos.joinToString(";")
                    with(sharedPref.edit()) {
                        putString("listPseudos", newPseudosString)
                        apply()
                    }
                }
            }
            val intent = Intent(this, ChoixListActivity::class.java)
            startActivity(intent)
            Log.d("MainActivity", "Pseudo saved: $currentPseudo")
        }
    }

}