package edu.temple.flossplayer

class NoteList {

    private val noteObjectList : ArrayList<NoteObject> by lazy {
        ArrayList()
    }

    fun add(noteObject: NoteObject) {
        noteObjectList.add(noteObject)
    }

    fun remove (noteObject: NoteObject) {
        noteObjectList.remove(noteObject)
    }

    fun clear() {
        noteObjectList.clear()
    }

    operator fun get(index: Int) = noteObjectList[index]

    fun size() = noteObjectList.size
}