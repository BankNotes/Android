package calculator.dbase;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DataBaseSQLHelper extends SQLiteOpenHelper {

	public static final String TABLE_INPUT_DATA = "input_data";
	public static final String INPUT_DATA_COLUMN_ID = "id";
	public static final String INPUT_DATA_COLUMN_INPUTSUM = "inputSum";
	public static final String INPUT_DATA_COLUMN_PERCENT = "percent";
	public static final String INPUT_DATA_COLUMN_BEGINDATE = "beginDate";
	public static final String INPUT_DATA_COLUMN_PERIOD = "period";
	public static final String INPUT_DATA_COLUMN_PAYTYPE_IS_ANNUITET = "payType";
	public static final String INPUT_DATA_COLUMN_COMP_TYPE = "comp_type";
	public static final String INPUT_DATA_COLUMN_NAME = "name";
	public static final String INPUT_DATA_COLUMN_CREDIT_SUM = "sum";

	public static final String TABLE_PAYMENTS = "payments";
	public static final String PAYMENTS_COLUMN_ID_CALC = "id_calc";
	public static final String PAYMENTS_COLUMN_PAY_ID = "pay_id";
	public static final String PAYMENTS_COLUMN_PAY = "pay";
	public static final String PAYMENTS_COLUMN_PAY_PERCENT = "pay_percent";
	public static final String PAYMENTS_COLUMN_PAY_FEE = "pay_fee";
	public static final String PAYMENTS_COLUMN_PAY_DATE = "pay_date";
	public static final String PAYMENTS_COLUMN_REMAIN = "remain";

	public static final String TABLE_TOTALS = "totals";
	public static final String TOTALS_COLUMN_ID_CALC = "id_calc";
	public static final String TOTALS_COLUMN_SUM_PAYS = "sumPays";
	public static final String TOTALS_COLUMN_SUM_PERCENT_PAYS = "sumPercentPays";
	public static final String TOTALS_COLUMN_SUM_PAY_FEE = "sumPayFee";

	public static final String TABLE_LIST_OF_LOAN = "list_of_loan";
	public static final String LIST_COLUMN_ID_CALC = "id_calc";
	public static final String LIST_COLUMN_SUM_OF_LOAN = "sum_of_loan";
	public static final String LIST_COLUMN_PERCENT = "percent";
	public static final String LIST_COLUMN_BEGIN_DATE = "begin_date";
	public static final String LIST_COLUMN_END_DATE = "end_date";
	public static final String LIST_COLUMN_QTY_PAYMENTS = "qty_payments";
	public static final String LIST_COLUMN_CREDIT_TYPE = "credit_type";
	public static final String LIST_COLUMN_CALC_TYPE = "calc_type";
	public static final String LIST_COLUMN_NAME_CALC = "name_calc";

	private static final String DATABASE_NAME = "calc.db";
	private static final int DATABASE_VERSION = 1;

	private static final String CREATE_TABLE_INPUT_DATA = "create table "
			+ TABLE_INPUT_DATA + " (" + INPUT_DATA_COLUMN_ID
			+ " integer primary key autoincrement, "
			+ INPUT_DATA_COLUMN_INPUTSUM + " real, "
			+ INPUT_DATA_COLUMN_PERCENT + " real, "
			+ INPUT_DATA_COLUMN_BEGINDATE + " text, "
			+ INPUT_DATA_COLUMN_PERIOD + " integer, "
			+ INPUT_DATA_COLUMN_PAYTYPE_IS_ANNUITET + " integer, "
			+ INPUT_DATA_COLUMN_COMP_TYPE + " text, "
			+ INPUT_DATA_COLUMN_CREDIT_SUM + " real,"
			+ INPUT_DATA_COLUMN_NAME + " text); ";

	private static final String CREATE_TABLE_PAYMENTS = "create table "
			+ TABLE_PAYMENTS + " (" + PAYMENTS_COLUMN_ID_CALC + " integer, "
			+ PAYMENTS_COLUMN_PAY_ID + " integer, " + PAYMENTS_COLUMN_PAY
			+ " real, " + PAYMENTS_COLUMN_PAY_PERCENT + " real, "
			+ PAYMENTS_COLUMN_PAY_FEE + " real, " + PAYMENTS_COLUMN_PAY_DATE
			+ " text, " + PAYMENTS_COLUMN_REMAIN + " real); ";

	private static final String CREATE_TABLE_TOTALS = "create table "
			+ TABLE_TOTALS + " (" + TOTALS_COLUMN_ID_CALC + " integer, "
			+ TOTALS_COLUMN_SUM_PAYS + " real, "
			+ TOTALS_COLUMN_SUM_PERCENT_PAYS + " real,"
			+ TOTALS_COLUMN_SUM_PAY_FEE + " real);";

	private static final String CREATE_TABLE_LIST_OF_LOAN = "create table "
			+ TABLE_LIST_OF_LOAN + " (" + LIST_COLUMN_ID_CALC + " integer, "
			+ LIST_COLUMN_SUM_OF_LOAN + " real, " + LIST_COLUMN_PERCENT
			+ " real, " + LIST_COLUMN_BEGIN_DATE + " text, "
			+ LIST_COLUMN_END_DATE + " text, " + LIST_COLUMN_QTY_PAYMENTS
			+ " integer, " + LIST_COLUMN_CREDIT_TYPE + " text, "
			+ LIST_COLUMN_CALC_TYPE + " text, " + LIST_COLUMN_NAME_CALC
			+ " text);";

	// private static final String DATABASE_CREATE = CREATE_TABLE_INPUT_DATA
	// + CREATE_TABLE_PAYMENTS + CREATE_TABLE_TOTALS
	// + CREATE_TABLE_LIST_OF_LOAN;

	public DataBaseSQLHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);

	}

	public void onCreate(SQLiteDatabase dBase) {
		dBase.execSQL(CREATE_TABLE_INPUT_DATA);
		dBase.execSQL(CREATE_TABLE_PAYMENTS);
		dBase.execSQL(CREATE_TABLE_LIST_OF_LOAN);
		dBase.execSQL(CREATE_TABLE_TOTALS);

	}

	public void onUpgrade(SQLiteDatabase dBase, int oldVersion, int newVersion) {
		/*
		 * if (oldVersion < newVersion & newVersion > DATABASE_VERSION) {
		 * String[] tables = {TABLE_INPUT_DATA, TABLE_PAYMENTS, TABLE_TOTALS,
		 * TABLE_LIST_OF_LOAN}; for (String table:tables){
		 * dBase.execSQL("drop table if exists "+table+";"); } onCreate(dBase);
		 * }
		 */
		String[] tables = { TABLE_INPUT_DATA, TABLE_PAYMENTS, TABLE_TOTALS,
				TABLE_LIST_OF_LOAN };
		for (String table : tables) {
			dBase.execSQL("drop table if exists " + table + ";");
		}
		onCreate(dBase);
	}
}
