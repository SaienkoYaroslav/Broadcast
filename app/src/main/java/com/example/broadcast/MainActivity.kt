package com.example.broadcast

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.widget.Button
import android.widget.ProgressBar
import androidx.appcompat.app.AppCompatActivity
import androidx.localbroadcastmanager.content.LocalBroadcastManager

class MainActivity : AppCompatActivity() {

    private lateinit var progressBar: ProgressBar

    private val localBroadcastManager by lazy {
        LocalBroadcastManager.getInstance(this)
    }

    private val receiver = object : BroadcastReceiver() {
        override fun onReceive(p0: Context?, p1: Intent?) {
            if (p1?.action == "loaded") {
                val percent = p1.getIntExtra("percent", 0)
                progressBar.progress = percent
            }
        }
    }

    private var count = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        progressBar = findViewById(R.id.progressBar)
        findViewById<Button>(R.id.button).setOnClickListener {
            Intent(MyReceiver.ACTION_CLICKED).apply {
                putExtra(MyReceiver.EXTRA_COUNT, count++)
                localBroadcastManager.sendBroadcast(this)
            }

        }

        val intentFilter = IntentFilter().apply {
            addAction(MyReceiver.ACTION_LOADED)
        }
        localBroadcastManager.registerReceiver(receiver, intentFilter)

        startService(Intent(this, MyService::class.java))
    }

    override fun onDestroy() {
        super.onDestroy()
        localBroadcastManager.unregisterReceiver(receiver)
    }
}
