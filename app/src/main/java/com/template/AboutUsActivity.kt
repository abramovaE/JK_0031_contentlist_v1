package com.template

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.template.databinding.ActivityAboutBinding

class AboutUsActivity: AppCompatActivity() {

    private lateinit var binding: ActivityAboutBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAboutBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val version = "${getString(R.string.version)}: ${BuildConfig.VERSION_NAME}"
        val size = "${getString(R.string.size)}: ${applicationInfo.publicSourceDir.length} mb"
        binding.version.text = version
        binding.weight.text = size
    }


    override fun onBackPressed() {
        val intent = Intent(this@AboutUsActivity, MenuActivity::class.java)
        startActivity(intent)
        overridePendingTransition(R.anim.to, R.anim.from_0y)
    }
}