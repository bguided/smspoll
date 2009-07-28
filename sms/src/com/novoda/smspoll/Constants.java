package com.novoda.smspoll;

import android.provider.Contacts;

public class Constants {
	public static final String		SENT						= "SMS_SENT";
	public static final String		DELIVERED					= "SMS_DELIVERED";
	public static final int			PICK_POLL					= 555;
	public static final String		INTENT_DATA_SMS_CONTENTS	= "SMS_DATA";
	public static final int			PICK_POLL_REQUEST			= 2345678;
	public static final int			PICK_CONTACT				= 1;
	public static final String[]	PEOPLE_PROJECTION			= new String[] { Contacts.People._ID, Contacts.People.PRIMARY_PHONE_ID, Contacts.People.TYPE,
			Contacts.People.NUMBER, Contacts.People.LABEL, Contacts.People.NAME, };
}
