package com.example.Adapter;

import android.app.ActionBar;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ameba.muser.R;
import com.example.ProgressTask.Delete_Message_ProgressTask;
import com.example.classes.Chat_data;
import com.example.classes.RoundedCornersGaganImg;
import com.example.classes.Util_Class;
import com.rockerhieu.emojicon.EmojiconTextView;

import org.apache.http.ParseException;

import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 Created by sharan on 22/9/15. */
public class Chat_sharan_Adapter extends BaseAdapter
{

    ArrayList<Chat_data> list;
    private Context con;
    SharedPreferences rem_pref;
    String userID = "", DateTemp = "", user_img = "", my_img = "";

    View result;
    Util_Class util;

    public Chat_sharan_Adapter(Context con, ArrayList<Chat_data> list, String user_img)
    {
        this.list = list;

        this.con = con;
        this.user_img = user_img;

        rem_pref = con.getSharedPreferences("Remember", con.MODE_WORLD_READABLE);

        my_img = rem_pref.getString("profile_image", "");
        userID = rem_pref.getString("user_id", "1");

        util=new Util_Class();
    }

    @Override
    public int getCount()
    {
        return list.size();
    }

    @Override
    public Object getItem(int position)
    {
        return list;
    }

    @Override
    public long getItemId(int position)
    {
        return list.get(position).hashCode();
    }

    @Override
    public View getView(final int position,  View row, ViewGroup parent)
    {


        String       Friend_id  = list.get(position).getFriend_id();
        final String message    = list.get(position).getMessage();
        final String date       = list.get(position).getAdded_date();
        final String message_id = list.get(position).getMessageID();

        LayoutInflater inflater = (LayoutInflater) con.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if (Friend_id.equalsIgnoreCase(userID))
        {
            row = inflater.inflate(R.layout.custom_other_chat, parent, false);
            EmojiconTextView tvMSG = (EmojiconTextView) row.findViewById(R.id.txtV_MyChatmsg);

            RoundedCornersGaganImg DP = (RoundedCornersGaganImg) row.findViewById(R.id.fb_MyChat);
            LinearLayout FramemsgLayout = (LinearLayout) row.findViewById(R.id.FrameLayoutMyChat);

            TextView tvTime = (TextView) row.findViewById(R.id.txtV_MyChatDate);

            FrameLayout imageContainer = (FrameLayout) row.findViewById(R.id.FrameLayoutIMAGE);

            DP.setImageUrl(con, user_img);



            FramemsgLayout.setVisibility(View.VISIBLE);
            imageContainer.setVisibility(View.GONE);
            // youVideo.setVisibility(View.GONE);

            try
            {
                tvMSG.setText(Util_Class.emoji_decode(message));
            }
            catch (UnsupportedEncodingException e)
            {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            tvTime.setText(util.get_time2(date));

        }

        else
        {

            row = inflater.inflate(R.layout.custom_my_chat, parent, false);

            EmojiconTextView tvMSG = (EmojiconTextView) row.findViewById(R.id.txtV_otherChatmsg);

            RoundedCornersGaganImg DP = (RoundedCornersGaganImg) row.findViewById(R.id.fb_otherChat);
            LinearLayout FramemsgLayout = (LinearLayout) row.findViewById(R.id.FrameLayoutotherChat);

            TextView tvTime = (TextView) row.findViewById(R.id.txtV_otherChatDate);

            FrameLayout imageContainer = (FrameLayout) row.findViewById(R.id.FrameLayoutIMAGE_Other);

            DP.setImageUrl(con, my_img);


            FramemsgLayout.setVisibility(View.VISIBLE);
            imageContainer.setVisibility(View.GONE);

            try
            {
                tvMSG.setText(Util_Class.emoji_decode(message));
            }
            catch (UnsupportedEncodingException e)
            {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            tvTime.setText(util.get_time2(date));

            row.setOnLongClickListener(new View.OnLongClickListener()
            {
                @Override
                public boolean onLongClick(View v)
                {
                    View.OnClickListener delete = new View.OnClickListener()
                    {
                        @Override
                        public void onClick(View v)
                        {
                            Util_Class.super_dialog.dismiss();

                            new Delete_Message_ProgressTask(con, message_id, position).execute();

                        }
                    };
                    Util_Class.show_super_dialog(con, delete, "");



                    return false;
                }
            });

        }

/*        LinearLayout lay_date = (LinearLayout) row.findViewById(R.id.lay_date);

        String formtDate = parseDateToddMMyyyy(date);
        if (!DateTemp.equals(formtDate))
        {

            LinearLayout.LayoutParams lpDate = new LinearLayout.LayoutParams(ActionBar.LayoutParams.WRAP_CONTENT, ActionBar.LayoutParams.WRAP_CONTENT);
            lpDate.gravity = Gravity.CENTER;

            TextView tvdate = new TextView(con);
            tvdate.setLayoutParams(lpDate);
            tvdate.setText(formtDate + "");
            tvdate.setTextColor(Color.BLACK);
            tvdate.setTextSize(15);
            tvdate.setPadding(5, 5, 5, 5);

            // tvdate.setBackgroundColor(R.drawable.btn_gray_pressed);
            tvdate.setGravity(Gravity.CENTER);
            lay_date.addView(tvdate);
        }
        DateTemp = formtDate;*/

        return row;

    }



    public void add_data(ArrayList<Chat_data> list)
    {
        this.list = list;
    }

   /* public static String parseDateToddMMyyyy(String time)
    {
        String           inputPattern  = "yyyy-MM-dd HH:mm:ss";
        String           outputPattern = "dd-MMMM";
        SimpleDateFormat inputFormat   = new SimpleDateFormat(inputPattern);
        SimpleDateFormat outputFormat  = new SimpleDateFormat(outputPattern);

        Date   date = null;
        String str  = null;

        try
        {
            date = inputFormat.parse(time);
            str = outputFormat.format(date);
        }
        catch (ParseException e)
        {

            e.printStackTrace();
        }
        catch (java.text.ParseException e)
        {

            e.printStackTrace();
        }
        return str;
    }*/

}
