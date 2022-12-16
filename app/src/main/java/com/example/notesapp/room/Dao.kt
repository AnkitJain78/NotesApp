package com.example.notesapp.room

import androidx.room.*
import androidx.room.Dao
import com.example.notesapp.model.Notes
import kotlinx.coroutines.flow.Flow

@Dao
interface Dao {
    @Insert
    suspend fun insert(note: Notes)

    @Update
    suspend fun update(note: Notes)

    @Delete
    suspend fun delete(note: Notes)

    @Query("Delete FROM notes_table")
    suspend fun deleteAll()

    @Query("SELECT * FROM notes_table ORDER BY id ASC")
    fun getAll(): Flow<List<Notes>>


}