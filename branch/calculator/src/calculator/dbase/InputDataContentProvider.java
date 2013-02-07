/*
 ******************************************************************************
 * Parts of this code sample are licensed under Apache License, Version 2.0   *
 * Copyright (c) 2009, Android Open Handset Alliance. All rights reserved.    *
 *																			  *																			*
 * Except as noted, this code sample is offered under a modified BSD license. *
 * Copyright (C) 2010, Motorola Mobility, Inc. All rights reserved.           *
 * 																			  *
 * For more details, see MOTODEV_Studio_for_Android_LicenseNotices.pdf        * 
 * in your installation folder.                                               *
 ******************************************************************************
 */
package calculator.dbase;

import java.util.HashMap;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.text.TextUtils;

public class InputDataContentProvider extends ContentProvider {

	private DbHelper dbHelper;
	private static HashMap<String, String> INPUT_DATA_PROJECTION_MAP;
	private static final String TABLE_NAME = "input_data";
	private static final String AUTHORITY = "calculator.ru.inputdatacontentprovider";

	public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY
			+ "/" + TABLE_NAME);
	public static final Uri ID_FIELD_CONTENT_URI = Uri.parse("content://"
			+ AUTHORITY + "/" + TABLE_NAME.toLowerCase() + "/id");
	public static final Uri INPUTSUM_FIELD_CONTENT_URI = Uri.parse("content://"
			+ AUTHORITY + "/" + TABLE_NAME.toLowerCase() + "/inputsum");
	public static final Uri PERCENT_FIELD_CONTENT_URI = Uri.parse("content://"
			+ AUTHORITY + "/" + TABLE_NAME.toLowerCase() + "/percent");
	public static final Uri BEGINDATE_FIELD_CONTENT_URI = Uri
			.parse("content://" + AUTHORITY + "/" + TABLE_NAME.toLowerCase()
					+ "/begindate");
	public static final Uri PERIOD_FIELD_CONTENT_URI = Uri.parse("content://"
			+ AUTHORITY + "/" + TABLE_NAME.toLowerCase() + "/period");
	public static final Uri PAYTYPE_FIELD_CONTENT_URI = Uri.parse("content://"
			+ AUTHORITY + "/" + TABLE_NAME.toLowerCase() + "/paytype");

	public static final String DEFAULT_SORT_ORDER = "id ASC";

	private static final UriMatcher URL_MATCHER;

	private static final int INPUT_DATA = 1;
	private static final int INPUT_DATA_ID = 2;
	private static final int INPUT_DATA_INPUTSUM = 3;
	private static final int INPUT_DATA_PERCENT = 4;
	private static final int INPUT_DATA_BEGINDATE = 5;
	private static final int INPUT_DATA_PERIOD = 6;
	private static final int INPUT_DATA_PAYTYPE = 7;

	// Content values keys (using column names)
	public static final String ID = "id";
	public static final String INPUTSUM = "inputSum";
	public static final String PERCENT = "percent";
	public static final String BEGINDATE = "beginDate";
	public static final String PERIOD = "period";
	public static final String PAYTYPE = "payType";

	public boolean onCreate() {
		dbHelper = new DbHelper(getContext(), true);
		return (dbHelper == null) ? false : true;
	}

	public Cursor query(Uri url, String[] projection, String selection,
			String[] selectionArgs, String sort) {
		SQLiteDatabase mDB = dbHelper.getReadableDatabase();
		SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
		switch (URL_MATCHER.match(url)) {
		case INPUT_DATA:
			qb.setTables(TABLE_NAME);
			qb.setProjectionMap(INPUT_DATA_PROJECTION_MAP);
			break;
		case INPUT_DATA_ID:
			qb.setTables(TABLE_NAME);
			qb.appendWhere("id='" + url.getPathSegments().get(2) + "'");
			break;
		case INPUT_DATA_INPUTSUM:
			qb.setTables(TABLE_NAME);
			qb.appendWhere("inputsum='" + url.getPathSegments().get(2) + "'");
			break;
		case INPUT_DATA_PERCENT:
			qb.setTables(TABLE_NAME);
			qb.appendWhere("percent='" + url.getPathSegments().get(2) + "'");
			break;
		case INPUT_DATA_BEGINDATE:
			qb.setTables(TABLE_NAME);
			qb.appendWhere("begindate='" + url.getPathSegments().get(2) + "'");
			break;
		case INPUT_DATA_PERIOD:
			qb.setTables(TABLE_NAME);
			qb.appendWhere("period='" + url.getPathSegments().get(2) + "'");
			break;
		case INPUT_DATA_PAYTYPE:
			qb.setTables(TABLE_NAME);
			qb.appendWhere("paytype='" + url.getPathSegments().get(2) + "'");
			break;

		default:
			throw new IllegalArgumentException("Unknown URL " + url);
		}
		String orderBy = "";
		if (TextUtils.isEmpty(sort)) {
			orderBy = DEFAULT_SORT_ORDER;
		} else {
			orderBy = sort;
		}
		Cursor c = qb.query(mDB, projection, selection, selectionArgs, null,
				null, orderBy);
		c.setNotificationUri(getContext().getContentResolver(), url);
		return c;
	}

	public String getType(Uri url) {
		switch (URL_MATCHER.match(url)) {
		case INPUT_DATA:
			return "vnd.android.cursor.dir/vnd.calculator.ru.input_data";
		case INPUT_DATA_ID:
			return "vnd.android.cursor.item/vnd.calculator.ru.input_data";
		case INPUT_DATA_INPUTSUM:
			return "vnd.android.cursor.item/vnd.calculator.ru.input_data";
		case INPUT_DATA_PERCENT:
			return "vnd.android.cursor.item/vnd.calculator.ru.input_data";
		case INPUT_DATA_BEGINDATE:
			return "vnd.android.cursor.item/vnd.calculator.ru.input_data";
		case INPUT_DATA_PERIOD:
			return "vnd.android.cursor.item/vnd.calculator.ru.input_data";
		case INPUT_DATA_PAYTYPE:
			return "vnd.android.cursor.item/vnd.calculator.ru.input_data";

		default:
			throw new IllegalArgumentException("Unknown URL " + url);
		}
	}

	public Uri insert(Uri url, ContentValues initialValues) {
		SQLiteDatabase mDB = dbHelper.getWritableDatabase();
		long rowID;
		ContentValues values;
		if (initialValues != null) {
			values = new ContentValues(initialValues);
		} else {
			values = new ContentValues();
		}
		if (URL_MATCHER.match(url) != INPUT_DATA) {
			throw new IllegalArgumentException("Unknown URL " + url);
		}

		rowID = mDB.insert("input_data", "input_data", values);
		if (rowID > 0) {
			Uri uri = ContentUris.withAppendedId(CONTENT_URI, rowID);
			getContext().getContentResolver().notifyChange(uri, null);
			return uri;
		}
		throw new SQLException("Failed to insert row into " + url);
	}

	public int delete(Uri url, String where, String[] whereArgs) {
		SQLiteDatabase mDB = dbHelper.getWritableDatabase();
		int count;
		String segment = "";
		switch (URL_MATCHER.match(url)) {
		case INPUT_DATA:
			count = mDB.delete(TABLE_NAME, where, whereArgs);
			break;
		case INPUT_DATA_ID:
			segment = "'" + url.getPathSegments().get(2) + "'";
			count = mDB.delete(TABLE_NAME,
					"id="
							+ segment
							+ (!TextUtils.isEmpty(where) ? " AND (" + where
									+ ')' : ""), whereArgs);
			break;
		case INPUT_DATA_INPUTSUM:
			segment = "'" + url.getPathSegments().get(2) + "'";
			count = mDB.delete(TABLE_NAME,
					"inputsum="
							+ segment
							+ (!TextUtils.isEmpty(where) ? " AND (" + where
									+ ')' : ""), whereArgs);
			break;
		case INPUT_DATA_PERCENT:
			segment = "'" + url.getPathSegments().get(2) + "'";
			count = mDB.delete(TABLE_NAME,
					"percent="
							+ segment
							+ (!TextUtils.isEmpty(where) ? " AND (" + where
									+ ')' : ""), whereArgs);
			break;
		case INPUT_DATA_BEGINDATE:
			segment = "'" + url.getPathSegments().get(2) + "'";
			count = mDB.delete(TABLE_NAME,
					"begindate="
							+ segment
							+ (!TextUtils.isEmpty(where) ? " AND (" + where
									+ ')' : ""), whereArgs);
			break;
		case INPUT_DATA_PERIOD:
			segment = "'" + url.getPathSegments().get(2) + "'";
			count = mDB.delete(TABLE_NAME,
					"period="
							+ segment
							+ (!TextUtils.isEmpty(where) ? " AND (" + where
									+ ')' : ""), whereArgs);
			break;
		case INPUT_DATA_PAYTYPE:
			segment = "'" + url.getPathSegments().get(2) + "'";
			count = mDB.delete(TABLE_NAME,
					"paytype="
							+ segment
							+ (!TextUtils.isEmpty(where) ? " AND (" + where
									+ ')' : ""), whereArgs);
			break;

		default:
			throw new IllegalArgumentException("Unknown URL " + url);
		}
		getContext().getContentResolver().notifyChange(url, null);
		return count;
	}

	public int update(Uri url, ContentValues values, String where,
			String[] whereArgs) {
		SQLiteDatabase mDB = dbHelper.getWritableDatabase();
		int count;
		String segment = "";
		switch (URL_MATCHER.match(url)) {
		case INPUT_DATA:
			count = mDB.update(TABLE_NAME, values, where, whereArgs);
			break;
		case INPUT_DATA_ID:
			segment = "'" + url.getPathSegments().get(2) + "'";
			count = mDB.update(TABLE_NAME, values,
					"id="
							+ segment
							+ (!TextUtils.isEmpty(where) ? " AND (" + where
									+ ')' : ""), whereArgs);
			break;
		case INPUT_DATA_INPUTSUM:
			segment = "'" + url.getPathSegments().get(2) + "'";
			count = mDB.update(TABLE_NAME, values,
					"inputsum="
							+ segment
							+ (!TextUtils.isEmpty(where) ? " AND (" + where
									+ ')' : ""), whereArgs);
			break;
		case INPUT_DATA_PERCENT:
			segment = "'" + url.getPathSegments().get(2) + "'";
			count = mDB.update(TABLE_NAME, values,
					"percent="
							+ segment
							+ (!TextUtils.isEmpty(where) ? " AND (" + where
									+ ')' : ""), whereArgs);
			break;
		case INPUT_DATA_BEGINDATE:
			segment = "'" + url.getPathSegments().get(2) + "'";
			count = mDB.update(TABLE_NAME, values,
					"begindate="
							+ segment
							+ (!TextUtils.isEmpty(where) ? " AND (" + where
									+ ')' : ""), whereArgs);
			break;
		case INPUT_DATA_PERIOD:
			segment = "'" + url.getPathSegments().get(2) + "'";
			count = mDB.update(TABLE_NAME, values,
					"period="
							+ segment
							+ (!TextUtils.isEmpty(where) ? " AND (" + where
									+ ')' : ""), whereArgs);
			break;
		case INPUT_DATA_PAYTYPE:
			segment = "'" + url.getPathSegments().get(2) + "'";
			count = mDB.update(TABLE_NAME, values,
					"paytype="
							+ segment
							+ (!TextUtils.isEmpty(where) ? " AND (" + where
									+ ')' : ""), whereArgs);
			break;

		default:
			throw new IllegalArgumentException("Unknown URL " + url);
		}
		getContext().getContentResolver().notifyChange(url, null);
		return count;
	}

	static {
		URL_MATCHER = new UriMatcher(UriMatcher.NO_MATCH);
		URL_MATCHER.addURI(AUTHORITY, TABLE_NAME.toLowerCase(), INPUT_DATA);
		URL_MATCHER.addURI(AUTHORITY, TABLE_NAME.toLowerCase() + "/id" + "/*",
				INPUT_DATA_ID);
		URL_MATCHER.addURI(AUTHORITY, TABLE_NAME.toLowerCase() + "/inputsum"
				+ "/*", INPUT_DATA_INPUTSUM);
		URL_MATCHER.addURI(AUTHORITY, TABLE_NAME.toLowerCase() + "/percent"
				+ "/*", INPUT_DATA_PERCENT);
		URL_MATCHER.addURI(AUTHORITY, TABLE_NAME.toLowerCase() + "/begindate"
				+ "/*", INPUT_DATA_BEGINDATE);
		URL_MATCHER.addURI(AUTHORITY, TABLE_NAME.toLowerCase() + "/period"
				+ "/*", INPUT_DATA_PERIOD);
		URL_MATCHER.addURI(AUTHORITY, TABLE_NAME.toLowerCase() + "/paytype"
				+ "/*", INPUT_DATA_PAYTYPE);

		INPUT_DATA_PROJECTION_MAP = new HashMap<String, String>();
		INPUT_DATA_PROJECTION_MAP.put(ID, "id");
		INPUT_DATA_PROJECTION_MAP.put(INPUTSUM, "inputsum");
		INPUT_DATA_PROJECTION_MAP.put(PERCENT, "percent");
		INPUT_DATA_PROJECTION_MAP.put(BEGINDATE, "begindate");
		INPUT_DATA_PROJECTION_MAP.put(PERIOD, "period");
		INPUT_DATA_PROJECTION_MAP.put(PAYTYPE, "paytype");

	}
}
