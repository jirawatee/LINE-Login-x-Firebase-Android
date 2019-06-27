package jp.line.android.sdk.sample;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.linecorp.linesdk.Scope;
import com.linecorp.linesdk.auth.LineAuthenticationParams;
import com.linecorp.linesdk.auth.LineLoginApi;
import com.linecorp.linesdk.auth.LineLoginResult;

import java.util.Arrays;
import java.util.Locale;

import jp.line.android.sdk.sample.helpers.FirebaseHelper;
import jp.line.android.sdk.sample.helpers.PreferenceHelper;
import jp.line.android.sdk.sample.interfaces.Constants;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
	private static final int REQUEST_CODE_1 = 1;
	private static final int REQUEST_CODE_2 = 2;
	private static final String TAG = MainActivity.class.getSimpleName();
	public TextView btnLoginNative, btnLoginBrowser, btnLoginEmail, txtStatus;
	private boolean isClicked = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		bindWidget();

		Log.d("ACCESS_TOKEN", PreferenceHelper.getAccessToken(this));
	}

	@Override
	protected void onResume() {
		super.onResume();

		//if (!isClicked) LineHelper.verifyToken(this);

		if (FirebaseAuth.getInstance().getCurrentUser() != null) {
			startActivity(new Intent(this, PostLoginEmailActivity.class));
			finish();
		} else {
			btnLoginNative.setVisibility(View.VISIBLE);
			btnLoginBrowser.setVisibility(View.VISIBLE);
			btnLoginEmail.setVisibility(View.VISIBLE);
		}
	}

	@Override
	public void onClick(View view) {
		isClicked = true;
		Intent LoginIntent;

		LineAuthenticationParams params = new LineAuthenticationParams.Builder()
				.scopes(Arrays.asList(Scope.OPENID_CONNECT, Scope.OC_EMAIL, Scope.PROFILE))
				.botPrompt(LineAuthenticationParams.BotPrompt.normal)
				.uiLocale(new Locale("th", "TH"))
				.build();

		switch (view.getId()) {
			case R.id.btn_login_native:
				LoginIntent = LineLoginApi.getLoginIntent(this, Constants.CHANNEL_ID, params);
				startActivityForResult(LoginIntent, REQUEST_CODE_1);
				break;
			case R.id.btn_login_browser:
				LoginIntent = LineLoginApi.getLoginIntentWithoutLineAppAuth(this, Constants.CHANNEL_ID, params);
				startActivityForResult(LoginIntent, REQUEST_CODE_1);
				break;
			case R.id.btn_login_email:
				startActivityForResult(new Intent(this, WebViewActivity.class), REQUEST_CODE_2);
				break;
		}
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == RESULT_OK) {
			switch (requestCode) {
				case REQUEST_CODE_1:
					LineLoginResult result = LineLoginApi.getLoginResultFromIntent(data);
					switch (result.getResponseCode()) {
						case SUCCESS:
							Intent intent = new Intent(this, PostLoginActivity.class);
							/*
							intent.putExtra(PostLoginActivity.LINE_LOGIN_USER_ID, result.getLineProfile().getUserId());
							intent.putExtra(PostLoginActivity.LINE_LOGIN_DISPLAY_NAME, result.getLineProfile().getDisplayName());
							intent.putExtra(PostLoginActivity.LINE_LOGIN_STATUS_MESSAGE, result.getLineProfile().getStatusMessage());
							intent.putExtra(PostLoginActivity.LINE_LOGIN_ACCESS_TOKEN, result.getLineCredential().getAccessToken().getTokenString());
							intent.putExtra(PostLoginActivity.LINE_LOGIN_PICTURE, result.getLineProfile().getPictureUrl().toString());
							*/
							intent.putExtra(PostLoginActivity.LINE_LOGIN_EMAIL, result.getLineIdToken().getEmail());
							startActivity(intent);
							finish();
							break;
						case CANCEL:
							isClicked = false;
							txtStatus.setText(R.string.login_cancel);
							break;
						default:
							isClicked = false;
							txtStatus.setText(result.getErrorData().toString());
							Log.e(TAG, result.getErrorData().toString());
					}
					break;
				case REQUEST_CODE_2:
					FirebaseHelper.getLineToken(this, data.getStringExtra(WebViewActivity.CODE));
					break;
			}
		}
	}

	private void bindWidget() {
		txtStatus = findViewById(R.id.txt_status);
		btnLoginNative = findViewById(R.id.btn_login_native);
		btnLoginBrowser = findViewById(R.id.btn_login_browser);
		btnLoginEmail = findViewById(R.id.btn_login_email);
		btnLoginNative.setOnClickListener(this);
		btnLoginBrowser.setOnClickListener(this);
		btnLoginEmail.setOnClickListener(this);
	}
}