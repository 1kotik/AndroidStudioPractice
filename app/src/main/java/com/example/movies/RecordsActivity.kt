package com.example.movies

import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.annotation.IntegerRes
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore

class RecordsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_records)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val users: ArrayList<User> = setData()
        val listView: ListView = findViewById(R.id.records)
        val userAdapter = UserAdapter(this, android.R.layout.simple_list_item_1, users)

        listView.adapter = userAdapter

        val back: TextView = findViewById(R.id.back)
        back.setOnClickListener {
            val intent = Intent(this, MainMenuActivity::class.java)
            startActivity(intent)
        }
    }

    private fun setData(): ArrayList<User> {
        val users = ArrayList<User>()
        val user = User("alex", 10)
        val db = Firebase.firestore
        db.collection("users").get().addOnSuccessListener { result ->
            for (document in result) {
                user.name = document.getString("name").toString()
                val record = Integer.parseInt(document.getString("record").toString())
                user.record = record
                users.add(user)
            }
        }

        return users
    }
}