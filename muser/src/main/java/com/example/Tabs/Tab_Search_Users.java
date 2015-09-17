package com.example.Tabs;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;

import com.ameba.muser.R;
import com.example.Adapter.Search_User_Adapter;
import com.example.ProgressTask.Search_Hashtags_User_ProgressTask;
import com.example.classes.Util_Class;

import java.util.ArrayList;
import java.util.HashMap;

public class Tab_Search_Users extends Fragment
{
    EditText  search_editText;
    ListView  users_listView;
    TextView  error_message;
    Context   con;
    ImageView temp_logo;

    View rootView;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        con = getActivity();
        if (savedInstanceState == null)
        {
            rootView = inflater.inflate(R.layout.tab_search_users, container, false);
            search_editText = (EditText) rootView.findViewById(R.id.search_editText);
            error_message = (TextView) rootView.findViewById(R.id.error_message);
            users_listView = (ListView) rootView.findViewById(R.id.users_listView);

            temp_logo = (ImageView) rootView.findViewById(R.id.temp_logo);

            show_temp_logo();
            error_message.setText("Search for Users.");
        }

        search_editText.setOnEditorActionListener(new OnEditorActionListener()
        {
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event)
            {
                if (actionId == EditorInfo.IME_ACTION_SEARCH)
                {
                    get_data_and_hit();
                    return true;
                }
                return false;
            }
        });

        search_editText.setOnTouchListener(new OnTouchListener()
        {
            @Override
            public boolean onTouch(View v, MotionEvent event)
            {
                final int DRAWABLE_LEFT   = 0;
                final int DRAWABLE_TOP    = 1;
                final int DRAWABLE_RIGHT  = 2;
                final int DRAWABLE_BOTTOM = 3;

                if (event.getAction() == MotionEvent.ACTION_UP)
                {
                    if (event.getRawX() >= (search_editText.getRight() - search_editText.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width()))
                    {
                        get_data_and_hit();

                        return true;
                    }
                }
                return false;
            }
        });

        return rootView;
    }

    public void get_data_and_hit()
    {
        String value = search_editText.getText().toString().trim();

        if (value.length() > 0)
        {
            //			users_listView.setVisibility(View.VISIBLE);
            //			error_message.setVisibility(View.GONE);

            show_temp_logo();
            error_message.setText(con.getResources().getString(R.string.please_wait));

            //new Search_Hashtags_User_ProgressTask(con, value, "U","Search").execute();

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB)
            {
                new Search_Hashtags_User_ProgressTask(con, Tab_Search_Users.this, value, "U", "Search").executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
            }
            else
            {
                new Search_Hashtags_User_ProgressTask(con, Tab_Search_Users.this, value, "U", "Search").execute();
            }

            InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(search_editText.getWindowToken(), 0);
        }
        else
        {
            Util_Class.show_Toast("Please enter search value.", con);
        }
    }

    Search_User_Adapter adapter;
    ArrayList<HashMap<String, String>> list = new ArrayList<>();

    public void set_data(ArrayList<HashMap<String, String>> list)
    {

        hide_temp_logo();
        if (this.list.size() == 0)
        {

            this.list = list;
            adapter = new Search_User_Adapter(con, this.list);
            users_listView.setAdapter(adapter);

        }
        else
        {
            this.list = list;
            adapter.add_data(this.list);
            adapter.notifyDataSetChanged();
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
        users_listView.setVisibility(View.GONE);

    }

    private void hide_temp_logo()
    {
        temp_logo.setVisibility(View.GONE);
        error_message.setVisibility(View.GONE);
        users_listView.setVisibility(View.VISIBLE);
    }

}