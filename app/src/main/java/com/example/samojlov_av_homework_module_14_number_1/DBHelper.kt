package com.example.samojlov_av_homework_module_14_number_1

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteException
import android.database.sqlite.SQLiteOpenHelper

class DBHelper(context: Context) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_NAME = "NOTE_DATABASE"
        private const val DATABASE_VERSION = 1
        const val TABLE_NAME = "note_table"
        const val KEY_ID = "id"
        const val KEY_STROKE = "stroke"
        const val KEY_COMPLETED = "completed"
        const val KEY_TIME = "time"

        @Volatile
        private var INSTANCE: DBHelper? = null

        fun getInstance(context: Context): DBHelper {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: DBHelper(context.applicationContext).also { INSTANCE = it }
            }
        }
    }

    override fun onCreate(db: SQLiteDatabase?) {
        val query =
            ("CREATE TABLE " + TABLE_NAME + " (" +
                    KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    KEY_STROKE + " TEXT, " +
                    KEY_COMPLETED + " INTEGER, " +
                    KEY_TIME + " TEXT" + ")")

        db?.execSQL(query)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db!!.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
        onCreate(db)
    }

    fun addNote(note: Note) {
        val db = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put(KEY_STROKE, note.stroke)
        contentValues.put(KEY_COMPLETED, note.completed)
        contentValues.put(KEY_TIME, note.time)
        try {
            db.insert(TABLE_NAME, null, contentValues)
        } catch (e: SQLiteException) {
            e.printStackTrace()
        }
        db.close()
    }

    @SuppressLint("Range", "Recycle")
    fun readNotes(): MutableList<Note> {
        val notesList = mutableListOf<Note>()
        val selectQuery = "SELECT * FROM $TABLE_NAME"
        val db = this.readableDatabase
        val cursor: Cursor?
        try {
            cursor = db.rawQuery(selectQuery, null)
        } catch (e: SQLiteException) {
            db.execSQL(selectQuery)
            return notesList
        }

        var noteId: Int
        var noteStroke: String
        var noteCompleted: Int
        var noteTime: String
        if (cursor.moveToFirst()) {
            do {
                noteId = cursor.getInt(cursor.getColumnIndex("id"))
                noteStroke = cursor.getString(cursor.getColumnIndex("stroke"))
                noteCompleted = cursor.getInt(cursor.getColumnIndex("completed"))
                noteTime = cursor.getString(cursor.getColumnIndex("time"))
                val note = Note(noteStroke, noteCompleted, noteTime, noteId)
                notesList.add(note)
            } while (cursor.moveToNext())
        }
        return notesList
    }

    fun removeAll() {
        val db = this.writableDatabase
        db.delete(TABLE_NAME, null, null)
    }

    fun updateNote(note: Note) {
        val db = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put(KEY_STROKE, note.stroke)
        contentValues.put(KEY_COMPLETED, note.completed)
        contentValues.put(KEY_TIME, note.time)
        try {
            db.update(TABLE_NAME, contentValues, "id=" + note.id, null)
        } catch (e: SQLiteException) {
            e.printStackTrace()
        }
        db.close()
    }

    fun deleteNote(note: Note) {
        val db = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put(KEY_ID, note.id)
        try {
            db.delete(TABLE_NAME, "id=" + note.id, null)
        } catch (e: SQLiteException) {
            e.printStackTrace()
        }
        db.close()
    }
}