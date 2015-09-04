package com.ameba.muser;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.example.ProgressTask.Upload_Video_FTP_ProgressTask;
import com.example.classes.Util_Class;

import java.io.IOException;

import it.sauronsoftware.ftp4j.FTPIllegalReplyException;


public
class CancelUploadingVideo extends Activity
{

    @Override
    protected
    void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_text_yes_no);



        TextView text = (TextView)findViewById(R.id.text);
        Button yes = (Button) findViewById(R.id.yes);
        Button no = (Button) findViewById(R.id.no);

        text.setText("You want to cancel the uploading ?");



        yes.setOnClickListener(new View.OnClickListener() {
            @Override public
            void onClick(View view)
            {
                stop_uploading();
            }
        });

        no.setOnClickListener(new View.OnClickListener()
        {

            @Override
            public void onClick(View v)
            {
               finish();

            }
        });



    }


    public void stop_uploading()
    {
        try
        {

            Upload_Video_FTP_ProgressTask.client.abortCurrentDataTransfer(true);
            //Log.e("Hello", "Hello");

            Upload_Video_FTP_ProgressTask.mNotifyManager.cancel(0);



            finish();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        catch (FTPIllegalReplyException e)
        {
            e.printStackTrace();
        }
    }


}
