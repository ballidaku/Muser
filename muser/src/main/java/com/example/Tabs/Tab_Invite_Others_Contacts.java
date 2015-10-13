package com.example.Tabs;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v4.app.Fragment;
import android.telephony.TelephonyManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.ProgressTask.Send_Invite_Contacts_Thread;
import com.ameba.muser.R;
import com.example.classes.MyArray;

public class Tab_Invite_Others_Contacts extends Fragment implements OnClickListener
{
    ListView contacts_listview;
    TextView total_contacts, invite_selected;
    ImageView check_all;

    ArrayList<HashMap<String, String>> list;
    Context                            con;
    boolean is_check_all = false;
    //ArrayList<Boolean> check_list;
    EditText           search_editText;

    View            rootView;
    Fragment        frag;
    Contact_Adapter adapter;
    ImageView       temp_logo;
    TextView        error_message;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {

        if (savedInstanceState == null)
        {
            rootView = inflater.inflate(R.layout.tab_invite_others_email_contacts, container, false);

            con = getActivity();
            frag = this;

            total_contacts = (TextView) rootView.findViewById(R.id.total_contacts);
            contacts_listview = (ListView) rootView.findViewById(R.id.listView);
            (check_all = (ImageView) rootView.findViewById(R.id.check_all)).setOnClickListener(this);
            (invite_selected = (TextView) rootView.findViewById(R.id.invite_selected)).setOnClickListener(this);

            search_editText = (EditText) rootView.findViewById(R.id.search_editText);

            list = new ArrayList<HashMap<String, String>>();

            temp_logo = (ImageView) rootView.findViewById(R.id.temp_logo);
            error_message = (TextView) rootView.findViewById(R.id.error_message);

            show_temp_logo();
            error_message.setText(con.getResources().getString(R.string.please_wait));

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB)
            {
                new Get_Contacts_ProgressTask().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
            }
            else
            {
                new Get_Contacts_ProgressTask().execute();
            }
        }

