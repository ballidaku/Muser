package com.example.ProgressTask;

import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;

import com.example.classes.Util_Class;
import com.ameba.muser.Drawer;
import com.ameba.muser.R;
import com.ameba.muser.Registration;

public class Forgot_Password_ProgressTask extends AsyncTask<String, Void, Void>
{
	Context con;
	private ProgressDialog dialog;
	String email = "", message = null, user_id, msg_json;

	public Forgot_Password_ProgressTask(Context con, String email)
	{
		this.con = con;
		this.email = email;
	}

	protected void onPreExecute()
	{
		Util_Class.dismiss_dialog();
		dialog = ProgressDialog.show(con, "", "");
		dialog.setContentView(R.layout.main);
		dialog.show();
	}

	@Override
	protected Void doInBackground(String... params)
	{
		String response = login();
		try
		{
			if(response != null)
			{
				if(response.equals("Slow"))
				{
					message = "Slow";
				}
				else
				{
					msg_json = new JSONObject(response).getString("status");
					if(msg_json.equals("Success"))
					{
						message = "Success";
					}
					else if(msg_json.equals("Failure"))
					{
						message = "Failure";
					}
				}
			}
			else
			{
				message = "null";
			}
		}
		catch(JSONException e)
		{
			e.printStackTrace();
			message = "null";
		}
		return null;
	}

	@Override
	protected void onPostExecute(Void result)
	{
		if(dialog.isShowing())
		{
			dialog.dismiss();
		}
		Util_Class.dismiss_dialog();
		OnClickListener retry = new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				new Forgot_Password_ProgressTask(con, email).execute();
			}
		};
		System.out.println("message-->" + message);
		if(message.equals("null"))
		{
			Util_Class.show_global_dialog(con, con.getResources().getString(R.string.server_error));
		}
		else if(message.equals("Slow"))
		{
			Util_Class.show_internet_dialog(con, retry);
		}
		else if(message.equals("Success"))
		{
			Util_Class.show_Toast(con.getResources().getString(R.string.forgot_password_sent), con);
		}
		else if(message.equals("Failure"))
		{
			Util_Class.show_global_dialog(con, con.getResources().getString(R.string.email_error));
		}
		/*
		 * else if(message.equals("Failure1")) {
		 * Util_Class.show_global_dialog(con,"Failure1"); }
		 */
	}

	public String login()
	{
		HttpPost httppost = new HttpPost(Util_Class.forgot_password);
		HttpParams httpParameters = new BasicHttpParams();
		// HttpConnectionParams.setConnectionTimeout(httpParameters,5000);
		// HttpConnectionParams.setSoTimeout(httpParameters, 5000);
		DefaultHttpClient httpclient = new DefaultHttpClient(httpParameters);
		try
		{
			List<NameValuePair> param = new ArrayList<NameValuePair>();
			param.add(new BasicNameValuePair("key", email));
			param.add(new BasicNameValuePair("registration_type", "M"));
			httppost.setEntity(new UrlEncodedFormEntity(param));
			HttpResponse response = httpclient.execute(httppost);
			String data = EntityUtils.toString(response.getEntity());
			Log.e("DATAAA", data);
			return data;
		}
		catch(SocketTimeoutException e)
		{
			e.printStackTrace();
			return "Slow";
		}
		catch(ConnectTimeoutException e)
		{
			e.printStackTrace();
			return "Slow";
		}
		catch(NullPointerException e)
		{
			e.printStackTrace();
			return "null";
		}
		catch(Exception e)
		{
			e.printStackTrace();
			return "Slow";
		}
	}
}
