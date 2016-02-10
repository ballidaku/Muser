package com.example.Adapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.Async_Thread.Super_AsyncTask;
import com.Async_Thread.Super_AsyncTask_Interface;
import com.example.ProgressTask.Accept_Reject_Request_Thread;
import com.example.Tabs.Tab_My_Profile_Sessions;
import com.example.classes.Global;
import com.example.classes.RoundedCornersGaganImg;
import com.example.classes.Util_Class;
import com.ameba.muser.Drawer;
import com.ameba.muser.Image_Video_Details;
import com.ameba.muser.Other_Profile;
import com.ameba.muser.R;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

public class Notification_Adapter extends BaseAdapter
{
    Context                            con;
    ArrayList<HashMap<String, String>> list;
    String                             type;
    SharedPreferences                  rem_pref;
    Fragment                           con2;

    int picture_count = 0, video_count = 0, session_count = 0, connect_count = 0;

    public Notification_Adapter(Context con, ArrayList<HashMap<String, String>> list, String type)
    {
        this.con = con;
        this.list = list;
        this.type = type;
        rem_pref = con.getSharedPreferences("Remember", con.MODE_WORLD_READABLE);

        picture_count = rem_pref.getInt("Picture_count", 0);
        video_count = rem_pref.getInt("Video_count", 0);
        session_count = rem_pref.getInt("Session_count", 0);
        connect_count = rem_pref.getInt("Connect_count", 0);

    }

    public Notification_Adapter(Context con, Fragment con2, ArrayList<HashMap<String, String>> list, String type)
    {
        this.con = con;
        this.con2 = con2;
        this.list = list;
        this.type = type;
        rem_pref = con.getSharedPreferences("Remember", con.MODE_WORLD_READABLE);

        picture_count = rem_pref.getInt("Picture_count", 0);
        video_count = rem_pref.getInt("Video_count", 0);
        session_count = rem_pref.getInt("Session_count", 0);
        connect_count = rem_pref.getInt("Connect_count", 0);

    }

    @Override
    public int getCount()
    {
        // TODO Auto-generated method stub
        return list.size();
    }

