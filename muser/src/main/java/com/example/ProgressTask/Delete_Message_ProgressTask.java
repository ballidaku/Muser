package com.example.ProgressTask;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;

import com.ameba.muser.Chat_sharan;
import com.ameba.muser.Chats;
import com.ameba.muser.R;
import com.example.Adapter.Chat_sharan_Adapter;
import com.example.classes.Util_Class;

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
import org.json.JSONException;
import org.json.JSONObject;

import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.List;

import static com.ameba.muser.R.*;

/**
 Created by sharan on 18/9/15. */
public class Delete_Message_ProgressTask extends AsyncTask<String, Void, Void>
{
    Context con;
    private ProgressDialog dialog;
    String message_id = "", message = null, msg_json;
    SharedPreferences rem_pref;
    int position;

    public Delete_Message_ProgressTask(Context con, String message_id,int position)
    {
        this.con = con;
        this.message_id = message_id;
        rem_pref = con.getSharedPreferences("Remember", con.MODE_WORLD_READABLE);
        this.position=position;
    }

    protected void onPreExecute()
    {
        Util_Class.dismiss_dialog();
        dialog = ProgressDialog.show(con, "", "");
        dialog.setContentView(layout.main);
        dialog.show();
    }

    @Override
    protected Void doInBackground(String... params)
    {
        String response = delete();
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
                    }
                    else if (msg_json.equals("Failure"))
                    {
                        message = "Failure";
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
        View.OnClickListener retry = new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                new Delete_Message_ProgressTask(con, message_id,position).execute();
            }
        };
        System.out.println("message-->" + message);
        if (message.equals("null"))
        {
            Util_Class.show_global_dialog(con, con.getResources().getString(string.server_error));
        }
        else if (message.equals("Slow"))
        {
            Util_Class.show_internet_dialog(con, retry);
        }
        else if (message.equals("Success"))
        {
            Util_Class.show_Toast("Deleted successfully", con);
            ((Chat_sharan)con).delete_message(position);
        }
        else if (message.equals("Failure"))
        {
            Util_Class.show_Toast("Failure", con);
            //Util_Class.show_global_dialog(con, con.getResources().getString(string.email_error));
        }



    }

    public String delete()
    {
        HttpPost httppost       = new HttpPost(Util_Class.delete_message);
        HttpParams httpParameters = new BasicHttpParams();
        DefaultHttpClient httpclient = new DefaultHttpClient(httpParameters);
        try
        {
            List<NameValuePair> param = new ArrayList<NameValuePair>();
            param.add(new BasicNameValuePair("msg_id", message_id));
            param.add(new BasicNameValuePair("user_id", rem_pref.getString("user_id", "")));
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
