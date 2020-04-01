package com.intelegain.agora.fragmments;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.intelegain.agora.R;


public class CustomLoader_constant extends Dialog
{
	public CustomLoader_constant(Context context)
	{
		super(context);
	}
	public CustomLoader_constant(Context context, int theme)
	{
		super(context, theme);
	}
	public void onWindowFocusChanged(boolean hasFocus) 
	{
		ImageView imageView = (ImageView) findViewById(R.id.spinnerImageView);
		AnimationDrawable spinner = (AnimationDrawable) imageView.getBackground();
		spinner.start();
	}
	public void setMessage(CharSequence message)
	{
		if (message != null && message.length() > 0)
		{
			findViewById(R.id.message).setVisibility(View.VISIBLE);
			TextView txt = (TextView) findViewById(R.id.message);
			txt.setText(message);
			txt.invalidate();
		}
	}
	public static CustomLoader_constant show(Context context, CharSequence message, boolean indeterminate, boolean cancelable,
                                             boolean hide, OnCancelListener cancelListener)
	{
		CustomLoader_constant dialog = new CustomLoader_constant(context,R.style.ProgressHUD);
		dialog.setTitle("");
		dialog.setContentView(R.layout.progress_hud);
		if (message == null || message.length() == 0)
		{
			dialog.findViewById(R.id.message).setVisibility(View.GONE);
		} 
		else 
		{
			TextView txt = (TextView) dialog.findViewById(R.id.message);
			txt.setText(message);
		}
		dialog.setCancelable(cancelable);
		dialog.setOnCancelListener(cancelListener);
		dialog.getWindow().getAttributes().gravity = Gravity.CENTER;
		WindowManager.LayoutParams lp = dialog.getWindow().getAttributes();
		lp.dimAmount = 0.2f;
		dialog.getWindow().setAttributes(lp);
		// dialog.getWindow().addFlags(WindowManager.LayoutParams.FLAG_BLUR_BEHIND);
		if (hide)
		{
			dialog.hide();
		}
		else
		{
			dialog.show();
		}
		dialog.setCancelable(false);
		return dialog;
	}	
}
