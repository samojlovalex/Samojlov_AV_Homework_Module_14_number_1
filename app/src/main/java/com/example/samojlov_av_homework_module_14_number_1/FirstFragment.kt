package com.example.samojlov_av_homework_module_14_number_1

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.samojlov_av_homework_module_14_number_1.databinding.FragmentFirstBinding
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

@Suppress("DEPRECATION")
class FirstFragment : Fragment() {

    private lateinit var binding: FragmentFirstBinding
    private lateinit var toolbarFirst: androidx.appcompat.widget.Toolbar
    private lateinit var editTextET: EditText
    private lateinit var recyclerViewRV: RecyclerView
    private lateinit var saveButtonBT: Button

    private var listNotes = mutableListOf<Note>()
    private var adapter: ListAdapter? = null
    private lateinit var db: DBHelper

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentFirstBinding.inflate(inflater, container, false)
        // Inflate the layout for this fragment
        db = DBHelper.getInstance(requireContext())
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        toolbarFirst = binding.toolbarFirst
        toolbarFirst.inflateMenu(R.menu.menu)
        toolbarFirst.setTitle(R.string.toolbar_title)
        toolbarFirst.setSubtitle(R.string.toolbar_subtitle)
        toolbarFirst.setOnMenuItemClickListener { item: MenuItem? ->
            when (item!!.itemId) {
                R.id.exitMenu -> {
                    Toast.makeText(
                        context,
                        getString(R.string.toast_exit),
                        Toast.LENGTH_LONG
                    ).show()
                    activity?.finish()
                }

                R.id.removeMenu -> {
                    Toast.makeText(
                        context,
                        getString(R.string.toast_remove),
                        Toast.LENGTH_LONG
                    ).show()
                    db.removeAll()
                }
            }
            return@setOnMenuItemClickListener super.onOptionsItemSelected(item)
        }

        init()
    }

    private fun init() {
        editTextET = binding.editTextET
        recyclerViewRV = binding.recyclerViewRV
        recyclerViewRV.layoutManager = LinearLayoutManager(context)

        saveButtonBT = binding.saveButtonBT

        listNotesInit()

        initAdapter()

        saveButtonBT.setOnClickListener {
            if (editTextET.text.isEmpty()) return@setOnClickListener

            createNote()
            listNotesInit()
            initAdapter()

        }

    }

    private fun listNotesInit() {
        listNotes = db.readNotes()
    }


    private fun createNote() {
        val stroke = editTextET.text.toString()
        val time = ZonedDateTime.now(ZoneId.of("Europe/Moscow"))
            .format(DateTimeFormatter.ofPattern("HH.mm.ss\ndd.MM.yy"))
        val note = Note(stroke, 0, time, 0)
        db.addNote(note)
        Toast.makeText(context, "${db.readNotes().size}", Toast.LENGTH_LONG).show()
        editTextET.text.clear()
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun initAdapter() {
        adapter = ListAdapter(listNotes, requireContext())
        recyclerViewRV.adapter = adapter
        recyclerViewRV.setHasFixedSize(true)
        adapter!!.notifyDataSetChanged()
    }
}