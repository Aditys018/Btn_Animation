package com.aditys.btn_animation

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.aditys.btn_animation.R
import com.aditys.btn_animation.CustomToggleButton


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val toggleButton = findViewById<CustomToggleButton>(R.id.customToggleButton)

        toggleButton.setOnClickListener {

            Toast.makeText(this, "Toggle is ${if (toggleButton.isToggleOn) "ON" else "OFF"}", Toast.LENGTH_SHORT).show()
        }
    }
}
