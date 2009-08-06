package com.novoda.smspoll.contentprovider;

import java.util.HashMap;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;

import com.novoda.smspoll.contentprovider.Provider.Answer;
import com.novoda.smspoll.contentprovider.Provider.Poll;
import com.novoda.smspoll.contentprovider.Provider.Respondant;

public class PollProvider extends ContentProvider {

	private static final String	TAG						= "SMS:";

	private static final String	POLL_TABLE_NAME			= "poll";
	private static final String	RESPONDANT_TABLE_NAME	= "respondant";
	private static final String	ANSWER_TABLE_NAME		= "answer";
	private static final String	DATABASE_NAME			= "smsPoll.db";

	private static final int	POLLS					= 0;
	private static final int	POLL_ID					= 1;
	private static final int	ANSWERS					= 2;
	private static final int	ANSWER_ID				= 3;

	/**
	 * This class helps open, create, and upgrade the database file.
	 */
	private static class DatabaseHelper extends SQLiteOpenHelper {

		private static final int	DATABASE_VERSION	= 1;

		DatabaseHelper(Context context, String name) {
			super(context, name, null, DATABASE_VERSION);
		}

		@Override
		public void onCreate(SQLiteDatabase db) {
			db.execSQL("CREATE TABLE " + POLL_TABLE_NAME + " (" + Poll._ID
					+ " INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, " + Poll.QUESTION + " TEXT,"
					+ Provider.Poll.STARTDATE + " DATE," + Poll.ENDDATE + " DATE" + ");");

			db.execSQL("CREATE TABLE " + ANSWER_TABLE_NAME + " (" + Answer._ID
					+ " INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, " + Answer.OPTION + " TEXT," + Answer.POLL_ID
					+ " INTEGER" + ");");

			db.execSQL("CREATE TABLE " + RESPONDANT_TABLE_NAME + " (" + Respondant._ID
					+ " INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, " + Respondant.ANSWER_ID + " INTEGER,"
					+ Respondant.POLL_ID + " INTEGER," + Respondant.PHONE_NO + " TEXT" + ");");
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			Log.w(TAG, "Upgrading database from version " + oldVersion + " to " + newVersion
					+ ", which will destroy all old data");
			db.execSQL("DROP TABLE IF EXISTS " + POLL_TABLE_NAME);
			db.execSQL("DROP TABLE IF EXISTS " + RESPONDANT_TABLE_NAME);
			db.execSQL("DROP TABLE IF EXISTS " + ANSWER_TABLE_NAME);
			onCreate(db);
		}
	}

	private static UriMatcher				sUriMatcher;

	private static HashMap<String, String>	sPollProjectionMap;

	private static HashMap<String, String>	sAnswerProjectionMap;

	private DatabaseHelper					mOpenHelper;

	@Override
	public boolean onCreate() {
		mOpenHelper = new DatabaseHelper(getContext(), DATABASE_NAME);
		return true;
	}

	@Override
	public int delete(Uri arg0, String arg1, String[] arg2) {
		return 0;
	}

	@Override
	public String getType(Uri uri) {
		return null;
	}

	@Override
	public Uri insert(Uri uri, ContentValues values) {
		return null;
	}

	@Override
	public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
		SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
		switch (sUriMatcher.match(uri)) {
			case POLLS:
				qb.setTables(POLL_TABLE_NAME);
				qb.setProjectionMap(sPollProjectionMap);
				break;

			case POLL_ID:
				qb.setTables(POLL_TABLE_NAME);
				qb.setProjectionMap(sPollProjectionMap);
				qb.appendWhere(Poll._ID + "=" + uri.getPathSegments().get(1));
				break;

			case ANSWERS:
				qb.setTables(ANSWER_TABLE_NAME);
				qb.setProjectionMap(sAnswerProjectionMap);
				break;
			case ANSWER_ID:
				qb.setTables(ANSWER_TABLE_NAME);
				qb.setProjectionMap(sAnswerProjectionMap);
				qb.appendWhere(Answer.POLL_ID + "=" + uri.getPathSegments().get(1) + " AND " + Answer._ID + "="
						+ uri.getPathSegments().get(3));
				break;

			default:
				throw new IllegalArgumentException("Unknown URI " + uri);
		}

		// Get the database and run the query
		SQLiteDatabase db = mOpenHelper.getReadableDatabase();
		Cursor c = qb.query(db, projection, selection, selectionArgs, null, null, null);
		c.setNotificationUri(getContext().getContentResolver(), uri);
		return c;
	}

	@Override
	public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
		return 0;
	}

	static {
		sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
		sUriMatcher.addURI(Provider.AUTHORITY, "polls", POLLS);
		sUriMatcher.addURI(Provider.AUTHORITY, "polls/#", POLL_ID);
		sUriMatcher.addURI(Provider.AUTHORITY, "polls/#/answers", ANSWERS);
		sUriMatcher.addURI(Provider.AUTHORITY, "polls/#/answers/#", ANSWER_ID);

		sPollProjectionMap = new HashMap<String, String>();
		sPollProjectionMap.put(Poll._ID, Poll._ID);
		sPollProjectionMap.put(Poll.ENDDATE, Poll.ENDDATE);
		sPollProjectionMap.put(Poll.QUESTION, Poll.QUESTION);
		sPollProjectionMap.put(Poll.STARTDATE, Poll.STARTDATE);

		sAnswerProjectionMap = new HashMap<String, String>();
		sAnswerProjectionMap.put(Answer._ID, Answer._ID);
		sAnswerProjectionMap.put(Answer.OPTION, Answer.OPTION);
		sAnswerProjectionMap.put(Answer.POLL_ID, Answer.POLL_ID);
	}

}
