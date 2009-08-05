package com.novoda.smspoll.util;

import java.io.IOException;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.novoda.util.SqlScanner;

public class DBUtil {
	private Context				mContext;
	private SQLiteDatabase		mDB;

	public DBUtil(Context context, String dbName) {
		mContext = context;
		mDB = context.openOrCreateDatabase(dbName, Context.MODE_WORLD_WRITEABLE, null);
	}

	private Context getContext() {
		return mContext;
	}

	/**
	 * 
	 * This is a convenience method similar to {@link executeDB(int resId)}
	 * 
	 * @param resName
	 *            the name of the sql file in res
	 */
	public void executeDB(String resName) {
		executeDB(getContext().getResources().getIdentifier(resName, "raw", "com.novoda.smspoll"));
	}

	/**
	 * 
	 * Will execute the SQL file, line by line. There is no strict checking for
	 * the SQL so it is recommended to test the SQL file using external tools
	 * first. For instance the following tools can help ensure the SQL is
	 * correct:
	 * 
	 * <a href=http://sqlitebrowser.sourceforge.net/>SQLiteBrowser</a> <a
	 * href=https://addons.mozilla.org/en-US/firefox/addon/5817>FF addin for
	 * SQLite</a>
	 * 
	 * @param resId
	 *            the resource id (under raw/)
	 */
	public void executeDB(int resId) {
		SqlScanner insertDB = new SqlScanner(getContext().getResources().openRawResource(resId));
		mDB.beginTransaction();
		while (insertDB.hasNext()) {
			mDB.execSQL(insertDB.next());
		}
		mDB.setTransactionSuccessful();
		mDB.endTransaction();
		
		try {
			insertDB.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	public SQLiteDatabase getDatabase() {
		return mDB;
	}

}
