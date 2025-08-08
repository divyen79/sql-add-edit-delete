package com.invitationmaker.lss.sqladddeleteedit

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class ItemDatabaseHelper(context: Context) : SQLiteOpenHelper(context, "itemDB.db", null, 1) {

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL("CREATE TABLE items (id INTEGER PRIMARY KEY AUTOINCREMENT, name TEXT)")
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS items")
        onCreate(db)
    }

    fun insertItem(name: String) {
        val db = writableDatabase
        val values = ContentValues()
        values.put("name", name)
        db.insert("items", null, values)
        db.close()
    }

    fun updateItem(id: Int, name: String) {
        val db = writableDatabase
        val values = ContentValues()
        values.put("name", name)
        db.update("items", values, "id=?", arrayOf(id.toString()))
        db.close()
    }

    fun deleteItem(id: Int) {
        val db = writableDatabase
        db.delete("items", "id=?", arrayOf(id.toString()))
        db.close()
    }

    fun getAllItems(): List<Item> {
        val list = mutableListOf<Item>()
        val db = readableDatabase
        val cursor = db.rawQuery("SELECT * FROM items", null)
        if (cursor.moveToFirst()) {
            do {
                val item = Item(cursor.getInt(0), cursor.getString(1))
                list.add(item)
            } while (cursor.moveToNext())
        }
        cursor.close()
        return list
    }
}
