package com.novoda.smspoll;


import org.w3c.dom.Text;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.Contacts;
import android.telephony.gsm.SmsManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CursorAdapter;
import android.widget.EditText;
import android.widget.Filterable;
import android.widget.MultiAutoCompleteTextView;
import android.widget.TextView;
import android.widget.Toast;

public class SMS extends Activity {
	Button		btnSendSMS;
	EditText	txtPhoneNo;
	EditText	txtMessage;
	private Menu	mOptMenu; 	

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.sms);

		btnSendSMS = (Button) findViewById(R.id.btnSendSMS);
		txtPhoneNo = (EditText) findViewById(R.id.txtPhoneNo);
		txtMessage = (EditText) findViewById(R.id.txtMessage);

		btnSendSMS.setOnClickListener(getNewSendSmsListener()); 
		
	    Cursor peopleCursor = 	getContentResolver().query(Contacts.People.CONTENT_URI, PEOPLE_PROJECTION, null, null, Contacts.People.DEFAULT_SORT_ORDER);
	    ContactListAdapter contactadapter = new	ContactListAdapter(this,peopleCursor);
	    
		MultiAutoCompleteTextView textView = (MultiAutoCompleteTextView) findViewById(R.id.txtPhoneNo);
        textView.setAdapter(contactadapter);
        textView.setTokenizer(new MultiAutoCompleteTextView.CommaTokenizer());
    }


    public static class ContactListAdapter extends CursorAdapter implements Filterable {
        public ContactListAdapter(Context context, Cursor c) {
            super(context, c);
            mContent = context.getContentResolver();
        }

        @Override
        public View newView(Context context, Cursor cursor, ViewGroup parent) {
            final LayoutInflater inflater = LayoutInflater.from(context);
            final TextView view = (TextView) inflater.inflate(
                    android.R.layout.simple_dropdown_item_1line, parent, false);
            view.setText(cursor.getString(5));
            return view;
        }

        @Override
        public void bindView(View view, Context context, Cursor cursor) {
            ((TextView) view).setText(cursor.getString(5));
        }

        @Override
        public String convertToString(Cursor cursor) {
            return cursor.getString(5);
        }

        @Override
        public Cursor runQueryOnBackgroundThread(CharSequence constraint) {
            if (getFilterQueryProvider() != null) {
                return getFilterQueryProvider().runQuery(constraint);
            }

            StringBuilder buffer = null;
            String[] args = null;
            if (constraint != null) {
                buffer = new StringBuilder();
                buffer.append("UPPER(");
                buffer.append(Contacts.ContactMethods.NAME);
                buffer.append(") GLOB ?");
                args = new String[] { constraint.toString().toUpperCase() + "*" };
            }

            return mContent.query(Contacts.People.CONTENT_URI, PEOPLE_PROJECTION,
                    buffer == null ? null : buffer.toString(), args,
                    Contacts.People.DEFAULT_SORT_ORDER);
        }

        private ContentResolver mContent;        
    }

    private static final String[] PEOPLE_PROJECTION = new String[] {
        Contacts.People._ID,
        Contacts.People.PRIMARY_PHONE_ID,
        Contacts.People.TYPE,
        Contacts.People.NUMBER,
        Contacts.People.LABEL,
        Contacts.People.NAME,
    };
	private static final String	TAG	= null;
    

	private OnClickListener getNewSendSmsListener() {
		return new View.OnClickListener() {
			public void onClick(View v) {
				String phoneNo = txtPhoneNo.getText().toString();
				String message = txtMessage.getText().toString();

				if (pollIsValid(phoneNo, message))
					sendSMS(phoneNo, message);
				else
					Toast.makeText(getBaseContext(), "Please enter both phone number and message.", Toast.LENGTH_SHORT).show();
			}
		};
	}
	
	public boolean onCreateOptionsMenu(Menu menu) {
		mOptMenu = menu;

		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.sms_menu_actions, menu);
		return true;
	}
	public boolean onOptionsItemSelected(MenuItem item) {

		Intent intent = new Intent();
		switch (item.getItemId()) {
			case R.id.attachpoll:
				Log.v(TAG, "User selected to add a new Poll");
				intent.setClassName(getBaseContext(), "com.novoda.smspoll.Poll");
				startActivityForResult(intent, Constants.PICK_POLL_REQUEST);
				return true;
		}
		return false;
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
		if (requestCode == Constants.PICK_POLL_REQUEST && resultCode == RESULT_OK) {
			Log.v(TAG, "A message has been intercepted with contents[" + intent.getStringExtra(Constants.INTENT_DATA_SMS_CONTENTS) + "]");
			Log.d(TAG, "Intent URI[" + intent.toURI() + "]");
			txtMessage.setText(intent.getStringExtra(Constants.INTENT_DATA_SMS_CONTENTS));
			txtMessage.invalidate();
		}
	}

	private boolean pollIsValid(String phoneNo, String message) {
		return phoneNo.length() > 0 && message.length() > 0;
	}

	private void sendSMS(String phoneNumber, String message) {
//		registerReceiver(new SmsSentReciever(), new IntentFilter(Constants.SENT));
//		registerReceiver(new SmsDeliveredReciever(), new IntentFilter(Constants.DELIVERED));

		PendingIntent sentPI = PendingIntent.getBroadcast(this, 0, new Intent(Constants.SENT), 0);
		PendingIntent deliveredPI = PendingIntent.getBroadcast(this, 0, new Intent(Constants.DELIVERED), 0);
		
		SmsManager.getDefault().sendTextMessage(phoneNumber, null, message, sentPI, deliveredPI);
	}

}
