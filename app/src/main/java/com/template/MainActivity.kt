package com.template

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import java.util.*
import kotlin.concurrent.schedule

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val intent = Intent(this, MenuActivity::class.java)
        Timer().schedule(1500){
            startActivity(intent)
            finish()
        }
    }
}