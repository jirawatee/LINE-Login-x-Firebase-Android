package jp.line.android.sdk.sample.helpers;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.linecorp.linesdk.LineAccessToken;
import com.linecorp.linesdk.LineApiResponse;
import com.linecorp.linesdk.LineCredential;
import com.linecorp.linesdk.LineProfile;
import com.linecorp.linesdk.api.LineApiClient;
import com.linecorp.linesdk.api.LineApiClientBuilder;

import java.lang.ref.WeakReference;

import jp.line.android.sdk.sample.MainActivity;
import jp.line.android.sdk.sample.PostLoginActivity;
import jp.line.android.sdk.sample.R;
import jp.line.android.sdk.sample.configs.GlideApp;
import jp.line.android.sdk.sample.interfaces.Constants;

import static com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade;

public class LineHelper {
	private static final String TAG = LineHelper.class.getSimpleName();
	private static LineApiClient lineApiClient;

	public static void verifyToken(MainActivity activity) {
		lineApiClient = new LineApiClientBuilder(activity, Constants.CHANNEL_ID).build();
		new VerifyTokenTask(activity).execute();
	}

	public static void getProfile(PostLoginActivity activity) {
		lineApiClient = new LineApiClientBuilder(activity, Constants.CHANNEL_ID).build();
		new GetProfileTask(activity).execute();
	}

	public static void refreshToken(PostLoginActivity activity) {
		lineApiClient = new LineApiClientBuilder(activity, Constants.CHANNEL_ID).build();
		new RefreshTokenTask(activity).execute();
	}

	public static void logout(Context context) {
		lineApiClient = new LineApiClientBuilder(context, Constants.CHANNEL_ID).build();
		new LogoutTask(context).execute();
	}

	private static class VerifyTokenTask extends AsyncTask<Void, Void, LineApiResponse<LineCredential>> {
		private WeakReference<MainActivity> activityReference;

		VerifyTokenTask(MainActivity activity) {
			activityReference = new WeakReference<>(activity);
		}

		@Override
		protected LineApiResponse<LineCredential> doInBackground(Void... params) {
			return lineApiClient.verifyToken();
		}

		@Override
		protected void onPostExecute(LineApiResponse<LineCredential> response) {
			MainActivity activity = activityReference.get();
			if (response.isSuccess()) {
				activity.startActivity(new Intent(activity, PostLoginActivity.class));
				activity.finish();
			} else {
				activity.btnLoginNative.setVisibility(View.VISIBLE);
				activity.btnLoginBrowser.setVisibility(View.VISIBLE);
				activity.btnLoginEmail.setVisibility(View.VISIBLE);
			}
		}
	}

	private static class GetProfileTask extends AsyncTask<Void, Void, LineApiResponse<LineProfile>> {
		private WeakReference<PostLoginActivity> activityReference;

		GetProfileTask(PostLoginActivity activity) {
			activityReference = new WeakReference<>(activity);
		}

		@Override
		protected LineApiResponse<LineProfile> doInBackground(Void... params) {
			return lineApiClient.getProfile();
		}

		@Override
		protected void onPostExecute(LineApiResponse<LineProfile> apiResponse) {
			PostLoginActivity activity = activityReference.get();
			if (apiResponse.isSuccess()) {
				LineProfile profile = apiResponse.getResponseData();

				ImageView profileImageView = activity.findViewById(R.id.profileImageView);
				GlideApp.with(activity)
						.load(profile.getPictureUrl())
						.transition(withCrossFade())
						.circleCrop()
						.into(profileImageView);

				activity.txtDisplayName.setText(profile.getDisplayName());
				activity.txtUserId.setText(activity.getString(R.string.profile_user_id, profile.getUserId()));
				activity.txtStatusMessage.setText(activity.getString(R.string.profile_status_message, profile.getStatusMessage()));

				String token = lineApiClient.getCurrentAccessToken().getResponseData().getAccessToken();
				Log.d(TAG, token);
				activity.txtAccessToken.setText(activity.getString(R.string.profile_access_token, token));
			} else {
				activity.txtDisplayName.setText(apiResponse.getErrorData().toString());
			}
		}
	}

	public static class RefreshTokenTask extends AsyncTask<Void, Void, LineApiResponse<LineAccessToken>> {
		private WeakReference<PostLoginActivity> activityReference;

		RefreshTokenTask(PostLoginActivity activity) {
			activityReference = new WeakReference<>(activity);
		}

		@Override
		protected LineApiResponse<LineAccessToken> doInBackground(Void... params) {
			return lineApiClient.refreshAccessToken();
		}

		@Override
		protected void onPostExecute(LineApiResponse<LineAccessToken> response) {
			PostLoginActivity activity = activityReference.get();
			if (response.isSuccess()) {
				String updatedAccessToken = lineApiClient.getCurrentAccessToken().getResponseData().getAccessToken();
				activity.txtAccessToken.setText(activity.getString(R.string.profile_access_token, updatedAccessToken));
			} else {
				activity.txtAccessToken.setText(response.getErrorData().toString());
			}
		}
	}

	private static class LogoutTask extends AsyncTask<Void, Void, LineApiResponse> {
		private WeakReference<Context> activityReference;

		LogoutTask(Context context) {
			activityReference = new WeakReference<>(context);
		}

		@Override
		protected LineApiResponse doInBackground(Void... params) {
			return lineApiClient.logout();
		}

		@Override
		protected void onPostExecute(LineApiResponse apiResponse) {
			Activity activity = (Activity) activityReference.get();
			if (apiResponse.isSuccess()) {
				activity.startActivity(new Intent(activity, MainActivity.class));
				activity.finish();
			} else {
				Toast.makeText(activity, "Logout Failed: " + apiResponse.getErrorData().getMessage(), Toast.LENGTH_SHORT).show();
			}
		}
	}
}
