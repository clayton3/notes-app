package edu.temple.flossplayer

import android.app.SearchManager
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProvider
import com.android.volley.RequestQueue
import com.android.volley.toolbox.Volley
import com.google.android.material.floatingactionbutton.FloatingActionButton

class MainActivity : AppCompatActivity() {

    private val searchURL = "https://kamorris.com/lab/flossplayer/search.php?query="
    private lateinit var addButton: FloatingActionButton

    private val isSingleContainer : Boolean by lazy{
        findViewById<View>(R.id.container2) == null
    }

    private val noteViewModel : NoteViewModel by lazy {
        ViewModelProvider(this)[NoteViewModel::class.java]
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        addButton = findViewById(R.id.floatingActionButton)
        // If we're switching from one container to two containers
        // clear BookPlayerFragment from container1
        if (supportFragmentManager.findFragmentById(R.id.container1) is NotePlayerFragment) {
            supportFragmentManager.popBackStack()
        }

        // If this is the first time the activity is loading, go ahead and add a BookListFragment
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .add(R.id.container1, NoteListFragment())
                .commit()
        } else
        // If activity loaded previously, there's already a BookListFragment
        // If we have a single container and a selected book, place it on top
            if (isSingleContainer && noteViewModel.getSelectedNote()?.value != null) {
                supportFragmentManager.beginTransaction()
                    .replace(R.id.container1, NotePlayerFragment())
                    .setReorderingAllowed(true)
                    .addToBackStack(null)
                    .commit()
            }

        // If we have two containers but no BookPlayerFragment, add one to container2
        if (!isSingleContainer && supportFragmentManager.findFragmentById(R.id.container2) !is NotePlayerFragment)
            supportFragmentManager.beginTransaction()
                .add(R.id.container2, NotePlayerFragment())
                .commit()


        // Respond to selection in portrait mode using flag stored in ViewModel
        noteViewModel.getSelectedNote()?.observe(this){
            if (!noteViewModel.hasViewedSelectedNote()) {
                if (isSingleContainer) {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.container1, NotePlayerFragment())
                        .setReorderingAllowed(true)
                        .addToBackStack(null)
                        .commit()
                }
                noteViewModel.markSelectedNoteViewed()
            }
        }

        findViewById<View>(R.id.searchImageButton).setOnClickListener {
            onSearchRequested()
        }
    }

    override fun onBackPressed() {
        // BackPress clears the selected book
        noteViewModel.clearSelectedNote()
        super.onBackPressed()
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)

        if (Intent.ACTION_SEARCH == intent!!.action) {
            intent.getStringExtra(SearchManager.QUERY)?.also {
                searchBooks(it)

                // Unselect previous book selection
                noteViewModel.clearSelectedNote()

                // Remove any unwanted DisplayFragments instances from the stack
                supportFragmentManager.popBackStack()
            }
        }

    }

    private fun makeNoteArr() : Array<NoteObject> {
        val noteList = arrayOfNulls<NoteObject>(10)

        for(i in 0 until 10)
            noteList[i] = NoteObject(i, "Title $i", "$i", "https://clickup.com/blog/wp-content/uploads/2020/01/note-taking.png")

        return noteList as Array<NoteObject>
    }

    private fun searchBooks(searchTerm: String) {
        noteViewModel.updateNotes(makeNoteArr())
    }
}