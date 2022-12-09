package edu.temple.flossplayer

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider
import com.squareup.picasso.Picasso

class NoteFragment : Fragment() {
    private lateinit var titleTextView: TextView
    private lateinit var bodyTextView: TextView
    private lateinit var coverImageView: ImageView
    private lateinit var noteViewModel: NoteViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_note, container, false).apply {
            titleTextView = findViewById(R.id.titleTextView)
            bodyTextView = findViewById(R.id.bodyTextView)
            coverImageView = findViewById(R.id.coverImageView)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        noteViewModel = ViewModelProvider(requireActivity())[NoteViewModel::class.java]

        noteViewModel.getSelectedNote()?.observe(requireActivity()) { updateNote(it) }
    }

    private fun updateNote(note: NoteObject?) {
        note?.run {
            titleTextView.text = title
            bodyTextView.text = body
            Picasso.get().load(coverUri).into(coverImageView)
        }
    }

    private fun saveToNote(note: NoteObject?){
        note?.run {
            title = titleTextView.text.toString()
            body = bodyTextView.text.toString()
        }
    }

    override fun onDestroyView() {
        noteViewModel.getSelectedNote()?.observe(requireActivity()) { saveToNote(it) }

        super.onDestroyView()
    }

}