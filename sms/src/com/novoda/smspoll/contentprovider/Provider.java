package com.novoda.smspoll.contentprovider;

import android.net.Uri;
import android.provider.BaseColumns;

public final class Provider {

	public static final String	DATABASE_NAME			= "smsPoll1.db";
	public static final int		DATABASE_VERSION	= 1;

	public static final class Poll implements BaseColumns {
		public static final Uri		CONTENT_URI	= Uri.parse("content://com.novoda.smspoll/polls");
		public static final String	QUESTION	= "question";
		public static final String	STARTDATE	= "startdate";
		public static final String	ENDDATE		= "enddate";
	}

}
