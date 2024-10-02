package com.example.movies.activity

import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.movies.R
import com.example.movies.model.User
import com.example.movies.adapter.UserAdapter
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
        val users = setData { retrievedUsers ->
            val recyclerView = findViewById<RecyclerView>(R.id.records)
            recyclerView.adapter = UserAdapter(retrievedUsers)
            recyclerView.layoutManager = LinearLayoutManager(this)
        }

        val user: User? =intent.getParcelableExtra("user")

        val back: TextView = findViewById(R.id.back)
        back.setOnClickListener {
            val intent = Intent(this, MainMenuActivity::class.java)
            intent.putExtra("user", user)
            startActivity(intent)
        }
    }

    private fun setData(callback: (ArrayList<User>) -> Unit) {
        val users = ArrayList<User>()
        val db = Firebase.firestore
        db.collection("users").get().addOnSuccessListener { result ->
            for (document in result) {
                users.add(
                    User(document.getString("name").toString(),
                    document.getString("record").toString().toInt())
                )
            }
            users.sortWith { u1, u2 -> u2.record.compareTo(u1.record) }
            callback(users)
        }
    }
}