package com.fshangala.y11lambo

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.lifecycle.ViewModel

class MainActivityViewModel : ViewModel() {
    //
}

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
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