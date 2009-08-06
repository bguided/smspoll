package com.novoda.smspoll.contentprovider;

import java.io.IOException;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;
import android.util.Log;

import com.novoda.smspoll.R;
import com.novoda.util.SqlScanner;

public class ContentProvider extends android.content.ContentProvider {

	private static final String		LOGTAG	= "ContentProvider";
	private DatabaseHelper			mDatabaseHelper;

	private ContentProviderFactory	factory	= new ContentProviderFactory();

	@Override
	public boolean onCreate() {
		mDatabaseHelper = new DatabaseHelper(getContext());
		return true;
	}

	@Override
	public int delete(Uri uri, String selection, String[] selectionArgs) {
		return factory.getContentProvider(uri, this, mDatabaseHelper).delete(uri, selection, selectionArgs);
	}

	@Override
	public String getType(Uri uri) {
		return factory.getContentProvider(uri, this, mDatabaseHelper).getType(uri);
	}

	@Override
	public Uri insert(Uri uri, ContentValues values) {
		return factory.getContentProvider(uri, this, mDatabaseHelper).insert(uri, values);
	}

	@Override
	public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
		return factory.getContentProvider(uri, this, mDatabaseHelper).query(uri, projection, selection, selectionArgs, sortOrder);
	}

	@Override
	public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
		return factory.getContentProvider(uri, this, mDatabaseHelper).update(uri, values, selection, selectionArgs);
	}	
	
	
	private static class DatabaseHelper extends SQLiteOpenHelper {
		private Context	mContext;

		public DatabaseHelper(Context context) {
			super(context, Provider.DATABASE_NAME, null, Provider.DATABASE_VERSION);
			mContext = context;
		}

		@Override
		public void onCreate(SQLiteDatabase db) {
			SqlScanner scanner = new SqlScanner(mContext.getResources().openRawResource(R.raw.db_create));
			while (scanner.hasNext()) {
				String sql = scanner.next();
				Log.i(LOGTAG, sql);
				if (sql != null)
					db.execSQL(sql + ";");
			}
			try {
				scanner.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			Log.w(LOGTAG, "Upgrading database from version " + oldVersion + " to " + newVersion + ", which will destroy all old data");
			SqlScanner scanner = new SqlScanner(mContext.getResources().openRawResource(R.raw.db_drop));
			while (scanner.hasNext()) {
				db.execSQL(scanner.next());
			}
			try {
				scanner.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			onCreate(db);
		}
	}

}
