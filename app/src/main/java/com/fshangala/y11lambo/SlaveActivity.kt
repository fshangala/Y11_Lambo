package com.fshangala.y11lambo

import android.content.Intent
import android.graphics.Bitmap
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.webkit.ValueCallback
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.ProgressBar
import android.widget.TextView
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class SlaveViewModel:ViewModel(){
    var sampleStatus = MutableLiveData<String>("")
}

class SlaveActivity : AppCompatActivity() {
    private var webView: WebView? = null
    private var model:SlaveViewModel? = null
    private var slaveStatus:TextView? = null
    private var progressBar:ProgressBar? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_slave)

        progressBar = findViewById(R.id.progressBar)
        webView = findViewById(R.id.webView)
        webView!!.settings.javaScriptEnabled = true
        slaveStatus = findViewById(R.id.slaveStatus)
        model = ViewModelProvider(this)[SlaveViewModel::class.java]
        webView!!.loadUrl("https://jack9.io/d/index.html#/")
        webView!!.webViewClient = object : WebViewClient(){
            override fun onPageFinished(view: WebView?, url: String?) {
                view!!.evaluateJavascript("javascript:alert(\"loading done\")",ValueCallback<String>(){
                    model!!.sampleStatus.value = it
                })
                progressBar!!.visibility = View.GONE
            }

            override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                progressBar!!.visibility = View.VISIBLE
            }
        }

        model!!.sampleStatus.observe(this, Observer {
            runOnUiThread{
                slaveStatus!!.text = it
            }
        })
    }

    fun openConfig(view: View){
        val intent = Intent(this,ConfigActivity::class.java)
        startActivity(intent)
    }

    private fun openSlave(){
        val intent = Intent(this,SlaveActivity::class.java)
        startActivity(intent)
    }

    fun openMaster(view: View){
        val intent = Intent(this,MasterActivity::class.java)
        startActivity(intent)
    }

}