package com.jdroid.android.sample.ui.webview

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import android.webkit.WebView
import com.jdroid.android.fragment.AbstractFragment
import com.jdroid.android.sample.R
import com.jdroid.android.webview.DefaultWebViewClient

class WebViewFragment : AbstractFragment() {

    override fun getContentFragmentLayout(): Int? {
        return R.layout.webview_fragment
    }

    @SuppressLint("SetJavaScriptEnabled")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val webView = findView<WebView>(R.id.webview)
        webView.webViewClient = DefaultWebViewClient()
        val webSettings = webView.settings
        webSettings.javaScriptEnabled = true
        webView.loadUrl("https://www.google.com")
    }
}
