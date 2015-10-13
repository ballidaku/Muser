package com.ameba.muser;

import org.json.JSONObject;

import android.annotation.TargetApi;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.example.classes.Util_Class;
import com.google.android.gcm.GCMBaseIntentService;
import com.google.android.gcm.GCMRegistrar;


public
class GCMIntentService extends GCMBaseIntentService
{

    private static final String TAG = "GCMIntentService";
    String status;
    Bundle bun;

    static String SENDER_ID = "423011141634";
    static int flag = 0;
    SharedPreferences rem_pref;

    public
    GCMIntentService()
    {
        super(SENDER_ID);

    }

    @Override
    protected
    void onRegistered(Context context, String registrationId)
    {
        Log.i(TAG, "Device registered: regId = " + registrationId);

    }

    @Override
    protected void onUnregistered(Context context, String registrationId)
    {
        Log.i(TAG, "Device unregistered");
        if(GCMRegistrar.isRegisteredOnServer(context))
        {

        }
        else
        {
            Log.i(TAG, "Ignoring unregister callback");
        }

    }



    @Override
    protected
    void onMessage(Context context, Intent intent)
    {

        rem_pref = context.getSharedPreferences("Remember", context.MODE_WORLD_READABLE);
        try
        {

            bun = intent.getExtras();
            final String response = intent.getStringExtra("message");
            Log.e("+++++++GCM message+++", response);

            final JSONObject jsonNoTi = new JSONObject(response);
            flag = Integer.valueOf(jsonNoTi.getString("flag"));   //1 for message

            String message=jsonNoTi.getString("message");



            if(rem_pref.getBoolean("is_notification_on", true))
            {
                String notication_message = "";


                if(flag == 4)// for messages
                {
                    if(rem_pref.getInt("message_count", 0) == 0)
                    {
                        notication_message = Util_Class.emoji_decode(jsonNoTi.getString("message"));
                        rem_pref.edit().putInt("message_count", 1).apply();
                    }
                    else
                    {
                        int message_count = rem_pref.getInt("message_count", 0) + 1;
                        rem_pref.edit().putInt("message_count", message_count).apply();

                    }



                }
                else if(flag == 3)   //for notifications
                {
                    String type = jsonNoTi.getString("type");


                    if(rem_pref.getInt("notification_count", 0) == 0)
                    {
                        notication_message = jsonNoTi.getString("message");
                        rem_pref.edit().putInt("notification_count", 1).apply();
                    }
                    else
                    {
                        int message_count = rem_pref.getInt("notification_count", 0) + 1;
                        rem_pref.edit().putInt("notification_count", message_count).apply();

                        notication_message = " " + message_count + " Notifications";
                    }

                   // Log.e("type",type);

                    if(type.equals("S"))
                    {
                        int message_count = rem_pref.getInt("S", 0) + 1;
                        rem_pref.edit().putInt("S", message_count).apply();
                    }
                    else if(type.equals("L"))
                    {
                        int message_count = rem_pref.getInt("L", 0) + 1;
                        rem_pref.edit().putInt("L", message_count).apply();
                    }
                    else if(type.equals("C"))
                    {
                        int message_count = rem_pref.getInt("C", 0) + 1;
                        rem_pref.edit().putInt("C", message_count).apply();
                    }
                    else if(type.equals("FC") || type.equals("T") || type.equals("RE"))
                    {
                        int message_count = rem_pref.getInt("FC", 0) + 1;
                        rem_pref.edit().putInt("FC", message_count).apply();
                    }
                    else if(type.equals("FAV"))
                    {
                        int message_count = rem_pref.getInt("FAV", 0) + 1;
                        rem_pref.edit().putInt("FAV", message_count).apply();
                    }



                    if(message.contains("Image"))
                    {
                        int message_count = rem_pref.getInt("Picture_count", 0) + 1;
                        rem_pref.edit().putInt("Picture_count", message_count).apply();
                    }
                    else  if(message.contains("Video"))
                    {
                        int message_count = rem_pref.getInt("Video_count", 0) + 1;
                        rem_pref.edit().putInt("Video_count", message_count).apply();
                    }
                    else  if(message.contains("subscribed") || message.contains("Session"))
                    {
                        int message_count = rem_pref.getInt("Session_count", 0) + 1;
                        rem_pref.edit().putInt("Session_count", message_count).apply();
                    }
                    else  if(message.contains("followed") || message.contains("request"))
                    {
                        int message_count = rem_pref.getInt("Connect_count", 0) + 1;
                        rem_pref.edit().putInt("Connect_count", message_count).apply();
                    }



                }

                Intent i = new Intent(Util_Class.BROADCAST_REFRESH_DRAWER);
                sendBroadcast(i);


                if(rem_pref.getInt("message_count", 0) > 1 && rem_pref.getInt("notification_count", 0) == 0)
                {
                    notication_message = "" + rem_pref.getInt("message_count", 0) + " Messages ";
                }
                else if(rem_pref.getInt("message_count", 0) == 0 && rem_pref.getInt("notification_count", 0) > 1)
                {
                    notication_message = " " + rem_pref.getInt("notification_count", 0) + " Notifications ";
                }
                else if(rem_pref.getInt("message_count", 0) == 1 && rem_pref.getInt("notification_count", 0) == 1)
                {
                    notication_message = "1 Messages " + "1 Notifications ";

                }
                else if(rem_pref.getInt("message_count", 0) >= 1 && rem_pref.getInt("notification_count", 0) >= 1)
                {
                    notication_message = " " + rem_pref.getInt("message_count", 0) + " Messages " + rem_pref.getInt("notification_count", 0) + " Notifications";
                }

                generateNotification(context, notication_message);


            }

        }
        catch (Exception e)
        {
            Log.e(TAG, "Inside Exception onMessage -> " + e.toString());
        }
        catch (Error e)
        {
            e.printStackTrace();
        }
    }

