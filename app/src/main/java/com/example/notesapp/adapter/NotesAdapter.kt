package com.example.notesapp.adapter

import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.notesapp.R
import com.example.notesapp.model.Notes
import com.example.notesapp.view.MainActivity
import com.example.notesapp.view.UpdateActivity

class NotesAdapter(private val activity: MainActivity) :
    RecyclerView.Adapter<NotesAdapter.NotesViewHolder>() {
    var notesList = ArrayList<Notes>()

    class NotesViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var title: TextView = itemView.findViewById(R.id.textViewTitle)
        var description: TextView = itemView.findViewById(R.id.textViewDescription)
        var cardView: CardView = itemView.findViewById(R.id.cardView)
        var imageShare: ImageView = itemView.findViewById(R.id.imageShare)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotesViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_note, parent, false)
        return NotesViewHolder(view)
    }

    override fun onBindViewHolder(holder: NotesViewHolder, position: Int) {
        val currentNote: Notes = notesList[position]
        holder.title.text = currentNote.title
        holder.description.text = currentNote.description
        holder.cardView.setOnClickListener {
            val intent = Intent(activity, UpdateActivity::class.java)
            intent.putExtra("titleToUpdate", currentNote.title)
            intent.putExtra("descriptionToUpdate", currentNote.description)
            intent.putExtra("currentId", currentNote.id)
            Log.d("value", "${currentNote.title} + ${currentNote.description} + ${currentNote.id}")
            activity.activityResultLauncher2.launch(intent)
        }
        holder.imageShare.setOnClickListener {
            val sendIntent: Intent = Intent().apply {
                action = Intent.ACTION_SEND
                putExtra(
                    Intent.EXTRA_TEXT,
                    "Hey Check out this note -- \nTitle = '${holder.title.text}' \nDescription  \n${holder.description.text}"
                )
                type = "text/plain"
            }

            val shareIntent = Intent.createChooser(sendIntent, null)
            activity.startActivity(shareIntent)
        }
    }

    override fun getItemCount(): Int {
        return notesList.size
    }

    fun setData(notesList: List<Notes>) {
        this.notesList = notesList as ArrayList<Notes>
        notifyDataSetChanged()
    }

    fun atPosition(position: Int): Notes {
        return notesList[position]
    }

}