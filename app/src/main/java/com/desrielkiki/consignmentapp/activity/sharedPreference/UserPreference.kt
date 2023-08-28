package com.desrielkiki.consignmentapp.activity.sharedPreference

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import com.desrielkiki.consignmentapp.activity.MainActivity
import com.desrielkiki.consignmentapp.R

class UserPreference : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Check if the business name is already saved in Shared Preferences
        val sharedPreferences = getSharedPreferences("MyPreferences", Context.MODE_PRIVATE)
        val businessName = sharedPreferences.getString("businessName", null)

        // If the business name is saved, start the Main Activity and finish this activity
        if (!businessName.isNullOrEmpty()) {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
            return
        }

        // Continue with the layout inflation and button click handling
        setContentView(R.layout.activity_user_preference)

        val etBusinessName = findViewById<EditText>(R.id.et_businessName)
        val btnSave = findViewById<Button>(R.id.btn_saveBusiness)

        // Save the business name to Shared Preferences when the Save button is clicked
        btnSave.setOnClickListener {
            val name = etBusinessName.text.toString()
            if (name.isNotEmpty()) {
                sharedPreferences.edit().putString("businessName", name).apply()

                // Start the Main Activity and finish this activity
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                finish()
            }
        }
    }
}