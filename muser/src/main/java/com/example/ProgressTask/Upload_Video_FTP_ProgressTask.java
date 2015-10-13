package com.example.ProgressTask;

import android.app.Activity;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationCompat.Builder;
import android.util.Log;

import com.ameba.muser.CancelUploadingVideo;
import com.ameba.muser.Drawer;
import com.ameba.muser.R;
import com.example.classes.Global;
import com.example.classes.Util_Class;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.UUID;

import it.sauronsoftware.ftp4j.FTPClient;
import it.sauronsoftware.ftp4j.FTPDataTransferListener;
import it.sauronsoftware.ftp4j.FTPIllegalReplyException;

/**
 * Created by Sharanpal on 7/16/2015.
 */
public
class Upload_Video_FTP_ProgressTask extends AsyncTask<String, Void, Void>
{
    Context con;
    String message = null, msg_json/*,video_duration*/;
    SharedPreferences rem_pref;
   public  static NotificationManager mNotifyManager;
    Builder mBuilder;
    File f;

    HashMap<String, String> map;//=new HashMap<String, String>();

    long total_size=0;

    boolean uploaded=false;
    public
    Upload_Video_FTP_ProgressTask(Context con, HashMap<String, String> map)
    {
        this.con = con;
        this.map = map;
         rem_pref = con.getSharedPreferences("Remember", con.MODE_WORLD_READABLE);

    }

    protected
    void onPreExecute()
    {
        Util_Class.dismiss_dialog();

        Util_Class.show_Toast("Uploading please wait", con);

        ((Activity) con).finish();

        //((Activity) con).moveTaskToBack(true);


        Intent dismissIntent = new Intent(con, CancelUploadingVideo.class);

        //dismissIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        PendingIntent piDismiss = PendingIntent.getActivity(con, 0, dismissIntent, PendingIntent.FLAG_UPDATE_CURRENT);




        mNotifyManager =(NotificationManager) con.getSystemService(Context.NOTIFICATION_SERVICE);
        mBuilder = new NotificationCompat.Builder(con);
        mBuilder.setContentTitle("Video Upload")
                    .setContentText("Uploading in progress")
                    .setPriority(2)
                    .setSmallIcon(R.drawable.app_logo)
                    .setContentIntent(piDismiss);
                  // .addAction(android.R.drawable.ic_delete, "Cancel", piDismiss);
                   // .setStyle(new NotificationCompat.BigTextStyle()
                //    .bigText("Uploading in progress"));


    }

    @Override
    protected
    Void doInBackground(String... params)
    {
        f = new File(map.get("image_path"));
        total_size=f.length();

        upload(f);

        return null;
    }

    @Override
    protected
    void onPostExecute(Void result)
    {
       if(uploaded)
        {
            //new Upload_Image_ProgressTask(con, map).execute();

            if( Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB )
            {
                new Upload_Image_ProgressTask(Drawer.con, map).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
            }
            else
            {
                new Upload_Image_ProgressTask(Drawer.con, map).execute();
            }
        }

    }

    public static FTPClient client = new FTPClient();
    /********* work only for Dedicated IP ***********/
    static final String	FTP_HOST	= "108.179.199.92";
    //static final String	FTP_HOST	= "ftp.amebatechnologies.com";
    /********* FTP USERNAME ***********/
    static final String	FTP_USER	= "muser_live";

    /********* FTP PASSWORD ***********/
    static final String	FTP_PASS	= "muser@123";

    public void upload(File path)
    {
        try
        {

            client.connect(FTP_HOST,7500);
            client.login(FTP_USER, FTP_PASS);
            client.setType(FTPClient.TYPE_BINARY);
           //client.changeDirectory("/uploads/videos/");
            client.changeDirectory("/uploads/" + rem_pref.getString("user_id", "") + "/videos/");

            //client.upload(path, new MyTransferListener());

            FileInputStream srcFileStream = new FileInputStream(path);

            String extension=(f.getName()).substring((f.getName()).indexOf("."), (f.getName()).length());
            String uniqueID = UUID.randomUUID().toString();

            client.upload(uniqueID+extension,srcFileStream,0,0,new MyTransferListener());

            map.put("video_name", uniqueID + extension);

            srcFileStream.close();
            client.logout();
            client.disconnect(true);


        }
        catch(Exception e)
        {
            e.printStackTrace();
            try
            {
                client.disconnect(true);
            }
            catch(Exception e2)
            {
                e2.printStackTrace();
            }
        }
    }





    long	transfBytes=0;
    public class MyTransferListener implements FTPDataTransferListener
    {


        public void started()
        {
            transfBytes	= 0;
//            bar.setProgress(0);
//            bar.setMax((int) count);
            //Log.i("Upload", count + "");

        }

        public void transferred(int length)
        {
            transfBytes += length;

            Handler handler = new Handler(Looper.getMainLooper());
            handler.post(new Runnable()
            {
                public void run()
                {
                 //   bar.setProgress((int)transfBytes);

                    mBuilder.setProgress((int) total_size, (int)transfBytes, false);
                    double c= (double) total_size;
                    double t= (double) transfBytes;
                    int p= (int) ((t/c)*100);
                    Log.e("p", "" + p);
                    mBuilder.setNumber(p);
                    mNotifyManager.notify(0, mBuilder.build());

                    if(p==100)
                    {
                        mNotifyManager.cancel(0);
                    }

                }
            });
        }

        @Override
        public void aborted()
        {
            Log.e("Uploading", "Aborted");
            //btn.setVisibility(View.VISIBLE);
            // Transfer aborted
            //Toast.makeText(getBaseContext(), " transfer aborted ,please try again...", Toast.LENGTH_SHORT).show();
            //System.out.println(" aborted ..." );
        }

        @Override
        public void completed()
        {
          /*  try
            {
                String extension=(f.getName()).substring((f.getName()).indexOf("."), (f.getName()).length());
                String uniqueID = UUID.randomUUID().toString();
                client.rename("/uploads/" + rem_pref.getString("user_id", "") + "/videos/"+f.getName(),uniqueID+extension);

                Log.e("GUID", "" + f.getName() + " ..............." + uniqueID + extension);

                map.put("image_path", uniqueID+extension);

            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
            catch (FTPIllegalReplyException e)
            {
                e.printStackTrace();
            }
            catch (FTPException e)
            {
                e.printStackTrace();
            }*/
            //btn.setVisibility(View.VISIBLE);
            // Transfer completed

            //Toast.makeText(getBaseContext(), " completed ...", Toast.LENGTH_SHORT).show();
            //System.out.println(" completed ..." );
            Log.e("Uploading", "Completed");
            uploaded=true;

        }

        @Override
        public void failed()
        {
            //btn.setVisibility(View.VISIBLE);
            // Transfer failed
            //System.out.println(" failed ...");
            Log.e("Uploading", "Failed");
            Util_Class.show_Toast("Uploading failed",con);
        }

//			Log.e("length", "" + MB);
        //Toast.makeText(getBaseContext(), " transferred ..." + length, Toast.LENGTH_SHORT).show();






    }


    public void cancel_uploading()
    {
        //upload.cancel(true);
        try
        {
            client.abortCurrentDataTransfer(true);
        }
        catch (IllegalStateException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        catch (IOException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        catch (FTPIllegalReplyException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }



}
