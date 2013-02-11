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

public class ListOfLoansDataProvider extends ContentProvider {
	private DbHelper dbHelper;

	private static HashMap<String, String> LIST_OF_LOANS_PROJECTION_MAP;
	private static final String TABLE_NAME = "list_of_loan";
	private static final String AUTHORITY = "calculator.ru.listofloansdataprovider";

	public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY
			+ "/" + TABLE_NAME);
	public static final Uri ID_FIELD_CONTENT_URI = Uri.parse("content://"
			+ AUTHORITY + "/" + TABLE_NAME.toLowerCase() + "/id");
	public static final Uri ID_CALC_FIELD_CONTENT_URI = Uri.parse("content://"
			+ AUTHORITY + "/" + TABLE_NAME.toLowerCase() + "/id_calc");
	public static final Uri SUM_OF_LOAN_FIELD_CONTENT_URI = Uri
			.parse("content://" + AUTHORITY + "/" + TABLE_NAME.toLowerCase()
					+ "/sum_of_loan");
	public static final Uri PERCENT_FIELD_CONTENT_URI = Uri.parse("content://"
			+ AUTHORITY + "/" + TABLE_NAME.toLowerCase() + "/percent");
	public static final Uri DATE_OF_LOAN_FIELD_CONTENT_URI = Uri
			.parse("content://" + AUTHORITY + "/" + TABLE_NAME.toLowerCase()
					+ "/date_of_loan");
	public static final Uri QTY_PAYMENTS_FIELD_CONTENT_URI = Uri
			.parse("content://" + AUTHORITY + "/" + TABLE_NAME.toLowerCase()
					+ "/qty_payments");
	public static final Uri CREDIT_TYPE_FIELD_CONTENT_URI = Uri
			.parse("content://" + AUTHORITY + "/" + TABLE_NAME.toLowerCase()
					+ "/credit_type");
	public static final Uri CALC_TYPE_FIELD_CONTENT_URI = Uri
			.parse("content://" + AUTHORITY + "/" + TABLE_NAME.toLowerCase()
					+ "/calc_type");

	public static final String DEFAULT_SORT_ORDER = "id ASC";

	private static final UriMatcher URL_MATCHER;

	private static final int LIST_OF_LOANS = 1;
	private static final int LIST_OF_LOANS_ID = 2;
	private static final int LIST_OF_LOANS_ID_CALC = 3;
	private static final int LIST_OF_LOANS_SUM = 4;
	private static final int LIST_OF_LOANS_PERCENT = 5;
	private static final int LIST_OF_LOANS_DATE = 6;
	private static final int LIST_OF_LOANS_QTY_PAYMENTS = 7;
	private static final int LIST_OF_LOANS_CREDIT_TYPE = 8;
	private static final int LIST_OF_LOANS_CALC_TYPE = 9;

	// Content values keys (using column names)
	public static final String ID = "id";
	public static final String ID_CALC = "id_calc";
	public static final String SUMOFLOAN = "sum_of_loan";
	public static final String PERCENT = "percent";
	public static final String DATEOFLOAN = "date_of_loan";
	public static final String QTYPAYMENTS = "qty_payments";
	public static final String CREDITTYPE = "credit_type";
	public static final String CALCTYPE = "calc_type";

	public boolean onCreate() {
		dbHelper = new DbHelper(getContext(), true);
		return (dbHelper == null) ? false : true;
	}

	public Cursor query(Uri url, String[] projection, String selection,
			String[] selectionArgs, String sortOrder) {
		SQLiteDatabase mDB = dbHelper.getReadableDatabase();
		SQLiteQueryBuilder qb = new SQLiteQueryBuilder();

		switch (URL_MATCHER.match(url)) {
		case (LIST_OF_LOANS):
			qb.setTables(TABLE_NAME);
			qb.setProjectionMap(LIST_OF_LOANS_PROJECTION_MAP);
			break;
		case (LIST_OF_LOANS_ID):
			qb.setTables(TABLE_NAME);
			qb.appendWhere("id='" + url.getPathSegments().get(2) + "'");
			break;
		case (LIST_OF_LOANS_ID_CALC):
			qb.setTables(TABLE_NAME);
			qb.appendWhere("id_calc='" + url.getPathSegments().get(2) + "'");
			break;
		case (LIST_OF_LOANS_SUM):
			qb.setTables(TABLE_NAME);
			qb.appendWhere("sum_of_loan'" + url.getPathSegments().get(2) + "'");
			break;
		case (LIST_OF_LOANS_PERCENT):
			qb.setTables(TABLE_NAME);
			qb.appendWhere("percent'" + url.getPathSegments().get(2) + "'");
			break;
		case (LIST_OF_LOANS_DATE):
			qb.setTables(TABLE_NAME);
			qb.appendWhere("date_of_loan'" + url.getPathSegments().get(2) + "'");
			break;
		case (LIST_OF_LOANS_QTY_PAYMENTS):
			qb.setTables(TABLE_NAME);
			qb.appendWhere("qty_payments'" + url.getPathSegments().get(2) + "'");
			break;
		case (LIST_OF_LOANS_CREDIT_TYPE):
			qb.setTables(TABLE_NAME);
			qb.appendWhere("credit_type'" + url.getPathSegments().get(2) + "'");
			break;
		case (LIST_OF_LOANS_CALC_TYPE):
			qb.setTables(TABLE_NAME);
			qb.appendWhere("calc_type'" + url.getPathSegments().get(2) + "'");
			break;
		default:
			throw new IllegalArgumentException("Unknown URL " + url);
		}
		String orderBy = "";
		if (TextUtils.isEmpty(sortOrder)) {
			orderBy = DEFAULT_SORT_ORDER;
		} else {
			orderBy = sortOrder;
		}
		Cursor c = qb.query(mDB, projection, selection, selectionArgs, null,
				null, orderBy);
		c.setNotificationUri(getContext().getContentResolver(), url);
		return c;
	}

	public String getType(Uri url) {
		switch (URL_MATCHER.match(url)) {
		case (LIST_OF_LOANS):
			return "vnd.android.cursor.dir/vnd.calculator.ru.list_of_loans";

		case (LIST_OF_LOANS_ID):

		case (LIST_OF_LOANS_ID_CALC):

		case (LIST_OF_LOANS_SUM):

		case (LIST_OF_LOANS_PERCENT):

		case (LIST_OF_LOANS_DATE):

		case (LIST_OF_LOANS_QTY_PAYMENTS):

		case (LIST_OF_LOANS_CREDIT_TYPE):

		case (LIST_OF_LOANS_CALC_TYPE):
			return "vnd.android.cursor.item/vnd.calculator.ru.list_of_loans";
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
		if (URL_MATCHER.match(url) != LIST_OF_LOANS) {
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
		case (LIST_OF_LOANS):
			count = mDB.delete(TABLE_NAME, where, whereArgs);
			break;
		case (LIST_OF_LOANS_ID):
			segment = "'" + url.getPathSegments().get(2) + "'";
			count = mDB.delete(TABLE_NAME,
					"id="
							+ segment
							+ (!TextUtils.isEmpty(where) ? " AND (" + where
									+ ')' : ""), whereArgs);
			break;
		case (LIST_OF_LOANS_ID_CALC):
			segment = "'" + url.getPathSegments().get(2) + "'";
			count = mDB.delete(TABLE_NAME,
					"id_calc="
							+ segment
							+ (!TextUtils.isEmpty(where) ? " AND (" + where
									+ ')' : ""), whereArgs);
			break;
		case (LIST_OF_LOANS_SUM):
			segment = "'" + url.getPathSegments().get(2) + "'";
			count = mDB.delete(TABLE_NAME,
					"sum_of_loan="
							+ segment
							+ (!TextUtils.isEmpty(where) ? " AND (" + where
									+ ')' : ""), whereArgs);
			break;
		case (LIST_OF_LOANS_PERCENT):
			segment = "'" + url.getPathSegments().get(2) + "'";
			count = mDB.delete(TABLE_NAME,
					"percent="
							+ segment
							+ (!TextUtils.isEmpty(where) ? " AND (" + where
									+ ')' : ""), whereArgs);
			break;
		case (LIST_OF_LOANS_DATE):
			segment = "'" + url.getPathSegments().get(2) + "'";
			count = mDB.delete(TABLE_NAME,
					"date_of_loan="
							+ segment
							+ (!TextUtils.isEmpty(where) ? " AND (" + where
									+ ')' : ""), whereArgs);
			break;
		case (LIST_OF_LOANS_QTY_PAYMENTS):
			segment = "'" + url.getPathSegments().get(2) + "'";
			count = mDB.delete(TABLE_NAME,
					"qty_payments="
							+ segment
							+ (!TextUtils.isEmpty(where) ? " AND (" + where
									+ ')' : ""), whereArgs);
			break;
		case (LIST_OF_LOANS_CREDIT_TYPE):
			segment = "'" + url.getPathSegments().get(2) + "'";
			count = mDB.delete(TABLE_NAME,
					"credit_type="
							+ segment
							+ (!TextUtils.isEmpty(where) ? " AND (" + where
									+ ')' : ""), whereArgs);
			break;
		case (LIST_OF_LOANS_CALC_TYPE):
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
		return count;
	}

	public int update(Uri url, ContentValues values, String where,
			String[] whereArgs) {
		SQLiteDatabase mDB = dbHelper.getWritableDatabase();
		int count;
		String segment = "";

		switch (URL_MATCHER.match(url)) {
		case (LIST_OF_LOANS):
			count = mDB.update(TABLE_NAME, values, where, whereArgs);
			break;
		case (LIST_OF_LOANS_ID):
			segment = "'" + url.getPathSegments().get(2) + "'";
			count = mDB.update(TABLE_NAME, values,
					"id="
							+ segment
							+ (!TextUtils.isEmpty(where) ? " AND (" + where
									+ ')' : ""), whereArgs);
			break;
		case (LIST_OF_LOANS_ID_CALC):
			segment = "'" + url.getPathSegments().get(2) + "'";
			count = mDB.update(TABLE_NAME, values,
					"id_calc="
							+ segment
							+ (!TextUtils.isEmpty(where) ? " AND (" + where
									+ ')' : ""), whereArgs);
			break;
		case (LIST_OF_LOANS_SUM):
			segment = "'" + url.getPathSegments().get(2) + "'";
			count = mDB.update(TABLE_NAME, values,
					"sum_of_loan="
							+ segment
							+ (!TextUtils.isEmpty(where) ? " AND (" + where
									+ ')' : ""), whereArgs);
			break;
		case (LIST_OF_LOANS_PERCENT):
			segment = "'" + url.getPathSegments().get(2) + "'";
			count = mDB.update(TABLE_NAME, values,
					"percent="
							+ segment
							+ (!TextUtils.isEmpty(where) ? " AND (" + where
									+ ')' : ""), whereArgs);
			break;
		case (LIST_OF_LOANS_DATE):
			segment = "'" + url.getPathSegments().get(2) + "'";
			count = mDB.update(TABLE_NAME, values,
					"date_of_loan="
							+ segment
							+ (!TextUtils.isEmpty(where) ? " AND (" + where
									+ ')' : ""), whereArgs);
			break;
		case (LIST_OF_LOANS_QTY_PAYMENTS):
			segment = "'" + url.getPathSegments().get(2) + "'";
			count = mDB.update(TABLE_NAME, values,
					"qty_payments="
							+ segment
							+ (!TextUtils.isEmpty(where) ? " AND (" + where
									+ ')' : ""), whereArgs);
			break;
		case (LIST_OF_LOANS_CREDIT_TYPE):
			segment = "'" + url.getPathSegments().get(2) + "'";
			count = mDB.update(TABLE_NAME, values,
					"credit_type="
							+ segment
							+ (!TextUtils.isEmpty(where) ? " AND (" + where
									+ ')' : ""), whereArgs);
			break;
		case (LIST_OF_LOANS_CALC_TYPE):
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
		URL_MATCHER.addURI(AUTHORITY, TABLE_NAME.toLowerCase(), LIST_OF_LOANS);
		URL_MATCHER.addURI(AUTHORITY, TABLE_NAME.toLowerCase() + "/id" + "/*",
				LIST_OF_LOANS_ID);
		URL_MATCHER.addURI(AUTHORITY, TABLE_NAME.toLowerCase() + "/id_calc"
				+ "/*", LIST_OF_LOANS_ID_CALC);
		URL_MATCHER.addURI(AUTHORITY, TABLE_NAME.toLowerCase() + "/sum_of_loan"
				+ "/*", LIST_OF_LOANS_SUM);
		URL_MATCHER.addURI(AUTHORITY, TABLE_NAME.toLowerCase() + "/percent"
				+ "/*", LIST_OF_LOANS_PERCENT);
		URL_MATCHER.addURI(AUTHORITY, TABLE_NAME.toLowerCase()
				+ "/date_of_loan" + "/*", LIST_OF_LOANS_DATE);
		URL_MATCHER.addURI(AUTHORITY, TABLE_NAME.toLowerCase()
				+ "/qty_payments" + "/*", LIST_OF_LOANS_QTY_PAYMENTS);
		URL_MATCHER.addURI(AUTHORITY, TABLE_NAME.toLowerCase() + "/credit_type"
				+ "/*", LIST_OF_LOANS_CREDIT_TYPE);
		URL_MATCHER.addURI(AUTHORITY, TABLE_NAME.toLowerCase() + "/calc_type"
				+ "/*", LIST_OF_LOANS_CALC_TYPE);

		LIST_OF_LOANS_PROJECTION_MAP = new HashMap<String, String>();
		LIST_OF_LOANS_PROJECTION_MAP.put(ID, "id");
		LIST_OF_LOANS_PROJECTION_MAP.put(ID_CALC, "id_calc");
		LIST_OF_LOANS_PROJECTION_MAP.put(SUMOFLOAN, "sum_of_loan");
		LIST_OF_LOANS_PROJECTION_MAP.put(PERCENT, "percent");
		LIST_OF_LOANS_PROJECTION_MAP.put(DATEOFLOAN, "date_of_loan");
		LIST_OF_LOANS_PROJECTION_MAP.put(QTYPAYMENTS, "qty_payments");
		LIST_OF_LOANS_PROJECTION_MAP.put(CREDITTYPE, "credit_type");
		LIST_OF_LOANS_PROJECTION_MAP.put(CALCTYPE, "calc_type");

	}
}
