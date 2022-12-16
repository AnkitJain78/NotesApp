package com.example.notesapp.view

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.notesapp.R

class AddNoteActivity : AppCompatActivity() {
    lateinit var editTextTitle: EditText
    lateinit var editTextDescription: EditText
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_note)
        editTextTitle = findViewById(R.id.editTextTitle)
        editTextDescription = findViewById(R.id.editTextDescription)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_add_note_activity, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.saveNote -> {
                val inputTitle: String = editTextTitle.text.toString()
                val inputDescription: String = editTextDescription.text.toString()
                if (inputTitle.isEmpty() || inputDescription.isEmpty())
                    Toast.makeText(
                        this@AddNoteActivity,
                        "Title or description can't be empty !!",
                        Toast.LENGTH_LONG
                    ).show()
                else {
                    intent.putExtra("com.example.notesapp.view.Title", inputTitle)
                    intent.putExtra("com.example.notesapp.view.Description", inputDescription)
                    setResult(RESULT_OK, intent)
                    finish()
                }
            }
            R.id.cancelNote -> {
                Toast.makeText(this@AddNoteActivity, "Current Note discarded", Toast.LENGTH_LONG)
                    .show()
                finish()
            }
        }
        return true
    }
}