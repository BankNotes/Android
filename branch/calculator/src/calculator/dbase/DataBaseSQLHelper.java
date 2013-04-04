package calculator.dbase;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * @author skobelev
 * This class generates data base
 */
public class DataBaseSQLHelper extends SQLiteOpenHelper {

	public static final String TABLE_INPUT_DATA = "input_data";
	public static final String INPUT_DATA_COLUMN_ID = "id";
	public static final String INPUT_DATA_COLUMN_INPUTSUM = "inputSum";
	public static final String INPUT_DATA_COLUMN_PERCENT = "percent";
	public static final String INPUT_DATA_COLUMN_BEGINDATE = "beginDate";
	public static final String INPUT_DATA_COLUMN_PERIOD = "period";
	public static final String INPUT_DATA_COLUMN_PAYTYPE_IS_ANNUITET = "payType";
	public static final String INPUT_DATA_COLUMN_CALCULATION_TYPE = "comp_type";
	public static final String INPUT_DATA_COLUMN_NAME = "name";
	public static final String INPUT_DATA_COLUMN_SUM_OF_LOAN = "sum";

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
	public static final String LIST_COLUMN_PERIOD = "period";
	public static final String LIST_COLUMN_SUM_OF_LOAN = "sum_of_loan";
	public static final String LIST_COLUMN_PERCENT = "percent";
	public static final String LIST_COLUMN_BEGIN_DATE = "begin_date";
	public static final String LIST_COLUMN_END_DATE = "end_date";
	public static final String LIST_COLUMN_CREDIT_TYPE = "credit_type";
	public static final String LIST_COLUMN_CALC_TYPE = "calc_type";
	public static final String LIST_COLUMN_NAME_CALC = "name_calc";
	public static final String LIST_COLUMN_OVERPAYMENT = "overpayment";
	public static final String LIST_COLUMN_TOTAL_PAYMENT = "total_payment";
//	public static final String LIST_COLUMN_PRINCIPAL = "principal";

	public static final String TABLE_CALCULATION_TYPE_NAMES = "calc_types_names";
	public static final String CALC_TYPE_NAMES_COLUMN_ID = "id";
	public static final String CALC_TYPE_NAMES_COLUMN_NAME = "name";

	public static final String TABLE_PAYMENT_TYPES = "payment_types";
	public static final String PAYMENT_TYPE_COLUMN_ID = "id_type";
	public static final String PAYMENT_TYPE_COLUMN_PAYMENT_NAME="payment_name";
	

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
			+ INPUT_DATA_COLUMN_CALCULATION_TYPE + " integer, "
			+ INPUT_DATA_COLUMN_SUM_OF_LOAN + " real," 
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
			+ LIST_COLUMN_PERIOD + " integer, " + LIST_COLUMN_SUM_OF_LOAN
			+ " real, " + LIST_COLUMN_PERCENT + " real, "
			+ LIST_COLUMN_BEGIN_DATE + " text, " + LIST_COLUMN_END_DATE
			+ " text, " + LIST_COLUMN_CREDIT_TYPE + " text, "
			+ LIST_COLUMN_CALC_TYPE + " text, " + LIST_COLUMN_NAME_CALC
			+ " text, " + LIST_COLUMN_OVERPAYMENT + " real, "
			+ LIST_COLUMN_TOTAL_PAYMENT + " real);"; 
//					", " + LIST_COLUMN_PRINCIPAL 	+ " real);";

	private static final String CREATE_TABLE_CALCULATION_TYPE_NAMES = "create table "
			+ TABLE_CALCULATION_TYPE_NAMES
			+ " ("
			+ CALC_TYPE_NAMES_COLUMN_ID
			+ " integer, " + CALC_TYPE_NAMES_COLUMN_NAME + " text);";

	private static final String CREATE_TABLE_PAYMENT_TYPES = "create table "
			+ TABLE_PAYMENT_TYPES +" ( "
			+ PAYMENT_TYPE_COLUMN_ID +" integer, "
			+ PAYMENT_TYPE_COLUMN_PAYMENT_NAME + " text);";
	public DataBaseSQLHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);

	}

	public void onCreate(SQLiteDatabase dBase) {
		dBase.execSQL(CREATE_TABLE_INPUT_DATA);
		dBase.execSQL(CREATE_TABLE_PAYMENTS);
		dBase.execSQL(CREATE_TABLE_LIST_OF_LOAN);
		dBase.execSQL(CREATE_TABLE_TOTALS);
		dBase.execSQL(CREATE_TABLE_CALCULATION_TYPE_NAMES);
		dBase.execSQL(CREATE_TABLE_PAYMENT_TYPES);
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
				TABLE_LIST_OF_LOAN, TABLE_CALCULATION_TYPE_NAMES, TABLE_PAYMENT_TYPES };
		for (String table : tables) {
			dBase.execSQL("drop table if exists " + table + ";");
		}
		onCreate(dBase);
	}
}
