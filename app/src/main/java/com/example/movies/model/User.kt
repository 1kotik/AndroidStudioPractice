package com.example.movies.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class User(val name: String, var record: Int) : Parcelable{

}