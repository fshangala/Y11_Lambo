package com.fshangala.y11lambo

import android.content.Intent
import android.graphics.Bitmap
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.SystemClock
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
import com.fshangala.y11.SlaveViewModel

class SlaveActivity : AppCompatActivity() {
    private var webView: WebView? = null
    private var model: SlaveViewModel? = null
    private var slaveStatus:TextView? = null
    private var progressBar:ProgressBar? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_slave)

        progressBar = findViewById(R.id.progressBar)
        webView = findViewById(R.id.webView)
        true.also { webView!!.settings.javaScriptEnabled = it }
        slaveStatus = findViewById(R.id.slaveStatus)
        model = ViewModelProvider(this)[SlaveViewModel::class.java]
        val url = "https://jack9.io/d/index.html#/"
        webView!!.loadUrl(url)
        webView!!.webViewClient = object : WebViewClient(){
            override fun onPageFinished(view: WebView?, url: String?) {
                SystemClock.sleep(5000)
                view!!.evaluateJavascript("document.querySelector(\"input[name='loginName']\").value='fishing'"){
                    runOnUiThread{
                        slaveStatus!!.text = it
                    }
                }
                view!!.evaluateJavascript("document.querySelector(\"input[name='password']\").value='somebody'"){
                    runOnUiThread{
                        slaveStatus!!.text = it
                    }
                }
                view!!.evaluateJavascript("document.querySelector(\"form[name='loginForm']\").submit()"){
                    runOnUiThread{
                        slaveStatus!!.text = it
                    }
                }
                runOnUiThread{
                    slaveStatus!!.text = "Loaded!"
                }
            }

            override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                runOnUiThread{
                    slaveStatus!!.text = "Loading..."
                }
            }
        }

        model!!.slaveStatus.observe(this) {
            slaveStatus!!.post { slaveStatus!!.text = it }
        }
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