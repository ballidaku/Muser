package com.example.ProgressTask;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;

import com.ameba.muser.Drawer;
import com.ameba.muser.Login;
import com.ameba.muser.R;
import com.ameba.muser.Registration;
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

/**
 * Created by sharan on 8/9/15.
 */
public class Logout_ProgressTask  extends AsyncTask<String, Void, Void>
{
    Context con;
    ProgressDialog dialog;
    String message="",msg_json="";
    SharedPreferences rem_pref;
//    public OnTaskCompleted listener=null;

    public Logout_ProgressTask(Context con)
    {
        this.con=con;


        rem_pref=con.getSharedPreferences("Remember",con.MODE_WORLD_READABLE);
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
        String response = logout();
        try
        {
            if(response != null)
            {
                if(response.equals("Slow"))
                {
                    message = "Slow";
                }
                else
                {
                    msg_json = new JSONObject(response).getString("status");
                    if(msg_json.equals("Success"))
                    {
                        message = "Success";

                    }
                    else
                    {
                        message=msg_json;

                    }
                }
            }
            else
            {
                message = "null";
            }
        }
        catch(JSONException e)
        {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(Void result)
    {
        if(dialog.isShowing())
        {
            dialog.dismiss();
        }

        Util_Class.dismiss_dialog();

        View.OnClickListener retry = new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Util_Class.internet_dialog.dismiss();
                new Logout_ProgressTask(con).execute();
            }
        };
        System.out.println("message-->" + message);
        if(message.equals("null"))
        {

            Util_Class.show_global_dialog(con, con.getResources().getString(R.string.server_error));
        }
        else if(message.equals("Slow"))
        {
            Util_Class.show_internet_dialog(con, retry);
        }
        else if(message.equals("Success"))
        {

            ((Drawer)con).logout();
        }
        else if(message.equals("Failure"))
        {
                Util_Class.show_global_dialog(con, con.getResources().getString(R.string.email_not_exists));
        }

    }

    public String logout()
    {
        HttpPost httppost = new HttpPost(Util_Class.logout);
        HttpParams httpParameters = new BasicHttpParams();
//		HttpConnectionParams.setConnectionTimeout(httpParameters, 4000);
//		HttpConnectionParams.setSoTimeout(httpParameters, 4000);
        DefaultHttpClient httpclient = new DefaultHttpClient(httpParameters);
        try
        {
            List<NameValuePair> param = new ArrayList<NameValuePair>();
            param.add(new BasicNameValuePair("user_id", rem_pref.getString("user_id", "")));

            Log.e("logout_progressTask",param.toString());
            httppost.setEntity(new UrlEncodedFormEntity(param));
            HttpResponse response = httpclient.execute(httppost);
            String data = EntityUtils.toString(response.getEntity());
            Log.e("DATAAA", data);
            return data;
        }
        catch(SocketTimeoutException e)
        {
            e.printStackTrace();
            return "Slow";
        }
        catch(ConnectTimeoutException e)
        {
            e.printStackTrace();
            return "Slow";
        }
        catch(NullPointerException e)
        {
            e.printStackTrace();
            return "null";
        }
        catch(Exception e)
        {
            e.printStackTrace();
            return "Slow";
        }
    }
}
