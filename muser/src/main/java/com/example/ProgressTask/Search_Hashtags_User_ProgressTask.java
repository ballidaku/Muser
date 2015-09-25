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
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;

import com.example.Adapter.Search_Hashtags_Adapter;
import com.example.Adapter.Search_User_Adapter;
import com.example.Tabs.Tab_Search_Hashtags;
import com.example.Tabs.Tab_Search_Users;
import com.example.classes.Util_Class;
import com.ameba.muser.R;
import com.ameba.muser.Tag_People;

public class Search_Hashtags_User_ProgressTask extends AsyncTask<String, Void, Void>
{
    Context                            con;
    SharedPreferences                  rem_pref;
    ArrayList<HashMap<String, String>> user_list;

    ArrayList<String> hashtags_list;
    String            message = null, msg_json, value, type, from_where;
    ProgressDialog dialog;
    Fragment       fragment;



    public Search_Hashtags_User_ProgressTask(Context con, Fragment fragment, String value, String type, String from_where)
    {
        this.con = con;
        this.value = value;
        this.type = type;
        this.from_where = from_where;
        this.fragment = fragment;

        rem_pref = con.getSharedPreferences("Remember", con.MODE_WORLD_READABLE);
    }

    public Search_Hashtags_User_ProgressTask(Context con, String value, String type, String from_where)
    {
        this.con = con;
        this.value = value;
        this.type = type;
        this.from_where = from_where;

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
        String response = get_search();
        try
        {
            if (response != null)
            {
                if (response.equals("Slow"))
                {
                    message = "Slow";
                }
                else
                {
                    msg_json = new JSONObject(response).getString("status");
                    if (msg_json.equals("Success"))
                    {
                        message = "Success";
                        JSONArray jo = new JSONObject(response).getJSONArray("tags");
                        if (type.equals("T"))
                        {
                            hashtags_list = new ArrayList<String>();
                            for (int i = 0; i < jo.length(); i++)
                            {
                                hashtags_list.add("#" + jo.getString(i));
                            }

                            Log.e("hashtags_list", "" + hashtags_list);

                        }
                        else if (type.equals("U") /*&& from_where.equals("Search")*/)
                        {
                            user_list = new ArrayList<HashMap<String, String>>();
                            for (int i = 0; i < jo.length(); i++)
                            {
                                JSONObject obj = jo.getJSONObject(i);
                                HashMap<String, String> map = new HashMap<String, String>();
                                map.put("user_id", obj.getString("user_id"));
                                map.put("user_name", obj.getString("user_name"));
                                map.put("profile_image", obj.getString("profile_image"));
                                user_list.add(map);
                            }
                        }
                        /*else if(type.equals("U") && from_where.equals("Tag_People"))
						{
							arrCountry = new ArrayList<ChipsItem>();

							for(int i = 0; i < jo.length(); i++)
							{
								JSONObject obj = jo.getJSONObject(i);
								HashMap<String, String> map = new HashMap<String, String>();
								map.put("user_id", obj.getString("user_id"));
								map.put("user_name", obj.getString("user_name"));
								map.put("profile_image", obj.getString("profile_image"));

								user_list.add(map);

								arrCountry.add(new ChipsItem(obj.getString("user_name"), R.drawable.ic_launcher));
							}

						}*/

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
        catch (JSONException e)
        {
            e.printStackTrace();
            message = "null";
        }
        return null;
    }

    @Override
    protected void onPostExecute(Void result)
    {
        if (dialog.isShowing())
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
                new Search_Hashtags_User_ProgressTask(con, value, type, from_where).execute();
            }
        };
        System.out.println("message-->" + message);
        if (message.equals("null"))
        {

            Util_Class.show_global_dialog(con, con.getResources().getString(R.string.server_error));
        }
        else if (message.equals("Slow"))
        {

            Util_Class.show_internet_dialog(con, retry);
        }
        else if (message.equals("Success"))
        {
            if (type.equals("T"))
            {
                //				My_Profile_Pictures_Adapter adapter = new My_Profile_Pictures_Adapter(con, list);
                //				Tab_My_Profile_Pictures.pictures_gridview.setAdapter(adapter);

                /*Search_Hashtags_Adapter adapter = new Search_Hashtags_Adapter(con, hashtags_list);
                Tab_Search_Hashtags.hashtags_listView.setAdapter(adapter);*/

                ((Tab_Search_Hashtags) fragment).set_data(hashtags_list);
            }
            else if (type.equals("U") && from_where.equals("Search"))
            {
                //				My_Profile_Videos_Adapter adapter = new My_Profile_Videos_Adapter(con, list);
                //				Tab_My_Profile_Videos.videos_gridview.setAdapter(adapter);
//                Search_User_Adapter adapter = new Search_User_Adapter(con, user_list);
                //                Tab_Search_Users.users_listView.setAdapter(adapter);

                ((Tab_Search_Users) fragment).set_data(user_list);

            }
            else if (type.equals("U") && from_where.equals("Tag_People"))
            {
				/*Search_User_Adapter adapter=new Search_User_Adapter(con,user_list);
				Captured_Image.tag_people.setAdapter(adapter);*/

                ((Tag_People) con).set_data(user_list);
				
				
				
				
				/*ChipsAdapter chipsAdapter = new ChipsAdapter(con, arrCountry);
				Captured_Image.tag_people.setAdapter(chipsAdapter);*/
            }
        }
        else if (message.equals("Failure"))
        {
            if (type.equals("T"))
            {
//                Tab_Search_Hashtags.hashtags_listView.setVisibility(View.GONE);
//                Tab_Search_Hashtags.error_message.setVisibility(View.VISIBLE);

                ((Tab_Search_Hashtags) fragment).on_Failure();

            }
            else if (type.equals("U") && from_where.equals("Search"))
            {
//                Tab_Search_Users.users_listView.setVisibility(View.GONE);
//                Tab_Search_Users.error_message.setVisibility(View.VISIBLE);

                ((Tab_Search_Users) fragment).on_Failure();
            }
            else if (type.equals("U") && from_where.equals("Tag_People"))
            {

                ((Tag_People) con).on_Failure();

            }

        }
    }

    public String get_search()
    {
        HttpPost   httppost       = new HttpPost(Util_Class.search);
        HttpParams httpParameters = new BasicHttpParams();
        //		HttpConnectionParams.setConnectionTimeout(httpParameters, 4000);
        //		HttpConnectionParams.setSoTimeout(httpParameters, 4000);
        DefaultHttpClient httpclient = new DefaultHttpClient(httpParameters);
        try
        {

            List<NameValuePair> param = new ArrayList<NameValuePair>();
            param.add(new BasicNameValuePair("search_term", value));
            param.add(new BasicNameValuePair("search_type", type));

            if (from_where.equals("Tag_People"))
            {
                param.add(new BasicNameValuePair("user_id", rem_pref.getString("user_id", "")));
            }

            Log.e("search param", param.toString());
            httppost.setEntity(new UrlEncodedFormEntity(param));
            HttpResponse response = httpclient.execute(httppost);
            String data = EntityUtils.toString(response.getEntity());
            Log.e("DATAAA", data);
            return data;
        }
        catch (SocketTimeoutException e)
        {
            e.printStackTrace();
            return "Slow";
        }
        catch (ConnectTimeoutException e)
        {
            e.printStackTrace();
            return "Slow";
        }
        catch (NullPointerException e)
        {
            e.printStackTrace();
            return "null";
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return "Slow";
        }
    }
}
