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

import usless.calculator.dbase.calculator.DbHelper;
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

public class PaymentsContentProvider extends ContentProvider {

	private DbHelper dbHelper;
	private static HashMap<String, String> PAYMENTS_PROJECTION_MAP;
	private static final String TABLE_NAME = "payments";
	private static final String AUTHORITY = "calculator.dbase.paymentscontentprovider";

	public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY
			+ "/" + TABLE_NAME);
	public static final Uri ID_FIELD_CONTENT_URI = Uri.parse("content://"
			+ AUTHORITY + "/" + TABLE_NAME.toLowerCase() + "/id");
	public static final Uri ID_CALC_FIELD_CONTENT_URI = Uri.parse("content://"
			+ AUTHORITY + "/" + TABLE_NAME.toLowerCase() + "/id_calc");
	public static final Uri PAY_ID_FIELD_CONTENT_URI = Uri.parse("content://"
			+ AUTHORITY + "/" + TABLE_NAME.toLowerCase() + "/pay_id");
	public static final Uri PAY_FIELD_CONTENT_URI = Uri.parse("content://"
			+ AUTHORITY + "/" + TABLE_NAME.toLowerCase() + "/pay");
	public static final Uri PAY_PERCENT_FIELD_CONTENT_URI = Uri
			.parse("content://" + AUTHORITY + "/" + TABLE_NAME.toLowerCase()
					+ "/pay_percent");
	public static final Uri PAY_FEE_FIELD_CONTENT_URI = Uri.parse("content://"
			+ AUTHORITY + "/" + TABLE_NAME.toLowerCase() + "/pay_fee");
	public static final Uri PAY_DATE_FIELD_CONTENT_URI = Uri.parse("content://"
			+ AUTHORITY + "/" + TABLE_NAME.toLowerCase() + "/pay_date");
	public static final Uri REMAIN_FIELD_CONTENT_URI = Uri.parse("content://"
			+ AUTHORITY + "/" + TABLE_NAME.toLowerCase() + "/remain");

	public static final String DEFAULT_SORT_ORDER = "id ASC";

	private static final UriMatcher URL_MATCHER;

	private static final int PAYMENTS = 1;
	private static final int PAYMENTS_ID = 2;
	private static final int PAYMENTS_ID_CALC = 3;
	private static final int PAYMENTS_PAY_ID = 4;
	private static final int PAYMENTS_PAY = 5;
	private static final int PAYMENTS_PAY_PERCENT = 6;
	private static final int PAYMENTS_PAY_FEE = 7;
	private static final int PAYMENTS_PAY_DATE = 8;
	private static final int PAYMENTS_REMAIN = 9;

	// Content values keys (using column names)
	public static final String ID = "id";
	public static final String ID_CALC = "id_calc";
	public static final String PAY_ID = "pay_id";
	public static final String PAY = "pay";
	public static final String PAY_PERCENT = "pay_percent";
	public static final String PAY_FEE = "pay_fee";
	public static final String PAY_DATE = "pay_date";
	public static final String REMAIN = "remain";

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
		case PAYMENTS:
			qb.setTables(TABLE_NAME);
			qb.setProjectionMap(PAYMENTS_PROJECTION_MAP);
			break;
		case PAYMENTS_ID:
			qb.setTables(TABLE_NAME);
			qb.appendWhere("id='" + url.getPathSegments().get(2) + "'");
			break;
		case PAYMENTS_ID_CALC:
			qb.setTables(TABLE_NAME);
			qb.appendWhere("id_calc='" + url.getPathSegments().get(2) + "'");
			break;
		case PAYMENTS_PAY_ID:
			qb.setTables(TABLE_NAME);
			qb.appendWhere("pay_id='" + url.getPathSegments().get(2) + "'");
			break;
		case PAYMENTS_PAY:
			qb.setTables(TABLE_NAME);
			qb.appendWhere("pay='" + url.getPathSegments().get(2) + "'");
			break;
		case PAYMENTS_PAY_PERCENT:
			qb.setTables(TABLE_NAME);
			qb.appendWhere("pay_percent='" + url.getPathSegments().get(2) + "'");
			break;
		case PAYMENTS_PAY_FEE:
			qb.setTables(TABLE_NAME);
			qb.appendWhere("pay_fee='" + url.getPathSegments().get(2) + "'");
			break;
		case PAYMENTS_PAY_DATE:
			qb.setTables(TABLE_NAME);
			qb.appendWhere("pay_date='" + url.getPathSegments().get(2) + "'");
			break;
		case PAYMENTS_REMAIN:
			qb.setTables(TABLE_NAME);
			qb.appendWhere("remain='" + url.getPathSegments().get(2) + "'");
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
		case PAYMENTS:
			return "vnd.android.cursor.dir/vnd.calculator.dbase.payments";
		case PAYMENTS_ID:
			return "vnd.android.cursor.item/vnd.calculator.dbase.payments";
		case PAYMENTS_ID_CALC:
			return "vnd.android.cursor.item/vnd.calculator.dbase.payments";
		case PAYMENTS_PAY_ID:
			return "vnd.android.cursor.item/vnd.calculator.dbase.payments";
		case PAYMENTS_PAY:
			return "vnd.android.cursor.item/vnd.calculator.dbase.payments";
		case PAYMENTS_PAY_PERCENT:
			return "vnd.android.cursor.item/vnd.calculator.dbase.payments";
		case PAYMENTS_PAY_FEE:
			return "vnd.android.cursor.item/vnd.calculator.dbase.payments";
		case PAYMENTS_PAY_DATE:
			return "vnd.android.cursor.item/vnd.calculator.dbase.payments";
		case PAYMENTS_REMAIN:
			return "vnd.android.cursor.item/vnd.calculator.dbase.payments";

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
		if (URL_MATCHER.match(url) != PAYMENTS) {
			throw new IllegalArgumentException("Unknown URL " + url);
		}

		rowID = mDB.insert("payments", "payments", values);
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
		case PAYMENTS:
			count = mDB.delete(TABLE_NAME, where, whereArgs);
			break;
		case PAYMENTS_ID:
			segment = "'" + url.getPathSegments().get(2) + "'";
			count = mDB.delete(TABLE_NAME,
					"id="
							+ segment
							+ (!TextUtils.isEmpty(where) ? " AND (" + where
									+ ')' : ""), whereArgs);
			break;
		case PAYMENTS_ID_CALC:
			segment = "'" + url.getPathSegments().get(2) + "'";
			count = mDB.delete(TABLE_NAME,
					"id_calc="
							+ segment
							+ (!TextUtils.isEmpty(where) ? " AND (" + where
									+ ')' : ""), whereArgs);
			break;
		case PAYMENTS_PAY_ID:
			segment = "'" + url.getPathSegments().get(2) + "'";
			count = mDB.delete(TABLE_NAME,
					"pay_id="
							+ segment
							+ (!TextUtils.isEmpty(where) ? " AND (" + where
									+ ')' : ""), whereArgs);
			break;
		case PAYMENTS_PAY:
			segment = "'" + url.getPathSegments().get(2) + "'";
			count = mDB.delete(TABLE_NAME,
					"pay="
							+ segment
							+ (!TextUtils.isEmpty(where) ? " AND (" + where
									+ ')' : ""), whereArgs);
			break;
		case PAYMENTS_PAY_PERCENT:
			segment = "'" + url.getPathSegments().get(2) + "'";
			count = mDB.delete(TABLE_NAME,
					"pay_percent="
							+ segment
							+ (!TextUtils.isEmpty(where) ? " AND (" + where
									+ ')' : ""), whereArgs);
			break;
		case PAYMENTS_PAY_FEE:
			segment = "'" + url.getPathSegments().get(2) + "'";
			count = mDB.delete(TABLE_NAME,
					"pay_fee="
							+ segment
							+ (!TextUtils.isEmpty(where) ? " AND (" + where
									+ ')' : ""), whereArgs);
			break;
		case PAYMENTS_PAY_DATE:
			segment = "'" + url.getPathSegments().get(2) + "'";
			count = mDB.delete(TABLE_NAME,
					"pay_date="
							+ segment
							+ (!TextUtils.isEmpty(where) ? " AND (" + where
									+ ')' : ""), whereArgs);
			break;
		case PAYMENTS_REMAIN:
			segment = "'" + url.getPathSegments().get(2) + "'";
			count = mDB.delete(TABLE_NAME,
					"remain="
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
		case PAYMENTS:
			count = mDB.update(TABLE_NAME, values, where, whereArgs);
			break;
		case PAYMENTS_ID:
			segment = "'" + url.getPathSegments().get(2) + "'";
			count = mDB.update(TABLE_NAME, values,
					"id="
							+ segment
							+ (!TextUtils.isEmpty(where) ? " AND (" + where
									+ ')' : ""), whereArgs);
			break;
		case PAYMENTS_ID_CALC:
			segment = "'" + url.getPathSegments().get(2) + "'";
			count = mDB.update(TABLE_NAME, values,
					"id_calc="
							+ segment
							+ (!TextUtils.isEmpty(where) ? " AND (" + where
									+ ')' : ""), whereArgs);
			break;
		case PAYMENTS_PAY_ID:
			segment = "'" + url.getPathSegments().get(2) + "'";
			count = mDB.update(TABLE_NAME, values,
					"pay_id="
							+ segment
							+ (!TextUtils.isEmpty(where) ? " AND (" + where
									+ ')' : ""), whereArgs);
			break;
		case PAYMENTS_PAY:
			segment = "'" + url.getPathSegments().get(2) + "'";
			count = mDB.update(TABLE_NAME, values,
					"pay="
							+ segment
							+ (!TextUtils.isEmpty(where) ? " AND (" + where
									+ ')' : ""), whereArgs);
			break;
		case PAYMENTS_PAY_PERCENT:
			segment = "'" + url.getPathSegments().get(2) + "'";
			count = mDB.update(TABLE_NAME, values,
					"pay_percent="
							+ segment
							+ (!TextUtils.isEmpty(where) ? " AND (" + where
									+ ')' : ""), whereArgs);
			break;
		case PAYMENTS_PAY_FEE:
			segment = "'" + url.getPathSegments().get(2) + "'";
			count = mDB.update(TABLE_NAME, values,
					"pay_fee="
							+ segment
							+ (!TextUtils.isEmpty(where) ? " AND (" + where
									+ ')' : ""), whereArgs);
			break;
		case PAYMENTS_PAY_DATE:
			segment = "'" + url.getPathSegments().get(2) + "'";
			count = mDB.update(TABLE_NAME, values,
					"pay_date="
							+ segment
							+ (!TextUtils.isEmpty(where) ? " AND (" + where
									+ ')' : ""), whereArgs);
			break;
		case PAYMENTS_REMAIN:
			segment = "'" + url.getPathSegments().get(2) + "'";
			count = mDB.update(TABLE_NAME, values,
					"remain="
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
		URL_MATCHER.addURI(AUTHORITY, TABLE_NAME.toLowerCase(), PAYMENTS);
		URL_MATCHER.addURI(AUTHORITY, TABLE_NAME.toLowerCase() + "/id" + "/*",
				PAYMENTS_ID);
		URL_MATCHER.addURI(AUTHORITY, TABLE_NAME.toLowerCase() + "/id_calc"
				+ "/*", PAYMENTS_ID_CALC);
		URL_MATCHER.addURI(AUTHORITY, TABLE_NAME.toLowerCase() + "/pay_id"
				+ "/*", PAYMENTS_PAY_ID);
		URL_MATCHER.addURI(AUTHORITY, TABLE_NAME.toLowerCase() + "/pay" + "/*",
				PAYMENTS_PAY);
		URL_MATCHER.addURI(AUTHORITY, TABLE_NAME.toLowerCase() + "/pay_percent"
				+ "/*", PAYMENTS_PAY_PERCENT);
		URL_MATCHER.addURI(AUTHORITY, TABLE_NAME.toLowerCase() + "/pay_fee"
				+ "/*", PAYMENTS_PAY_FEE);
		URL_MATCHER.addURI(AUTHORITY, TABLE_NAME.toLowerCase() + "/pay_date"
				+ "/*", PAYMENTS_PAY_DATE);
		URL_MATCHER.addURI(AUTHORITY, TABLE_NAME.toLowerCase() + "/remain"
				+ "/*", PAYMENTS_REMAIN);

		PAYMENTS_PROJECTION_MAP = new HashMap<String, String>();
		PAYMENTS_PROJECTION_MAP.put(ID, "id");
		PAYMENTS_PROJECTION_MAP.put(ID_CALC, "id_calc");
		PAYMENTS_PROJECTION_MAP.put(PAY_ID, "pay_id");
		PAYMENTS_PROJECTION_MAP.put(PAY, "pay");
		PAYMENTS_PROJECTION_MAP.put(PAY_PERCENT, "pay_percent");
		PAYMENTS_PROJECTION_MAP.put(PAY_FEE, "pay_fee");
		PAYMENTS_PROJECTION_MAP.put(PAY_DATE, "pay_date");
		PAYMENTS_PROJECTION_MAP.put(REMAIN, "remain");

	}
}
