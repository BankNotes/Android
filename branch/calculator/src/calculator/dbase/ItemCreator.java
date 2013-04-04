package calculator.dbase;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import calculator.ru.activity.allcomputs.ItemOfCalc;

public class ItemCreator {
	private ItemOfCalc myItem;
	private int id, period;
	private double sumOfLoan, percent, overpayment, totalPayments;
	private String beginDate, endDate, creditType, calcType, nameCalc;
	private boolean checked;

	SQLiteDatabase db;

	/**
	 * This class generates Item of calculation by id
	 * 
	 * @param idCalc
	 * @param dBase
	 */
	public ItemCreator(int idCalc, SQLiteDatabase dBase) {
		this.db = dBase;
		this.id = idCalc;
		this.checked = false;
		setParams();

		myItem = new ItemOfCalc(id, period, sumOfLoan, percent, beginDate,
				endDate, creditType, calcType, nameCalc, checked, overpayment,
				totalPayments);

	}

	public ItemOfCalc getItemOfCalc() {
		return this.myItem;
	}

	private void setParams() {
		String[] columnsInpDt = {
				DataBaseSQLHelper.INPUT_DATA_COLUMN_BEGINDATE,
				DataBaseSQLHelper.INPUT_DATA_COLUMN_CALCULATION_TYPE,
				DataBaseSQLHelper.INPUT_DATA_COLUMN_NAME,
				DataBaseSQLHelper.INPUT_DATA_COLUMN_PAYTYPE_IS_ANNUITET,
				DataBaseSQLHelper.INPUT_DATA_COLUMN_PERCENT,
				DataBaseSQLHelper.INPUT_DATA_COLUMN_PERIOD,
				DataBaseSQLHelper.INPUT_DATA_COLUMN_SUM_OF_LOAN };
		String[] argsInpDt = { id + "" };
		Cursor curInptDt = db.query(DataBaseSQLHelper.TABLE_INPUT_DATA,
				columnsInpDt, DataBaseSQLHelper.INPUT_DATA_COLUMN_ID + "=?",
				argsInpDt, null, null, null);
		if (curInptDt.getCount() > 0) {
			curInptDt.moveToFirst();
			beginDate = curInptDt.getString(0);
			nameCalc = curInptDt.getString(2);
			percent = curInptDt.getDouble(4);
			period = curInptDt.getInt(5);
			sumOfLoan = curInptDt.getDouble(6);
		}
		curInptDt.close();

		String query = "select " + DataBaseSQLHelper.PAYMENTS_COLUMN_PAY_DATE
				+ " from " + DataBaseSQLHelper.TABLE_PAYMENTS + " p1 where "
				+ DataBaseSQLHelper.PAYMENTS_COLUMN_ID_CALC + " = ?" + " and "
				+ DataBaseSQLHelper.PAYMENTS_COLUMN_PAY_ID + " = (select max("
				+ DataBaseSQLHelper.PAYMENTS_COLUMN_PAY_ID + ") from "
				+ DataBaseSQLHelper.TABLE_PAYMENTS + " p2 where p2."
				+ DataBaseSQLHelper.PAYMENTS_COLUMN_ID_CALC + "=p1."
				+ DataBaseSQLHelper.PAYMENTS_COLUMN_ID_CALC + ");";
		Cursor endDateCur = db.rawQuery(query, argsInpDt);
		if (endDateCur.getCount() > 0) {
			endDateCur.moveToFirst();
			endDate = endDateCur.getString(0);
		}
		endDateCur.close();

		query = "select " + DataBaseSQLHelper.PAYMENT_TYPE_COLUMN_PAYMENT_NAME
				+ ", ct." + DataBaseSQLHelper.CALC_TYPE_NAMES_COLUMN_NAME
				+ " from " + DataBaseSQLHelper.TABLE_INPUT_DATA + " ipt join "
				+ DataBaseSQLHelper.TABLE_PAYMENT_TYPES + " pt on ipt."
				+ DataBaseSQLHelper.INPUT_DATA_COLUMN_PAYTYPE_IS_ANNUITET
				+ " = pt." + DataBaseSQLHelper.PAYMENT_TYPE_COLUMN_ID
				+ " join " + DataBaseSQLHelper.TABLE_CALCULATION_TYPE_NAMES
				+ " ct on ct." + DataBaseSQLHelper.CALC_TYPE_NAMES_COLUMN_ID
				+ " = ipt."
				+ DataBaseSQLHelper.INPUT_DATA_COLUMN_CALCULATION_TYPE
				+ " where ipt." + DataBaseSQLHelper.INPUT_DATA_COLUMN_ID + "=?";
		Cursor ptct = db.rawQuery(query, argsInpDt);
		if (ptct.getCount() > 0) {
			ptct.moveToFirst();
			creditType = ptct.getString(0);
			calcType = ptct.getString(1);
		}
		ptct.close();

		String[] columns = { DataBaseSQLHelper.TOTALS_COLUMN_SUM_PAYS,
				DataBaseSQLHelper.TOTALS_COLUMN_SUM_PERCENT_PAYS };
		Cursor total = db.query(DataBaseSQLHelper.TABLE_TOTALS, columns, DataBaseSQLHelper.TOTALS_COLUMN_ID_CALC+"=?",
				argsInpDt, null, null, null);
		if(total.getCount()>0){
			total.moveToFirst();
			totalPayments = total.getDouble(0);
			overpayment = total.getDouble(1);
		}
		total.close();
	}
}
