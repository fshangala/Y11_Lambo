package com.fshangala.y11lambo

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.fshangala.y11.MasterViewModel

class MasterActivity : AppCompatActivity() {
    var sharedPref:SharedPreferences? = null
    var model: MasterViewModel? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_master)

        sharedPref = getSharedPreferences("MySettings", Context.MODE_PRIVATE)
        model = ViewModelProvider(this)[MasterViewModel::class.java]

        val connectionStatus = findViewById<TextView>(R.id.masterConnectionStatus)

        model!!.connectionStatus.observe(this, Observer{
            runOnUiThread{
                connectionStatus.text = it
            }
        })

        model!!.createConnection(sharedPref!!)
    }

    fun openConfig(view: View){
        val intent = Intent(this,ConfigActivity::class.java)
        startActivity(intent)
    }

    fun openSlave(view: View){
        val intent = Intent(this,SlaveActivity::class.java)
        startActivity(intent)
    }

    fun openMaster(view: View){
        val intent = Intent(this,MasterActivity::class.java)
        startActivity(intent)
    }
}