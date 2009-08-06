package com.novoda.smspoll.contentprovider;

import android.net.Uri;
import android.provider.BaseColumns;

public final class Provider {

	public static final String	AUTHORITY			= "com.novoda.smspoll";
	public static final String	DATABASE_NAME		= "smsPoll.db";
	public static final int		DATABASE_VERSION	= 1;

	public static final class Poll implements BaseColumns {
		public static final Uri		CONTENT_URI	= Uri.parse("content://com.novoda.smspoll/polls");
		public static final String	QUESTION	= "question";
		public static final String	STARTDATE	= "startdate";
		public static final String	ENDDATE		= "enddate";
	}

	public static final class Answer implements BaseColumns {
		public static final Uri		CONTENT_URI	= Uri.parse("content://com.novoda.smspoll/polls/#/answers");
		public static final String	POLL_ID		= "_id_poll";
		public static final String	OPTION		= "option";
	}

	public static final class Respondant implements BaseColumns {
		public static final Uri		CONTENT_URI	= Uri.parse("content://com.novoda.smspoll/#/answers/#/respondants");
		public static final String	POLL_ID		= "_id_poll";
		public static final String	ANSWER_ID	= "_id_answer";
		public static final String	PHONE_NO	= "phone_no";
	}

}
