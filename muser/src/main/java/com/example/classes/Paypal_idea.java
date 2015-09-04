package com.example.classes;

import java.math.BigDecimal;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import com.paypal.android.sdk.payments.PayPalConfiguration;
import com.paypal.android.sdk.payments.PayPalPayment;
import com.paypal.android.sdk.payments.PayPalService;
import com.paypal.android.sdk.payments.PaymentActivity;

public class Paypal_idea
{
	Context	con;

	String	Money	= "";

	public Paypal_idea(Context con , String Mone)
	{
		this.con = con;
		this.Money = Mone;

		Intent intent = new Intent(con, PayPalService.class);
		intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, config);
		con.startService(intent);

	}

	/**
	 * - Set to PayPalConfiguration.ENVIRONMENT_PRODUCTION to move real money.
	 * 
	 * - Set to PayPalConfiguration.ENVIRONMENT_SANDBOX to use your test
	 * credentials from https://developer.paypal.com
	 * 
	 * - Set to PayPalConfiguration.ENVIRONMENT_NO_NETWORK to kick the tires
	 * without communicating to PayPal's servers.
	 */
	private static final String			CONFIG_ENVIRONMENT		= PayPalConfiguration.ENVIRONMENT_PRODUCTION;												//
	// private static final String CONFIG_ENVIRONMENT =
	// PayPalConfiguration.ENVIRONMENT_SANDBOX;
	// note that these credentials will differ between live & sandbox
	// environments.Aebt13sE9p42WiyOZEciVAx8za_Krra33ovUC9Zk-MDvTzxEGlhoc7ot2vMVXR7zbFYaTdUeDTgPSHrK


	//Sandbox
	//private static final String			CONFIG_CLIENT_ID		= "AdcRM6yRRqIvVxOd9YIQWoT-uOBS7N__stIPWxQs-A-i6GWQr3mwIY8C0D3KsMioJvcopdmUujC5s1Lk";//"AVrKN71G-ToK3bs84YpJBCAe5ePKg_vr0_iqYJrRjt4JHqvfni3KwSeZv1CA4N-f5-gD-6dYqFnobMOa";//"Aebt13sE9p42WiyOZEciVAx8za_Krra33ovUC9Zk-MDvTzxEGlhoc7ot2vMVXR7zbFYaTdUeDTgPSHrK";


	//live
	private static final String			CONFIG_CLIENT_ID		= "ARdpTHNEy5VzO6S9u60Jq18C7eHNz_g9Taw3iBM7ofHA6czrzYbdAUQGgwzsnIvZp7XQiktMClneYg4q";


	private static final int			REQUEST_CODE_PAYMENT	= 1;

	private static PayPalConfiguration	config					= new PayPalConfiguration().environment(CONFIG_ENVIRONMENT).clientId(CONFIG_CLIENT_ID)

																.merchantName("MUSER").merchantPrivacyPolicyUri(Uri.parse("https://www.example.com/privacy"))
																.merchantUserAgreementUri(Uri.parse("https://www.example.com/legal"));

	public void onBuyPressed()
	{
		/*
		 * PAYMENT_INTENT_SALE will cause the payment to complete immediately.
		 * Change PAYMENT_INTENT_SALE to - PAYMENT_INTENT_AUTHORIZE to only
		 * authorize payment and capture funds later. - PAYMENT_INTENT_ORDER to
		 * create a payment for authorization and capture later via calls from
		 * your server.
		 * 
		 * Also, to include additional payment details and an item list, see
		 * getStuffToBuy() below.
		 */
		PayPalPayment thingToBuy = getThingToBuy(PayPalPayment.PAYMENT_INTENT_SALE);
		// PayPalPayment thingToBuy2 =
		// getStuffToBuy(PayPalPayment.PAYMENT_INTENT_SALE);

		/*
		 * See getStuffToBuy(..) for examples of some available payment options.
		 */

		// addAppProvidedShippingAddress(thingToBuy);

		// enableShippingAddressRetrieval(thingToBuy, true);

		Intent intent = new Intent(con, PaymentActivity.class);

		intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, config);

		intent.putExtra(PaymentActivity.EXTRA_PAYMENT, thingToBuy);

		((Activity) con).startActivityForResult(intent, REQUEST_CODE_PAYMENT);

	}

	private PayPalPayment getThingToBuy(String paymentIntent)
	{
		return new PayPalPayment(new BigDecimal(Money), "USD", "Pay", paymentIntent);
	}

	

}
