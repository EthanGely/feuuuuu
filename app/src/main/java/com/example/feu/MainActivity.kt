package com.example.feu

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import okhttp3.*
import java.io.IOException

class MainActivity : AppCompatActivity() {

    private val esp32Ip = "http://192.168.93.16" // 🔹 Remplace par l'IP de ton ESP32
    private val client = OkHttpClient()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val statusText = findViewById<TextView>(R.id.statusText)
        val greenButton = findViewById<Button>(R.id.greenButton)
        val yellowButton = findViewById<Button>(R.id.yellowButton)
        val redButton = findViewById<Button>(R.id.redButton)
        val stopButton = findViewById<Button>(R.id.stopButton)

        greenButton.setOnClickListener { sendTrafficState("Green", statusText) }
        yellowButton.setOnClickListener { sendTrafficState("Yellow", statusText) }
        redButton.setOnClickListener { sendTrafficState("Red", statusText) }
        stopButton.setOnClickListener { sendTrafficState("Off", statusText) }
    }

    private fun sendTrafficState(state: String, statusText: TextView) {
        val url = "$esp32Ip/on?color=$state"

        val request = Request.Builder()
            .url(url)
            .get()
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                runOnUiThread { statusText.text = "Erreur : Impossible d'envoyer la commande" }
            }

            override fun onResponse(call: Call, response: Response) {
                runOnUiThread { statusText.text = "État actuel : $state" }
            }
        })
    }
}
