package jp.line.android.sdk.sample.helpers;

import android.app.Dialog;
import android.content.Context;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import jp.line.android.sdk.sample.R;

public class DialogHelper {
	private static Dialog mDialog;

	public static void showDialog(Context context) {
		mDialog = new Dialog(context, R.style.DialogCustom);
		mDialog.addContentView(
				new ProgressBar(context),
				new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT)
		);
		mDialog.setCancelable(false);
		if (!mDialog.isShowing()) {
			mDialog.show();
		}
	}

	public static void dismissDialog() {
		if (mDialog != null && mDialog.isShowing()) {
			mDialog.dismiss();
		}
	}
}
