package com.example.notesapp.repository

import androidx.annotation.WorkerThread
import com.example.notesapp.model.Notes
import com.example.notesapp.room.Dao
import kotlinx.coroutines.flow.Flow

class NotesRepository(private val dao: Dao) {
    val allNotes: Flow<List<Notes>> = dao.getAll()
    @WorkerThread
    suspend fun insert(note: Notes) {
        dao.insert(note)
    }

    @WorkerThread
    suspend fun update(note: Notes) {
        dao.update(note)
    }

    @WorkerThread
    suspend fun delete(note: Notes) {
        dao.delete(note)
    }

    @WorkerThread
    suspend fun deleteAll() {
        dao.deleteAll()
    }
}
