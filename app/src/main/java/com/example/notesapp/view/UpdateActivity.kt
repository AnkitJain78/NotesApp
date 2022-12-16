package com.example.notesapp.view

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.notesapp.R


class UpdateActivity : AppCompatActivity() {
    lateinit var editTextTitleUpdate: EditText
    lateinit var editTextDescriptionUpdate: EditText
    var id: Int = -1
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_update)
        editTextTitleUpdate = findViewById(R.id.editTextTitleUpdate)
        editTextDescriptionUpdate = findViewById(R.id.editTextDescriptionUpdate)
        val title = intent.getStringExtra("titleToUpdate")
        val description = intent.getStringExtra("descriptionToUpdate")
        editTextTitleUpdate.setText(title)
        editTextDescriptionUpdate.setText(description)
        id = intent.getIntExtra("currentId", -1)
        Log.d("hey", "$title + $description + $id")

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_add_note_activity, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.saveNote -> {
                val inputTitle: String = editTextTitleUpdate.text.toString()
                val inputDescription: String = editTextDescriptionUpdate.text.toString()
                if (inputTitle.isEmpty() || inputDescription.isEmpty()) {
                    Toast.makeText(
                        this@UpdateActivity, "Title or description can't be empty !!",
                        Toast.LENGTH_LONG
                    ).show()
                } else if (id != -1) {
                    val intent2 = Intent()
                    intent2.putExtra("updatedTitle", inputTitle)
                    intent2.putExtra("updatedDescription", inputDescription)
                    intent2.putExtra("idForUpdate", id)
                    setResult(RESULT_OK, intent2)
                    finish()
                } else
                    Toast.makeText(this@UpdateActivity, "Error in updation", Toast.LENGTH_LONG)
                        .show()
            }

            R.id.cancelNote -> {
                Toast.makeText(this@UpdateActivity, "No Updation", Toast.LENGTH_LONG)
                    .show()
                finish()
            }
        }
        return true
    }
}