        search_editText.addTextChangedListener(new TextWatcher()
        {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after)
            {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count)
            {

                Log.e("count", "" + count);

                if(list.size()>0)
                {
                    ArrayList<HashMap<String, String>> tempList = new ArrayList<>();

                    for (HashMap<String, String> data : list)
                    {

                        try
                        {
                            if ((data.get("name").toLowerCase()).startsWith(s.toString().toLowerCase()))
                            {
                                tempList.add(data);
                            }
                        }
                        catch (Exception e)
                        {
                            Log.e("user_name", "" + data.get("name"));
                            e.printStackTrace();
                        }

                    }

                    if (count > 0)
                    {

                        show_logo_or_not(tempList.size());
                        adapter.add_data(tempList);
                        adapter.notifyDataSetChanged();
                    }
                    else
                    {
                        show_logo_or_not(list.size());

                        adapter.add_data(list);
                        adapter.notifyDataSetChanged();
                    }

                }

            }

            @Override
            public void afterTextChanged(Editable s)
            {

            }
        });

        return rootView;
    }

    @Override
    public void onClick(View v)
    {
        switch (v.getId())
        {
            case R.id.check_all://***********************************************************
                if (is_check_all = !is_check_all)
                {
                    check_all.setImageResource(R.drawable.check_box);
                    for (int i = 0; i < list.size(); i++)
                    {
                        //check_list.set(i, true);
                        list.get(i).put("is_checked", "yes");
                    }
                    contacts_listview.invalidateViews();
                }
                else
                {
                    check_all.setImageResource(R.drawable.uncheck_box);
                    for (int i = 0; i < list.size(); i++)
                    {
                        list.get(i).put("is_checked","no");
                        //check_list.set(i, false);
                    }
                    contacts_listview.invalidateViews();
                }

                break;

            case R.id.invite_selected://***********************************************************

                String contacts = "";
                for (int i = 0; i < list.size(); i++)
                {
                    if (list.get(i).get("is_checked").equals("yes"))
                    {
                        contacts += contacts.isEmpty() ? list.get(i).get("phoneNo") : "," + list.get(i).get("phoneNo");
                    }
                }
                Log.e("contacts", contacts);

                String code = "+" + GetCountryZipCode();

                Log.e("GetCountryZipCode", "" + code);

                if (!contacts.isEmpty())
                {
                    new Send_Invite_Contacts_Thread(con, frag, contacts, code);
                }

                break;

            default:
                break;
        }

    }

    public void refresh_checkbox()
    {
        for (int i = 0; i < list.size(); i++)
        {
            list.get(i).put("is_checked","no");
           // check_list.set(i, false);
        }
        contacts_listview.invalidateViews();
    }

    public String GetCountryZipCode()
    {
        String CountryID      = "";
        String CountryZipCode = "";

        TelephonyManager manager = (TelephonyManager) getActivity().getSystemService(Context.TELEPHONY_SERVICE);
        // getNetworkCountryIso
        CountryID = manager.getSimCountryIso().toUpperCase();
        String[] rl = this.getResources().getStringArray(R.array.CountryCodes);
        for (int i = 0; i < rl.length; i++)
        {
            String[] g = rl[i].split(",");
            if (g[1].trim().equals(CountryID.trim()))
            {
                CountryZipCode = g[0];
                break;
            }
        }
        return CountryZipCode;
    }

    class Contact_Adapter extends BaseAdapter
    {
        ArrayList<HashMap<String, String>> local_list;

        Contact_Adapter(ArrayList<HashMap<String, String>> list)
        {
            local_list = list;
        }

        @Override
        public View getView(final int position, View row, ViewGroup parent)
        {
            LayoutInflater inflater = (LayoutInflater) con.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = inflater.inflate(R.layout.custom_email_contacts, parent, false);

            TextView        name  = (TextView) row.findViewById(R.id.name);
            TextView        phone = (TextView) row.findViewById(R.id.phone);
            final ImageView check = (ImageView) row.findViewById(R.id.check);

            name.setText(local_list.get(position).get("name"));
            phone.setText(local_list.get(position).get("phoneNo"));

            row.setOnClickListener(new OnClickListener()
            {

                @Override
                public void onClick(View arg0)
                {

                    //check_list.set(position, !check_list.get(position)); ************************************************************

                    if (is_check_all)
                    {
                        is_check_all = !is_check_all;
                        check_all.setImageResource(R.drawable.uncheck_box);
                    }

                    if (local_list.get(position).get("is_checked").equals("no"))
                    {
                        local_list.get(position).put("is_checked","yes");
                        check.setImageResource(R.drawable.check_box);
                    }
                    else
                    {
                        local_list.get(position).put("is_checked","no");
                        check.setImageResource(R.drawable.uncheck_box);
                    }
                }

            });

            if (local_list.get(position).get("is_checked").equals("yes"))
            {
                check.setImageResource(R.drawable.check_box);
            }
            else
            {
                check.setImageResource(R.drawable.uncheck_box);
            }

            return row;
        }

        @Override
        public long getItemId(int arg0)
        {
            // TODO Auto-generated method stub
            return 0;
        }

        @Override
        public Object getItem(int arg0)
        {
            // TODO Auto-generated method stub
            return null;
        }

        @Override
        public int getCount()
        {
            // TODO Auto-generated method stub
            return local_list.size();
        }

        public void add_data(ArrayList<HashMap<String, String>> list)
        {
            local_list = list;
        }
    }

    ;

    public/*ArrayList<HashMap<String, String>>*/void get_contacts()
    {
        //ArrayList<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();
        ContentResolver cr  = getActivity().getContentResolver();
        Cursor          cur = cr.query(ContactsContract.Contacts.CONTENT_URI, null, null, null, null);
        if (cur.getCount() > 0)
        {
            while (cur.moveToNext())
            {
                String id = cur.getString(cur.getColumnIndex(ContactsContract.Contacts._ID));
                String name = cur.getString(cur.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
                if (Integer.parseInt(cur.getString(cur.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER))) > 0)
                {
                    Cursor pCur = cr.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?", new String[]{id}, null);
                    while (pCur.moveToNext())
                    {
                        String phoneNo = pCur.getString(pCur.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                        //Toast.makeText(NativeContentProvider.this, "Name: " + name + ", Phone No: " + phoneNo, Toast.LENGTH_SHORT).show();

                        HashMap<String, String> map = new HashMap<String, String>();
                        map.put("name", name);
                        map.put("phoneNo", phoneNo);
                        map.put("is_checked", "no");
                        list.add(map);

                    }
                    pCur.close();
                }
            }
            //			sorting list according to name
            Collections.sort(list, new Comparator<HashMap<String, String>>()
            {
                @Override
                public int compare(HashMap<String, String> text1, HashMap<String, String> text2)
                {
                    return text1.get("name").compareToIgnoreCase(text2.get("name"));
                }
            });

        }

        //	return list;

        //Log.e("Phone", ""+list);
    }

    public class Get_Contacts_ProgressTask extends AsyncTask<Void, Void, Void>
    {

        @Override
        protected Void doInBackground(Void... params)
        {
            get_contacts();
            return null;
        }

        @Override
        protected void onPostExecute(Void result)
        {

            super.onPostExecute(result);//***********************************************************************

           /* check_list = new ArrayList<Boolean>();
            for (int i = 0; i < list.size(); i++)
            {
                check_list.add(is_check_all);
            }*/

            total_contacts.setText("" + list.size() + " contacts");

            adapter = new Contact_Adapter(list);
            contacts_listview.setAdapter(adapter);

            show_logo_or_not(list.size());

        }
    }

    public void show_logo_or_not(int size)
    {
        if (size == 0)
        {
            on_Failure();
        }
        else
        {
            hide_temp_logo();
        }
    }

    public void on_Failure()
    {

        show_temp_logo();
        error_message.setText(con.getResources().getString(R.string.no_data_found));
    }

    private void show_temp_logo()
    {
        temp_logo.setVisibility(View.VISIBLE);
        error_message.setVisibility(View.VISIBLE);
        contacts_listview.setVisibility(View.GONE);

    }

    private void hide_temp_logo()
    {
        temp_logo.setVisibility(View.GONE);
        error_message.setVisibility(View.GONE);
        contacts_listview.setVisibility(View.VISIBLE);
    }

}