    @Override
    public Object getItem(int arg0)
    {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public long getItemId(int arg0)
    {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public View getView(final int position, View row, ViewGroup parent)
    {
        LayoutInflater inflater = (LayoutInflater) con.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        row = inflater.inflate(R.layout.custom_notification, parent, false);

        RoundedCornersGaganImg image       = (RoundedCornersGaganImg) row.findViewById(R.id.image);
        TextView               description = (TextView) row.findViewById(R.id.description);
        final TextView         time        = (TextView) row.findViewById(R.id.time);

        ImageView accept = (ImageView) row.findViewById(R.id.accept);
        ImageView reject = (ImageView) row.findViewById(R.id.reject);

		
		/*String styledText = "<font color='black'><b>Dannie Dee </b></font>(friends with <font color='black'><b>"
				+ "Jorge Luis"
				+ "</b></font> )and also commented on <font color='black'><b>"
				+" Sharanpal" + "</b></font> ";*/

        if (type.equals("I") || type.equals("V"))
        {

            image.setImageUrl(con, list.get(position).get("showing_image"));
            //			Drawer.imageLoader.displayImage(list.get(position).get("showing_image"), image, Drawer.options);
            String styledText = "<font color='#4F606A'><b>" + list.get(position).get("showing_name") + " </b> </font><font color='Black' style='font-size:9px;'> " + list.get(position).get("activity") + "</font>";
            description.setText(Html.fromHtml(styledText));

            row.setOnClickListener(new OnClickListener()
            {

                @Override
                public void onClick(View v)
                {
					
					/*Intent i=new Intent(con,Image_Video_Details.class);
					i.putExtra("map",list.get(position));
					i.putExtra("time", time.getText().toString());
					i.putExtra("type", type);
					con.startActivity(i);*/

                /*    Intent i = new Intent(con, Image_Video_Details.class);
                    i.putExtra("map", list.get(position));
                    i.putExtra("post_id", list.get(position).get("post_id"));
                    i.putExtra("user_id", rem_pref.getString("user_id", ""));
                    i.putExtra("from_where", "Notification");
                    i.putExtra("which", "IV");
                    con.startActivity(i);
*/

                    HashMap<String, String> map = new HashMap();
                    map.put("user_id", "");
                    map.put("data_type", "");
                    map.put("post_id", list.get(position).get("post_id"));
                    map.put("myfavid", rem_pref.getString("user_id", ""));
                   // map.put("session", "S");

                    Hit_Service(map);

                }
            });
        }
        else if (type.equals("S"))
        {
            image.setImageUrl(con, list.get(position).get("profile_image"));
            //			Drawer.imageLoader.displayImage(list.get(position).get("profile_image"), image, Drawer.options);
            String styledText = "<font color='#4F606A'><b>" + list.get(position).get("user_name") + "</b> </font><font color='Black' style='font-size:9px;'> " + list.get(position).get("activity") + "</font>";
            description.setText(Html.fromHtml(styledText));

            row.setOnClickListener(new OnClickListener()
            {

                @Override
                public void onClick(View v)
                {

                    if (list.get(position).get("activity").contains("liked") || list.get(position).get("activity").contains("commented") || list.get(position).get("activity").contains("tagged"))
                    {
						/*Intent i=new Intent(con,Image_Video_Details.class);
						i.putExtra("post_id",list.get(position).get("post_id"));
						i.putExtra("user_id",rem_pref.getString("user_id", ""));
						i.putExtra("from_where", "Notification");
						i.putExtra("which", "S");
						con.startActivity(i);*/

                        HashMap<String, String> map = new HashMap();
                        map.put("user_id", "");
                        map.put("data_type", "");
                        map.put("post_id", list.get(position).get("post_id"));
                        map.put("myfavid", rem_pref.getString("user_id", ""));
                        map.put("session", "S");

                        Hit_Service(map);

                    }
                    else if (list.get(position).get("activity").contains("subscribed"))
                    {
                        Global.set_user_id(list.get(position).get("user_id"));
                        Global.set_friend_id(rem_pref.getString("user_id", ""));

                        Intent i = new Intent(con, Other_Profile.class);
                        con.startActivity(i);
                    }

                }
            });
        }
        else if (type.equals("C"))
        {
            //first N then changed by arsh to R on sunday 31 may
            if (list.get(position).get("approve_status").equals("R") && !list.get(position).get("from_user_id").equals(rem_pref.getString("user_id", "")))
            {
                image.setImageUrl(con, list.get(position).get("from_profile_image"));
                //Drawer.imageLoader.displayImage(list.get(position).get("from_profile_image"), image, Drawer.options);

                String styledText = "<font color='#4F606A'><b>" + list.get(position).get("from_user_name") + "</b></font><font color='Black' style='font-size:9px;'> is requesting to follow you.</font>";
                description.setText(Html.fromHtml(styledText));

                accept.setVisibility(View.VISIBLE);
                reject.setVisibility(View.VISIBLE);

                accept.setOnClickListener(new OnClickListener()
                {

                    @Override
                    public void onClick(View arg0)
                    {
                        new Accept_Reject_Request_Thread(con, con2, list.get(position).get("from_user_id"), "Y");

                    }
                });

                reject.setOnClickListener(new OnClickListener()
                {

                    @Override
                    public void onClick(View arg0)
                    {
                        new Accept_Reject_Request_Thread(con, con2, list.get(position).get("from_user_id"), "N");

                    }
                });

                row.setOnClickListener(new OnClickListener()
                {

                    @Override
                    public void onClick(View arg0)
                    {
						
						/*Global.set_user_id(list.get(position).get("from_user_id"));
						Global.set_friend_id(rem_pref.getString("user_id", ""));
						
						Intent i=new Intent(con,Other_Profile.class);
						//i.putExtra("user_id", list.get(position).get("user_id"));
						con.startActivity(i);*/

                        if (list.get(position).get("from_user_id").equals(rem_pref.getString("user_id", "")))
                        {
                            ((Drawer) con).click();
                        }
                        else
                        {
                            Global.set_user_id(list.get(position).get("from_user_id"));
                            Global.set_friend_id(rem_pref.getString("user_id", ""));

                            Intent i = new Intent(con, Other_Profile.class);
                            //i.putExtra("user_id", list.get(position).get("user_id"));
                            con.startActivity(i);
                        }

                    }
                });

            }
            else if (list.get(position).get("approve_status").equals("N") && list.get(position).get("from_user_id").equals(rem_pref.getString("user_id", "")))
            {

                image.setImageUrl(con, list.get(position).get("to_profile_image"));
                //Drawer.imageLoader.displayImage(list.get(position).get("to_profile_image"), image, Drawer.options);

                String styledText = "<font color='#4F606A'><b> You're </b></font><font color='Black' style='font-size:9px;'> requesting to follow " + list.get(position).get("to_user_name") + ".</font>";
                description.setText(Html.fromHtml(styledText));

                row.setOnClickListener(new OnClickListener()
                {

                    @Override
                    public void onClick(View arg0)
                    {

                        Global.set_user_id(list.get(position).get("to_user_id"));
                        Global.set_friend_id(rem_pref.getString("user_id", ""));

                        Intent i = new Intent(con, Other_Profile.class);
                        //i.putExtra("user_id", list.get(position).get("user_id"));
                        con.startActivity(i);

                    }
                });
            }
            else if (list.get(position).get("from_user_id").equals(rem_pref.getString("user_id", "")) && list.get(position).get("approve_status").equals("R"))
            {
                image.setImageUrl(con, list.get(position).get("to_profile_image"));
                //Drawer.imageLoader.displayImage(list.get(position).get("to_profile_image"), image, Drawer.options);
                String styledText = "<font color='#4F606A'><b> You </b></font><font color='Black' style='font-size:9px;'> " + list.get(position).get("activity") + list.get(position).get("to_user_name") + "." + "</font>";
                description.setText(Html.fromHtml(styledText));

                row.setOnClickListener(new OnClickListener()
                {

                    @Override
                    public void onClick(View arg0)
                    {

                        Global.set_user_id(list.get(position).get("to_user_id"));
                        Global.set_friend_id(rem_pref.getString("user_id", ""));

                        Intent i = new Intent(con, Other_Profile.class);
                        //i.putExtra("user_id", list.get(position).get("user_id"));
                        con.startActivity(i);

                    }
                });
            }
            else if (list.get(position).get("from_user_id").equals(rem_pref.getString("user_id", "")) && list.get(position).get("approve_status").equals("Y"))
            {
                image.setImageUrl(con, list.get(position).get("to_profile_image"));
                //Drawer.imageLoader.displayImage(list.get(position).get("to_profile_image"), image, Drawer.options);
                String styledText = "<font color='#4F606A'><b> You're </b></font><font color='Black' style='font-size:9px;'> " + list.get(position).get("activity") + list.get(position).get("to_user_name") + "." + "</font>";
                description.setText(Html.fromHtml(styledText));

                row.setOnClickListener(new OnClickListener()
                {

                    @Override
                    public void onClick(View arg0)
                    {

                        Global.set_user_id(list.get(position).get("to_user_id"));
                        Global.set_friend_id(rem_pref.getString("user_id", ""));

                        Intent i = new Intent(con, Other_Profile.class);
                        //i.putExtra("user_id", list.get(position).get("user_id"));
                        con.startActivity(i);

                    }
                });
            }
            else if (list.get(position).get("to_user_id").equals(rem_pref.getString("user_id", "")) && list.get(position).get("activity").trim().equals("request"))
            {

                image.setImageUrl(con, list.get(position).get("from_profile_image"));
                //Drawer.imageLoader.displayImage(list.get(position).get("from_profile_image"), image, Drawer.options);
                String styledText = "<font color='#4F606A'><b>" + list.get(position).get("from_user_name") + "</b> </font><font color='Black' style='font-size:9px;'> " + " has been added to your friends list." + "</font>";
                description.setText(Html.fromHtml(styledText));

                row.setOnClickListener(new OnClickListener()
                {

                    @Override
                    public void onClick(View arg0)
                    {

                        Global.set_user_id(list.get(position).get("from_user_id"));
                        Global.set_friend_id(rem_pref.getString("user_id", ""));

                        Intent i = new Intent(con, Other_Profile.class);
                        //i.putExtra("user_id", list.get(position).get("user_id"));
                        con.startActivity(i);

                    }
                });
            }
            else if (list.get(position).get("to_user_id").equals(rem_pref.getString("user_id", "")))
            {

                image.setImageUrl(con, list.get(position).get("from_profile_image"));
                //Drawer.imageLoader.displayImage(list.get(position).get("from_profile_image"), image, Drawer.options);
                String styledText = "<font color='#4F606A'><b>" + list.get(position).get("from_user_name") + "</b> </font><font color='Black' style='font-size:9px;'> " + "is now " + list.get(position).get("activity") + " you" + "." + "</font>";
                description.setText(Html.fromHtml(styledText));

                row.setOnClickListener(new OnClickListener()
                {

                    @Override
                    public void onClick(View arg0)
                    {

                        Global.set_user_id(list.get(position).get("from_user_id"));
                        Global.set_friend_id(rem_pref.getString("user_id", ""));


                        Intent i = new Intent(con, Other_Profile.class);
                        //i.putExtra("user_id", list.get(position).get("user_id"));
                        con.startActivity(i);

                    }
                });
            }

            else
            {
                image.setImageUrl(con, list.get(position).get("from_profile_image"));
                //Drawer.imageLoader.displayImage(list.get(position).get("from_profile_image"), image, Drawer.options);
                String styledText = "<font color='#4F606A'><b>" + list.get(position).get("from_user_name") + "</b> </font><font color='Black' style='font-size:9px;'> " + list.get(position).get("activity") + list.get(position).get("to_user_name") + "." + "</font>";
                description.setText(Html.fromHtml(styledText));

                row.setOnClickListener(new OnClickListener()
                {

                    @Override
                    public void onClick(View arg0)
                    {

                        Global.set_user_id(list.get(position).get("from_user_id"));
                        Global.set_friend_id(rem_pref.getString("user_id", ""));

                        Intent i = new Intent(con, Other_Profile.class);
                        //i.putExtra("user_id", list.get(position).get("user_id"));
                        con.startActivity(i);

                    }
                });
            }

        }
        else if (type.equals("F"))
        {

            image.setImageUrl(con, list.get(position).get("profile_image"));
            //String who=list.get(position).get("user_id").equals(rem_pref.getString("user_id", "")?"you":;
            //Drawer.imageLoader.displayImage(list.get(position).get("profile_image"), image, Drawer.options);
            String styledText = "<font color='#4F606A'><b>" + list.get(position).get("user_name") + " </b> </font><font color='Black' style='font-size:9px;'> " + list.get(position).get("activity") + "</font>";
            description.setText(Html.fromHtml(styledText));

            row.setOnClickListener(new OnClickListener()
            {

                @Override
                public void onClick(View v)
                {

                    if (list.get(position).get("activity").contains("liked") ||
                              list.get(position).get("activity").contains("tagged") ||
                              list.get(position).get("activity").contains("commented") ||
                              list.get(position).get("activity").contains("likes")||
                              list.get(position).get("activity").contains("favoured"))
                    {
                      /*  Intent i = new Intent(con, Image_Video_Details.class);
                        i.putExtra("post_id", list.get(position).get("post_id"));
                        i.putExtra("user_id", list.get(position).get("user_id"));
                        i.putExtra("from_where", "Notification");
                        i.putExtra("which", "");
                        con.startActivity(i);*/

                        HashMap<String, String> map = new HashMap();
                        map.put("user_id", "");
                        map.put("data_type", "");
                        map.put("post_id", list.get(position).get("post_id"));
                        map.put("myfavid", rem_pref.getString("user_id", ""));
                        // map.put("session", "S");

                        if (!list.get(position).get("post_id").equals("0"))
                        {
                            Hit_Service(map);
                        }

                    }
                    else if (list.get(position).get("activity").contains("followed") ||
                              list.get(position).get("activity").contains("training with"))
                    {
                        Global.set_user_id(list.get(position).get("friend_id"));
                        Global.set_friend_id(rem_pref.getString("user_id", ""));

                        Intent i = new Intent(con, Other_Profile.class);
                        con.startActivity(i);
                    }
                  /*  else if (list.get(position).get("activity").contains("commented"))// actually this is for under trending under friends
                    {
                       *//* Intent i = new Intent(con, Image_Video_Details.class);
                        i.putExtra("post_id", list.get(position).get("post_id"));
                        i.putExtra("user_id", rem_pref.getString("user_id", ""));
                        i.putExtra("from_where", "Notification");
                        i.putExtra("which", "IV");
                        con.startActivity(i);*//*

                        HashMap<String, String> map = new HashMap();
                        map.put("user_id", "");
                        map.put("data_type", "");
                        map.put("post_id", list.get(position).get("post_id"));
                        map.put("myfavid", rem_pref.getString("user_id", ""));
                        // map.put("session", "S");

                        Hit_Service(map);
                    }*/

                }
            });

        }

        if (picture_count > 0 && type.equals("I") && picture_count > position)
        {
            description.setTypeface(null, Typeface.BOLD);
        }
        else if (video_count > 0 && type.equals("V") && video_count > position)
        {
            description.setTypeface(null, Typeface.BOLD);
        }
        else if (session_count > 0 && type.equals("S") && session_count > position)
        {
            description.setTypeface(null, Typeface.BOLD);
        }
        else if (connect_count > 0 && type.equals("C") && connect_count > position)
        {
            description.setTypeface(null, Typeface.BOLD);
        }

        Util_Class util = new Util_Class();

        time.setText(util.get_time2(list.get(position).get("date")));

        return row;
    }

    public void add_data(ArrayList<HashMap<String, String>> list)
    {
        this.list = list;
        picture_count = 0;
        video_count = 0;
        session_count = 0;
        connect_count = 0;

    }

    String post_owner_user_id="";
    public void Hit_Service(final HashMap<String, String> map)
    {


        Util_Class.execute(new Super_AsyncTask(con, map, Util_Class.get_pictures_videos, new Super_AsyncTask_Interface()
        {

            @Override
            public void onTaskCompleted(String output)
            {
                Log.e("Response is", output);
                try
                {


                    JSONObject object = new JSONObject(output);
                    if (object.getString("status").equals("Success") || object.getString("status").equals("Private"))
                    {
                       // Toast.makeText(con, "Success", Toast.LENGTH_LONG).show();

                        JSONArray jo = object.getJSONArray("post_info");
                        ArrayList<HashMap<String, String>> list = new ArrayList<>();
                        for (int i = 0; i < jo.length(); i++)
                        {
                            JSONObject obj = jo.getJSONObject(i);
                            HashMap<String, String> map = new HashMap<String, String>();

                            post_owner_user_id=obj.getString("user_id");
                            map.put("user_id", obj.getString("user_id"));
                            map.put("user_name", obj.getString("user_name"));
                            map.put("profile_image", obj.getString("profile_image"));
                            map.put("post_id", obj.getString("post_id"));
                            map.put("trainer_id", obj.getString("trainer_id"));
                            map.put("trainer_name", obj.getString("trainer_name"));
                            map.put("category_id", obj.getString("category_id"));
                            map.put("category_name", obj.getString("category_name"));
                            map.put("data_type", obj.getString("data_type"));
                            map.put("data", obj.getString("data"));
                            map.put("thumbnail", obj.getString("thumbnail"));
                            map.put("caption", obj.getString("caption"));
                            map.put("tag", obj.getString("tag"));
                            map.put("latitude", obj.getString("latitude"));
                            map.put("duration", obj.getString("duration"));
                            map.put("fitness_goal", obj.getString("fitness_goal"));
                            map.put("like_count", obj.getString("like_count"));
                            map.put("comment_count", obj.getString("comment_count"));
                            map.put("date", obj.getString("date"));
                            map.put("focus_id", obj.getString("focus_id"));
                            map.put("focus_name", obj.getString("focus_name"));
                            map.put("is_self_liked", obj.getString("is_self_liked"));
                            map.put("is_self_favourite", obj.getString("is_self_favourite"));
                            map.put("location", obj.getString("location"));

                            if (obj.getJSONObject("get_post_owner").getString("is_repost").equals("Y"))
                            {
                                map.put("is_repost", "Y");
                                JSONObject ob = obj.getJSONObject("get_post_owner").getJSONObject("user_info");

                                map.put("repost_user_id", ob.getString("user_id"));
                                map.put("repost_user_name", ob.getString("user_name"));
                                map.put("repost_profile_image", ob.getString("profile_image"));
                            }
                            else
                            {
                                map.put("is_repost", "N");
                            }

                            if (obj.getJSONObject("tagged_peoples").getString("is_tagged_peoples").equals("Y"))
                            {
                                map.put("is_tagged", "Y");
                                JSONArray array = obj.getJSONObject("tagged_peoples").getJSONArray("tagged_ppl");

                                String tagged_user_id = "", tagged_user_name = "";
                                for (int j = 0; j < array.length(); j++)
                                {
                                    tagged_user_id += j == 0 ? array.getJSONObject(j).getString("user_id") : "," + array.getJSONObject(j).getString("user_id");
                                    tagged_user_name += j == 0 ? "@" + array.getJSONObject(j).getString("user_name") : ", @" + array.getJSONObject(j).getString("user_name");
                                }
                                Log.e(tagged_user_id, tagged_user_name);

                                map.put("tagged_user_id", tagged_user_id);
                                map.put("tagged_user_name", tagged_user_name);
                            }
                            else
                            {
                                map.put("is_tagged", "N");
                            }

                            map.put("is_self_subscribed", obj.getString("is_self_subscribed"));
                            map.put("is_self_recommended", obj.getString("is_self_recommended"));
                            map.put("is_self_posted", obj.getString("is_self_posted"));

                            list.add(map);

                        }

                        Intent i=new Intent(con,Image_Video_Details.class);
                        i.putExtra("post_id",map.get("post_id"));
                        i.putExtra("user_id",post_owner_user_id);
                        i.putExtra("from_where", "Notification");
                        i.putExtra("which", "S");
                        i.putExtra("map", list.get(0));
                        con.startActivity(i);

                        //((Image_Video_Details) con).set_data(list.get(0));

                        Log.e("list", "" + list.size());

                    }
                    else
                    {
                        Toast.makeText(con, object.getString("message"), Toast.LENGTH_SHORT).show();
                    }
                }
                catch (Exception ex)
                {
                    Log.e("Exception is", ex.toString());
                }
            }
        }));

    }

}
