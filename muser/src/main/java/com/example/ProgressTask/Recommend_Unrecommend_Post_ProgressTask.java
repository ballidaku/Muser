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
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;

import com.example.classes.Util_Class;
import com.ameba.muser.Image_Video_Details;
import com.ameba.muser.R;

public class Recommend_Unrecommend_Post_ProgressTask extends AsyncTask<String, Void, Void>
{
	Context con;
	//private ProgressDialog dialog;
	String message = null, msg_json,msg="";
	SharedPreferences rem_pref;

	String post_id,action;

	public Recommend_Unrecommend_Post_ProgressTask(Context con, String post_id,String action)
	{
		this.con = con;
		this.post_id = post_id;
		this.action=action;
		Log.e("post_id", post_id);
		rem_pref = con.getSharedPreferences("Remember", con.MODE_WORLD_READABLE);
	}

	protected void onPreExecute()
	{
		Util_Class.dismiss_dialog();
		/*dialog = ProgressDialog.show(con, "", "");
		dialog.setContentView(R.layout.main);
		dialog.show();*/
	}

	@Override
	protected Void doInBackground(String... params)
	{
		String response = get_pictures();
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
					else
					{
						message = "Failure";
						// message = null;
					}
					
					msg = new JSONObject(response).getString("message");
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
		/*if(dialog.isShowing())
		{
			dialog.dismiss();
		}*/
		Util_Class.dismiss_dialog();
		OnClickListener retry = new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				Util_Class.internet_dialog.dismiss();
				new Recommend_Unrecommend_Post_ProgressTask(con, post_id,action).execute();
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
			
		
				Util_Class.show_Toast(msg, con);
				
				
				//((Image_Video_Details) con).set_recommemded_postdata();
				
				((Image_Video_Details) con).refresh();
				
				
			//	new Get_Pictures_Videos_ProgressTask(con,user_id,"",post_id).execute();
				
			
		
			
			
		}
		else if(message.equals("Failure"))
		{ 
			Util_Class.show_Toast(msg, con);
			
		}
	}

	public String get_pictures()
	{
		
		
		HttpPost httppost = new HttpPost(Util_Class.set_recommended);
		HttpParams httpParameters = new BasicHttpParams();
//		HttpConnectionParams.setConnectionTimeout(httpParameters, 4000);
//		HttpConnectionParams.setSoTimeout(httpParameters, 4000);
		DefaultHttpClient httpclient = new DefaultHttpClient(httpParameters);
		try
		{
			
			List<NameValuePair> param = new ArrayList<NameValuePair>();
			param.add(new BasicNameValuePair("user_id", rem_pref.getString("user_id", "")));
			param.add(new BasicNameValuePair("post_id", post_id));
			param.add(new BasicNameValuePair("status", action));
			
			Log.e("Rewcommend post  param", param.toString());
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
