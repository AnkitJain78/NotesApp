package com.example.notesapp.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.notesapp.model.Notes
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Database(entities = [Notes::class], version = 1)
abstract class NotesDatabase : RoomDatabase() {
    abstract fun getDao(): Dao

    companion object {
        private var INSTANCE: NotesDatabase? = null
        fun getDatabase(context: Context, coroutineScope: CoroutineScope): NotesDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    NotesDatabase::class.java,
                    "notes_database"
                )
                    .addCallback(NotesCallback(coroutineScope))
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }

    private class NotesCallback(private val coroutineScope: CoroutineScope) :
        RoomDatabase.Callback() {
        override fun onCreate(db: SupportSQLiteDatabase) {
            super.onCreate(db)
            INSTANCE?.let {
                val dao = it.getDao()
                coroutineScope.launch {
                    dao.insert(Notes("Title 1", "Description 1"))
                    dao.insert(Notes("Title 2", "Description 2"))
                    dao.insert(Notes("Title 3", "Description 3"))
                }
            }
        }
    }
}