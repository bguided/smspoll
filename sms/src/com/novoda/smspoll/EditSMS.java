package com.novoda.smspoll;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Contacts;
import android.provider.Contacts.People;
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
import android.widget.ImageButton;
import android.widget.MultiAutoCompleteTextView;
import android.widget.TextView;
import android.widget.Toast;

public class EditSMS extends Activity {
	private static final String	TAG	= null;
	Button						btnSendSMS;
	ImageButton					btnAddContacts;
	EditText					txtPhoneNo;
	EditText					txtMessage;
	private Menu				mOptMenu;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.edit_sms);

		btnSendSMS = (Button) findViewById(R.id.btnSendSMS);
		txtPhoneNo = (EditText) findViewById(R.id.txtPhoneNo);
		txtMessage = (EditText) findViewById(R.id.txtMessage);
		btnAddContacts = (ImageButton) findViewById(R.id.addContact);
		disableSendSMSButton();
		
		btnAddContacts.setOnClickListener(getNewAddContactsListener());

		Cursor peopleCursor = getContentResolver().query(Contacts.People.CONTENT_URI, Constants.PEOPLE_PROJECTION, null, null, Contacts.People.DEFAULT_SORT_ORDER);
		ContactListAdapter contactadapter = new ContactListAdapter(this, peopleCursor);

		MultiAutoCompleteTextView textView = (MultiAutoCompleteTextView) findViewById(R.id.txtPhoneNo);
		textView.setAdapter(contactadapter);
		textView.setTokenizer(new MultiAutoCompleteTextView.CommaTokenizer());
	}

	private void disableSendSMSButton() {
		btnSendSMS.setText("Attach Poll");		
		btnSendSMS.setOnClickListener(getNewAttachPollListener());
	}
	
	private void enableSendSMSButton() {
		btnSendSMS.setText(R.string.btn_send_poll);
		btnSendSMS.setOnClickListener(getNewSendSmsListener());
	}

	public boolean onCreateOptionsMenu(Menu menu) {
		mOptMenu = menu;

		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.sms_menu_actions, menu);
		return true;
	}

	public boolean onOptionsItemSelected(MenuItem item) {

		switch (item.getItemId()) {
			case R.id.attachpoll:
				return attachPoll();
		}
		return false;
	}

	private boolean attachPoll() {
		Log.v(TAG, "User selected to add a new Poll");
		Intent intent = new Intent();
		intent.setClassName(getBaseContext(), "com.novoda.smspoll.EditPoll");
		startActivityForResult(intent, Constants.PICK_POLL_REQUEST);
		return true;
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
		if (requestCode == Constants.PICK_POLL_REQUEST && resultCode == RESULT_OK) {
			Log.v(TAG, "A message has been intercepted with contents[" + intent.getStringExtra(Constants.INTENT_DATA_SMS_CONTENTS) + "]");
			Log.d(TAG, "Intent URI[" + intent.toURI() + "]");
			txtMessage.setText(intent.getStringExtra(Constants.INTENT_DATA_SMS_CONTENTS));
			txtMessage.invalidate();
		}

		if (requestCode == Constants.PICK_CONTACT && resultCode == RESULT_OK) {
			Uri contactData = intent.getData();
			Cursor c = managedQuery(contactData, null, null, null, null);
			if (c.moveToFirst()) {
				String name = c.getString(c.getColumnIndexOrThrow(People.NAME));
				String currentContents = txtPhoneNo.getText().toString();
				txtPhoneNo.setText(currentContents + "," + name + ",");
			}
		}

	}

	private OnClickListener getNewAddContactsListener() {
		return new View.OnClickListener() {
			public void onClick(View v) {
				Intent intent = new Intent(Intent.ACTION_PICK, People.CONTENT_URI);
				startActivityForResult(intent, Constants.PICK_CONTACT);
			}
		};
	}

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
	
	private OnClickListener getNewAttachPollListener() {
		return new View.OnClickListener() {
			public void onClick(View v) {
				attachPoll();
			}
		};
	}

	public static class ContactListAdapter extends CursorAdapter implements Filterable {
		public ContactListAdapter(Context context, Cursor c) {
			super(context, c);
			mContent = context.getContentResolver();
		}

		@Override
		public View newView(Context context, Cursor cursor, ViewGroup parent) {
			final LayoutInflater inflater = LayoutInflater.from(context);
			final TextView view = (TextView) inflater.inflate(android.R.layout.simple_dropdown_item_1line, parent, false);
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

			return mContent.query(Contacts.People.CONTENT_URI, Constants.PEOPLE_PROJECTION, buffer == null ? null : buffer.toString(), args, Contacts.People.DEFAULT_SORT_ORDER);
		}

		private ContentResolver	mContent;
	}

	private boolean pollIsValid(String phoneNo, String message) {
		return phoneNo.length() > 0 && message.length() > 0;
	}

	private void sendSMS(String phoneNumber, String message) {
		// registerReceiver(new SmsSentReciever(), new
		// IntentFilter(Constants.SENT));
		// registerReceiver(new SmsDeliveredReciever(), new
		// IntentFilter(Constants.DELIVERED));

		PendingIntent sentPI = PendingIntent.getBroadcast(this, 0, new Intent(Constants.SENT), 0);
		PendingIntent deliveredPI = PendingIntent.getBroadcast(this, 0, new Intent(Constants.DELIVERED), 0);

		SmsManager.getDefault().sendTextMessage(phoneNumber, null, message, sentPI, deliveredPI);
	}

}
