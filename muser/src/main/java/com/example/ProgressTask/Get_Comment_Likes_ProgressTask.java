package com.example.ProgressTask;

import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.Collections;
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
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.media.Image;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;

import com.example.Adapter.Comments_Adapter;
import com.example.classes.Util_Class;
import com.ameba.muser.Image_Video_Details;
import com.ameba.muser.R;
import com.ameba.muser.View_All_Comments;
import com.ameba.muser.View_All_Likes;

public class Get_Comment_Likes_ProgressTask extends AsyncTask<String, Void, Void>
{
	Context con;
	//private ProgressDialog dialog;
	String message = null, msg_json;
	SharedPreferences rem_pref;
	ArrayList<HashMap<String, String>> list;
	String type, post_id;
	//boolean scroll;
	int limit;
	public static Comments_Adapter adp;
	String from_where,like_count,comment_count;

	public Get_Comment_Likes_ProgressTask(/* boolean scroll, */Context con, String type, String post_id, int limit, String from_where)
	{
		
		this.con = con;
		this.type = type;
		this.limit = limit;
		this.post_id = post_id;
		/* this.scroll=scroll; */
		this.from_where = from_where;

		rem_pref = con.getSharedPreferences("Remember", con.MODE_WORLD_READABLE);
	}

	protected void onPreExecute()
	{
		Util_Class.dismiss_dialog();
//		dialog = ProgressDialog.show(con, "", "");
//		dialog.setContentView(R.layout.main);
//		dialog.show();
	}

	@Override
	protected Void doInBackground(String... params)
	{
		String response = get_followers_following_blocks();
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
						JSONArray jo = new JSONObject(response).getJSONArray("comments");
						list = new ArrayList<HashMap<String, String>>();
						for(int i = 0; i < jo.length(); i++)
						{
							JSONObject obj = jo.getJSONObject(i);
							HashMap<String, String> map = new HashMap<String, String>();
							map.put("post_id", obj.getString("post_id"));
							map.put("data", obj.getString("data"));
							map.put("date", obj.getString("date"));
							

							JSONObject jo3 = obj.getJSONObject("user_info");

							map.put("user_id", jo3.getString("user_id"));
							map.put("user_name", jo3.getString("user_name"));
							map.put("profile_image", jo3.getString("profile_image"));

							
							list.add(map);
						}
						
						like_count=new JSONObject(response).getString("like_count");
						comment_count=new JSONObject(response).getString("comment_count");
						
						
					}
					else if(msg_json.equals("Failure"))
					{
						message = "Failure";
					}
					else if(msg_json.equals("Failure1"))
					{
						message = "Failure1";
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
				new Get_Comment_Likes_ProgressTask(/* scroll, */con, type, post_id, limit, from_where).execute();
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

			/* if(scroll==true) {
			 * 
			 * Image_Details.comments_listview.addNewData(list); scroll=false;
			 * 
			 * } else { */
			if(from_where.equals("View_All_Comments"))
			{
				View_All_Comments.comment_listview.onRefreshComplete();
				Collections.reverse(View_All_Comments.list);
				
				View_All_Comments.list.addAll(list);
				Collections.reverse(View_All_Comments.list);
				
				adp = new Comments_Adapter(con, View_All_Comments.list,from_where);
				View_All_Comments.comment_listview.setAdapter(adp);
			}
			else if(from_where.equals("Image_Details"))
			{
				if(list.size() > 0)
				{
					Image_Video_Details.view_all_comments.setText("view all " + comment_count + " comments");
					Image_Video_Details.likes_count.setText(like_count+" like(s)");
				}
				Image_Video_Details.comment_list.addAll(list);
				Collections.reverse(Image_Video_Details.comment_list);
				adp = new Comments_Adapter(con, Image_Video_Details.comment_list, from_where);
				
				
				Image_Video_Details.comments_listview.setAdapter(adp);
				Image_Video_Details.setListViewHeightBasedOnItems();
				
			/*	Image_Video_Details.scrollview.post(new Runnable()
				{
					public void run()
					{
						Image_Video_Details.scrollview.scrollTo(0, 0);
					}
				});*/
			}
			else   // View_All_Likes
			{
				View_All_Likes.list.addAll(list);
				View_All_Likes.likes_listview.onRefreshComplete();
				adp = new Comments_Adapter(con, View_All_Likes.list, from_where);
				View_All_Likes.likes_listview.setAdapter(adp);
			}
			

			//	}

			/* if(type.equals("B")) { Blocks_Adapter adapter = new Blocks_Adapter(con, list); Blocks.block_listview.setAdapter(adapter); } else if(type.equals("F")) { Followers_Adapter adapter=new
			 * Followers_Adapter(con, list); Followers.followers_listview.setAdapter(adapter); My_Profile_Videos_Adapter adapter = new My_Profile_Videos_Adapter(con, list);
			 * Tab_My_Profile_Videos.videos_gridview.setAdapter(adapter); } else if(type.equals("FG")) { Following_Adapter adapter=new Following_Adapter(con, list);
			 * Following.following_listview.setAdapter(adapter); } */

		}
		else if(message.equals("Failure"))
		{
			if(from_where.equals("View_All_Comments"))
			{
				View_All_Comments.comment_listview.onRefreshComplete();
			}
			else if(from_where.equals("View_All_Likes"))
			{
				View_All_Likes.likes_listview.onRefreshComplete();
			}

			//Util_Class.show_global_dialog(con, con.getResources().getString(R.string.no_data_found));
		}
		/* else if(message.equals("Failure")) {
		 * 
		 * Util_Class.show_global_dialog(con, con.getResources().getString(R.string.no_data_found)); } */

	}

	public String get_followers_following_blocks()
	{
		HttpPost httppost = new HttpPost(Util_Class.get_comments_likes);
		HttpParams httpParameters = new BasicHttpParams();
//		HttpConnectionParams.setConnectionTimeout(httpParameters, 4000);
//		HttpConnectionParams.setSoTimeout(httpParameters, 4000);
		DefaultHttpClient httpclient = new DefaultHttpClient(httpParameters);
		try
		{
			List<NameValuePair> param = new ArrayList<NameValuePair>();
			param.add(new BasicNameValuePair("post_id", post_id));
			param.add(new BasicNameValuePair("type", type));
			param.add(new BasicNameValuePair("limit", String.valueOf(limit)));

			Log.e("login_progressTask param", param.toString());

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
