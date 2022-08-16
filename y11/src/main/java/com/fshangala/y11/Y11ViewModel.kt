package com.fshangala.y11

import android.content.SharedPreferences
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import okhttp3.*

open class Y11ViewModel : ViewModel() {
    private val appClient = OkHttpClient()
    private var appSocket: WebSocket? = null

    var connectionStatus = MutableLiveData<String>("")
    var automationEvents = MutableLiveData<AutomationEvents>()

    fun createConnection(sharedPref: SharedPreferences){
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

    fun sendCommand(data:AutomationObject){
        try {
            this.appSocket?.send(data.toString())
        } catch (ex: Exception) {
            connectionStatus.value = ex.toString()
        }
    }
}