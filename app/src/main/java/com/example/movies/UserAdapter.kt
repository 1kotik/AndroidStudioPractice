package com.example.movies

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView

class UserAdapter : ArrayAdapter<User> {
    var inflater: LayoutInflater
    var layout: Int
    var users: ArrayList<User>

    constructor(context: Context, resource: Int, _users: ArrayList<User>) :
            super(context, resource, _users) {
        users = _users
        layout=resource
        inflater=LayoutInflater.from(context)
    }


    fun getView_(position: Int, convertView: View, parent: ViewGroup): View{
        val view=inflater.inflate(this.layout, parent, false)
        val nameView=view.findViewById<TextView>(R.id.name)
        val recordView=view.findViewById<TextView>(R.id.record)

        val user=users.get(position)

        nameView.text = user.name
        recordView.setText(user.record)

        return view
    }
}