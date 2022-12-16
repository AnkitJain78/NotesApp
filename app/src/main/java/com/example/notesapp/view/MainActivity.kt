package com.example.notesapp.view

import android.content.DialogInterface
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.notesapp.NotesApplication
import com.example.notesapp.R
import com.example.notesapp.adapter.NotesAdapter
import com.example.notesapp.model.Notes
import com.example.notesapp.viewModel.NotesViewModel
import com.example.notesapp.viewModel.NotesViewModelFactory

class MainActivity : AppCompatActivity() {
    lateinit var notesViewModel: NotesViewModel
    lateinit var recyclerView: RecyclerView
    lateinit var notesAdapter: NotesAdapter
    lateinit var activityResultLauncher: ActivityResultLauncher<Intent>
    lateinit var activityResultLauncher2: ActivityResultLauncher<Intent>
    lateinit var appSettingPrefs: SharedPreferences
    lateinit var sharedPrefsEditor: SharedPreferences.Editor

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        appSettingPrefs = getSharedPreferences("AppSettingPrefs",0)
        sharedPrefsEditor = appSettingPrefs.edit()
        var isNightModeOn : Boolean = appSettingPrefs.getBoolean("NightMode",false)
        if(isNightModeOn)
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        else
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)

        recyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)
        notesAdapter = NotesAdapter(this)
        recyclerView.adapter = notesAdapter
        register()

        ItemTouchHelper(object :
            ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return true
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                notesViewModel.delete(notesAdapter.atPosition(viewHolder.adapterPosition))
            }
        }).attachToRecyclerView(recyclerView)

        val viewModelFactory = NotesViewModelFactory((application as NotesApplication).repository)
        notesViewModel = ViewModelProvider(this, viewModelFactory).get(NotesViewModel::class.java)
        notesViewModel.allNotes.observe(this, Observer {
            notesAdapter.setData(it)
        })
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_items, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.addNote -> {
                val intent = Intent(this@MainActivity, AddNoteActivity::class.java)
                activityResultLauncher.launch(intent)
            }
            R.id.deleteAll -> dialog()
            R.id.changeTheme -> themeChangerDialog()
        }
        return true
    }

    private fun themeChangerDialog() {
        val dialog = AlertDialog.Builder(this@MainActivity,R.style.AlertDialogCustom)
        dialog.setTitle("Which theme do you want?")
        dialog.setPositiveButton("Light", DialogInterface.OnClickListener { _, _ ->
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            sharedPrefsEditor.putBoolean("NightMode",false)
            sharedPrefsEditor.apply()
        })
        dialog.setNegativeButton("Dark", DialogInterface.OnClickListener { _, _ ->
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            sharedPrefsEditor.putBoolean("NightMode",true)
            sharedPrefsEditor.apply()
        })
        dialog.create().show()
    }

    private fun dialog() {
        val dialog = AlertDialog.Builder(this@MainActivity,R.style.AlertDialogCustom)
        dialog.setTitle("Delete All Notes")
        dialog.setMessage("if click yes, all notes will be deleted. If you want to delete a specific note, swipe left or right")
        dialog.setPositiveButton("Yes", DialogInterface.OnClickListener { _, _ ->
            notesViewModel.deleteAll()
        })
        dialog.setNegativeButton("No", DialogInterface.OnClickListener { dialog2, _ ->
            dialog2.cancel()
        })
        dialog.create().show()
    }

    private fun register() {
        activityResultLauncher = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) {
            val resultCode = it.resultCode
            val data = it.data
            if (resultCode == RESULT_OK && data != null) {
                val notesTitle: String =
                    data.getStringExtra("com.example.notesapp.view.Title").toString()
                val notesDescription: String =
                    data.getStringExtra("com.example.notesapp.view.Description").toString()
                val note = Notes(notesTitle, notesDescription)
                notesViewModel.insert(note)
                Toast.makeText(
                    this@MainActivity, "Note Saved",
                    Toast.LENGTH_LONG
                ).show()
            }
        }
        activityResultLauncher2 = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) {
            val resultCode = it.resultCode
            val data = it.data
            if (resultCode == RESULT_OK && data != null) {
                val notesTitle: String =
                    data.getStringExtra("updatedTitle").toString()
                val notesDescription: String =
                    data.getStringExtra("updatedDescription").toString()
                val notesId: Int = data.getIntExtra("idForUpdate", -1)
                Log.d("update", "$notesTitle + $notesDescription + $notesId")
                val note = Notes(notesTitle, notesDescription)
                note.id = notesId
                notesViewModel.update(note)
                Toast.makeText(
                    this@MainActivity, "Note Updated",
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }

}