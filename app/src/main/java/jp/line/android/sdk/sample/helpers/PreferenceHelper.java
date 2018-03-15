package jp.line.android.sdk.sample.helpers;

import android.content.Context;
import android.content.SharedPreferences;

public class PreferenceHelper {
	private static final String MY_PREF_NAME = "MY_PREF_NAME";
	private static final String ACCESS_TOKEN = "access_token";
	private static final String ID_TOKEN = "id_token";
	private static final String REFRESH_TOKEN = "refresh_token";

	public static void setAccessToken(Context ctx, String value) {
		setPref(ctx, ACCESS_TOKEN, value);
	}

	public static String getAccessToken(Context ctx) {
		return getPref(ctx, ACCESS_TOKEN, "");
	}

	public static void setIdToken(Context ctx, String value) {
		setPref(ctx, ID_TOKEN, value);
	}

	public static String getIdToken(Context ctx) {
		return getPref(ctx, ID_TOKEN, "");
	}

	public static void setRefreshToken(Context ctx, String value) {
		setPref(ctx, REFRESH_TOKEN, value);
	}

	public static String getRefreshToken(Context ctx) {
		return getPref(ctx, REFRESH_TOKEN, "");
	}

	private static void setPref(Context ctx, String key, String value) {
		SharedPreferences.Editor settings = ctx.getSharedPreferences(MY_PREF_NAME, Context.MODE_PRIVATE).edit();
		settings.putString(key, value).apply();
	}

	private static String getPref(Context ctx, String key, String defaultValue) {
		SharedPreferences settings = ctx.getSharedPreferences(MY_PREF_NAME, Context.MODE_PRIVATE);
		return settings.getString(key, defaultValue);
	}

	private static void setPref(Context ctx, String key, int value) {
		SharedPreferences.Editor settings = ctx.getSharedPreferences(MY_PREF_NAME, Context.MODE_PRIVATE).edit();
		settings.putInt(key, value).apply();
	}

	private static int getPref(Context ctx, String key, int defaultValue) {
		SharedPreferences settings = ctx.getSharedPreferences(MY_PREF_NAME, Context.MODE_PRIVATE);
		return settings.getInt(key, defaultValue);
	}

	private static void setPref(Context ctx, String key, float value) {
		SharedPreferences.Editor settings = ctx.getSharedPreferences(MY_PREF_NAME, Context.MODE_PRIVATE).edit();
		settings.putFloat(key, value).apply();
	}

	private static float getPref(Context ctx, String key, float defaultValue) {
		SharedPreferences settings = ctx.getSharedPreferences(MY_PREF_NAME, Context.MODE_PRIVATE);
		return settings.getFloat(key, defaultValue);
	}

	private static void setPref(Context ctx, String key, long value) {
		SharedPreferences.Editor settings = ctx.getSharedPreferences(MY_PREF_NAME, Context.MODE_PRIVATE).edit();
		settings.putLong(key, value).apply();
	}

	private static long getPref(Context ctx, String key, long defaultValue) {
		SharedPreferences settings = ctx.getSharedPreferences(MY_PREF_NAME, Context.MODE_PRIVATE);
		return settings.getLong(key, defaultValue);
	}

	private static void setPref(Context ctx, String key, boolean value) {
		SharedPreferences.Editor settings = ctx.getSharedPreferences(MY_PREF_NAME, Context.MODE_PRIVATE).edit();
		settings.putBoolean(key, value).apply();
	}

	private static boolean getPref(Context ctx, String key, boolean defaultValue) {
		SharedPreferences settings = ctx.getSharedPreferences(MY_PREF_NAME, Context.MODE_PRIVATE);
		return settings.getBoolean(key, defaultValue);
	}

	public static void clearPrefAll(Context ctx) {
		ctx.getSharedPreferences(MY_PREF_NAME, Context.MODE_PRIVATE).edit().clear().apply();
	}
}