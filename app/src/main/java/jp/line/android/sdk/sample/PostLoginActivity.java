package jp.line.android.sdk.sample;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import jp.line.android.sdk.sample.helpers.LineHelper;

public class PostLoginActivity extends AppCompatActivity implements View.OnClickListener{
	public TextView txtDisplayName, txtUserId, txtStatusMessage, txtAccessToken, txtEmail;
	public ImageView profileImageView;

	/*
	public static final String LINE_LOGIN_DISPLAY_NAME = "displayname";
	public static final String LINE_LOGIN_USER_ID = "uid";
	public static final String LINE_LOGIN_STATUS_MESSAGE = "status";
	public static final String LINE_LOGIN_ACCESS_TOKEN = "token";
	public static final String LINE_LOGIN_PICTURE = "picture";
	*/

	public static final String LINE_LOGIN_EMAIL = "email";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_post_login);
		bindWidget();

		/*
		String displayname = getIntent().getStringExtra(LINE_LOGIN_DISPLAY_NAME);
		String uid = getIntent().getStringExtra(LINE_LOGIN_USER_ID);
		String status = getIntent().getStringExtra(LINE_LOGIN_STATUS_MESSAGE);
		String token = getIntent().getStringExtra(LINE_LOGIN_ACCESS_TOKEN);
		String picture = getIntent().getStringExtra(LINE_LOGIN_PICTURE);
		*/

		String email = getIntent().getStringExtra(LINE_LOGIN_EMAIL);
		txtEmail.setText(getString(R.string.profile_email, email));

		/*
		txtAccessToken.setText(token);
		txtDisplayName.setText(displayname);
		txtStatusMessage.setText(status);
		txtUserId.setText(uid);
		GlideApp.with(this)
				.load(picture)
				.transition(withCrossFade())
				.circleCrop()
				.into(profileImageView);
		*/
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
		profileImageView = findViewById(R.id.profileImageView);
		txtDisplayName = findViewById(R.id.txt_display_name);
		txtUserId = findViewById(R.id.txt_user_id);
		txtStatusMessage = findViewById(R.id.txt_status_message);
		txtAccessToken = findViewById(R.id.txt_access_token);
		txtEmail = findViewById(R.id.txt_email);
		findViewById(R.id.btn_refresh_token).setOnClickListener(this);
		findViewById(R.id.btn_logout).setOnClickListener(this);
	}
}
