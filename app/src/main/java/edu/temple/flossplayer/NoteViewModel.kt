package edu.temple.flossplayer

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class NoteViewModel : ViewModel() {

    val noteList: NoteList by lazy {
        NoteList()
    }

    private val selectedNoteObject: MutableLiveData<NoteObject>? by lazy {
        MutableLiveData()
    }

    // This item serves only as a notifier. We don't actually
    // care about the data it's storing. It's just a means to
    // have an observer be notified that something (new books have been added)
    // has happened
    private val updatedNoteList : MutableLiveData<Int> by lazy {
        MutableLiveData()
    }

    // Flag to determine if one-off event should fire
    private var viewedNote = false

    fun getSelectedNote(): LiveData<NoteObject>? {
        return selectedNoteObject
    }

    fun setSelectedNote(selectedNoteObject: NoteObject) {
        viewedNote = false
        this.selectedNoteObject?.value = selectedNoteObject
    }

    fun clearSelectedNote () {
        viewedNote = true
        selectedNoteObject?.value = null
    }

    fun markSelectedNoteViewed () {
        viewedNote = true
    }

    fun hasViewedSelectedNote() : Boolean {
        return viewedNote
    }

    fun updateNotes (noteObjects: Array<NoteObject>) {
        noteList.clear()
        for (i in 0 until noteObjects.size) {
            noteList.add(noteObjects[i])
        }
        notifyUpdatedNoteList()
    }

    // The indirect observable for those that want to know when
    // the book list has changed
    fun getUpdatedNoteList() : LiveData<out Any> {
        return updatedNoteList
    }

    // A trivial update used to indirectly notify observers that the Booklist has changed
    private fun notifyUpdatedNoteList() {
        updatedNoteList.value = updatedNoteList.value?.plus(1)
    }
}