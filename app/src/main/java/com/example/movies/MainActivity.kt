package com.example.movies

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        val signUp: TextView = findViewById(R.id.sign_up)
        signUp.setOnClickListener {
            val intent = Intent(this, SignUpActivity::class.java)
            startActivity(intent)
        }

        val name: TextView = findViewById(R.id.name)
        val password: TextView = findViewById(R.id.password)
        val signIn: Button = findViewById(R.id.sign_in)

        signIn.setOnClickListener {
            if (name.text.isEmpty()) {
                Toast.makeText(this, "Provide your name!", Toast.LENGTH_LONG).show()
            } else if (password.text.isEmpty()) {
                Toast.makeText(this, "Invalid password!", Toast.LENGTH_LONG).show()
            } else {
                val db = Firebase.firestore
                db.collection("users").get().addOnSuccessListener { result ->
                    for (document in result) {
                        if (document.getString("name") == name.text.toString()
                            && document.getString("password") == password.text.toString()
                        ){
                            startActivity(Intent(this,MainMenuActivity::class.java))
                        }
                    }
                }.addOnFailureListener {
                    Toast.makeText(this, "Something went wrong!", Toast.LENGTH_LONG).show()
                }
            }
        }
    }
}