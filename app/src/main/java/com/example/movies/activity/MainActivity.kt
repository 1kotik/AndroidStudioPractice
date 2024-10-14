package com.example.movies.activity

import android.content.Intent
import android.content.pm.ActivityInfo
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.movies.R
import com.example.movies.model.User
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
        //setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
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
                Toast.makeText(this, "Введите имя!", Toast.LENGTH_LONG).show()
            } else if (password.text.isEmpty()) {
                Toast.makeText(this, "Неверный пароль!", Toast.LENGTH_LONG).show()
            } else {
                val db = Firebase.firestore
                db.collection("users").get().addOnSuccessListener { result ->
                    for (document in result) {
                        if (document.getString("name") == name.text.toString()
                            && document.getString("password") == password.text.toString()
                        ) {
                            val intent = Intent(this, MainMenuActivity::class.java)
                            val user = User(
                                document.getString("name").toString(),
                                document.getString("record").toString().toInt()
                            )
                            intent.putExtra("user", user)
                            startActivity(intent)
                            return@addOnSuccessListener;
                        } else if (document.getString("name") == name.text.toString()
                            && document.getString("password") != password.text.toString()
                        ) {
                            Toast.makeText(this, "Неверный пароль!", Toast.LENGTH_LONG).show()
                            return@addOnSuccessListener;
                        }
                    }
                    Toast.makeText(this, "Этого пользователя не существует", Toast.LENGTH_LONG).show()
                }.addOnFailureListener {
                    Toast.makeText(this, "Что-то пошло не так", Toast.LENGTH_LONG).show()
                }
            }
        }
    }
}