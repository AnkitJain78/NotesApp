package com.example.notesapp.viewModel

import androidx.lifecycle.*
import com.example.notesapp.model.Notes
import com.example.notesapp.repository.NotesRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class NotesViewModel(private val repository: NotesRepository) : ViewModel() {

    var allNotes: LiveData<List<Notes>> = repository.allNotes.asLiveData()

    fun insert(note: Notes) {
        viewModelScope.launch(Dispatchers.IO) { repository.insert(note) }
    }

    fun update(note: Notes) {
        viewModelScope.launch(Dispatchers.IO) { repository.update(note) }
    }

    fun delete(note: Notes) {
        viewModelScope.launch(Dispatchers.IO) { repository.delete(note) }
    }

    fun deleteAll() {
        viewModelScope.launch(Dispatchers.IO) { repository.deleteAll() }
    }
}

class NotesViewModelFactory(private var repository: NotesRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(NotesViewModel::class.java)) {
            return NotesViewModel(repository) as T
        } else
            throw IllegalArgumentException("Unknown ViewModel class")
    }
}