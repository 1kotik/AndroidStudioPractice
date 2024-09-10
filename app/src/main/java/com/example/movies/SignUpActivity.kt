package com.example.movies

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore

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
        //var sp=getSharedPreferences("PC", Context.MODE_PRIVATE).edit()
        val signIn: TextView =findViewById(R.id.sign_in)
        signIn.setOnClickListener {
            val intent= Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

        val name:TextView=findViewById(R.id.name)
        val password:TextView=findViewById(R.id.password)
        val signUp:Button=findViewById(R.id.sign_up)

        signUp.setOnClickListener {
            if(name.text.isEmpty()){
                Toast.makeText(this, "Provide your name!", Toast.LENGTH_LONG).show()
            }
            else if(password.text.isEmpty()||password.text.length<8){
                Toast.makeText(this, "Invalid password!", Toast.LENGTH_LONG).show()
            }
            else{

                val db=Firebase.firestore
                val user=hashMapOf(
                    "name" to name.text.toString(),
                    "password" to password.text.toString(),
                    "record" to "0"
                )

                db.collection("users").add(user).addOnSuccessListener { documentReference ->
                    //sp.putString("Name",name.text.toString()).commit()
                    val intent= Intent(this, MainMenuActivity::class.java)
                    startActivity(intent)

                }
                    .addOnFailureListener {
                        Toast.makeText(this, "Something went wrong", Toast.LENGTH_LONG).show()
                    }
            }
        }
    }
}