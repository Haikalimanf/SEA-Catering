package com.example.seacatering.utils

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Context
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

object DateTimePickerUtil {
    fun showDateTimePicker(context: Context, onDateTimeSelected: (String) -> Unit) {
        val calender = Calendar.getInstance()

        val datePicker = DatePickerDialog(
            context,
            { _, year, month, dayOfMonth ->
                val selectedDate = Calendar.getInstance()
                selectedDate.set(year, month, dayOfMonth)

                // Setelah pilih tanggal, lanjut ke time picker
                TimePickerDialog(
                    context,
                    { _, hourOfDay, minute ->
                        selectedDate.set(Calendar.HOUR_OF_DAY, hourOfDay)
                        selectedDate.set(Calendar.MINUTE, minute)

                        val format = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault())
                        val formatted = format.format(selectedDate.time)
                        onDateTimeSelected(formatted)
                    },
                    calender.get(Calendar.HOUR_OF_DAY),
                    calender.get(Calendar.MINUTE),
                    true
                ).show()
            },
            calender.get(Calendar.YEAR),
            calender.get(Calendar.MONTH),
            calender.get(Calendar.DAY_OF_MONTH)
        )
        datePicker.show()
    }
}
