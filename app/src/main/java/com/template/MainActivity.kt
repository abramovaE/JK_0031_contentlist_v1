package com.template

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.animation.AnimationUtils
import com.template.databinding.ActivityMainBinding
import java.util.*
import kotlin.concurrent.schedule

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        var intent = Intent(this, MenuActivity::class.java)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        Timer().schedule(1500){
            startActivity(intent)
            overridePendingTransition(R.anim.to, R.anim.from)
            binding.imageView.clearAnimation()
            finish()
        }
    }

    override fun onResume() {
        super.onResume()
        val startAnimation = AnimationUtils.loadAnimation(this, R.anim.icon_anim)
        binding.imageView.startAnimation(startAnimation)
    }
}