package com.example.Tabs;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ameba.muser.R;
import com.example.Adapter.Invite_Others_Twitter_Adapter;
import com.example.ProgressTask.Get_Twitter_Friends_Thread;
import com.example.classes.CannonballTwitterLoginButton;
import com.example.classes.CustomService;
import com.example.classes.EndlessListView;
import com.example.classes.Util_Class;
import com.twitter.sdk.android.Twitter;
import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.TwitterApiClient;
import com.twitter.sdk.android.core.TwitterAuthConfig;
import com.twitter.sdk.android.core.TwitterAuthToken;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.TwitterSession;
import com.twitter.sdk.android.core.models.User;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;

import io.fabric.sdk.android.Fabric;
import twitter4j.DirectMessage;
import twitter4j.TwitterFactory;
import twitter4j.conf.ConfigurationBuilder;

public
class Tab_Invite_Others_Twitter extends Fragment implements OnClickListener, EndlessListView.EndlessListener
{
    Tab_Invite_Others_Twitter con2;
    public EndlessListView twitter_listView;
    public boolean is_cjheck_all = false;
    public ArrayList<HashMap<String, String>> list;
    public static TextView total_contacts;
    public static CannonballTwitterLoginButton twitter;
    TextView invite_selected, invite;
    public ImageView check_all;
    LinearLayout layout;

    Context con;
    View rootView;
    boolean scroll = false;
    String twitter_username = "";

    public
    View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {

        if (savedInstanceState == null)
        {
            rootView = inflater.inflate(R.layout.tab_invite_other_twitter, container, false);
            con = getActivity();
            con2 = this;

            TwitterAuthConfig authConfig = new TwitterAuthConfig(Util_Class.TWITTER_KEY, Util_Class.TWITTER_SECRET);
            Fabric.with(con, new Twitter(authConfig));


            list = new ArrayList<>();
            twitter = (CannonballTwitterLoginButton) rootView.findViewById(R.id.twitter);
            //twitter_listView = (ListView) rootView.findViewById(R.id.listView);
            twitter_listView = (EndlessListView) rootView.findViewById(R.id.listView);
            twitter_listView.setListener(this);

            (invite = (TextView) rootView.findViewById(R.id.invite)).setOnClickListener(this);
            (invite_selected = (TextView) rootView.findViewById(R.id.invite_selected)).setOnClickListener(this);
            total_contacts = (TextView) rootView.findViewById(R.id.total_contacts);
            (check_all = (ImageView) rootView.findViewById(R.id.check_all)).setOnClickListener(this);
            layout = (LinearLayout) rootView.findViewById(R.id.layout);


            //layout.setVisibility(View.VISIBLE);
            //invite.setVisibility(View.GONE);
            //new Get_Twitter_Friends_Thread(con,"Ameba123");

            try
            {
                TwitterSession session = Twitter.getSessionManager().getActiveSession();
                Log.e("session.getUserName()", ".." + session.getUserName());

                if (session != null)
                {
                    if (!session.getUserName().equals(""))
                    {
                        layout.setVisibility(View.VISIBLE);
                        invite.setVisibility(View.GONE);
                        twitter_username = session.getUserName();
                        refresh();
                        // new Get_Twitter_Friends_Thread(con2, session.getUserName());
                    }
                }
                else
                {
                    layout.setVisibility(View.GONE);
                    invite.setVisibility(View.VISIBLE);
                }

            }
            catch (Exception e)
            {
                layout.setVisibility(View.GONE);
                invite.setVisibility(View.VISIBLE);

                e.printStackTrace();
            }

        }


        twitter.setCallback(new Callback<TwitterSession>()
        {
            @Override
            public
            void success(Result<TwitterSession> result)
            {
                Log.e("Twitter", "Success");
                // Log.e("Twitter",
                // ""+result.data.getUserName()+""+result.data.getUserId()+""+result.data.getUserName());
                TwitterSession session = Twitter.getSessionManager().getActiveSession();

                getTwitterData(session);
            }

            @Override
            public
            void failure(TwitterException exception)
            {
                Log.e("Twitter", "failure");
            }
        });

        return rootView;
    }


    public
    void refresh()
    {
        new Get_Twitter_Friends_Thread(con2, twitter_username, next_cursor);
    }


    Invite_Others_Twitter_Adapter adapter;
    String next_cursor = "-1";

    public
    void set_data(ArrayList<HashMap<String, String>> list, String next_cursor)
    {
        this.list = list;
        this.next_cursor = next_cursor;


        Log.e("twitter list", " oye    " + list.size());



        if (scroll == true)
        {
            twitter_listView.addNewData(list);
            scroll = false;
        }
        else
        {
            adapter = new Invite_Others_Twitter_Adapter(con, con2, list, false);
            twitter_listView.setAdapter(adapter);
        }

        if (list.size() == 0)
        {
            total_contacts.setText("0 contacts");
        }
        else
        {
            total_contacts.setText("" + adapter.list.size() + " contacts");
        }


    }


