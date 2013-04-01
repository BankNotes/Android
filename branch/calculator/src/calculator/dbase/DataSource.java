package calculator.dbase;

import java.util.ArrayList;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import calculator.ru.R;
import calculator.ru.activity.allcomputs.ItemOfCalc;
import calculator.ru.activity.showsheduler.RowOfSheduller;

public class DataSource {
	SQLiteDatabase myBase;
	DataBaseSQLHelper dbSqlHelper;
	Context context;

	public DataSource(Context context) {
		dbSqlHelper = new DataBaseSQLHelper(context);
		this.context = context;

	}

	public void open() throws SQLException {
		myBase = dbSqlHelper.getWritableDatabase();

		Cursor cur = myBase.query(
				DataBaseSQLHelper.TABLE_CALCULATION_TYPE_NAMES, null, null,
				null, null, null, null);
		if (cur.getCount() == 0) {
			String[] loanTypes = context.getResources().getStringArray(
					R.array.loanType);
			ContentValues values = new ContentValues();
			for (int i = 0; i < loanTypes.length; i++) {
				values.put(DataBaseSQLHelper.CALC_TYPE_NAMES_COLUMN_ID, i);
				values.put(DataBaseSQLHelper.CALC_TYPE_NAMES_COLUMN_NAME,
						loanTypes[i]);
				myBase.insert(DataBaseSQLHelper.TABLE_CALCULATION_TYPE_NAMES,
						null, values);
			}
		}
		cur.close();

	}

	public long insertInputData(InputData inputData) {
		ContentValues cValues = new ContentValues();

		cValues.put(DataBaseSQLHelper.INPUT_DATA_COLUMN_INPUTSUM,
				inputData.getSum());
		cValues.put(DataBaseSQLHelper.INPUT_DATA_COLUMN_PERCENT,
				inputData.getPercent());
		cValues.put(DataBaseSQLHelper.INPUT_DATA_COLUMN_BEGINDATE,
				inputData.getBeginDate());
		cValues.put(DataBaseSQLHelper.INPUT_DATA_COLUMN_PERIOD,
				inputData.getPeriod());
		cValues.put(DataBaseSQLHelper.INPUT_DATA_COLUMN_PAYTYPE_IS_ANNUITET,
				inputData.getPaymentType());
		cValues.put(DataBaseSQLHelper.INPUT_DATA_COLUMN_CALCULATION_TYPE,
				inputData.getCalcType());
		cValues.put(DataBaseSQLHelper.INPUT_DATA_COLUMN_NAME,
				inputData.getName());

		long insertId = myBase.insert(DataBaseSQLHelper.TABLE_INPUT_DATA, null,
				cValues);

		PaymentsSheduler paySheduler = new PaymentsSheduler(context);
		paySheduler.createSheduler();

		return insertId;
	}

	public InputData getLastInputData() {
		InputData lastInputData = null;
		String[] columns = { DataBaseSQLHelper.INPUT_DATA_COLUMN_ID,
				DataBaseSQLHelper.INPUT_DATA_COLUMN_BEGINDATE,
				DataBaseSQLHelper.INPUT_DATA_COLUMN_CALCULATION_TYPE,
				DataBaseSQLHelper.INPUT_DATA_COLUMN_SUM_OF_LOAN,
				DataBaseSQLHelper.INPUT_DATA_COLUMN_NAME,
				DataBaseSQLHelper.INPUT_DATA_COLUMN_PAYTYPE_IS_ANNUITET,
				DataBaseSQLHelper.INPUT_DATA_COLUMN_PERCENT,
				DataBaseSQLHelper.INPUT_DATA_COLUMN_PERIOD };
		String[] args = { getLastIdCalc() + "" };
		Cursor cur = myBase.query(DataBaseSQLHelper.TABLE_INPUT_DATA, columns,
				DataBaseSQLHelper.INPUT_DATA_COLUMN_ID + "=?", args, null,
				null, DataBaseSQLHelper.INPUT_DATA_COLUMN_ID);
		if (cur != null) {
			cur.moveToLast();
			lastInputData = new InputData(cur.getDouble(3), cur.getDouble(6),
					cur.getString(1), cur.getInt(7), false, cur.getInt(2),
					cur.getInt(5), cur.getString(4));

			cur.close();
		}
		return lastInputData;
	}

	public int getLastIdCalc() {
		String[] columns = { DataBaseSQLHelper.INPUT_DATA_COLUMN_ID };
		int lastId = 0;
		Cursor curInptData = myBase.query(DataBaseSQLHelper.TABLE_INPUT_DATA,
				columns, null, null, null, null,
				DataBaseSQLHelper.INPUT_DATA_COLUMN_ID);
		if (curInptData != null) {
			curInptData.moveToLast();
			lastId = curInptData.getInt(0);
			curInptData.close();

		}
		curInptData.close();
		return lastId;
	}

