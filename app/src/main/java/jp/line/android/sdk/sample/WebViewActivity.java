package jp.line.android.sdk.sample;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.LinkedHashMap;
import java.util.Map;

import jp.line.android.sdk.sample.interfaces.Constants;

public class WebViewActivity extends AppCompatActivity {
	private WebView mWebView;
	public static final String CODE = "code";
	private static final String CALLBACK_URL = "https://mokmoon.com/bangkok";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_web_view);
		mWebView = findViewById(R.id.webview);
		MyWebSetting();

		Uri.Builder builder = new Uri.Builder()
				.scheme("https")
				.authority("access.line.me")
				.appendPath("oauth2")
				.appendPath("v2.1")
				.appendPath("authorize")
				.appendQueryParameter("response_type", "code")
				.appendQueryParameter("client_id", Constants.CHANNEL_ID)
				.appendQueryParameter("redirect_uri", CALLBACK_URL)
				.appendQueryParameter("state", "12345abcde")
				.appendQueryParameter("scope", "openid email profile")
				.appendQueryParameter("nonce", "09876xyz")
				.appendQueryParameter("bot_prompt", "normal");
		String query = builder.build().toString();

		mWebView.setWebViewClient(new WebViewClient() {
			@Override
			public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
				String url = request.getUrl().toString();
				if (url.startsWith(CALLBACK_URL)) {
					String arr[] = url.split(CALLBACK_URL);
					try {
						Map<String, String> pairs = splitQuery(arr[1].substring(1));
						if (pairs.get(CODE) != null) {
							Intent intent = new Intent();
							intent.putExtra(CODE, pairs.get(CODE));
							setResult(RESULT_OK, intent);
							finish();
						} else {
							setResult(RESULT_CANCELED);
							finish();
						}
					} catch (UnsupportedEncodingException e) {
						e.printStackTrace();
					}
				}
				return super.shouldOverrideUrlLoading(view, request);
			}
		});
		mWebView.loadUrl(query);
	}

	@Override
	public void onBackPressed() {
		if (mWebView.canGoBack()) {
			mWebView.goBack();
		} else {
			super.onBackPressed();
		}
	}

	@SuppressLint("SetJavaScriptEnabled")
	private void MyWebSetting() {
		WebSettings ws = mWebView.getSettings();
		ws.setJavaScriptEnabled(true);
		ws.setJavaScriptCanOpenWindowsAutomatically(true);
		ws.setDomStorageEnabled(true);
		ws.setUseWideViewPort(true);
		ws.setLoadWithOverviewMode(true);
		ws.setBuiltInZoomControls(true);
		ws.setSupportZoom(true);
		ws.setDisplayZoomControls(false);
	}

	public static Map<String, String> splitQuery(String queryString) throws UnsupportedEncodingException {
		Map<String, String> query_pairs = new LinkedHashMap<>();
		String[] pairs = queryString.split("&");
		for (String pair : pairs) {
			int idx = pair.indexOf("=");
			query_pairs.put(URLDecoder.decode(pair.substring(0, idx), "UTF-8"), URLDecoder.decode(pair.substring(idx + 1), "UTF-8"));
		}
		return query_pairs;
	}
}
