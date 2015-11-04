package com.ameba.muser;

import java.util.ArrayList;
import java.util.HashMap;

import com.example.Adapter.Messages_Adapter;
import com.example.ProgressTask.Get_Messages_Contacts_Thread;
import com.example.ProgressTask.Get_Trending_FriendActivities_ProgressTask;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class Messages extends Fragment
{
    Messages messages;
    ListView messages_list;
    Context  con;
    Messages con2;
    ArrayList<HashMap<String, String>> list = new ArrayList<>();
    Messages_Adapter  adapter;
    SharedPreferences rem_pref;
    TextView          error_message;
    ImageView         temp_logo;

    View v;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {

        if (savedInstanceState == null)
        {

            v = inflater.inflate(R.layout.messages, container, false);

            con = getActivity();
            con2 = this;
            messages = this;
            rem_pref = con.getSharedPreferences("Remember", con.MODE_WORLD_READABLE);
            messages_list = (ListView) v.findViewById(R.id.messages_list);
            error_message = (TextView) v.findViewById(R.id.error_message);
            temp_logo = (ImageView) v.findViewById(R.id.temp_logo);

        }

        return v;
    }

    @Override
    public void onResume()
    {
        super.onResume();

        show_temp_logo();
        error_message.setText(con.getResources().getString(R.string.please_wait));

        new Get_Messages_Contacts_Thread(con, con2);


    }

    public void add_list(ArrayList<HashMap<String, String>> list)
    {
        rem_pref.edit().putInt("message_count", 0).apply();
        ((Drawer) con).refresh_menu_logo();

        hide_temp_logo();

        this.list = list;
        adapter = new Messages_Adapter(con,con2, list);
        messages_list.setAdapter(adapter);

    }

    public void delete_message(int pos)
    {
        list.remove(pos);
        add_list(list);
    }

    private void show_temp_logo()
    {
        temp_logo.setVisibility(View.VISIBLE);
        error_message.setVisibility(View.VISIBLE);
        messages_list.setVisibility(View.GONE);

    }

    private void hide_temp_logo()
    {
        temp_logo.setVisibility(View.GONE);
        error_message.setVisibility(View.GONE);
        messages_list.setVisibility(View.VISIBLE);
    }

    public void on_Failure()
    {

        show_temp_logo();
        error_message.setText(con.getResources().getString(R.string.no_data_found));
    }

}
