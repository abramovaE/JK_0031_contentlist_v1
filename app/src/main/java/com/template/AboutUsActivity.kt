package com.template

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.template.databinding.ActivityAboutBinding

class AboutUsActivity: AppCompatActivity() {

    lateinit var binding: ActivityAboutBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAboutBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.version.text = "version"
        binding.weight.text = "weight"
    }

}