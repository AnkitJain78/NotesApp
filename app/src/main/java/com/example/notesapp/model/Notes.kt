package com.example.notesapp.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "notes_table")
class Notes(val title: String, val description: String) {
    @PrimaryKey(autoGenerate = true)
    var id = 0
}