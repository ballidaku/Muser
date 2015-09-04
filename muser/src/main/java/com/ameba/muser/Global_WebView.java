package com.ameba.muser;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;
import android.widget.Toast;

public class Global_WebView extends Activity implements OnClickListener
{
	TextView		title;
	WebView			webview;
	Context			con;
	ProgressDialog	progressBar;
	
	@SuppressLint("SetJavaScriptEnabled")
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_global_webview);
		con=this;
		
		title=(TextView)findViewById(R.id.title);
		((TextView)findViewById(R.id.back)).setOnClickListener(this);
		webview=(WebView)findViewById(R.id.webview);
		
		
		
		title.setText(getIntent().getStringExtra("title"));
		
		
		WebSettings settings = webview.getSettings();
		settings.setJavaScriptEnabled(true);
		webview.setScrollBarStyle(WebView.SCROLLBARS_OUTSIDE_OVERLAY);

		final AlertDialog alertDialog = new AlertDialog.Builder(this).create();

		progressBar = ProgressDialog.show(con,getIntent().getStringExtra("title"), "Loading...");

		webview.setWebViewClient(new WebViewClient()
		{
			public boolean shouldOverrideUrlLoading(WebView view, String url)
			{
				Log.i("TAG", "Processing webview url click...");
				view.loadUrl(url);
				return true;
			}

			public void onPageFinished(WebView view, String url)
			{
				Log.i("TAG", "Finished loading URL: " + url);
				if(progressBar.isShowing())
				{
					progressBar.dismiss();
				}
			}

			@SuppressWarnings("deprecation")
			public void onReceivedError(WebView view, int errorCode, String description, String failingUrl)
			{
				Log.e("TAG", "Error: " + description);
				
				alertDialog.setTitle("Error");
				alertDialog.setMessage(description);
				alertDialog.setButton("OK", new DialogInterface.OnClickListener()
				{
					public void onClick(DialogInterface dialog, int which)
					{
						return;
					}
				});
				alertDialog.show();
			}
		});
		webview.loadUrl(getIntent().getStringExtra("url"));
		
		
	}

	@Override
	public void onClick(View v)
	{
		switch (v.getId())
		{
			case R.id.back:
				 ((Activity) con).finish();
				break;

			default:
				break;
		}
		
	}
}
