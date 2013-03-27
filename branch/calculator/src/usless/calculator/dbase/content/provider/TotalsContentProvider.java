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
package usless.calculator.dbase.content.provider;

import java.util.HashMap;

import calculator.dbase.DbHelper;

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

public class TotalsContentProvider extends ContentProvider {

	private DbHelper dbHelper;
	private static HashMap<String, String> TOTALS_PROJECTION_MAP;
	private static final String TABLE_NAME = "totals";
	private static final String AUTHORITY = "calculator.dbase.totalscontentprovider";

	public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY
			+ "/" + TABLE_NAME);
	public static final Uri ID_FIELD_CONTENT_URI = Uri.parse("content://"
			+ AUTHORITY + "/" + TABLE_NAME.toLowerCase() + "/id");
	public static final Uri ID_CALC_FIELD_CONTENT_URI = Uri.parse("content://"
			+ AUTHORITY + "/" + TABLE_NAME.toLowerCase() + "/id_calc");
	public static final Uri SUMPAYS_FIELD_CONTENT_URI = Uri.parse("content://"
			+ AUTHORITY + "/" + TABLE_NAME.toLowerCase() + "/sumpays");
	public static final Uri SUMPERCENTPAYS_FIELD_CONTENT_URI = Uri
			.parse("content://" + AUTHORITY + "/" + TABLE_NAME.toLowerCase()
					+ "/sumpercentpays");
	public static final Uri SUMPAYFEE_FIELD_CONTENT_URI = Uri
			.parse("content://" + AUTHORITY + "/" + TABLE_NAME.toLowerCase()
					+ "/sumpayfee");

	public static final String DEFAULT_SORT_ORDER = "id ASC";

	private static final UriMatcher URL_MATCHER;

	private static final int TOTALS = 1;
	private static final int TOTALS_ID = 2;
	private static final int TOTALS_ID_CALC = 3;
	private static final int TOTALS_SUMPAYS = 4;
	private static final int TOTALS_SUMPERCENTPAYS = 5;
	private static final int TOTALS_SUMPAYFEE = 6;

	// Content values keys (using column names)
	public static final String ID = "id";
	public static final String ID_CALC = "id_calc";
	public static final String SUMPAYS = "sumPays";
	public static final String SUMPERCENTPAYS = "sumPercentPays";
	public static final String SUMPAYFEE = "sumPayFee";

	public boolean onCreate() {
		dbHelper = new DbHelper(getContext(), true);
		return (dbHelper == null) ? false : true;
	}

	public Cursor query(Uri url, String[] projection, String selection,
			String[] selectionArgs, String sort) {
//		SQLiteDatabase mDB = dbHelper.getReadableDatabase();
		SQLiteDatabase mDB = dbHelper.getWritableDatabase();
		SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
		switch (URL_MATCHER.match(url)) {
		case TOTALS:
			qb.setTables(TABLE_NAME);
			qb.setProjectionMap(TOTALS_PROJECTION_MAP);
			break;
		case TOTALS_ID:
			qb.setTables(TABLE_NAME);
			qb.appendWhere("id='" + url.getPathSegments().get(2) + "'");
			break;
		case TOTALS_ID_CALC:
			qb.setTables(TABLE_NAME);
			qb.appendWhere("id_calc='" + url.getPathSegments().get(2) + "'");
			break;
		case TOTALS_SUMPAYS:
			qb.setTables(TABLE_NAME);
			qb.appendWhere("sumpays='" + url.getPathSegments().get(2) + "'");
			break;
		case TOTALS_SUMPERCENTPAYS:
			qb.setTables(TABLE_NAME);
			qb.appendWhere("sumpercentpays='" + url.getPathSegments().get(2)
					+ "'");
			break;
		case TOTALS_SUMPAYFEE:
			qb.setTables(TABLE_NAME);
			qb.appendWhere("sumpayfee='" + url.getPathSegments().get(2) + "'");
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
		case TOTALS:
			return "vnd.android.cursor.dir/vnd.calculator.dbase.totals";
		case TOTALS_ID:
			return "vnd.android.cursor.item/vnd.calculator.dbase.totals";
		case TOTALS_ID_CALC:
			return "vnd.android.cursor.item/vnd.calculator.dbase.totals";
		case TOTALS_SUMPAYS:
			return "vnd.android.cursor.item/vnd.calculator.dbase.totals";
		case TOTALS_SUMPERCENTPAYS:
			return "vnd.android.cursor.item/vnd.calculator.dbase.totals";
		case TOTALS_SUMPAYFEE:
			return "vnd.android.cursor.item/vnd.calculator.dbase.totals";

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
		if (URL_MATCHER.match(url) != TOTALS) {
			throw new IllegalArgumentException("Unknown URL " + url);
		}

		rowID = mDB.insert("totals", "totals", values);
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
		case TOTALS:
			count = mDB.delete(TABLE_NAME, where, whereArgs);
			break;
		case TOTALS_ID:
			segment = "'" + url.getPathSegments().get(2) + "'";
			count = mDB.delete(TABLE_NAME,
					"id="
							+ segment
							+ (!TextUtils.isEmpty(where) ? " AND (" + where
									+ ')' : ""), whereArgs);
			break;
		case TOTALS_ID_CALC:
			segment = "'" + url.getPathSegments().get(2) + "'";
			count = mDB.delete(TABLE_NAME,
					"id_calc="
							+ segment
							+ (!TextUtils.isEmpty(where) ? " AND (" + where
									+ ')' : ""), whereArgs);
			break;
		case TOTALS_SUMPAYS:
			segment = "'" + url.getPathSegments().get(2) + "'";
			count = mDB.delete(TABLE_NAME,
					"sumpays="
							+ segment
							+ (!TextUtils.isEmpty(where) ? " AND (" + where
									+ ')' : ""), whereArgs);
			break;
		case TOTALS_SUMPERCENTPAYS:
			segment = "'" + url.getPathSegments().get(2) + "'";
			count = mDB.delete(TABLE_NAME,
					"sumpercentpays="
							+ segment
							+ (!TextUtils.isEmpty(where) ? " AND (" + where
									+ ')' : ""), whereArgs);
			break;
		case TOTALS_SUMPAYFEE:
			segment = "'" + url.getPathSegments().get(2) + "'";
			count = mDB.delete(TABLE_NAME,
					"sumpayfee="
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
		case TOTALS:
			count = mDB.update(TABLE_NAME, values, where, whereArgs);
			break;
		case TOTALS_ID:
			segment = "'" + url.getPathSegments().get(2) + "'";
			count = mDB.update(TABLE_NAME, values,
					"id="
							+ segment
							+ (!TextUtils.isEmpty(where) ? " AND (" + where
									+ ')' : ""), whereArgs);
			break;
		case TOTALS_ID_CALC:
			segment = "'" + url.getPathSegments().get(2) + "'";
			count = mDB.update(TABLE_NAME, values,
					"id_calc="
							+ segment
							+ (!TextUtils.isEmpty(where) ? " AND (" + where
									+ ')' : ""), whereArgs);
			break;
		case TOTALS_SUMPAYS:
			segment = "'" + url.getPathSegments().get(2) + "'";
			count = mDB.update(TABLE_NAME, values,
					"sumpays="
							+ segment
							+ (!TextUtils.isEmpty(where) ? " AND (" + where
									+ ')' : ""), whereArgs);
			break;
		case TOTALS_SUMPERCENTPAYS:
			segment = "'" + url.getPathSegments().get(2) + "'";
			count = mDB.update(TABLE_NAME, values,
					"sumpercentpays="
							+ segment
							+ (!TextUtils.isEmpty(where) ? " AND (" + where
									+ ')' : ""), whereArgs);
			break;
		case TOTALS_SUMPAYFEE:
			segment = "'" + url.getPathSegments().get(2) + "'";
			count = mDB.update(TABLE_NAME, values,
					"sumpayfee="
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
		URL_MATCHER.addURI(AUTHORITY, TABLE_NAME.toLowerCase(), TOTALS);
		URL_MATCHER.addURI(AUTHORITY, TABLE_NAME.toLowerCase() + "/id" + "/*",
				TOTALS_ID);
		URL_MATCHER.addURI(AUTHORITY, TABLE_NAME.toLowerCase() + "/id_calc"
				+ "/*", TOTALS_ID_CALC);
		URL_MATCHER.addURI(AUTHORITY, TABLE_NAME.toLowerCase() + "/sumpays"
				+ "/*", TOTALS_SUMPAYS);
		URL_MATCHER.addURI(AUTHORITY, TABLE_NAME.toLowerCase()
				+ "/sumpercentpays" + "/*", TOTALS_SUMPERCENTPAYS);
		URL_MATCHER.addURI(AUTHORITY, TABLE_NAME.toLowerCase() + "/sumpayfee"
				+ "/*", TOTALS_SUMPAYFEE);

		TOTALS_PROJECTION_MAP = new HashMap<String, String>();
		TOTALS_PROJECTION_MAP.put(ID, "id");
		TOTALS_PROJECTION_MAP.put(ID_CALC, "id_calc");
		TOTALS_PROJECTION_MAP.put(SUMPAYS, "sumpays");
		TOTALS_PROJECTION_MAP.put(SUMPERCENTPAYS, "sumpercentpays");
		TOTALS_PROJECTION_MAP.put(SUMPAYFEE, "sumpayfee");

	}
}
