package com.ameba.muser;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.Adapter.Find_Friends_Adapter;
import com.example.ProgressTask.Get_Find_Friends_ProgressTask;
import com.example.ProgressTask.Get_Home_Pictures_Videos_ProgressTask;
import com.example.classes.Global;

import java.util.ArrayList;
import java.util.HashMap;

public class Find_Friends extends Fragment
{

    Context  con;
    ListView find_friends_listView;
    TextView error_message;
    View     v;
    ImageView temp_logo;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {

        if (savedInstanceState == null)
        {

            v = inflater.inflate(R.layout.find_friends, container, false);
            con = getActivity();
            find_friends_listView = (ListView) v.findViewById(R.id.find_friends_listView);
            error_message = (TextView) v.findViewById(R.id.error_message);

            temp_logo = (ImageView) v.findViewById(R.id.temp_logo);

            //  new Get_Find_Friends_ProgressTask(con).execute();

            show_temp_logo();
            error_message.setText(con.getResources().getString(R.string.please_wait));


            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB)
            {
                new Get_Find_Friends_ProgressTask(con,Find_Friends.this).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
            }
            else
            {
                new Get_Find_Friends_ProgressTask(con,Find_Friends.this).execute();
            }
        }

        return v;
    }

    ArrayList<HashMap<String, String>> list = new ArrayList<>();

    public void set_data(ArrayList<HashMap<String, String>> list)
    {
        this.list.clear();
        this.list=list;

        show_logo_or_not(this.list.size());

        Find_Friends_Adapter adapter=new Find_Friends_Adapter(con,Find_Friends.this,this.list);
        find_friends_listView.setAdapter(adapter);



    }

    public void show_logo_or_not(int size)
    {
        if (size == 0)
        {
            on_Failure();
        }
        else
        {
            hide_temp_logo();
        }
    }



    public void on_Failure()
    {

        show_temp_logo();
        error_message.setText(con.getResources().getString(R.string.no_data_found));
    }

    private void show_temp_logo()
    {
        temp_logo.setVisibility(View.VISIBLE);
        error_message.setVisibility(View.VISIBLE);
        find_friends_listView.setVisibility(View.GONE);

    }

    private void hide_temp_logo()
    {
        temp_logo.setVisibility(View.GONE);
        error_message.setVisibility(View.GONE);
        find_friends_listView.setVisibility(View.VISIBLE);
    }
}
