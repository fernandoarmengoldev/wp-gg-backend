package com.fernandoarmengol.ggwp.ui.activities

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.appcompat.app.AppCompatActivity
import com.fernandoarmengol.ggwp.databinding.ActivityWebBinding

@Suppress("DEPRECATED_IDENTITY_EQUALS")
class WebActivity : AppCompatActivity() {
    lateinit var binding: ActivityWebBinding

    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWebBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.web.webViewClient = object : WebViewClient(){
            override fun shouldOverrideUrlLoading(
                view: WebView?,
                request: WebResourceRequest?
            ): Boolean {
                return true
            }
        }

        binding.web.settings.javaScriptEnabled = true
        binding.web.loadUrl(intent.extras!!.getString("EXTRA_URL").toString())
    }

    override fun onBackPressed() {
        if (binding.web.url != intent.extras!!.getString("EXTRA_URL").toString()) {
            binding.web.goBack()
        } else {
            super.onBackPressed()
        }
    }
}