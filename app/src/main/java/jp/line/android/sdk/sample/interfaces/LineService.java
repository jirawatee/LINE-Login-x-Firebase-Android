package jp.line.android.sdk.sample.interfaces;

import java.util.HashMap;

import jp.line.android.sdk.sample.models.FirebaseCustomToken;
import jp.line.android.sdk.sample.models.LineToken;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface LineService {
	@FormUrlEncoded
	@POST("token/")
	Call<LineToken> getTokens(
			@Field("grant_type") String grant_type,
			@Field("code") String code,
			@Field("redirect_uri") String uri,
			@Field("client_id") String client_id,
			@Field("client_secret") String client_secret
	);

	@Headers({"Content-Type: application/json", "Accept: application/json"})
	@POST("createCustomToken/")
	Call<FirebaseCustomToken> createCustomToken(
			@Body HashMap<String, String> body
	);
}