    @Override
    public
    void loadData()
    {
        //Log.e("Get_Virtual_Tour_ProgressTask.listsize%10", ""+list.size()%10);
        if (!next_cursor.equals("0"))
        {

            scroll = true;


            if (Util_Class.checknetwork(con))
            {
                refresh();
            }
            else
            {
                Util_Class.show_Toast("Internet is not available", con);
            }

        }
        else
        {
            Log.e("Twitter", "NO PAGE LEFT");
        }

    }


    @Override
    public
    void onClick(View v)
    {
        switch (v.getId())
        {
            case R.id.check_all:
                if (is_cjheck_all = !is_cjheck_all)
                {
                    check_all.setImageResource(R.drawable.check_box);
                    adapter = new Invite_Others_Twitter_Adapter(con, con2, list, true);
                    twitter_listView.setAdapter(adapter);
                }
                else
                {
                    check_all.setImageResource(R.drawable.uncheck_box);
                    adapter = new Invite_Others_Twitter_Adapter(con, con2, list, false);
                    twitter_listView.setAdapter(adapter);
                }
                break;

            case R.id.invite_selected:

                if (list.size() != 0 && adapter.get_selected_screen_name().size() != 0)
                {
                    new Send_messages().execute();
                }

                break;

            case R.id.invite:


                twitter.performClick();


                break;
            default:
                break;
        }

    }


    public
    class Send_messages extends AsyncTask<String, Void, Void>
    {


        protected
        void onPreExecute()
        {

        }

        @Override
        protected
        Void doInBackground(String... params)
        {
            for (int i = 0; i < adapter.get_selected_screen_name().size(); i++)
            {
                try
                {
                    postMessage(adapter.get_selected_screen_name().get(i));
                }
                catch (UnsupportedEncodingException | TwitterException | twitter4j.TwitterException e)
                {

                    e.printStackTrace();
                }
            }

            return null;
        }

        @Override
        protected
        void onPostExecute(Void result)
        {
            Util_Class.show_Toast("Message is successfully sent.", con);
            refresh_checkbox();
        }


    }

    public void refresh_checkbox()
    {
        adapter.do_refresh();
        twitter_listView.invalidateViews();
    }


    private
    void postMessage(String screen_name) throws UnsupportedEncodingException, TwitterException, twitter4j.TwitterException
    {

        ConfigurationBuilder cb = new ConfigurationBuilder();
        //  ameba
        /*cb.setDebugEnabled(true)
                .setOAuthConsumerKey("B3nRiGsngNy6q0ucSmBEasjqg")
                .setOAuthConsumerSecret("pYZbTq1G6b3hnRbUP2jmgZI7rZaRiFpxiK5Ys0tcFt9XjkKaiU")
                .setOAuthAccessToken("2919415273-XDcEjkFVnRESCkFsIgOpQH3sZWdCjuvzCsgqT3x")
                .setOAuthAccessTokenSecret("rYaYjKTUmjj9E78xcd4f12W0gcMtgybuwaBws2jqQeRxB");*/

        // client _MUSER__
        cb.setDebugEnabled(true)
                .setOAuthConsumerKey("FQr4TxIrANTAutbAvfxzIMaxQ")
                .setOAuthConsumerSecret("Dk9dYCjLCyNoSGr6j20mAQWfGQqB1smY6IPOM13rPARU68pp61")
                .setOAuthAccessToken("3149245877-NlEBjPFjeOHEey6vG0SsxGXN50gnS6zPxdQkbBh")
                .setOAuthAccessTokenSecret("WO30CXJOYxd7jVRcA53JJYLsSxozE00rY6cGcqTZMYEro");

        TwitterFactory tf = new TwitterFactory(cb.build());
        twitter4j.Twitter sender = tf.getInstance();

        DirectMessage message = sender.sendDirectMessage("@" + screen_name, "Join me on Muser ! - I'm on Muser. Install the app. https://play.google.com/store/apps/details?id=com.ameba.muser&hl=fr_CA");

        //Log.e("Message=====", "----------" + message);

    }


    public
    void getTwitterData(final TwitterSession session)
    {
        MyTwitterApiClient tapiclient = new MyTwitterApiClient(session);
        tapiclient.getCustomService().show(session.getUserId(), new Callback<User>()
        {
            @Override
            public
            void success(Result<User> result)
            {
                // Do something with result, which provides a Tweet
                // inside of result.data
                TwitterAuthToken authToken = session.getAuthToken();
                String token = authToken.token;
                String secret = authToken.secret;


                Log.e("result.data.screenName", "....." + result.data.screenName);
                // layout.setVisibility(View.VISIBLE);
                // invite.setVisibility(View.GONE);
                //new Get_Twitter_Friends_Thread(con2, result.data.screenName);


                if (!result.data.screenName.equals(""))
                {
                    layout.setVisibility(View.VISIBLE);
                    invite.setVisibility(View.GONE);
                    twitter_username = session.getUserName();
                    refresh();
                    // new Get_Twitter_Friends_Thread(con2, session.getUserName());
                }
            }

            public
            void failure(TwitterException exception)
            {
                // Do something on failure
            }
        });
    }

    class MyTwitterApiClient extends TwitterApiClient
    {
        public
        MyTwitterApiClient(TwitterSession session)
        {
            super(session);
        }

        public
        CustomService getCustomService()
        {
            return getService(CustomService.class);
        }
    }


}