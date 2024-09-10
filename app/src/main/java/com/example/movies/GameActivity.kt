package com.example.movies

import android.os.Bundle
import android.view.GestureDetector
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

fun setQuestions(): ArrayList<Question> {
    val questions = ArrayList<Question>()
    val lines = arrayOf("test1", "test2", "test3", "test4", "test5")
    val answers = arrayOf(true, false, true, false, true)
    val question = Question("?", true)
    for (i in 0..4) {
        questions.add(Question(lines[i], answers[i]))
    }
    return questions
}

val questions = setQuestions()


class GameActivity : AppCompatActivity() {

    lateinit var gestureDetector: GestureDetector
    var x2: Float = 0.0f
    var x1: Float = 0.0f
    var y2: Float = 0.0f
    var y1: Float = 0.0f

    companion object{
        const val MIN_DISTANCE=150
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_game)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }



}