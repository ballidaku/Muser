package com.ameba.muser;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import com.Async_Thread.Super_AsyncTask;
import com.Async_Thread.Super_AsyncTask_Interface;
import com.example.Adapter.Transaction_Adapter;
import com.example.classes.Util_Class;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Transaction_History extends Activity implements View.OnClickListener
{

    Context           con;
    SharedPreferences rem_pref;

    ListView listv_transaction_history;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transaction__history);

        con = this;

        (findViewById(R.id.back)).setOnClickListener(this);

        listv_transaction_history=(ListView)findViewById(R.id.listv_transaction_history);

        rem_pref = con.getSharedPreferences("Remember", con.MODE_WORLD_READABLE);

        HashMap<String, String> map = new HashMap<>();
        map.put("user_id", rem_pref.getString("user_id", ""));

        Hit_Service(map);

    }

    @Override
    public void onClick(View v)
    {
        switch (v.getId())
        {
            case R.id.back:
                this.finish();
                break;

            default:
                break;
        }
    }

    public void Hit_Service(HashMap<String, String> map)
    {
        Util_Class.execute(new Super_AsyncTask(con, map, Util_Class.main + "/user.php?muser=transection_history", new Super_AsyncTask_Interface()
        {

            @Override
            public void onTaskCompleted(String output)
            {

                try
                {
                    JSONObject object = new JSONObject(output);
                    if (object.getString("status").equalsIgnoreCase("Success"))
                    {
                        JSONArray array = object.getJSONArray("transaction_history");

                        ArrayList<HashMap<String, String>> list = new ArrayList<>();

                        for (int i = 0; i < array.length(); i++)
                        {

                            JSONObject inner_obj = array.getJSONObject(i);
                            HashMap<String, String> map = new HashMap<>();

                            map.put("transaction_history_id", inner_obj.getString("transaction_history_id"));
                            map.put("other_id", inner_obj.getString("other_id"));
                            map.put("other_name", inner_obj.getString("other_name"));
                            map.put("other_image", inner_obj.getString("other_image"));
                            map.put("funds", inner_obj.getString("funds"));
                            map.put("receiver_fund", inner_obj.getString("receiver_fund"));
                            map.put("muser_fund", inner_obj.getString("muser_fund"));
                            map.put("payment_status", inner_obj.getString("payment_status"));
                            map.put("added_date", inner_obj.getString("added_date"));

                            list.add(map);

                        }


                        Transaction_Adapter adapter=new Transaction_Adapter(con,list);

                        listv_transaction_history.setAdapter(adapter);
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
