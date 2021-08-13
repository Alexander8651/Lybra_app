package com.amatai.lybra_app.ui.fragments

import android.graphics.Bitmap
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import com.amatai.lybra_app.R
import com.amatai.lybra_app.databinding.FragmentVideoBinding

class VideoFragment : Fragment() {

    lateinit var binding:FragmentVideoBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentVideoBinding.inflate(inflater)
        context ?: binding.root

        binding.webViewVideo.webViewClient = object : WebViewClient() {
            override fun shouldOverrideUrlLoading(
                view: WebView?,
                request: WebResourceRequest?
            ): Boolean {
                return false
            }

            override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                super.onPageStarted(view, url, favicon)
                try {
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }

            override fun onPageFinished(view: WebView?, url: String?) {
                super.onPageFinished(view, url)

            }
        }
        val setting = binding.webViewVideo.settings
        setting.javaScriptEnabled = true
        binding.webViewVideo.loadUrl("https://tutoriales.corporacionochodemarzo.org/tutorialapioptions.php")
        return binding.root
    }

}