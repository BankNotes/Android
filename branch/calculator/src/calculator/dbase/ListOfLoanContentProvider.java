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

public class ListOfLoanContentProvider extends ContentProvider {

	private DbHelper dbHelper;
	private static HashMap<String, String> LIST_OF_LOAN_PROJECTION_MAP;
	private static final String TABLE_NAME = "list_of_loan";
	private static final String AUTHORITY = "calculator.dbase.listofloancontentprovider";

	public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY
			+ "/" + TABLE_NAME);
	public static final Uri ID_CALC_FIELD_CONTENT_URI = Uri.parse("content://"
			+ AUTHORITY + "/" + TABLE_NAME.toLowerCase() + "/id_calc");
	public static final Uri SUM_OF_LOAN_FIELD_CONTENT_URI = Uri
			.parse("content://" + AUTHORITY + "/" + TABLE_NAME.toLowerCase()
					+ "/sum_of_loan");
	public static final Uri PERCENT_FIELD_CONTENT_URI = Uri.parse("content://"
			+ AUTHORITY + "/" + TABLE_NAME.toLowerCase() + "/percent");
	public static final Uri BEGIN_DATE_FIELD_CONTENT_URI = Uri
			.parse("content://" + AUTHORITY + "/" + TABLE_NAME.toLowerCase()
					+ "/begin_date");
	public static final Uri END_DATE_FIELD_CONTENT_URI = Uri.parse("content://"
			+ AUTHORITY + "/" + TABLE_NAME.toLowerCase() + "/end_date");
	public static final Uri QTY_PAYMENTS_FIELD_CONTENT_URI = Uri
			.parse("content://" + AUTHORITY + "/" + TABLE_NAME.toLowerCase()
					+ "/qty_payments");
	public static final Uri CREDIT_TYPE_FIELD_CONTENT_URI = Uri
			.parse("content://" + AUTHORITY + "/" + TABLE_NAME.toLowerCase()
					+ "/credit_type");
	public static final Uri CALC_TYPE_FIELD_CONTENT_URI = Uri
			.parse("content://" + AUTHORITY + "/" + TABLE_NAME.toLowerCase()
					+ "/calc_type");

	public static final String DEFAULT_SORT_ORDER = "id_calc ASC";

	private static final UriMatcher URL_MATCHER;

	private static final int LIST_OF_LOAN = 1;
	private static final int LIST_OF_LOAN_ID_CALC = 2;
	private static final int LIST_OF_LOAN_SUM_OF_LOAN = 3;
	private static final int LIST_OF_LOAN_PERCENT = 4;
	private static final int LIST_OF_LOAN_BEGIN_DATE = 5;
	private static final int LIST_OF_LOAN_END_DATE = 6;
	private static final int LIST_OF_LOAN_QTY_PAYMENTS = 7;
	private static final int LIST_OF_LOAN_CREDIT_TYPE = 8;
	private static final int LIST_OF_LOAN_CALC_TYPE = 9;

	// Content values keys (using column names)
	public static final String ID_CALC = "id_calc";
	public static final String SUM_OF_LOAN = "sum_of_loan";
	public static final String PERCENT = "percent";
	public static final String BEGIN_DATE = "begin_date";
	public static final String END_DATE = "end_date";
	public static final String QTY_PAYMENTS = "qty_payments";
	public static final String CREDIT_TYPE = "credit_type";
	public static final String CALC_TYPE = "calc_type";

	public boolean onCreate() {
		dbHelper = new DbHelper(getContext(), true);
		return (dbHelper == null) ? false : true;
	}

	public Cursor query(Uri url, String[] projection, String selection,
			String[] selectionArgs, String sort) {
		SQLiteDatabase mDB = dbHelper.getReadableDatabase();
		SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
		switch (URL_MATCHER.match(url)) {
		case LIST_OF_LOAN:
			qb.setTables(TABLE_NAME);
			qb.setProjectionMap(LIST_OF_LOAN_PROJECTION_MAP);
			break;
		case LIST_OF_LOAN_ID_CALC:
			qb.setTables(TABLE_NAME);
			qb.appendWhere("id_calc='" + url.getPathSegments().get(2) + "'");
			break;
		case LIST_OF_LOAN_SUM_OF_LOAN:
			qb.setTables(TABLE_NAME);
			qb.appendWhere("sum_of_loan='" + url.getPathSegments().get(2) + "'");
			break;
		case LIST_OF_LOAN_PERCENT:
			qb.setTables(TABLE_NAME);
			qb.appendWhere("percent='" + url.getPathSegments().get(2) + "'");
			break;
		case LIST_OF_LOAN_BEGIN_DATE:
			qb.setTables(TABLE_NAME);
			qb.appendWhere("begin_date='" + url.getPathSegments().get(2) + "'");
			break;
		case LIST_OF_LOAN_END_DATE:
			qb.setTables(TABLE_NAME);
			qb.appendWhere("end_date='" + url.getPathSegments().get(2) + "'");
			break;
		case LIST_OF_LOAN_QTY_PAYMENTS:
			qb.setTables(TABLE_NAME);
			qb.appendWhere("qty_payments='" + url.getPathSegments().get(2)
					+ "'");
			break;
		case LIST_OF_LOAN_CREDIT_TYPE:
			qb.setTables(TABLE_NAME);
			qb.appendWhere("credit_type='" + url.getPathSegments().get(2) + "'");
			break;
		case LIST_OF_LOAN_CALC_TYPE:
			qb.setTables(TABLE_NAME);
			qb.appendWhere("calc_type='" + url.getPathSegments().get(2) + "'");
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
		case LIST_OF_LOAN:
			return "vnd.android.cursor.dir/vnd.calculator.dbase.list_of_loan";
		case LIST_OF_LOAN_ID_CALC:
			return "vnd.android.cursor.item/vnd.calculator.dbase.list_of_loan";
		case LIST_OF_LOAN_SUM_OF_LOAN:
			return "vnd.android.cursor.item/vnd.calculator.dbase.list_of_loan";
		case LIST_OF_LOAN_PERCENT:
			return "vnd.android.cursor.item/vnd.calculator.dbase.list_of_loan";
		case LIST_OF_LOAN_BEGIN_DATE:
			return "vnd.android.cursor.item/vnd.calculator.dbase.list_of_loan";
		case LIST_OF_LOAN_END_DATE:
			return "vnd.android.cursor.item/vnd.calculator.dbase.list_of_loan";
		case LIST_OF_LOAN_QTY_PAYMENTS:
			return "vnd.android.cursor.item/vnd.calculator.dbase.list_of_loan";
		case LIST_OF_LOAN_CREDIT_TYPE:
			return "vnd.android.cursor.item/vnd.calculator.dbase.list_of_loan";
		case LIST_OF_LOAN_CALC_TYPE:
			return "vnd.android.cursor.item/vnd.calculator.dbase.list_of_loan";

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
		if (URL_MATCHER.match(url) != LIST_OF_LOAN) {
			throw new IllegalArgumentException("Unknown URL " + url);
		}

		rowID = mDB.insert("list_of_loan", "list_of_loan", values);
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
		case LIST_OF_LOAN:
			count = mDB.delete(TABLE_NAME, where, whereArgs);
			break;
		case LIST_OF_LOAN_ID_CALC:
			segment = "'" + url.getPathSegments().get(2) + "'";
			count = mDB.delete(TABLE_NAME,
					"id_calc="
							+ segment
							+ (!TextUtils.isEmpty(where) ? " AND (" + where
									+ ')' : ""), whereArgs);
			break;
		case LIST_OF_LOAN_SUM_OF_LOAN:
			segment = "'" + url.getPathSegments().get(2) + "'";
			count = mDB.delete(TABLE_NAME,
					"sum_of_loan="
							+ segment
							+ (!TextUtils.isEmpty(where) ? " AND (" + where
									+ ')' : ""), whereArgs);
			break;
		case LIST_OF_LOAN_PERCENT:
			segment = "'" + url.getPathSegments().get(2) + "'";
			count = mDB.delete(TABLE_NAME,
					"percent="
							+ segment
							+ (!TextUtils.isEmpty(where) ? " AND (" + where
									+ ')' : ""), whereArgs);
			break;
		case LIST_OF_LOAN_BEGIN_DATE:
			segment = "'" + url.getPathSegments().get(2) + "'";
			count = mDB.delete(TABLE_NAME,
					"begin_date="
							+ segment
							+ (!TextUtils.isEmpty(where) ? " AND (" + where
									+ ')' : ""), whereArgs);
			break;
		case LIST_OF_LOAN_END_DATE:
			segment = "'" + url.getPathSegments().get(2) + "'";
			count = mDB.delete(TABLE_NAME,
					"end_date="
							+ segment
							+ (!TextUtils.isEmpty(where) ? " AND (" + where
									+ ')' : ""), whereArgs);
			break;
		case LIST_OF_LOAN_QTY_PAYMENTS:
			segment = "'" + url.getPathSegments().get(2) + "'";
			count = mDB.delete(TABLE_NAME,
					"qty_payments="
							+ segment
							+ (!TextUtils.isEmpty(where) ? " AND (" + where
									+ ')' : ""), whereArgs);
			break;
		case LIST_OF_LOAN_CREDIT_TYPE:
			segment = "'" + url.getPathSegments().get(2) + "'";
			count = mDB.delete(TABLE_NAME,
					"credit_type="
							+ segment
							+ (!TextUtils.isEmpty(where) ? " AND (" + where
									+ ')' : ""), whereArgs);
			break;
		case LIST_OF_LOAN_CALC_TYPE:
			segment = "'" + url.getPathSegments().get(2) + "'";
			count = mDB.delete(TABLE_NAME,
					"calc_type="
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
		case LIST_OF_LOAN:
			count = mDB.update(TABLE_NAME, values, where, whereArgs);
			break;
		case LIST_OF_LOAN_ID_CALC:
			segment = "'" + url.getPathSegments().get(2) + "'";
			count = mDB.update(TABLE_NAME, values,
					"id_calc="
							+ segment
							+ (!TextUtils.isEmpty(where) ? " AND (" + where
									+ ')' : ""), whereArgs);
			break;
		case LIST_OF_LOAN_SUM_OF_LOAN:
			segment = "'" + url.getPathSegments().get(2) + "'";
			count = mDB.update(TABLE_NAME, values,
					"sum_of_loan="
							+ segment
							+ (!TextUtils.isEmpty(where) ? " AND (" + where
									+ ')' : ""), whereArgs);
			break;
		case LIST_OF_LOAN_PERCENT:
			segment = "'" + url.getPathSegments().get(2) + "'";
			count = mDB.update(TABLE_NAME, values,
					"percent="
							+ segment
							+ (!TextUtils.isEmpty(where) ? " AND (" + where
									+ ')' : ""), whereArgs);
			break;
		case LIST_OF_LOAN_BEGIN_DATE:
			segment = "'" + url.getPathSegments().get(2) + "'";
			count = mDB.update(TABLE_NAME, values,
					"begin_date="
							+ segment
							+ (!TextUtils.isEmpty(where) ? " AND (" + where
									+ ')' : ""), whereArgs);
			break;
		case LIST_OF_LOAN_END_DATE:
			segment = "'" + url.getPathSegments().get(2) + "'";
			count = mDB.update(TABLE_NAME, values,
					"end_date="
							+ segment
							+ (!TextUtils.isEmpty(where) ? " AND (" + where
									+ ')' : ""), whereArgs);
			break;
		case LIST_OF_LOAN_QTY_PAYMENTS:
			segment = "'" + url.getPathSegments().get(2) + "'";
			count = mDB.update(TABLE_NAME, values,
					"qty_payments="
							+ segment
							+ (!TextUtils.isEmpty(where) ? " AND (" + where
									+ ')' : ""), whereArgs);
			break;
		case LIST_OF_LOAN_CREDIT_TYPE:
			segment = "'" + url.getPathSegments().get(2) + "'";
			count = mDB.update(TABLE_NAME, values,
					"credit_type="
							+ segment
							+ (!TextUtils.isEmpty(where) ? " AND (" + where
									+ ')' : ""), whereArgs);
			break;
		case LIST_OF_LOAN_CALC_TYPE:
			segment = "'" + url.getPathSegments().get(2) + "'";
			count = mDB.update(TABLE_NAME, values,
					"calc_type="
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
		URL_MATCHER.addURI(AUTHORITY, TABLE_NAME.toLowerCase(), LIST_OF_LOAN);
		URL_MATCHER.addURI(AUTHORITY, TABLE_NAME.toLowerCase() + "/id_calc"
				+ "/*", LIST_OF_LOAN_ID_CALC);
		URL_MATCHER.addURI(AUTHORITY, TABLE_NAME.toLowerCase() + "/sum_of_loan"
				+ "/*", LIST_OF_LOAN_SUM_OF_LOAN);
		URL_MATCHER.addURI(AUTHORITY, TABLE_NAME.toLowerCase() + "/percent"
				+ "/*", LIST_OF_LOAN_PERCENT);
		URL_MATCHER.addURI(AUTHORITY, TABLE_NAME.toLowerCase() + "/begin_date"
				+ "/*", LIST_OF_LOAN_BEGIN_DATE);
		URL_MATCHER.addURI(AUTHORITY, TABLE_NAME.toLowerCase() + "/end_date"
				+ "/*", LIST_OF_LOAN_END_DATE);
		URL_MATCHER.addURI(AUTHORITY, TABLE_NAME.toLowerCase()
				+ "/qty_payments" + "/*", LIST_OF_LOAN_QTY_PAYMENTS);
		URL_MATCHER.addURI(AUTHORITY, TABLE_NAME.toLowerCase() + "/credit_type"
				+ "/*", LIST_OF_LOAN_CREDIT_TYPE);
		URL_MATCHER.addURI(AUTHORITY, TABLE_NAME.toLowerCase() + "/calc_type"
				+ "/*", LIST_OF_LOAN_CALC_TYPE);

		LIST_OF_LOAN_PROJECTION_MAP = new HashMap<String, String>();
		LIST_OF_LOAN_PROJECTION_MAP.put(ID_CALC, "id_calc");
		LIST_OF_LOAN_PROJECTION_MAP.put(SUM_OF_LOAN, "sum_of_loan");
		LIST_OF_LOAN_PROJECTION_MAP.put(PERCENT, "percent");
		LIST_OF_LOAN_PROJECTION_MAP.put(BEGIN_DATE, "begin_date");
		LIST_OF_LOAN_PROJECTION_MAP.put(END_DATE, "end_date");
		LIST_OF_LOAN_PROJECTION_MAP.put(QTY_PAYMENTS, "qty_payments");
		LIST_OF_LOAN_PROJECTION_MAP.put(CREDIT_TYPE, "credit_type");
		LIST_OF_LOAN_PROJECTION_MAP.put(CALC_TYPE, "calc_type");

	}
}
