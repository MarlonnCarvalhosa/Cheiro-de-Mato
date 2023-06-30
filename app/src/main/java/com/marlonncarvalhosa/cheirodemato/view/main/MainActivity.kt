package com.marlonncarvalhosa.cheirodemato.view.main

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.content.ContextCompat
import com.marlonncarvalhosa.cheirodemato.R
import com.marlonncarvalhosa.cheirodemato.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private var binding: ActivityMainBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater).apply {
            setContentView(root)
        }
        supportActionBar?.hide()
    }

    fun setColorStatusBar(color: Int) {
        window?.statusBarColor = ContextCompat.getColor(this, color)
    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }
}