package com.example.Tabs;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.ProgressTask.Send_Invite_Email_Thread;
import com.example.classes.MyArray;
import com.ameba.muser.R;

public
class Tab_Invite_Others_Email extends Fragment implements OnClickListener
{
    ArrayList<MyArray> list;
    TextView total_contacts, invite_selected;
    ImageView check_all;
    Context con;
    ListView email_listview;


    boolean is_check_all = false;
    ArrayList<Boolean> check_list;
    View rootView;
    Fragment frag;

    public
    View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {

        if(savedInstanceState == null)
        {
            rootView = inflater.inflate(R.layout.tab_invite_others_email_contacts, container, false);

            con = getActivity();
            frag = this;

            total_contacts = (TextView) rootView.findViewById(R.id.total_contacts);
            email_listview = (ListView) rootView.findViewById(R.id.listView);
            (check_all = (ImageView) rootView.findViewById(R.id.check_all)).setOnClickListener(this);
            (invite_selected = (TextView) rootView.findViewById(R.id.invite_selected)).setOnClickListener(this);

            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB)
            {
                new Get_Email_ProgressTask().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
            }
            else
            {
                new Get_Email_ProgressTask().execute();
            }
        }

        return rootView;
    }


    @Override
    public
    void onClick(View v)
    {
        switch(v.getId())
        {
            case R.id.check_all:
                if(is_check_all = !is_check_all)
                {
                    check_all.setImageResource(R.drawable.check_box);
                    for(int i = 0; i < list.size(); i++)
                    {
                        check_list.set(i, true);
                    }
                    email_listview.invalidateViews();
                }
                else
                {
                    check_all.setImageResource(R.drawable.uncheck_box);
                    for(int i = 0; i < list.size(); i++)
                    {
                        check_list.set(i, false);
                    }
                    email_listview.invalidateViews();
                }


                break;

            case R.id.invite_selected:
                String email = "";
                for(int i = 0; i < list.size(); i++)
                {
                    if(check_list.get(i))
                        for(int j = 0; j < list.get(i).getEmails().size(); j++)
                        {

                            email += email.isEmpty() ? list.get(i).getEmails().get(j) : "," + list.get(i).getEmails().get(j);
                        }


                }
                if(!email.isEmpty())
                {
                    new Send_Invite_Email_Thread(con, frag, email);
                }
                break;


            default:
                break;
        }

    }

    public
    void refresh_checkbox()
    {
        for(int i = 0; i < list.size(); i++)
        {
            check_list.set(i, false);
        }
        email_listview.invalidateViews();
    }


    BaseAdapter adapter = new BaseAdapter()
    {


        @Override
        public
        View getView(final int position, View row, ViewGroup parent)
        {
            LayoutInflater inflater = (LayoutInflater) con.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = inflater.inflate(R.layout.custom_email_contacts, parent, false);

            TextView name = (TextView) row.findViewById(R.id.name);
            TextView phone = (TextView) row.findViewById(R.id.phone);
            final ImageView check = (ImageView) row.findViewById(R.id.check);

            name.setText(list.get(position).getName());
            ArrayList<String> email_list = list.get(position).getEmails();
            String emails = "";
            for(int i = 0; i < email_list.size(); i++)
            {
                emails += i == 0 ? email_list.get(i) : "\n" + email_list.get(i);
            }

            phone.setText(emails);


            row.setOnClickListener(new OnClickListener()
            {

                @Override
                public
                void onClick(View arg0)
                {

                    check_list.set(position, !check_list.get(position));

                    if(is_check_all)
                    {
                        is_check_all = !is_check_all;
                        check_all.setImageResource(R.drawable.uncheck_box);
                    }

                    if(check_list.get(position))
                    {
                        check.setImageResource(R.drawable.check_box);
                    }
                    else
                    {
                        check.setImageResource(R.drawable.uncheck_box);
                    }
                }

            });


            if(check_list.get(position))
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
        public
        long getItemId(int arg0)
        {
            // TODO Auto-generated method stub
            return 0;
        }

        @Override
        public
        Object getItem(int arg0)
        {
            // TODO Auto-generated method stub
            return null;
        }

        @Override
        public
        int getCount()
        {
            // TODO Auto-generated method stub
            return list.size();
        }
    };


    public
    class Get_Email_ProgressTask extends AsyncTask<Void, Void, Void>
    {

        @Override
        protected
        Void doInBackground(Void... params)
        {
            get_emails();
            return null;
        }

        @Override
        protected
        void onPostExecute(Void result)
        {

            super.onPostExecute(result);



            Collections.sort(list, new Comparator<MyArray>() {
                @Override
                public int compare(MyArray s1, MyArray s2) {
                    return s1.getName().compareToIgnoreCase(s2.getName());
                }
            });






        check_list=new ArrayList<Boolean>();
        for( int i = 0;i<list.size();i++)
        {
            check_list.add(is_check_all);
        }


        email_listview.setAdapter(adapter);
        total_contacts.setText(""+list.size()+" contacts");

    }

}


    public
    void get_emails()
    {
        list = new ArrayList<MyArray>();
        ContentResolver contactResolver = con.getContentResolver();

        //Cursor cursor = contactResolver.query(Uri.parse(aContact.getLookupUri()), null, null, null, null);
        Cursor cursor = contactResolver.query(ContactsContract.Contacts.CONTENT_URI, null, null, null, null);
        if(cursor.getCount() > 0 /*&& cursor.moveToFirst()*/)
        {


            while(cursor.moveToNext())
            {
                String displayName = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
                //String photoUri = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.PHOTO_THUMBNAIL_URI));
                String contactId = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID));
                //String lookupKey = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.LOOKUP_KEY));

                //String number="";
                if(Integer.parseInt(cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER))) > 0)
                {
                /*	Cursor pCur = contactResolver.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?", new String[] { contactId }, null);

					while (pCur.moveToNext())
					{
						String phone = pCur.getString(pCur.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
						//String type = pCur.getString(pCur.getColumnIndex(ContactsContract.CommonDataKinds.Phone.TYPE));
					//	String s = (String) ContactsContract.CommonDataKinds.Phone.getTypeLabel(con.getResources(), Integer.parseInt(type), "");
						//number=phone;
						//Log.i("TAG", s + " phone: " + phone);
					}

					pCur.close();*/


                    //String mail="";
                    Cursor emailCursor = contactResolver.query(ContactsContract.CommonDataKinds.Email.CONTENT_URI, null, ContactsContract.CommonDataKinds.Email.CONTACT_ID + " = ?", new String[]{contactId}, null);
                    ArrayList<String> emails = new ArrayList<String>();
                    while(emailCursor.moveToNext())
                    {
                        String phone = emailCursor.getString(emailCursor.getColumnIndex(ContactsContract.CommonDataKinds.Email.DATA));
                        //int type = emailCursor.getInt(emailCursor.getColumnIndex(ContactsContract.CommonDataKinds.Email.TYPE));
                        //String s = (String) ContactsContract.CommonDataKinds.Email.getTypeLabel(con.getResources(), type, "");
                        //	mail=phone;

                        emails.add(phone);
                        //Log.i("TAG", s + " email: " + phone);
                        //Log.e(displayName, number+"...."+mail);

                    }

                    if(emails.size() > 0)
                    {
                        list.add(new MyArray(displayName, emails));
                    }


                    emailCursor.close();

                }
            }

        }
        cursor.close();

        //	Log.i("List", ""+list);

    }


}