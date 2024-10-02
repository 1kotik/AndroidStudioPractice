package com.example.movies.activity

import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.movies.R
import com.example.movies.model.User

class MainMenuActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main_menu)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        val user: User? =intent.getParcelableExtra("user")
        val play: TextView = findViewById(R.id.play)
        play.setOnClickListener {
            val intent = Intent(this, GameActivity::class.java)
            intent.putExtra("user", user)
            startActivity(intent)
        }

        val records: TextView = findViewById(R.id.records)
        records.setOnClickListener {
            val intent = Intent(this, RecordsActivity::class.java)
            intent.putExtra("user", user)
            startActivity(intent)
        }

        val exit: TextView = findViewById(R.id.exit)
        exit.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
    }
}