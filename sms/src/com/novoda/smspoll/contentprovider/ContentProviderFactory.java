package com.novoda.smspoll.contentprovider;

import android.content.ContentProvider;
import android.content.UriMatcher;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;

public class ContentProviderFactory {

	// URI Matcher information
	public static final String		URI_BASE				= "com.novoda.smspoll";
	public static final UriMatcher	URI_MATCHER				= new UriMatcher(UriMatcher.NO_MATCH);

	public static final int			POLLS					= 0;
	public static final int			POLL_ID					= 1;
	public static final int			POLL_QUESTION			= 2;
	public static final int			POLL_START_DATE			= 3;
	public static final int			POLL_END_DATE			= 4;

	static {
		URI_MATCHER.addURI(URI_BASE, "polls", POLLS);
		URI_MATCHER.addURI(URI_BASE, "polls/#", POLL_ID);
		URI_MATCHER.addURI(URI_BASE, "polls/#/question", POLL_QUESTION);
		URI_MATCHER.addURI(URI_BASE, "polls/#/startdate", POLL_START_DATE);
		URI_MATCHER.addURI(URI_BASE, "polls/#/enddate", POLL_END_DATE);

	}

	public ContentProviderFactoryItem getContentProvider(Uri uri, ContentProvider cp, SQLiteOpenHelper mOpenHelper) {
		int match = URI_MATCHER.match(uri);
		switch (match) {
			case POLLS:
				return new PollsProvider(cp, mOpenHelper);

			default:
				throw new IllegalArgumentException("Unknown URL " + uri);
		}
	}
}