    @Override
    protected
    void onDeletedMessages(Context context, int total)
    {
        Log.i(TAG, "Received deleted messages notification");
        String message = "";
        generateNotification(context, message);
    }

    @Override
    public
    void onError(Context context, String errorId)
    {
        Log.i(TAG, "Received error: " + errorId);

    }

    @Override
    protected
    boolean onRecoverableError(Context context, String errorId)
    {
        // log message
        Log.i(TAG, "Received recoverable error: " + errorId);
        return super.onRecoverableError(context, errorId);
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    private
    void generateNotification(Context context, String message)
    {
        int icon = R.drawable.app_logo;

        try
        {

            NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            String title = "Muser" + "";

            Intent notificationIntent = null;


            notificationIntent = new Intent(context, Drawer.class);

            if(flag == 3)
            {

                notificationIntent.setAction("Notification");
                //rem_pref.edit().putString("current_frag","Notification").commit();
            }
            else if(flag == 4)
            {
                notificationIntent.setAction("Messages");
                //rem_pref.edit().putString("current_frag","Messages").commit();
            }

            notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            PendingIntent intent = PendingIntent.getActivity(context, 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);


            Uri defaultSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

            if(defaultSound == null)
            {
                defaultSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE);
                if(defaultSound == null)
                {
                    defaultSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
                }
            }


            NotificationCompat.Builder builder = new NotificationCompat.Builder(context).setContentTitle(title).setContentText(message).setContentIntent(intent).setSmallIcon(icon)
                    .setLights(Color.MAGENTA, 1, 2).setAutoCancel(true).setSound(defaultSound);

            android.app.Notification not = new NotificationCompat.BigTextStyle(builder).bigText(message).build();

            if(defaultSound == null)
            {

            }
            else
            {
                not.defaults |= android.app.Notification.DEFAULT_VIBRATE;
                not.defaults |= android.app.Notification.DEFAULT_SOUND;
            }


            //100 for all except uploading
            notificationManager.notify(100, not);

        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        catch (Error e)
        {
            e.printStackTrace();
        }

    }
}
