package com.invitationmaker.lss.sqladddeleteedit

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class MainActivity : AppCompatActivity() {

    private lateinit var dbHelper: ItemDatabaseHelper
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: ItemAdapter
    private var itemList = mutableListOf<Item>()
    private var editingItemId: Int? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        dbHelper = ItemDatabaseHelper(this)
        val etItemName = findViewById<EditText>(R.id.etItemName)
        val btnAdd = findViewById<Button>(R.id.btnAdd)
        recyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)

        loadItems()

        btnAdd.setOnClickListener {
            val name = etItemName.text.toString().trim()
            if (name.isEmpty()) {
                Toast.makeText(this, "Enter item name", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (editingItemId != null) {
                dbHelper.updateItem(editingItemId!!, name)
                editingItemId = null
                btnAdd.text = "Add"
            } else {
                dbHelper.insertItem(name)
            }

            etItemName.text.clear()
            loadItems()
        }
    }

    private fun loadItems() {
        itemList = dbHelper.getAllItems().toMutableList()
        adapter = ItemAdapter(itemList,
            onEdit = { item ->
                findViewById<EditText>(R.id.etItemName).setText(item.name)
                editingItemId = item.id
                findViewById<Button>(R.id.btnAdd).text = "Update"
            },
            onDelete = { item ->
                dbHelper.deleteItem(item.id)
                loadItems()
            }
        )
        recyclerView.adapter = adapter
    }
}
