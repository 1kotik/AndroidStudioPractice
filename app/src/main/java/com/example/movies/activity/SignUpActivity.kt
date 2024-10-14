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
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

class SignUpActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_sign_up)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        //setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        //var sp=getSharedPreferences("PC", Context.MODE_PRIVATE).edit()
        val signIn: TextView = findViewById(R.id.sign_in)
        signIn.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

        val signUp: Button = findViewById(R.id.sign_up)

        signUp.setOnClickListener {
            val existence = isUsernameExists { check ->
                if (!check) {
                    addUser()
                } else {
                    Toast.makeText(this, "Username already exists!", Toast.LENGTH_LONG).show()
                }
            }
        }

    }

    private fun isUsernameExists(callback: (Boolean) -> Unit) = GlobalScope.launch {
        val name: TextView = findViewById(R.id.name)
        val users = ArrayList<String>()
        val db = Firebase.firestore

        // Use async to launch a new coroutine for data retrieval
        val usernameExists = async(Dispatchers.IO) {
            val snapshot = db.collection("users")
                .whereEqualTo("name", name.text.toString()).get().await()
            snapshot.documents.isNotEmpty()
        }

        // Wait for the async operation to complete before calling callback
        val doesExist = usernameExists.await()
        withContext(Dispatchers.Main) {
            callback(doesExist)
        }
    }

    private fun addUser() {
        val db = Firebase.firestore
        val name: TextView = findViewById(R.id.name)
        val password: TextView = findViewById(R.id.password)
        if (name.text.isEmpty()) {
            Toast.makeText(this, "Provide your name!", Toast.LENGTH_LONG).show()
        } else if (password.text.isEmpty() || password.text.length < 8) {
            Toast.makeText(this, "Invalid password!", Toast.LENGTH_LONG).show()
        } else {
            val user = hashMapOf(
                "name" to name.text.toString(),
                "password" to password.text.toString(),
                "record" to "0"
            )
            db.collection("users").add(user).addOnSuccessListener { documentReference ->
                //sp.putString("Name",name.text.toString()).commit()
                val intent = Intent(this, MainMenuActivity::class.java)
                intent.putExtra("user", user)
                startActivity(intent)

            }
                .addOnFailureListener {
                    Toast.makeText(this, "Something went wrong", Toast.LENGTH_LONG).show()
                }
        }
    }
}
