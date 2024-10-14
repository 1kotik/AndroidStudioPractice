package com.example.movies.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
class Question() : Parcelable {
    lateinit var line: String
    var answer: Boolean = false

    constructor(_line: String, _answer: Boolean) : this() {
        line = _line
        answer = _answer
    }
}