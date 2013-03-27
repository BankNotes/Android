package calculator.dbase;

import java.util.ArrayList;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
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
	}

	public void close() {
		myBase.close();
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
				inputData.isAnnuitetPayment());
		cValues.put(DataBaseSQLHelper.INPUT_DATA_COLUMN_COMP_TYPE,
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
				DataBaseSQLHelper.INPUT_DATA_COLUMN_COMP_TYPE,
				DataBaseSQLHelper.INPUT_DATA_COLUMN_CREDIT_SUM,
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
					cur.getString(1), cur.getInt(7), false, cur.getString(2),
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
				DataBaseSQLHelper.PAYMENTS_COLUMN_PAY_FEE,
				DataBaseSQLHelper.PAYMENTS_COLUMN_PAY_PERCENT,
				DataBaseSQLHelper.PAYMENTS_COLUMN_REMAIN };
		String[] args = { getLastIdCalc() + "" };
		Cursor cur = myBase.query(DataBaseSQLHelper.TABLE_PAYMENTS, columns,
				DataBaseSQLHelper.PAYMENTS_COLUMN_ID_CALC + "=?", args, null,
				null, DataBaseSQLHelper.PAYMENTS_COLUMN_PAY_ID);
		ArrayList<RowOfSheduller> rows = new ArrayList<RowOfSheduller>();
		if (cur != null) {
			cur.moveToFirst();
			while (!cur.isLast()) {
				RowOfSheduller row = new RowOfSheduller(cur.getInt(0),
						cur.getString(1), cur.getDouble(2), cur.getDouble(3),
						cur.getDouble(4), cur.getDouble(5));
				rows.add(row);
				cur.moveToNext();
			}
			cur.close();
			return rows;
		}

		return null;
	}

	public RowOfSheduller getTotalsSheduler() {
		String[] columns = { 
				
				DataBaseSQLHelper.PAYMENTS_COLUMN_PAY,
				DataBaseSQLHelper.PAYMENTS_COLUMN_PAY_FEE,
				DataBaseSQLHelper.PAYMENTS_COLUMN_PAY_PERCENT, };
		String[] args = { getLastIdCalc() + "" };
		Cursor cur = myBase.query(DataBaseSQLHelper.TABLE_PAYMENTS, columns,
				DataBaseSQLHelper.PAYMENTS_COLUMN_ID_CALC + "=?", args,
				DataBaseSQLHelper.PAYMENTS_COLUMN_ID_CALC, null, null);
		RowOfSheduller rowTotal = null;
		if (cur != null) {
			cur.moveToLast();
			rowTotal = new RowOfSheduller(0, "", cur.getDouble(0),
					cur.getDouble(2), cur.getDouble(1), 0.0);
			cur.close();
			return rowTotal;
		}

		return rowTotal;
	}
}
