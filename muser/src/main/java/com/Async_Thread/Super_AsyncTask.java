package com.Async_Thread;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.ameba.muser.R;
import com.example.classes.Util_Class;

import java.util.HashMap;


/**
 Created by sharan on 10/9/15. */
public class Super_AsyncTask extends AsyncTask<Void, Void, String>
{

    String URL;
    HashMap<String, String> inputData = null;
    Context        con;
    ProgressDialog dialog;
    Super_AsyncTask_Interface listener = null;
    //Constant_Class            constant = new Constant_Class();

    // static Super_AsyncTask super_asyncTask;

    public Super_AsyncTask(Context con, HashMap<String, String> inputData, String URL, Super_AsyncTask_Interface listener)
    {
        this.con = con;
        this.inputData = inputData;
        this.URL = URL;
        this.listener = listener;
        // super_asyncTask=this;

       // constant.hide_keyboard(con);
    }

    public Super_AsyncTask(Context con, String URL,Super_AsyncTask_Interface listener)
    {
        this.con = con;
        this.URL = URL;
        this.listener=listener;
       // constant.hide_keyboard(con);
    }


  /*  public void cancelAsync()
    {
        if (super_asyncTask!=null)
        {
            super_asyncTask.cancel(true);
        }

    }*/




    @Override
    protected void onPreExecute()
    {
        super.onPreExecute();

        dialog = ProgressDialog.show(con, "", "");
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dialog.setContentView(R.layout.progress_dialog);
        dialog.show();

    }

    @Override
    protected String doInBackground(Void... params)
    {
        String response = "";

        Log.e("inputData",""+ inputData);
        try
        {
            if (inputData != null)
            {
                response = new WebServiceHandler().performPostCall(URL, inputData);
            }
            else
            {
                response = new WebServiceHandler().performGetCall(URL);
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        return response;
    }

    @Override
    protected void onPostExecute(String ResponseString)
    {
        super.onPostExecute(ResponseString);

        if (dialog.isShowing())
        {
            dialog.dismiss();
        }

        Log.e("Response for " + con.getClass().getName(), " " + ResponseString);

        if (!ResponseString.equals("SLOW") && !ResponseString.equals("ERROR"))
        {
            listener.onTaskCompleted(ResponseString);

        }
        else if (ResponseString.equals("SLOW"))
        {
            Util_Class.show_Toast("Please check your network.", con);
        }
        else if (ResponseString.equals("ERROR"))
        {
            Util_Class.show_Toast("Server side error.", con);
        }

    }
}
