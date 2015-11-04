package com.ameba.muser;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.example.classes.Util_Class;

import org.appsroid.fxpro.PhotoActivity;
import org.appsroid.fxpro.library.Constants;

public class Dialog_Insert_Paypal extends Activity implements View.OnClickListener
{

    EditText edtxt_paypalid;

    Context con;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dialog_insert_paypal);


        con=this;

        findViewById(R.id.imgv_submit).setOnClickListener(this);

        edtxt_paypalid = (EditText) findViewById(R.id.edtxt_paypalid);

        edtxt_paypalid.setText(getIntent().getStringExtra("paypal_email"));


    }

    @Override
    public void onClick(View v)
    {

        switch (v.getId())
        {
            case R.id.imgv_submit:

                get_check();

                break;

            default:
                break;
        }

    }


    public void get_check()
    {

        if(edtxt_paypalid.getText().toString().trim().length()==0 )
        {
            Util_Class.show_Toast("Please enter paypal email.", con);
        }
        else if(!Util_Class.isValidEmail(edtxt_paypalid.getText().toString()))
        {
            Util_Class.show_Toast("Please enter valid paypal email.", con);
        }
        else
        {
            Intent intent = new Intent();
            intent.putExtra("paypal_email", edtxt_paypalid.getText().toString());
            setResult(55, intent);

            finish();

        }

    }
}
