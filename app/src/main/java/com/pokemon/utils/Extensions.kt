package com.pokemon.utils

import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import java.util.*

fun View.visible() {
    visibility = View.VISIBLE
}

fun View.gone() {
    visibility = View.GONE
}

fun View.invisible() {
    visibility = View.INVISIBLE
}

fun isNight(): Boolean {
    val currentHour = Calendar.getInstance(Locale.getDefault()).get(Calendar.HOUR_OF_DAY)
    return (currentHour <= 7 || currentHour >= 18)
}

fun AppCompatActivity.showToast(message: String) {
    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
}

fun getIdFromUrl(url: String): Int {
    return "/-?[0-9]+/$".toRegex()
        .find(url)!!.value.filter { it.isDigit() || it == '-' }
        .toInt()

}