	public ArrayList<RowOfSheduller> getShedullerTable() {
		String[] columns = { DataBaseSQLHelper.PAYMENTS_COLUMN_PAY_ID,
				DataBaseSQLHelper.PAYMENTS_COLUMN_PAY_DATE,
				DataBaseSQLHelper.PAYMENTS_COLUMN_PAY,
				DataBaseSQLHelper.PAYMENTS_COLUMN_PAY_PERCENT,
				DataBaseSQLHelper.PAYMENTS_COLUMN_PAY_FEE,
				DataBaseSQLHelper.PAYMENTS_COLUMN_REMAIN };
		String[] args = { getLastIdCalc() + "" };

		Cursor cur = myBase.query(DataBaseSQLHelper.TABLE_PAYMENTS, columns,
				DataBaseSQLHelper.PAYMENTS_COLUMN_ID_CALC + "=?", args, null,
				null, DataBaseSQLHelper.PAYMENTS_COLUMN_PAY_ID);

		ArrayList<RowOfSheduller> rows = new ArrayList<RowOfSheduller>();

		if (!cur.equals(null)) {

			cur.moveToFirst();
			String beginDate = "";
			String endDate = "";

			while (!cur.isAfterLast()) {

				if (cur.getPosition() == 0) {
					beginDate = cur.getString(1);
				}

				RowOfSheduller row = new RowOfSheduller(cur.getInt(0),
						cur.getString(1), cur.getDouble(2), cur.getDouble(3),
						cur.getDouble(4), cur.getDouble(5));
				if (row.getRemain() < 0) {
					row.setRemain(0);
				}
				rows.add(row);
				cur.moveToNext();
				if (cur.isLast()) {
					endDate = cur.getString(1);
				}
			}

			ContentValues values = new ContentValues();
			int currentId = getLastIdCalc();
			values.put(DataBaseSQLHelper.LIST_COLUMN_ID_CALC, currentId);
			values.put(DataBaseSQLHelper.LIST_COLUMN_BEGIN_DATE, beginDate);
			// values.put(DataBaseSQLHelper.LIST_COLUMN_CALC_TYPE,
			// cur.getInt(2));
			values.put(DataBaseSQLHelper.LIST_COLUMN_END_DATE, endDate);

			myBase.insert(DataBaseSQLHelper.TABLE_LIST_OF_LOAN, null, values);

			cur.close();
			return rows;
		}

		return null;
	}

	public RowOfSheduller getTotalsSheduler() {
		String[] columns = {
				DataBaseSQLHelper.PAYMENTS_COLUMN_ID_CALC,
				"sum(" + DataBaseSQLHelper.PAYMENTS_COLUMN_PAY + ") payment",
				"sum(" + DataBaseSQLHelper.PAYMENTS_COLUMN_PAY_FEE
						+ ") pay_fee",
				"sum(" + DataBaseSQLHelper.PAYMENTS_COLUMN_PAY_PERCENT
						+ ") pay_percent", };
		String[] args = { getLastIdCalc() + "" };
		Cursor cur = myBase.query(DataBaseSQLHelper.TABLE_PAYMENTS, columns,
				DataBaseSQLHelper.PAYMENTS_COLUMN_ID_CALC + "=?", args,
				DataBaseSQLHelper.PAYMENTS_COLUMN_ID_CALC, null, null);
		RowOfSheduller rowTotal = null;
		if (cur != null) {
			cur.moveToFirst();
			rowTotal = new RowOfSheduller(0, "", cur.getDouble(1),
					cur.getDouble(3), cur.getDouble(2), 0.0);

			cur.close();

			// insert totals
			ContentValues values = new ContentValues();
			values.put(DataBaseSQLHelper.TOTALS_COLUMN_ID_CALC, getLastIdCalc());
			values.put(DataBaseSQLHelper.TOTALS_COLUMN_SUM_PAYS,
					rowTotal.getPayment());
			values.put(DataBaseSQLHelper.TOTALS_COLUMN_SUM_PAY_FEE,
					rowTotal.getPayFee());
			values.put(DataBaseSQLHelper.TOTALS_COLUMN_SUM_PERCENT_PAYS,
					rowTotal.getPayPercent());

			myBase.insert(DataBaseSQLHelper.TABLE_TOTALS, null, values);
			return rowTotal;
		}

		return rowTotal;
	}

	public String getLoanTypeName(int idCalc) {
		String calcTypeName = "";
		int idCalcName = -1;

		String[] columnInptDt = { DataBaseSQLHelper.INPUT_DATA_COLUMN_CALCULATION_TYPE };
		String[] selectionArgs = { idCalc + "" };

		Cursor getIdCalcName = myBase.query(DataBaseSQLHelper.TABLE_INPUT_DATA,
				columnInptDt, DataBaseSQLHelper.INPUT_DATA_COLUMN_ID + "=?",
				selectionArgs, null, null, null);

		if (getIdCalcName.getCount() > 0) {
			getIdCalcName.moveToPosition(0);
			idCalcName = getIdCalcName.getInt(0);
		}
		getIdCalcName.close();

		String[] columns = { DataBaseSQLHelper.CALC_TYPE_NAMES_COLUMN_NAME };
		String[] args = { idCalcName + "" };
		Cursor cur = myBase.query(
				DataBaseSQLHelper.TABLE_CALCULATION_TYPE_NAMES, columns,
				DataBaseSQLHelper.CALC_TYPE_NAMES_COLUMN_ID + "=?", args, null,
				null, null);
		if (cur != null) {
			cur.moveToPosition(0);

			calcTypeName = cur.getString(0);
		}
		cur.close();
		return calcTypeName;
	}

	public ArrayList<ItemOfCalc> getListOfCalculations() {

		return null;
	}

	public void close() {
		myBase.close();
	}

	public void deleteSelectedItemsOfList(String[] selectionArgs) {
		myBase.delete(DataBaseSQLHelper.TABLE_INPUT_DATA,
				DataBaseSQLHelper.INPUT_DATA_COLUMN_ID + "=?", selectionArgs);

		myBase.delete(DataBaseSQLHelper.TABLE_PAYMENTS,
				DataBaseSQLHelper.PAYMENTS_COLUMN_ID_CALC + "=?", selectionArgs);

		myBase.delete(DataBaseSQLHelper.TABLE_TOTALS,
				DataBaseSQLHelper.TOTALS_COLUMN_ID_CALC + "=?", selectionArgs);

		myBase.delete(DataBaseSQLHelper.TABLE_LIST_OF_LOAN,
				DataBaseSQLHelper.LIST_COLUMN_ID_CALC + "=?", selectionArgs);
	}

}
