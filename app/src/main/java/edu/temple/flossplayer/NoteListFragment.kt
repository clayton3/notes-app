package edu.temple.flossplayer

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso

class NoteListFragment : Fragment() {

    private lateinit var noteViewModel : NoteViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        noteViewModel = ViewModelProvider(requireActivity())[NoteViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_note_list, container, false)
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val onClick: (NoteObject) -> Unit = {
            // Update the ViewModel
                noteObject: NoteObject ->
            noteViewModel.setSelectedNote(noteObject)
            // Inform the activity of the selection so as to not have the event replayed
            // when the activity is restarted
        }

        with(view as RecyclerView) {
            layoutManager = GridLayoutManager(requireActivity(), 2)

            adapter = BookListAdapter(noteViewModel.noteList, onClick)

            noteViewModel.getUpdatedNoteList().observe(requireActivity()) {
                adapter?.notifyDataSetChanged()
            }
        }

    }

    class BookListAdapter (_noteList: NoteList, _onClick: (NoteObject) -> Unit) : RecyclerView.Adapter<BookListAdapter.BookViewHolder>() {
        private val noteList = _noteList
        private val onClick = _onClick

        inner class BookViewHolder (layout : View): RecyclerView.ViewHolder (layout) {
            val titleTextView : TextView = layout.findViewById(R.id.titleTextView)
            val imageView : ImageView = layout.findViewById(R.id.imageView)

            init {
                layout.setOnClickListener {
                    onClick(noteList[adapterPosition])
                }
            }
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BookViewHolder {
            return BookViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.notelist_items_layout, parent, false))
        }

        // Bind the book to the holder along with the values for the views
        override fun onBindViewHolder(holder: BookViewHolder, position: Int) {
            holder.titleTextView.text = noteList[position].title
            Picasso.get().load(noteList[position].coverUri).into(holder.imageView)
        }

        override fun getItemCount(): Int {
            return noteList.size()
        }

    }

}