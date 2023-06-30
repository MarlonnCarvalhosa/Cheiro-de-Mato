package com.marlonncarvalhosa.cheirodemato.view.init

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.content.ContextCompat
import com.marlonncarvalhosa.cheirodemato.databinding.ActivityInitBinding

class InitActivity : AppCompatActivity() {

    private var binding: ActivityInitBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityInitBinding.inflate(layoutInflater).apply {
            setContentView(root)
            supportActionBar?.hide()
        }
    }

    fun setColorStatusBar(color: Int) {
        window?.statusBarColor = ContextCompat.getColor(this, color)
    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }
}