package com.ameba.muser;

import com.example.ProgressTask.Get_Funds_Thread;
import com.example.ProgressTask.Get_Toatal_Clients_Thread;
import com.example.ProgressTask.Withdraw_Funds_Thread;
import com.example.classes.Global;
import com.example.classes.Paypal_idea;
import com.example.classes.Util_Class;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

public class Wallet extends Fragment implements OnClickListener
{

	Context	con;
	static TextView balance_text;
	Wallet frag;
	SharedPreferences rem_pref;
	View v;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{

		if(savedInstanceState==null)
		{
			v = inflater.inflate(R.layout.wallet, container, false);

			con = getActivity();
			frag = this;

			rem_pref = con.getSharedPreferences("Remember", con.MODE_WORLD_READABLE);

			balance_text = (TextView) v.findViewById(R.id.balance);
			( v.findViewById(R.id.addFunds)).setOnClickListener(this);
			( v.findViewById(R.id.withdrawFunds)).setOnClickListener(this);
			( v.findViewById(R.id.train_with)).setOnClickListener(this);
			( v.findViewById(R.id.cancel_session)).setOnClickListener(this);
			( v.findViewById(R.id.total_clients)).setOnClickListener(this);
			( v.findViewById(R.id.insidefirstLinearlayout)).setOnClickListener(this);

		}
		
		return v;
	}
	
	@Override
	public void onResume()
	{
		super.onResume();
		
		System.out.println(rem_pref.getString("current_frag",""));
		if(rem_pref.getString("current_frag","").equals("Wallet"))
		{
			refresh();
		}
	}
	
	public void refresh()
	{
		new Get_Funds_Thread(con);
	}
	
	public static void set_balance(String balance)
	{
		balance_text.setText("$ "+balance);
	}

	EditText amount_edittext,withdraw_edittext;
	
	@Override
	public void onClick(View v)
	{
		switch (v.getId())
		{

			case R.id.insidefirstLinearlayout:

				startActivity(new Intent(con,Transaction_History.class));

				break;

			case R.id.addFunds:
				OnClickListener y = new OnClickListener()
				{
					@Override
					public void onClick(View v)
					{
//						InputMethodManager inputMethodManager = (InputMethodManager) con.getSystemService(con.INPUT_METHOD_SERVICE);
//						inputMethodManager.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
							

						if(!amount_edittext.getText().toString().isEmpty())
						{
							Util_Class.super_dialog.dismiss();
							
							Paypal_idea p = new Paypal_idea(con, amount_edittext.getText().toString());
							p.onBuyPressed();
							Global.setFund(amount_edittext.getText().toString());
						}
						else
						{
							Util_Class.show_Toast("Please enter amount", con);
						}

					}
				};

				amount_edittext = Util_Class.show_fragment_super_dialog(con, frag, y,"amount_edittext");
				break;
				
			case R.id.withdrawFunds:
				OnClickListener y1 = new OnClickListener()
				{
					@Override
					public void onClick(View v)
					{
						 if(getActivity().getCurrentFocus()!=null) 
						 {
						        InputMethodManager inputMethodManager = (InputMethodManager) getActivity().getSystemService(getActivity().INPUT_METHOD_SERVICE);
						        inputMethodManager.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), 0);
						 }
					
						String withdraw=withdraw_edittext.getText().toString();
						if(!withdraw.isEmpty())
						{
							Util_Class.super_dialog.dismiss();
							new Withdraw_Funds_Thread(con,frag,withdraw);
						}
						else
						{
							Util_Class.show_Toast("Please enter amount", con);
						}

					}
				};

				withdraw_edittext = Util_Class.show_fragment_super_dialog(con, frag, y1,"withdraw_edittext");
				
				break;
				
				
			case R.id.train_with:
				
				startActivity(new Intent(con,Trainers.class));
				
				break;
				
			case R.id.cancel_session:
				startActivity(new Intent(con,Cancel_Sessions.class));
				
				break;
				
			case R.id.total_clients:
				startActivity(new Intent(con,Total_Clients.class));
				
				break;

			default:
				break;
		}
		
	}
}
