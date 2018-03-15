package jp.line.android.sdk.sample;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import jp.line.android.sdk.sample.configs.GlideApp;
import jp.line.android.sdk.sample.helpers.PreferenceHelper;

import static com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade;

public class PostLoginEmailActivity extends AppCompatActivity implements View.OnClickListener{
	public TextView txtDisplayName, txtUserId, txtEmail, txtAccessToken;
	private FirebaseAuth mAuth;
	private ImageView imgProfile;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_post_login_email);
		bindWidget();
		mAuth = FirebaseAuth.getInstance();
	}

	@Override
	protected void onStart() {
		super.onStart();
		FirebaseUser user = mAuth.getCurrentUser();
		if (user != null) {
			txtDisplayName.setText(user.getDisplayName());
			txtUserId.setText(getString(R.string.profile_uid, user.getUid()));
			txtEmail.setText(getString(R.string.profile_email, user.getEmail()));
			txtAccessToken.setText(getString(R.string.profile_access_token, PreferenceHelper.getAccessToken(this)));
			GlideApp.with(this).load(user.getPhotoUrl()).transition(withCrossFade()).circleCrop().thumbnail(0.1f).into(imgProfile);
		} else {
			mAuth.signOut();
			startActivity(new Intent(this, MainActivity.class));
			finish();
		}
	}

	@Override
	public void onClick(View view) {
		switch (view.getId()) {
			case R.id.btn_logout:
				mAuth.signOut();
				startActivity(new Intent(this, MainActivity.class));
				finish();
				break;
		}
	}

	private void bindWidget() {
		imgProfile = findViewById(R.id.profileImageView);
		txtDisplayName = findViewById(R.id.txt_display_name);
		txtUserId = findViewById(R.id.txt_user_id);
		txtEmail = findViewById(R.id.txt_email);
		txtAccessToken = findViewById(R.id.txt_access_token);
		findViewById(R.id.btn_logout).setOnClickListener(this);
	}
}
