package com.pmr.app.activities

import android.os.Bundle
import android.preference.PreferenceActivity
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.Toolbar
import com.pmr.app.R

class SettingsActivity : PreferenceActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        addPreferencesFromResource(R.xml.fragment_preference)
        setupActionBar()

    }

    private fun setupActionBar() {
        layoutInflater.inflate(
            R.layout.toolbar,
            findViewById<View>(android.R.id.content) as ViewGroup
        )
        val toolbar: Toolbar = findViewById<View>(R.id.toolbar) as Toolbar
        toolbar.setTitle("Settings Activity")
    }
}