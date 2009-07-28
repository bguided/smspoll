package com.novoda.smspoll;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.gsm.SmsMessage;
import android.widget.Toast;

public class SmsReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {

		Bundle bundle = intent.getExtras();
		SmsMessage[] msgs = null;
		String str = "";
		String answer = "";

		if (bundle != null) {
			Object[] pdus = (Object[]) bundle.get("pdus");
			msgs = new SmsMessage[pdus.length];

			for (int i = 0; i < msgs.length; i++) {
				msgs[i] = SmsMessage.createFromPdu((byte[]) pdus[i]);
				str += "SMS from " + msgs[i].getOriginatingAddress();
				str += " :";
				str += msgs[i].getMessageBody().toString();
				str += "\n";
			}
			 
			if(str.contains("#")){
				int hashIndex = str.indexOf("#");
				String everythingAfterHash = str.substring(hashIndex);
			    Matcher matcher = Pattern.compile("[1..9]").matcher(everythingAfterHash);

			    if(matcher.matches()){
			    	answer = Character.toString(everythingAfterHash.charAt(matcher.regionStart()));
			    }
			}

			Toast.makeText(context, "Someone replied with answer:["+ answer +"], Answer 2 is in the lead" , Toast.LENGTH_SHORT).show();
		}
	}
}
