package com.example.crudapp

import android.annotation.SuppressLint
import android.app.ProgressDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.DatePicker
import android.widget.TextView
import android.widget.Toast
import com.example.crudapp.helper.DBHelper
import com.google.android.material.textfield.TextInputEditText
import kotlin.concurrent.thread

class MainActivity : AppCompatActivity() {
    lateinit var inputName : TextInputEditText
    lateinit var inputBirth : TextInputEditText
    lateinit var buttonSubmit : Button
    lateinit var buttonPrint : Button
    lateinit var textID : TextView
    lateinit var textName : TextView
    lateinit var textBirth : TextView
    var progressDialog : ProgressDialog? = null

    @SuppressLint("Range")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        inputName = findViewById(R.id.inputName)
        inputBirth = findViewById(R.id.inputBirth)
        buttonSubmit = findViewById(R.id.buttonSubmit)
        buttonPrint = findViewById(R.id.buttonPrint)
        textID = findViewById(R.id.textID)
        textName = findViewById(R.id.textName)
        textBirth = findViewById(R.id.textBirth)

        buttonSubmit.setOnClickListener {
            val db = DBHelper(this, null)
            val name = inputName.text.toString()
            val birth = inputBirth.text.toString()

            if (name == "" || birth == "") {
                Toast.makeText(this@MainActivity, "Please Insert the Fields", Toast.LENGTH_SHORT).show()
            } else {
                db.addProfile(name,birth)
            }

            inputName.text!!.clear()
            inputBirth.text!!.clear()
        }

        loadHandler()

        buttonPrint.setOnClickListener {
            loadHandler()
        }
    }

    @SuppressLint("Range")
    fun loadHandler() {
        val db = DBHelper(this, null)
        val cursor = db.getProfile()

        progressDialog = ProgressDialog(this@MainActivity)
        progressDialog!!.setTitle("Loading")
        progressDialog!!.setMessage("Wait for a minutes ... data will show")
        progressDialog!!.max = 100
        progressDialog!!.progress = 1
        progressDialog!!.show()

        Thread(Runnable {
            try {
                Thread.sleep(1000)
            } catch (e : Exception) {
                e.printStackTrace()
            }
            progressDialog!!.dismiss()
        }).start()

        if(cursor!!.moveToFirst()) {
            textID.text = ""
            textName.text = ""
            textBirth.text = ""
            textID.append(cursor.getString(
                cursor.getColumnIndex(DBHelper.ID_COL)) + "\n")
            textName.append(cursor.getString(
                cursor.getColumnIndex(DBHelper.NAME_COL)) + "\n")
            textBirth.append(
                cursor.getString(cursor.getColumnIndex(DBHelper.BIRTH_COL)) + "\n")
        }

        if (cursor.moveToNext()) {
            while (cursor.moveToNext()) {
                textID.append(cursor.getString(
                    cursor.getColumnIndex(DBHelper.ID_COL)) + "\n")
                textName.append(cursor.getString(
                    cursor.getColumnIndex(  DBHelper.NAME_COL)) + "\n")
                textBirth.append(cursor.getString(
                    cursor.getColumnIndex(DBHelper.BIRTH_COL)) + "\n")
            }
        }

        cursor.close()
    }
}