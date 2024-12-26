package com.example.myapplication

import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.ListView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    //    private var ed_book: EditText? = null
//    private var ed_price: EditText? = null
//    private var btn_query: Button? = null
//    private var btn_insert: Button? = null
//    private var btn_update: Button? = null
//    private var btn_delete: Button? = null
//    private var listView: ListView? = null
    private lateinit var adapter: ArrayAdapter<String>
    private val item = ArrayList<String>()
    private lateinit var dbrw: SQLiteDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        var ed_book = findViewById<EditText>(R.id.ed_book)
        var ed_price = findViewById<EditText>(R.id.ed_price)
        var btn_query = findViewById<Button>(R.id.btn_query)
        var btn_delete = findViewById<Button>(R.id.btn_delete)
        var btn_insert = findViewById<Button>(R.id.btn_insert)
        var btn_update = findViewById<Button>(R.id.btn_update)
        var listView = findViewById<ListView>(R.id.listview)
        adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, item)
        listView.setAdapter(adapter)
        dbrw = MyDBHelper(this).writableDatabase
        btn_insert.setOnClickListener(View.OnClickListener { view: View? ->
            if (ed_book.length() < 1 || ed_price.length() < 1) Toast.makeText(
                this@MainActivity,
                "欄位請勿流空",
                Toast.LENGTH_SHORT
            ).show() else {
                try {
                    dbrw!!.execSQL(
                        "INSERT INTO myTable(book,price) values(?,?)",
                        arrayOf<Any>(
                            ed_book.getText().toString(),
                            ed_price.getText().toString()
                        )
                    )
                    Toast.makeText(
                        this@MainActivity, "新增書名" + ed_book.getText().toString()
                                + "  價格" + ed_price.getText().toString(), Toast.LENGTH_SHORT
                    ).show()
                    ed_book.setText("")
                    ed_price.setText("")
                } catch (e: Exception) {
                    e.printStackTrace()
                    Toast.makeText(this@MainActivity, "新增失敗" + e.message, Toast.LENGTH_SHORT)
                        .show()
                }
            }
        })
        btn_update.setOnClickListener(View.OnClickListener { view: View? ->
            if (ed_book.length() < 1 || ed_price.length() < 1) Toast.makeText(
                this@MainActivity,
                "欄位請勿流空",
                Toast.LENGTH_SHORT
            ).show() else {
                try {
                    dbrw!!.execSQL(
                        "UPDATE mytable SET price = "
                                + ed_price.getText().toString()
                                + " WHERE book LIKE '"
                                + ed_book.getText().toString() + "'"
                    )
                    Toast.makeText(
                        this@MainActivity, "更新書名" + ed_book.getText().toString() +
                                "   價格" + ed_price.getText().toString(), Toast.LENGTH_SHORT
                    ).show()
                    ed_book.setText("")
                    ed_price.setText("")
                } catch (e: Exception) {
                    e.printStackTrace()
                    Toast.makeText(this@MainActivity, "更新失敗" + e.message, Toast.LENGTH_SHORT)
                        .show()
                }
            }
        })
        btn_delete.setOnClickListener(View.OnClickListener { view: View? ->
            if (ed_book.length() < 1 || ed_price.length() < 1) Toast.makeText(
                this@MainActivity,
                "欄位請勿流空",
                Toast.LENGTH_SHORT
            ).show() else {
                try {
                    dbrw!!.execSQL(
                        "DELETE FROM myTable WHERE book LIKE '" + ed_book.getText().toString() + "'"
                    )
                    Toast.makeText(
                        this@MainActivity,
                        "刪除書名" + ed_book.getText().toString(),
                        Toast.LENGTH_SHORT
                    ).show()
                    ed_book.setText("")
                    ed_price.setText("")
                } catch (e: Exception) {
                    e.printStackTrace()
                    Toast.makeText(this@MainActivity, "刪除失敗" + e.message, Toast.LENGTH_SHORT)
                        .show()
                }
            }
        })
        btn_query.setOnClickListener(View.OnClickListener { view: View? ->
            val c: Cursor
            c = if (ed_book.length() < 1) dbrw!!.rawQuery(
                "SELECT * FROM myTable",
                null
            ) else dbrw!!.rawQuery(
                "SELECT * FROM myTable WHERE book LIKE '" + ed_book.getText().toString() + "'",
                null
            )
            c.moveToFirst()
            item.clear()
            Toast.makeText(this@MainActivity, "共有" + c.count + "筆", Toast.LENGTH_SHORT)
                .show()
            for (i in 0 until c.count) {
                item.add("書籍" + c.getString(0) + "\t\t\t\t價格" + c.getString(1))
                c.moveToNext()
            }
            adapter!!.notifyDataSetChanged()
            c.close()
        })
    }

    override fun onDestroy() {
        super.onDestroy()
        dbrw!!.close()
    }
}