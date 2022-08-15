package com.fshangala.y11lambo

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import okhttp3.*
import org.json.JSONObject

class AutomationEvents(json:String): JSONObject(json) {
    val eventType:String = this.optString("event_type")
    val eventName:String = this.optString("event")
}

class MasterViewModel : ViewModel() {
    private val appClient = OkHttpClient()
    private var appSocket: WebSocket? = null

    var connectionStatus = MutableLiveData<String>("")
    var automationEvents = MutableLiveData<AutomationEvents>()

    fun createConnection(sharedPref:SharedPreferences){
        connectionStatus.value = "Connecting..."

        val hostIp = sharedPref.getString("hostIp","192.168.43.145")
        val hostPort = sharedPref.getInt("hostPort",8000).toString()
        val hostCode = sharedPref.getString("hostCode","sample")

        val host = "ws://$hostIp:$hostPort/ws/pcautomation/$hostCode/"
        val appRequest = Request.Builder().url(host).build()

        try {
            appClient.newWebSocket(appRequest,object: WebSocketListener(){
                override fun onOpen(webSocket: WebSocket, response: Response) {
                    appSocket = webSocket
                    webSocket.send("{\"event_type\":\"connection\",\"event\":\"phone_connected\",\"args\":[],\"kwargs\":{}}")
                    connectionStatus.postValue("Connected!")
                }

                override fun onMessage(webSocket: WebSocket, text: String) {
                    automationEvents.postValue(AutomationEvents(text))
                }

                override fun onClosed(webSocket: WebSocket, code: Int, reason: String) {
                    connectionStatus.postValue("Disconnected!")
                }

                override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) {
                    connectionStatus.postValue("Error: ${t.toString()}")
                }
            })
            appClient.dispatcher.executorService.shutdown()
        } catch (ex: Exception){
            connectionStatus.value = ex.toString()
        }
    }
}

class MasterActivity : AppCompatActivity() {
    var sharedPref:SharedPreferences? = null
    var model:MasterViewModel? = null
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