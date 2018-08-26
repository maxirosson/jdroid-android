package com.jdroid.android.sample.ui.webview;

import android.os.Bundle;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;

import com.jdroid.android.fragment.AbstractFragment;
import com.jdroid.android.sample.R;
import com.jdroid.android.webview.DefaultWebViewClient;

public class WebViewFragment extends AbstractFragment {

	@Override
	public Integer getContentFragmentLayout() {
		return R.layout.webview_fragment;
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);

		WebView webView = findView(R.id.webview);
		webView.setWebViewClient(new DefaultWebViewClient());
		WebSettings webSettings = webView.getSettings();
		webSettings.setJavaScriptEnabled(true);
		webView.loadUrl("https://www.google.com");
	}
}
