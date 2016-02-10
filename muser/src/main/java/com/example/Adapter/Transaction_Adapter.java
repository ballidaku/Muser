package com.example.Adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.ameba.muser.R;
import com.example.classes.RoundedCornersGaganImg;
import com.example.classes.Util_Class;

import java.util.ArrayList;
import java.util.HashMap;

/**
 Created by sharan on 23/1/16. */
public class Transaction_Adapter extends BaseAdapter
{
    Context                            con;
    ArrayList<HashMap<String, String>> list;
    SharedPreferences                  rem_pref;
    Util_Class util_class = new Util_Class();

    public Transaction_Adapter(Context con, ArrayList<HashMap<String, String>> list)
    {
        this.con = con;
        this.list = list;
        rem_pref = con.getSharedPreferences("Remember", con.MODE_WORLD_READABLE);
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

        row = inflater.inflate(R.layout.custom_transaction_history, parent, false);

        RoundedCornersGaganImg user_image = (RoundedCornersGaganImg) row.findViewById(R.id.user_image);

        TextView               user_name  = (TextView) row.findViewById(R.id.user_name);
        TextView               txtv_transaction_date  = (TextView) row.findViewById(R.id.txtv_transaction_date);
        TextView               amount  = (TextView) row.findViewById(R.id.amount);
        TextView               txtv_status  = (TextView) row.findViewById(R.id.txtv_status);


        user_image.setImageUrl(con, list.get(position).get("other_image"));
        user_name.setText(list.get(position).get("other_name"));


        txtv_transaction_date.setText(util_class.get_date(list.get(position).get("added_date")));




        if(list.get(position).get("payment_status").equalsIgnoreCase("Paid"))
        {
            amount.setText("-$ "+list.get(position).get("funds"));
            txtv_status.setText("Paid");
            txtv_status.setTextColor(Color.RED);

        }
        else
        {
            amount.setText("+$ "+list.get(position).get("receiver_fund"));
            txtv_status.setText("Recieved");
            txtv_status.setTextColor(con.getResources().getColor(R.color.Green));
        }


        return row;
    }

    public void add_data(ArrayList<HashMap<String, String>> list)
    {
        this.list = list;
    }

}
