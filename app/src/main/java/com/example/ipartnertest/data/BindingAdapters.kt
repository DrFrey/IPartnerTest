package com.example.ipartnertest.data

import androidx.databinding.BindingAdapter
import com.google.android.material.textview.MaterialTextView
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle

@BindingAdapter("convertDate")
fun convertDate(textView: MaterialTextView, string: String) {
    val timeStamp = string.toLong()
    val date = LocalDateTime.ofInstant(Instant.ofEpochSecond(timeStamp), ZoneId.systemDefault())
    textView.text = date.format(DateTimeFormatter.ofLocalizedDateTime(FormatStyle.FULL).withZone(ZoneId.systemDefault()))
}