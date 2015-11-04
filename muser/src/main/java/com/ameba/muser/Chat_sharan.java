package com.ameba.muser;

import android.app.ActionBar;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Rect;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.example.Adapter.Chat_sharan_Adapter;
import com.example.ProgressTask.Get_unreadChat;
import com.example.classes.Chat_data;
import com.example.classes.Data_list;
import com.example.classes.Util_Class;
import com.rockerhieu.emojicon.EmojiconEditText;
import com.rockerhieu.emojicon.EmojiconGridFragment;
import com.rockerhieu.emojicon.EmojiconsFragment;
import com.rockerhieu.emojicon.emoji.Emojicon;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.CoreProtocolPNames;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

/**
 Created by sharan on 22/9/15. */
public class Chat_sharan extends FragmentActivity implements View.OnClickListener, EmojiconGridFragment.OnEmojiconClickedListener, EmojiconsFragment.OnEmojiconBackspaceClickedListener
{
    FrameLayout      emojicons;
    EmojiconEditText edMSG;
    String userID = "", frndID = "", user_img = "", my_img = "", usr_name = "";
    ImageView keyboard, smilly;
    public static boolean chats = false;

    ListView          listv_chat;
    Context           con;
    SharedPreferences preferences;
    Util_Class util = new Util_Class();



    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_sharan);

        con = this;



        edMSG = (EmojiconEditText) findViewById(R.id.editT_msg);

        (findViewById(R.id.send)).setOnClickListener(this);

        (findViewById(R.id.back)).setOnClickListener(this);

        (smilly = (ImageView) findViewById(R.id.smilly)).setOnClickListener(this);

        (keyboard = (ImageView) findViewById(R.id.keyboard)).setOnClickListener(this);

        emojicons = (FrameLayout) findViewById(R.id.emojicons);

        listv_chat = (ListView) findViewById(R.id.listv_chat);

        preferences = getSharedPreferences("Remember", Context.MODE_WORLD_READABLE);
        userID = preferences.getString("user_id", "1");

        my_img = preferences.getString("profile_image", "");

        user_img = getIntent().getStringExtra("img");
        frndID = getIntent().getStringExtra("id");

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB)
        {
            new Get_unreadChat(responseHandlerChat, userID, con, frndID, "A").executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        }
        else
        {

            new Get_unreadChat(responseHandlerChat, userID, con, frndID, "A").execute();
        }


      /*  int height =util.get_keyboard_height(con);
        if (height != 0)
        {
            changeKeyboardHeight(height);
        }
        else
        {*/
        View activityRootView = findViewById(R.id.main);
        checkKeyboardHeight(activityRootView);
        //}


       /* listv_chat.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id)
            {

                if(list.get(position).getFriend_id().equalsIgnoreCase(userID))
                {
                    view.setBackgroundColor(Color.parseColor("#B3E2FF"));
                }

                return true;
            }
        });*/

    }



    @Override
    public void onBackPressed()
    {

        changeKeyboardHeight(0);
        Log.e("onBack", "BACK");
    }

    @Override
    protected void onStop()
    {

        chats = false;
        super.onStop();
    }

    @Override
    protected void onPause()
    {
        chats = false;
        super.onPause();
    }

    @Override
    protected void onResume()
    {
        chats = true;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB)
        {
            new Get_unreadChat(responseHandlerChat, userID, con, frndID, "U").executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        }
        else
        {

            new Get_unreadChat(responseHandlerChat, userID, con, frndID, "U").execute();
        }
        super.onResume();
    }

    @Override
    public void onClick(View arg0)
    {

        switch (arg0.getId())
        {
            case R.id.send:

                if (!edMSG.getText().toString().trim().equals(""))
                {

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB)
                    {

                        new sendChat(edMSG.getText().toString()).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                    }
                    else
                    {

                        new sendChat(edMSG.getText().toString()).execute();
                    }

                }
                else
                {
                    edMSG.setText("");
                    edMSG.setHintTextColor(Color.RED);
                }

                break;

            case R.id.smilly:

                emoji_keyboard("emoji");

                break;

            case R.id.keyboard:
                edMSG.requestFocus();
                emoji_keyboard("keyboard");

                //show_keyboard();

                break;

            case R.id.back:

                chats = false;
                this.finish();

                break;

            default:

                break;
        }

    }

    @Override
    protected void onResumeFragments()
    {
        setEmojiconFragment();
        super.onResumeFragments();
    }

    boolean isKeyBoardVisible;

    private void changeKeyboardHeight(int height)
    {

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ActionBar.LayoutParams.MATCH_PARENT, height);
        emojicons.setLayoutParams(params);

    }

    private void checkKeyboardHeight(final View parentLayout)
    {

        parentLayout.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener()
        {

            @Override
            public void onGlobalLayout()
            {

                Rect r = new Rect();
                parentLayout.getWindowVisibleDisplayFrame(r);

                int screenHeight     = parentLayout.getRootView().getHeight();
                int heightDifference = screenHeight - (r.bottom);

                Log.e("heightDifference", "" + heightDifference);

                if (heightDifference > 100)
                {
                    preferences.edit().putInt("keyboard_height", heightDifference).apply();

                    isKeyBoardVisible = true;
                    changeKeyboardHeight(heightDifference);

                    Log.e("Hello", "Ki gal bandra");
                    //parentLayout.getViewTreeObserver().removeGlobalOnLayoutListener(this);

                }
                else
                {

                    isKeyBoardVisible = false;

                }

            }
        });

    }

    @Override
    public void onEmojiconClicked(Emojicon emojicon)
    {
        EmojiconsFragment.input(edMSG, emojicon);
    }

    @Override
    public void onEmojiconBackspaceClicked(View v)
    {
        EmojiconsFragment.backspace(edMSG);
    }

    private void setEmojiconFragment()
    {
        boolean useSystemDefault = false;
        getSupportFragmentManager().beginTransaction().replace(R.id.emojicons, EmojiconsFragment.newInstance(useSystemDefault)).commit();
    }

    public void emoji_keyboard(String s)
    {

        if (isKeyBoardVisible)
        {
            emojicons.setVisibility(View.GONE);
        }

        if (s.equals("keyboard"))
        {
            keyboard.setVisibility(View.GONE);
            smilly.setVisibility(View.VISIBLE);

            final InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.toggleSoftInputFromWindow(edMSG.getApplicationWindowToken(), InputMethodManager.SHOW_FORCED, 0);
            //
            emojicons.setVisibility(View.GONE);

        }
        else
        {

            keyboard.setVisibility(View.VISIBLE);
            smilly.setVisibility(View.GONE);

            emojicons.setVisibility(View.VISIBLE);

            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(edMSG.getWindowToken(), 0);

        }

    }

    Handler responseHandlerChat = new Handler()
    {
        public void handleMessage(Message msg)
        {

            Data_list data = (Data_list) msg.getData().get("Chat");

            // Showmsgs(data.getList());

            set_chat_data(data.getList());

            if (chats)
            {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB)
                {
                    new Get_unreadChat(responseHandlerChat, userID, con, frndID, "U").executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                }
                else
                {

                    new Get_unreadChat(responseHandlerChat, userID, con, frndID, "U").execute();
                }
            }
        }
    };

    ArrayList<Chat_data> list = new ArrayList<>();

    Chat_sharan_Adapter adapter;

    public void set_chat_data(ArrayList<Chat_data> list)
    {
        try
        {
            if (this.list.size() == 0)
            {
                this.list = list;
                adapter = new Chat_sharan_Adapter(con, list, user_img);
                listv_chat.setAdapter(adapter);
            }
            else
            {

                adapter.add_data(list);
                listv_chat.invalidateViews();

            }
        }
        catch (Exception e)// when no data in the message list
        {
            adapter = new Chat_sharan_Adapter(con, list, user_img);
            listv_chat.setAdapter(adapter);

            e.printStackTrace();
        }
        scrollMyListViewToBottom();

    }

    public void delete_message(int pos)
    {
        list.remove(pos);
        set_chat_data(list);
    }

    private void scrollMyListViewToBottom()
    {
        listv_chat.post(new Runnable()
        {
            @Override
            public void run()
            {
                // Select the last row so it will scroll into view...
                listv_chat.setSelection(adapter.getCount() - 1);
            }
        });
    }

    class sendChat extends AsyncTask<Void, Void, Void>
    {
        String jsonStr = "";

        String message = "";
        int    pos;
        String formattedDateJOBJ;

        Chat_data chatData;

        public sendChat(String msg)
        {

            try
            {
                this.message = Util_Class.emoji_encode(msg);
            }
            catch (UnsupportedEncodingException e)
            {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

            try
            {
                pos = adapter.getCount();
            }
            catch (Exception e)
            {
                pos = 0;
                e.printStackTrace();
            }

        }

        @Override
        protected void onPreExecute()
        {

            Calendar         c      = Calendar.getInstance();
            SimpleDateFormat dfJOBJ = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            formattedDateJOBJ = dfJOBJ.format(c.getTime());

            chatData = new Chat_data(frndID, message, "", formattedDateJOBJ);

            // ArrayList<Chat_data> list = new ArrayList<Chat_data>();
            list.add(chatData);

            set_chat_data(list);

            // Showmsgs(list);******************************************************************************************************************************************
            edMSG.setText("");

            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... params)
        {

            try
            {

                List<NameValuePair> param = new ArrayList<NameValuePair>();
                param.add(new BasicNameValuePair("sender_id", userID));
                param.add(new BasicNameValuePair("receiver_id", frndID));
                param.add(new BasicNameValuePair("message", message));
                // param.add(new BasicNameValuePair("timezone",
                // UtilClass.timezone));



                HttpParams httpParams = new BasicHttpParams();
                httpParams.setParameter(CoreProtocolPNames.PROTOCOL_VERSION, HttpVersion.HTTP_1_1);

                HttpClient httpClient = new DefaultHttpClient(httpParams);

                HttpEntity httpEntity = null;
                HttpResponse httpResponse = null;

                HttpPost httpPost = new HttpPost(Util_Class.save_messages);
                if (params != null)
                {
                    httpPost.setEntity(new UrlEncodedFormEntity(param));
                }

                httpResponse = httpClient.execute(httpPost);
                httpEntity = httpResponse.getEntity();
                jsonStr = EntityUtils.toString(httpEntity);

                // jsonStr = sh.makeServiceCall(UtilClass.url + "/chat/unread",
                // 2, param);

                if (jsonStr != null)
                {

                    try
                    {

                        Log.e("Response", "" + jsonStr);

                        JSONObject jsonobject = new JSONObject(jsonStr);

                        message = jsonobject.getString("message");

                    }
                    catch (Exception e)
                    {

                        message = "Error";
                        Log.e("Error", e.getMessage());
                        e.printStackTrace();
                    }
                }

            }
            catch (Exception e)
            {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result)
        {

            try
            {



                JSONObject jsonobject = new JSONObject(jsonStr);

                String response = jsonobject.getString("status");
                //				{"status":"Success","message_info":{"message_id":39,"sender_id":"2","receiver_id":"1","message":"G","read_status":"N","added_date":"2015-05-14 11:44:48"},"message":"Message save successfully."}
                if (response.equalsIgnoreCase("Success"))
                {
                    Log.e("message_id", "" + jsonobject.getJSONObject("message_info").getString("message_id"));

                    list.get(pos).set_message_id(jsonobject.getJSONObject("message_info").getString("message_id"));

                    set_chat_data(list);

                }

            }
            catch (Exception e)
            {

                e.printStackTrace();
            }

            super.onPostExecute(result);
        }

    }

    public void HideEmoji()
    {
        emojicons.setVisibility(View.GONE);
        smilly.setVisibility(View.VISIBLE);
        keyboard.setVisibility(View.GONE);
    }

}
