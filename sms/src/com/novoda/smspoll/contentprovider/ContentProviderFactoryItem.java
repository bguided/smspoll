package com.novoda.smspoll.contentprovider;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;

public abstract class ContentProviderFactoryItem extends ContentProviderFactory {
	private volatile static ContentProvider		uContentProvider;
	protected volatile static SQLiteOpenHelper	mSQLiteOpenHelper;

	public abstract int delete(Uri uri, String selection, String[] selectionArgs);

	public abstract String getType(Uri uri);

	public abstract Uri insert(Uri uri, ContentValues values);

	public abstract Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder);

	public abstract int update(Uri uri, ContentValues values, String selection, String[] selectionArgs);
	
	public Context getContext() {
		return uContentProvider.getContext();
	}

	public SQLiteDatabase getReadableDatabase() {
		return mSQLiteOpenHelper.getReadableDatabase();
	}

	public SQLiteDatabase getWritableDatabase() {
		return mSQLiteOpenHelper.getWritableDatabase();
	}
	
	/*
	 * We ensure we only have one instance of the global content provider and db
	 * for all subclasses
	 */
	public ContentProviderFactoryItem(ContentProvider cp, SQLiteOpenHelper mDBe) {
		if (uContentProvider == null) {
			synchronized (ContentProviderFactoryItem.class) {
				if (uContentProvider == null) {
					uContentProvider = cp;
				}
			}
		}

		if (mSQLiteOpenHelper == null) {
			synchronized (ContentProviderFactoryItem.class) {
				if (mSQLiteOpenHelper == null) {
					mSQLiteOpenHelper = mDBe;
				}
			}
		}
	}

}
