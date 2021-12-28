package com.ezatpanah.solidprinciplesandmvvm.framework

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.ezatpanah.core.data.NoteModel
import com.ezatpanah.core.repository.NoteRepository
import com.ezatpanah.core.usecase.AddNote
import com.ezatpanah.core.usecase.GetAllNotes
import com.ezatpanah.core.usecase.GetNote
import com.ezatpanah.core.usecase.RemoveNote
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class NoteViewModel(application: Application) : AndroidViewModel(application) {

    private val coroutineScope = CoroutineScope(Dispatchers.IO)
    val repository =NoteRepository(RoomNoteDataSource(application))
    val useCases =UseCases(
        AddNote(repository),
        GetAllNotes(repository),
        GetNote(repository),
        RemoveNote(repository)
    )

    val saved=MutableLiveData<Boolean>()
    val currentNote=MutableLiveData<NoteModel?>()

    fun saveNote(noteModel: NoteModel){
        coroutineScope.launch {
            useCases.addNote(noteModel)
            saved.postValue(true)
        }
    }

    fun getNote(id:Long){
        coroutineScope.launch {
            val noteModel = useCases.getNote(id)
            currentNote.postValue(noteModel)
        }
    }

    fun deleteNote(noteModel: NoteModel){
        coroutineScope.launch {
            useCases.removeNote(noteModel)
            saved.postValue(true)
        }
    }
}