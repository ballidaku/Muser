package com.example.ProgressTask;

import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.HashMap;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;

import com.example.Adapter.Expandable_list_adapter;
import com.example.classes.Expandable_collection;
import com.example.classes.Util_Class;
import com.ameba.muser.R;
import com.ameba.muser.Recomended;

public class Get_Recommended_ProgressTask extends AsyncTask<String, Void, Void>
{
	Context con;
	private ProgressDialog dialog;
	String message = null, msg_json;
	SharedPreferences rem_pref;
	ArrayList<Expandable_collection> expandable_list;
	//String type;

	public Get_Recommended_ProgressTask(Context con/*, String type*/)
	{
		this.con = con;
		//this.type = type;
		
		//Log.e("type", type);
		rem_pref = con.getSharedPreferences("Remember", con.MODE_WORLD_READABLE);
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
						JSONArray jo = new JSONObject(response).getJSONArray("category_info");
						
						expandable_list=new ArrayList<Expandable_collection>();
						for(int i = 0; i < jo.length(); i++)
						{
							JSONObject obj = jo.getJSONObject(i);
							
							
							String category_id = obj.getString("category_id");
							String category_name = obj.getString("category_name");
							
							JSONArray array = obj.getJSONArray("users_info");
							
							ArrayList<HashMap<String, String>> child_list = new ArrayList<HashMap<String, String>>();
							for(int j = 0; j < array.length(); j++)
							{
								JSONObject obj1 = array.getJSONObject(j);
								HashMap<String, String> map = new HashMap<String, String>();

								map.put("user_id", obj1.getString("user_id"));
								map.put("key", obj1.getString("key"));
								map.put("web_address", obj1.getString("web_address"));
								map.put("phone_number", obj1.getString("phone_number"));
								map.put("user_name", obj1.getString("user_name"));
								map.put("full_name", obj1.getString("full_name"));
								map.put("user_description", obj1.getString("user_description"));
								map.put("profile_image", obj1.getString("profile_image"));
								map.put("privacy_status", obj1.getString("privacy_status"));
								map.put("registration_type", obj1.getString("registration_type"));
								map.put("member_type", obj1.getString("member_type"));

								JSONObject jo2 = obj1.getJSONObject("optional_info");

								map.put("facebook_name", jo2.getString("facebook_name"));
								map.put("facebook_phone", jo2.getString("facebook_phone"));
								map.put("instagram_name", jo2.getString("instagram_name"));
								map.put("instagram_phone", jo2.getString("instagram_phone"));
								map.put("twitter_name", jo2.getString("twitter_name"));
								map.put("twitter_phone", jo2.getString("twitter_phone"));

								JSONObject jo3 = obj1.getJSONObject("profile_counts");

								map.put("followers_count", jo3.getString("followers_count"));
								map.put("following_count", jo3.getString("following_count"));
								map.put("post_count", jo3.getString("post_count"));
								child_list.add(map);

								Log.e("child_list", ""+child_list);
							}
							
							Expandable_collection t1=new Expandable_collection(category_id, category_name, child_list);
							
							expandable_list.add(t1);
						}
						
					}
					else
					{
						message = "Failure";
						// message = null;
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
				Util_Class.internet_dialog.dismiss();
				new Get_Recommended_ProgressTask(con).execute();
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
			Log.e("LLLLLLLLLLLL",""+ expandable_list);
			
				/*My_Profile_Pictures_Adapter adapter = new My_Profile_Pictures_Adapter(con, list);
				Tab_My_Profile_Pictures.pictures_gridview.setAdapter(adapter);*/
			
			Expandable_list_adapter adapter=new Expandable_list_adapter(con, expandable_list);
			Recomended.listView.setAdapter(adapter);

			for (int i = 0; i <expandable_list.size() ; i++)
			{
				if(expandable_list.get(i).get_child_list().size()>0)
				{
					Recomended.listView.expandGroup(i);
				}
			}

			
		}
		else if(message.equals("Failure"))
		{
			
//				Tab_My_Profile_Videos.videos_gridview.setVisibility(View.GONE);
//				Tab_My_Profile_Videos.error_message.setVisibility(View.VISIBLE);
//				Tab_My_Profile_Videos.error_message.setText(con.getResources().getString(R.string.no_data_found));
//			
			
			//Util_Class.show_global_dialog(con, con.getResources().getString(R.string.no_data_found));
		}
	}

	public String get_pictures()
	{
		HttpPost httppost = new HttpPost(Util_Class.get_recommended);
		//HttpParams httpParameters = new BasicHttpParams();
//		HttpConnectionParams.setConnectionTimeout(httpParameters, 4000);
//		HttpConnectionParams.setSoTimeout(httpParameters, 4000);
		DefaultHttpClient httpclient = new DefaultHttpClient();
		try
		{
			
			/*List<NameValuePair> param = new ArrayList<NameValuePair>();
			param.add(new BasicNameValuePair("user_id", Global.get_user_id()));
			param.add(new BasicNameValuePair("data_type", type));
			Log.e("login_progressTask param", param.toString());*/
			//httppost.setEntity(new UrlEncodedFormEntity(param));
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
