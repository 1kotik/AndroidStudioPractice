package com.example.movies.activity

import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.GestureDetector
import android.view.MotionEvent
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.PackageManagerCompat.LOG_TAG
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.movies.R
import com.example.movies.model.Question
import com.example.movies.model.User
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import kotlin.math.abs


fun setQuestions(): ArrayList<Question> {
    val questions = ArrayList<Question>()
    val lines = arrayOf(
        "Мельбурн — столица Австралии",
        "Череп – самая крепкая кость в человеческом теле",
        "Google изначально назывался BackRub",
        "Помидоры - это фрукты",
        "Клеопатра была египетского происхождения",
        "Бананы - это ягоды",
        "Coca-Cola существует во всех странах мира",
        "Курица может жить без головы еще долго после того, как ее отрубили",
        "ДНК людей на 95 процентов совпадает с бананами",
        "Жирафы говорят «му»",
        "Все млекопитающие живут на суше",
        "Кофе готовят из ягод",
        "Животное с самым большим мозгом по отношению к телy - мyравей",
        "Около 70 процентов живых сyществ Земли - бактерии",
        "У улитки около 25 000 зубов",
        "Крокодилы глотают камни, чтобы глубже нырнуть"
    )
    val answers = arrayOf(
        false, false, true, true, false, true,
        false, true, false, true, false, true, true,
        true, true, true
    )
    for (i in lines.indices) {
        questions.add(Question(lines[i], answers[i]))
    }
    return questions
}


class GameActivity : AppCompatActivity(), GestureDetector.OnGestureListener {
    lateinit var gestureDetector: GestureDetector
    var x1: Float = 0.0f
    var x2: Float = 0.0f
    var y1: Float = 0.0f
    var y2: Float = 0.0f
    private var questions = setQuestions()
    private lateinit var question: Question
    private lateinit var user: User

    companion object {
        const val MIN_DISTANCE = 150
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
        //setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        gestureDetector = GestureDetector(this, this)
        if (getScore() == 0) {
            val fetchedUser: User? = intent.getParcelableExtra("user")
            if (fetchedUser != null) {
                user = fetchedUser
            }

            setRandomQuestion()
        }

        val quit: Button = findViewById(R.id.exit)

        quit.setOnClickListener {
            if (user.record.toString().toInt() < getScore()) {
                updateUser()
            }
            val intent = Intent(this, MainMenuActivity::class.java)
            intent.putExtra("user", user)
            startActivity(intent)
        }
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        if (event != null) {
            gestureDetector.onTouchEvent(event)
        }

        when (event?.action) {
            0 -> {
                x1 = event.x
                y1 = event.y
            }

            1 -> {
                x2 = event.x
                y2 = event.y

                val valueX: Float = x2 - x1

                if (abs(valueX) > MIN_DISTANCE) {
                    if (x2 > x1) {
                        playerAnswered(true)
                    } else {
                        playerAnswered(false)
                    }
                }
            }
        }
        return super.onTouchEvent(event)
    }

    override fun onDown(p0: MotionEvent): Boolean {
        return false
    }

    override fun onShowPress(p0: MotionEvent) {

    }

    override fun onSingleTapUp(p0: MotionEvent): Boolean {
        return false
    }

    override fun onScroll(p0: MotionEvent?, p1: MotionEvent, p2: Float, p3: Float): Boolean {
        return false
    }

    override fun onLongPress(p0: MotionEvent) {
        if (getLives() == 0) {
            Toast.makeText(this, "У тебя не осталось жизней", Toast.LENGTH_SHORT).show()
        } else {
            questions.remove(question)
            setRandomQuestion()
            setLives(getLives() - 1)
            changeScore(false)
        }
    }

    private fun getLives(): Int {
        val lives = findViewById<TextView>(R.id.lives)
        return lives.text.toString().substring(lives.text.length - 1).toInt()
    }

    private fun setLives(value: Int) {
        val lives = findViewById<TextView>(R.id.lives)
        lives.text = "Жизни: " + value.toString()
    }

    override fun onFling(p0: MotionEvent?, p1: MotionEvent, p2: Float, p3: Float): Boolean {
        return false
    }


    private fun setRandomQuestion() {
        if (questions.isNotEmpty()) {
            question = questions[(0..<questions.size).random()]
            val questionView: TextView = findViewById(R.id.question)
            questionView.text = question.line
        } else {
            createWinDialog(this)
        }
    }

    private fun getScore(): Int {
        val score: TextView = findViewById(R.id.score)
        return score.text.toString().toInt()
    }

    private fun changeScore(increase: Boolean) {
        val score: TextView = findViewById(R.id.score)
        if (increase) {
            score.text = (getScore() + 1).toString()
        } else {
            score.text = (getScore() - 1).toString()
        }
        if (user.record < getScore()) {
            user.record = getScore()
        }
    }

    private fun updateUser() {
        val db = Firebase.firestore
        db.collection("users").get().addOnSuccessListener { result ->
            for (document in result) {
                if (document.getString("name").toString() == user.name) {
                    val ref = document.reference
                    ref.update("record", getScore().toString())

                }
            }
        }
    }

    private fun playerAnswered(answer: Boolean) {
        questions.remove(question)
        if (answer == question.answer) {
            setRandomQuestion()
            changeScore(true)
            Toast.makeText(this, "Верно!", Toast.LENGTH_SHORT).show()
        } else if (getLives() != 0) {
//            setLives(getLives()-1)
//            setRandomQuestion()
//            Toast.makeText(this, "Неверно!", Toast.LENGTH_SHORT).show()
            createLoseDialog(this)
        } else {
            if (user.record.toString().toInt() < getScore()) {
                updateUser()
            }
            createLoseDialog(this)
        }
    }

    private fun createLoseDialog(activity: Activity?) {
        val builder: AlertDialog.Builder = AlertDialog.Builder(activity)
        builder.setTitle("Вы проиграли!").setPositiveButton("Еще раз") { _, _ ->
            val score: TextView = findViewById(R.id.score)
            score.text = "0"
            setLives(3)
            questions.clear()
            questions = setQuestions()
            setRandomQuestion()
        }
            .setNegativeButton("Выйти") { _, _ ->
                val intent = Intent(this, MainMenuActivity::class.java)
                intent.putExtra("user", user)
                startActivity(intent)
            }
        builder.create().show()
    }

    private fun createWinDialog(activity: Activity?) {
        val builder: AlertDialog.Builder = AlertDialog.Builder(activity)
        builder.setTitle("Вопросов больше не осталось. Вы выиграли!")
            .setPositiveButton("Еще раз") { _, _ ->
                val score: TextView = findViewById(R.id.score)
                score.text = "0"
                setLives(3)
                questions.clear()
                questions = setQuestions()
                setRandomQuestion()
            }
            .setNegativeButton("Выйти") { _, _ ->
                val intent = Intent(this, MainMenuActivity::class.java)
                intent.putExtra("user", user)
                startActivity(intent)
            }
        builder.create().show()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putInt("score", getScore())
        outState.putInt("lives", getLives())
        outState.putParcelable("user", user)
        outState.putParcelableArrayList("questions", questions)
        outState.putParcelable("question", question)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        val score: TextView = findViewById(R.id.score)
        val questionText: TextView = findViewById(R.id.question)
        questions = savedInstanceState.getParcelableArrayList("questions")!!
        question = savedInstanceState.getParcelable("question")!!
        questionText.text = question.line
        user = savedInstanceState.getParcelable("user")!!
        setLives(savedInstanceState.getInt("lives"))
        score.text = savedInstanceState.getInt("score").toString()
    }

}

