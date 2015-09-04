package com.example.ProgressTask;

import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.HashMap;
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

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;

import com.example.Tabs.Tab_Home_Pictures;
import com.example.Tabs.Tab_Home_Sessions;
import com.example.Tabs.Tab_Home_Videos;
import com.example.Tabs.Tab_My_Favourite_Pictures;
import com.example.Tabs.Tab_My_Favourite_Videos;
import com.example.Tabs.Tab_My_Profile_Pictures;
import com.example.Tabs.Tab_My_Profile_Sessions;
import com.example.Tabs.Tab_My_Profile_Videos;
import com.example.Tabs.Tab_Trending_Pictures;
import com.example.Tabs.Tab_Trending_Videos;
import com.example.classes.Util_Class;
import com.ameba.muser.R;
import com.ameba.muser.View_All_Comments;

public class Add_Like_Comment_Repost_ProgressTask extends AsyncTask<String, Void, Void>
{
	Context con;
	Fragment con2;
	SharedPreferences rem_pref;
	HashMap<String, String> map; 

	String status = null, msg_json,message;

	//ProgressDialog dialog;
	public Add_Like_Comment_Repost_ProgressTask(Context con,HashMap<String, String> map)
	{
		this.con = con;
		this.map = map;

		rem_pref = con.getSharedPreferences("Remember", con.MODE_WORLD_READABLE);
	}

	public Add_Like_Comment_Repost_ProgressTask(Context con, Fragment con2, HashMap<String, String> map)
	{
		this.con = con;
		this.con2 = con2;
		this.map = map;

		rem_pref = con.getSharedPreferences("Remember", con.MODE_WORLD_READABLE);
	}

	protected void onPreExecute()
	{
		Util_Class.dismiss_dialog();
//		 dialog = ProgressDialog.show(con, "", ""); 
//		 dialog.setContentView(R.layout.main); 
//		 dialog.show(); 
	}

	@Override
	protected Void doInBackground(String... params)
	{
		String response = add_like_comment_repost();
		try
		{
			if(response != null)
			{
				if(response.equals("Slow"))
				{
					status = "Slow";
				}
				else
				{
					msg_json = new JSONObject(response).getString("status");
					if(msg_json.equals("Success"))
					{
						status = "Success";
						message=new JSONObject(response).getString("message");		
					}
					else
					{
						status = "Failure";
						// message = null;
					}
				}
			}
			else
			{
				status = "null";
			}
		}
		catch(JSONException e)
		{
			e.printStackTrace();
			status = "null";
		}
		return null;
	}

	@Override
	protected void onPostExecute(Void result)
	{
//		if(dialog.isShowing())
//		{
//			dialog.dismiss();
//		}
		Util_Class.dismiss_dialog();
		OnClickListener retry = new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				Util_Class.internet_dialog.dismiss();
				 new Add_Like_Comment_Repost_ProgressTask(con, map).execute();
			}
		};
		System.out.println("message-->" + status);
		if(status.equals("null"))
		{

			Util_Class.show_global_dialog(con, con.getResources().getString(R.string.server_error));
		}
		else if(status.equals("Slow"))
		{

			Util_Class.show_internet_dialog(con, retry);
		}
		else if(status.equals("Success"))
		{
			if(con instanceof View_All_Comments)
			{
				
			}
			else
			{
				Util_Class.show_Toast(message, con);
			}
			
			
			if(con2 instanceof Tab_Home_Pictures)
			{
				((Tab_Home_Pictures) con2).refresh();
			}
			else if(con2 instanceof Tab_Home_Videos)
			{
				((Tab_Home_Videos) con2).refresh();
			}
			else if(con2 instanceof Tab_Home_Sessions)
			{
				((Tab_Home_Sessions) con2).refresh();
			}
			else if(con2 instanceof Tab_My_Profile_Pictures)
			{
				((Tab_My_Profile_Pictures) con2).refresh();
			}
			else if(con2 instanceof Tab_My_Profile_Videos)
			{
				((Tab_My_Profile_Videos) con2).refresh();
			}
			else if(con2 instanceof Tab_My_Profile_Sessions)
			{
				((Tab_My_Profile_Sessions) con2).refresh();
			}
			else if(con2 instanceof Tab_Trending_Pictures)
			{
				((Tab_Trending_Pictures) con2).refresh();
			}
			else if(con2 instanceof Tab_Trending_Videos)
			{
				((Tab_Trending_Videos) con2).refresh();
			}
			else if(con2 instanceof Tab_My_Favourite_Pictures)
			{
				((Tab_My_Favourite_Pictures) con2).refresh();
			}
			else if(con2 instanceof Tab_My_Favourite_Videos)
			{
				((Tab_My_Favourite_Videos) con2).refresh();
			}
			
//			if(map.get("action").equals("R"))
//			{
//				Util_Class.show_Toast("This post is reposted on your profile.", con);
//			}
			
			//String type=map.get("type").equals("Image") ? "I" : "V";
					
			
			//new Get_Pictures_Videos_ProgressTask(con,"",type,map.get("post_id")).execute();
			/*if(type.equals("T"))
			{
//				My_Profile_Pictures_Adapter adapter = new My_Profile_Pictures_Adapter(con, list);
//				Tab_My_Profile_Pictures.pictures_gridview.setAdapter(adapter);
				
				
				Search_Hashtags_Child_Adapter adapter = new Search_Hashtags_Child_Adapter(con,hashtags_list_child);
				Search_Hashtags_Child.gridView.setAdapter(adapter);
			}*/
			/*else
			{
//				My_Profile_Videos_Adapter adapter = new My_Profile_Videos_Adapter(con, list);
//				Tab_My_Profile_Videos.videos_gridview.setAdapter(adapter);
				Search_User_Adapter adapter=new Search_User_Adapter(con,user_list);
				Tab_Search_Users.users_listView.setAdapter(adapter);
			}*/
		}
		else if(status.equals("Failure"))
		{
			/*if(type.equals("T"))
			{
				Tab_Search_Hashtags.hashtags_listView.setVisibility(View.GONE);
				Tab_Search_Hashtags.error_message.setVisibility(View.VISIBLE);

			}
			else
			{
				Tab_Search_Users.users_listView.setVisibility(View.GONE);
				Tab_Search_Users.error_message.setVisibility(View.VISIBLE);
			}*/

			//Util_Class.show_global_dialog(con, con.getResources().getString(R.string.no_data_found));
		}
	}

	public String add_like_comment_repost()
	{
		HttpPost httppost = new HttpPost(Util_Class.add_like_comment_repost);
		HttpParams httpParameters = new BasicHttpParams();
//		HttpConnectionParams.setConnectionTimeout(httpParameters, 4000);
//		HttpConnectionParams.setSoTimeout(httpParameters, 4000);
		DefaultHttpClient httpclient = new DefaultHttpClient(httpParameters);
		try
		{

			List<NameValuePair> param = new ArrayList<NameValuePair>();
			param.add(new BasicNameValuePair("user_id", rem_pref.getString("user_id", "")));
			param.add(new BasicNameValuePair("post_id", map.get("post_id")));
			param.add(new BasicNameValuePair("action", map.get("action")));
			param.add(new BasicNameValuePair("data", map.get("data")));
			
			
			Log.e("search child param", param.toString());
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
