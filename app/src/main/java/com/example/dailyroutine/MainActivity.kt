package com.example.dailyroutine

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.ListView
import android.widget.TimePicker
import androidx.appcompat.app.AppCompatActivity
import android.content.Intent
import android.os.Build
import androidx.core.app.ActivityCompat

class MainActivity : AppCompatActivity() {

    private lateinit var listView: ListView
    private lateinit var addButton: Button
    private var routines: MutableList<RoutineItem> = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        listView = findViewById(R.id.routineListView)
        addButton = findViewById(R.id.addRoutineButton)

        routines = RoutineStorage.loadRoutines(this).toMutableList()
        refreshList()
if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.POST_NOTIFICATIONS), 100)
        }

        val serviceIntent = Intent(this, RoutineService::class.java)
        startForegroundService(serviceIntent)



        addButton.setOnClickListener {
            showAddRoutineDialog()
        }
    }

    private fun refreshList() {
        val displayList = routines.map {
            val startStr = String.format("%02d:%02d", it.startHour, it.startMinute)
            val endStr = String.format("%02d:%02d", it.endHour, it.endMinute)
            "$startStr - $endStr : ${it.taskName}"
        }
        val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, displayList)
        listView.adapter = adapter
    }

    private fun showAddRoutineDialog() {
        val dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_add_routine, null)
        val startTimePicker = dialogView.findViewById<TimePicker>(R.id.startTimePicker)
        val endTimePicker = dialogView.findViewById<TimePicker>(R.id.endTimePicker)
        val taskNameEditText = dialogView.findViewById<EditText>(R.id.taskNameEditText)

        startTimePicker.setIs24HourView(true)
        endTimePicker.setIs24HourView(true)

        AlertDialog.Builder(this)
            .setTitle("Add Routine")
            .setView(dialogView)
            .setPositiveButton("Save") { _, _ ->
                val taskName = taskNameEditText.text.toString().trim()
                if (taskName.isNotEmpty()) {
                    val newItem = RoutineItem(
                        startTimePicker.hour,
                        startTimePicker.minute,
                        endTimePicker.hour,
                        endTimePicker.minute,
                        taskName
                    )
                    routines.add(newItem)
                    routines.sortBy { it.startHour * 60 + it.startMinute }
                    RoutineStorage.saveRoutines(this, routines)
                    refreshList()
                }
            }
            .setNegativeButton("Cancel", null)
            .show()
    }
}
