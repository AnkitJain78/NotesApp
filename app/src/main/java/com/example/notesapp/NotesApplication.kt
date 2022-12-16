package com.example.notesapp

import android.app.Application
import com.example.notesapp.repository.NotesRepository
import com.example.notesapp.room.NotesDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob

class NotesApplication : Application() {
    val applicationScope = CoroutineScope(SupervisorJob())
    val database by lazy { NotesDatabase.getDatabase(this, applicationScope) }
    val repository by lazy { NotesRepository(database.getDao()) }
}