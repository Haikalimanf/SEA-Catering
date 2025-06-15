package com.example.seacatering.utils

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Context
import android.widget.Toast
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

object DateTimePickerUtil {

    fun showDateRangePicker(context: Context, onRangeSelected: (startDate: String, endDate: String) -> Unit) {
        val calendar = Calendar.getInstance()
        val formatter = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())

        DatePickerDialog(
            context,
            { _, startYear, startMonth, startDayOfMonth ->
                val startCal = Calendar.getInstance().apply {
                    set(startYear, startMonth, startDayOfMonth)
                }

                DatePickerDialog(
                    context,
                    { _, endYear, endMonth, endDayOfMonth ->
                        val endCal = Calendar.getInstance().apply {
                            set(endYear, endMonth, endDayOfMonth)
                        }

                        // Validasi agar endDate tidak sebelum startDate
                        if (endCal.before(startCal)) {
                            Toast.makeText(context, "End date cannot be before start date", Toast.LENGTH_SHORT).show()
                        } else {
                            val startDateStr = formatter.format(startCal.time)
                            val endDateStr = formatter.format(endCal.time)
                            onRangeSelected(startDateStr, endDateStr)
                        }
                    },
                    startCal.get(Calendar.YEAR),
                    startCal.get(Calendar.MONTH),
                    startCal.get(Calendar.DAY_OF_MONTH)
                ).apply {
                    datePicker.minDate = startCal.timeInMillis // batas minimal: tanggal mulai
                }.show()

            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        ).show()
    }
}


