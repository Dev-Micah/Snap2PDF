package com.micahnyabuto.snap2pdf.utils

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import java.util.Calendar


@Composable
fun Greeting(){
    val greetingText = greetingMessage()

    Text(text = greetingText,
        style = MaterialTheme.typography.titleMedium
    )

}

fun greetingMessage():String{
    val hour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY)

    return when (hour){
        in 0..5 -> "Good Night"
        in 6..11 -> "Good Morning 🌞"
        in 12..17 -> "Good Afternoon "
        else -> "Good Evening "
    }
}