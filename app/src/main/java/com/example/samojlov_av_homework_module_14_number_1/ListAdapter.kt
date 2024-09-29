package com.example.samojlov_av_homework_module_14_number_1

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView


class ListAdapter(private val list: MutableList<Note>, context: Context) :
    RecyclerView.Adapter<ListAdapter.NotesViewHolder>() {

    private var onNotesClickListener: OnNotesClickListener? = null
    private val db = DBHelper(context)

    interface OnNotesClickListener {
        fun onNotesClick(notes: Note, position: Int)
    }

    class NotesViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val number: TextView = itemView.findViewById(R.id.noteNumberTV)
        val stroke: TextView = itemView.findViewById(R.id.noteTextTV)
        val time: TextView = itemView.findViewById(R.id.timeTV)
        val completedCB: CheckBox = itemView.findViewById(R.id.noteCompletedCB)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotesViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.list_item, parent, false)
        return NotesViewHolder(itemView)
    }

    override fun getItemCount() = list.size

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: NotesViewHolder, position: Int) {
        val note = list[position]
        holder.number.text = (position + 1).toString()
        holder.stroke.text = note.stroke
        holder.time.text = note.time

        holder.completedCB.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                note.completed = 1
            } else note.completed = 0

            checkFlag(note,holder)

            db.updateNote(note)
        }

        checkFlag(note, holder)
    }

    private fun checkFlag(
        note: Note,
        holder: NotesViewHolder
    ) {
        if (note.completed == 1) {
            holder.number.setTextColor(Color.parseColor("#808080"))
            holder.stroke.setTextColor(Color.parseColor("#808080"))
        } else {
            holder.number.setTextColor(Color.parseColor("#0000ff"))
            holder.stroke.setTextColor(Color.parseColor("#0000ff"))
        }
    }

    fun setOnNotesClickListener(onNotesClickListener: OnNotesClickListener) {
        this.onNotesClickListener = onNotesClickListener
    }

}
