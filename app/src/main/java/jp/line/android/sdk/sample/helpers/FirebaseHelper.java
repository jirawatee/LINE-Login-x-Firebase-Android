package jp.line.android.sdk.sample.helpers;

import android.content.Intent;
import androidx.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import java.util.HashMap;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import jp.line.android.sdk.sample.MainActivity;
import jp.line.android.sdk.sample.PostLoginEmailActivity;
import jp.line.android.sdk.sample.R;
import jp.line.android.sdk.sample.interfaces.Constants;
import jp.line.android.sdk.sample.interfaces.LineService;
import jp.line.android.sdk.sample.models.FirebaseCustomToken;
import jp.line.android.sdk.sample.models.LineToken;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class FirebaseHelper {
	private static final String TAG = FirebaseHelper.class.getSimpleName();

	public static void getLineToken(MainActivity activity, String lineCode) {
		getLineTokenTask(activity, lineCode);
	}

	private static void getLineTokenTask(final MainActivity activity, String lineCode) {
		DialogHelper.showDialog(activity);
		Retrofit retrofit = getRetrofit(Constants.LINE_TOKEN_API);
		LineService service = retrofit.create(LineService.class);

		Call<LineToken> call = service.getTokens(
				"authorization_code",
				lineCode,
				"https://mokmoon.com/bangkok",
				Constants.CHANNEL_ID,
				Constants.CHANNEL_SECRET
		);
		call.enqueue(new Callback<LineToken>() {
			@Override
			public void onResponse(Call<LineToken> call, Response<LineToken> response) {
				LineToken lineToken = response.body();
				if (lineToken != null) {
					Log.d(TAG, lineToken.getAccess_token());
					PreferenceHelper.setAccessToken(activity, lineToken.getAccess_token());
					PreferenceHelper.setRefreshToken(activity, lineToken.getRefresh_token());
					PreferenceHelper.setIdToken(activity, lineToken.getId_token());
					getFirebaseCustomTokenTask(activity, lineToken);
				} else {
					DialogHelper.dismissDialog();
					activity.txtStatus.setText(R.string.login_error_invalid_code);
				}
			}
			@Override
			public void onFailure(Call<LineToken> call, Throwable throwable) {
				DialogHelper.dismissDialog();
				activity.txtStatus.setText(R.string.login_error_invalid_code);
			}
		});
	}

	private static void getFirebaseCustomTokenTask(final MainActivity activity, LineToken lineToken) {
		Retrofit retrofit = getRetrofit(Constants.CLOUD_FUNCTIONS_API);
		LineService service = retrofit.create(LineService.class);

		Claims claims = parseJwtWithoutSignature(lineToken.getId_token());

		HashMap<String, String> params = new HashMap<>();
		params.put("access_token", lineToken.getAccess_token());
		params.put("picture", claims.get("picture", String.class));
		params.put("email", claims.get("email", String.class));
		params.put("name", claims.get("name", String.class));
		params.put("id", claims.getSubject());

		Call<FirebaseCustomToken> call = service.createCustomToken(params);
		call.enqueue(new Callback<FirebaseCustomToken>() {
			@Override
			public void onResponse(Call<FirebaseCustomToken> call, Response<FirebaseCustomToken> response) {
				FirebaseCustomToken firebaseCustomToken = response.body();
				if (firebaseCustomToken != null) {
					String firebaseToken = firebaseCustomToken.getFirebase_token();
					FirebaseAuth auth = FirebaseAuth.getInstance();

					if (firebaseToken != null) {
						auth.signInWithCustomToken(firebaseToken).addOnCompleteListener(activity, new OnCompleteListener<AuthResult>() {
							@Override
							public void onComplete(@NonNull Task<AuthResult> task) {
								DialogHelper.dismissDialog();
								if (task.isSuccessful()) {
									activity.startActivity(new Intent(activity, PostLoginEmailActivity.class));
									activity.finish();
								} else {
									activity.txtStatus.setText(task.getException().getMessage());
								}
							}
						});
					} else {
						activity.txtStatus.setText(R.string.login_error_multiple);
					}
				} else {
					DialogHelper.dismissDialog();
					activity.txtStatus.setText(R.string.login_error);
				}
			}
			@Override
			public void onFailure(Call<FirebaseCustomToken> call, Throwable throwable) {
				DialogHelper.dismissDialog();
				activity.txtStatus.setText(R.string.login_error + ": " + throwable.getMessage());
			}
		});
	}

	private static Retrofit getRetrofit(String baseUrl) {
		return new Retrofit.Builder()
				.baseUrl(baseUrl)
				.addConverterFactory(GsonConverterFactory.create())
				.build();
	}

	private static Claims parseJwtWithoutSignature(String jwt) {
		int lastIndex = jwt.lastIndexOf('.');
		String withoutSignature = jwt.substring(0, lastIndex + 1);
		return Jwts.parser().parseClaimsJwt(withoutSignature).getBody();
	}
}
