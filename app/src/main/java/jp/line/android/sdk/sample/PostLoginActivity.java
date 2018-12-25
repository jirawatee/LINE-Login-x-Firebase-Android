package jp.line.android.sdk.sample;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import jp.line.android.sdk.sample.helpers.LineHelper;

public class PostLoginActivity extends AppCompatActivity implements View.OnClickListener{
	public TextView txtDisplayName, txtUserId, txtStatusMessage, txtAccessToken;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_post_login);
		bindWidget();
	}

	@Override
	protected void onStart() {
		super.onStart();
		LineHelper.getProfile(this);
	}

	@Override
	public void onClick(View view) {
		switch (view.getId()) {
			case R.id.btn_refresh_token:
				LineHelper.refreshToken(this);
				break;
			case R.id.btn_logout:
				LineHelper.logout(this);
				break;
		}
	}

	private void bindWidget() {
		txtDisplayName = findViewById(R.id.txt_display_name);
		txtUserId = findViewById(R.id.txt_user_id);
		txtStatusMessage = findViewById(R.id.txt_status_message);
		txtAccessToken = findViewById(R.id.txt_access_token);
		findViewById(R.id.btn_refresh_token).setOnClickListener(this);
		findViewById(R.id.btn_logout).setOnClickListener(this);
	}
}
