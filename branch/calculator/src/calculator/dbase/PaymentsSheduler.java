package calculator.dbase;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class PaymentsSheduler {

	DataBaseSQLHelper dHelper;
	private int id = 0;
	private SQLiteDatabase dBase;
	private InputData inputData;
	private double sumOfCredit = 0;

	public PaymentsSheduler(SQLiteDatabase dBase) {
//		dHelper = new DataBaseSQLHelper(context);
		this.dBase=dBase;
//		dBase = dHelper.getWritableDatabase();
		String[] columns = { DataBaseSQLHelper.INPUT_DATA_COLUMN_ID,
				DataBaseSQLHelper.INPUT_DATA_COLUMN_INPUTSUM,
				DataBaseSQLHelper.INPUT_DATA_COLUMN_PERCENT,
				DataBaseSQLHelper.INPUT_DATA_COLUMN_BEGINDATE,
				DataBaseSQLHelper.INPUT_DATA_COLUMN_PERIOD,
				DataBaseSQLHelper.INPUT_DATA_COLUMN_PAYTYPE_IS_ANNUITET,
				DataBaseSQLHelper.INPUT_DATA_COLUMN_CALCULATION_TYPE,
				DataBaseSQLHelper.INPUT_DATA_COLUMN_NAME };

		Cursor cursor = dBase.query(DataBaseSQLHelper.TABLE_INPUT_DATA,
				columns, null, null, null, null, null);
		double inputSum = 0;
		double percent = 0;
		int period = 0;
		String beginDate = "";
		int calcType = 0;
		int isAnnuitet = 0;
		String nameCalc = "";
		boolean isYearPeriod = false;
		if (cursor != null) {
			cursor.moveToLast();

			id = cursor.getInt(0);
			inputSum = cursor.getDouble(1);
			percent = cursor.getDouble(2);
			beginDate = cursor.getString(3);
			period = cursor.getInt(4);
			isAnnuitet = cursor.getInt(5);
			calcType = cursor.getInt(6);
			nameCalc = cursor.getString(7);
		}

		cursor.close();
		inputData = new InputData(inputSum, percent, beginDate, period,
				isYearPeriod, calcType, isAnnuitet, nameCalc);

	}

	public void createSheduler() {

		if (inputData.getPaymentType() == 1) {
			double k = (inputData.getPercent() / 1200 * Math.pow(
					(1 + inputData.getPercent() / 1200), inputData.getPeriod()))
					/ (Math.pow((1 + inputData.getPercent() / 1200),
							inputData.getPeriod()) - 1);

			double payment = 0;

			switch (inputData.getCalcType()) {
			case InputData.BY_PAY:
				sumOfCredit = inputData.getSum()
						/ ((inputData.getPercent() / 1200 * Math.pow(
								(1 + inputData.getPercent() / 1200),
								inputData.getPeriod())) / (Math.pow(
								(1 + inputData.getPercent() / 1200),
								inputData.getPeriod()) - 1));
				payment = inputData.getSum();
				break;

			case InputData.BY_SUM:
				payment = k * inputData.getSum();
				sumOfCredit = inputData.getSum();
				break;

			case InputData.BY_PROFIT:
				payment = inputData.getSum() * 0.4; // profit (zp)
				sumOfCredit = payment
						/ ((inputData.getPercent() / 1200 * Math.pow(
								(1 + inputData.getPercent() / 1200),
								inputData.getPeriod())) / (Math.pow(
								(1 + inputData.getPercent() / 1200),
								inputData.getPeriod()) - 1));
				break;
			}

			ContentValues values = new ContentValues();
			values.put(DataBaseSQLHelper.INPUT_DATA_COLUMN_SUM_OF_LOAN,
					sumOfCredit);
			String[] whereArgs = { id + "" };
			dBase.update(DataBaseSQLHelper.TABLE_INPUT_DATA, values,
					DataBaseSQLHelper.INPUT_DATA_COLUMN_ID + "=?", whereArgs);

			double firstPayPercent = sumOfCredit * inputData.getPercent()
					/ 1200;
			double firstPayFee = payment - firstPayPercent;
			double firstRemain = sumOfCredit - firstPayFee;

			ContentValues paymentsValues = new ContentValues();

			String paymentDate = getNextPayDate(inputData.getBeginDate());

			// first pay
			paymentsValues.put(DataBaseSQLHelper.PAYMENTS_COLUMN_ID_CALC, id);
			paymentsValues.put(DataBaseSQLHelper.PAYMENTS_COLUMN_PAY_ID, 1);
			paymentsValues.put(DataBaseSQLHelper.PAYMENTS_COLUMN_PAY, payment);
			paymentsValues.put(DataBaseSQLHelper.PAYMENTS_COLUMN_PAY_PERCENT,
					firstPayPercent);
			paymentsValues.put(DataBaseSQLHelper.PAYMENTS_COLUMN_PAY_FEE,
					firstPayFee);
			paymentsValues.put(DataBaseSQLHelper.PAYMENTS_COLUMN_PAY_DATE,
					paymentDate);
			paymentsValues.put(DataBaseSQLHelper.PAYMENTS_COLUMN_REMAIN,
					firstRemain);

			dBase.insert(DataBaseSQLHelper.TABLE_PAYMENTS, null, paymentsValues);

			double payPercent = firstRemain * inputData.getPercent() / 1200;
			double payFee = payment - payPercent;
			double remain = firstRemain - payFee;

			paymentDate = getNextPayDate(paymentDate);
			for (int payId = 2; payId <= inputData.getPeriod(); payId++) {

				paymentsValues.put(DataBaseSQLHelper.PAYMENTS_COLUMN_ID_CALC,
						id);
				paymentsValues.put(DataBaseSQLHelper.PAYMENTS_COLUMN_PAY_ID,
						payId);
				paymentsValues.put(DataBaseSQLHelper.PAYMENTS_COLUMN_PAY,
						payment);
				paymentsValues.put(
						DataBaseSQLHelper.PAYMENTS_COLUMN_PAY_PERCENT,
						payPercent);
				paymentsValues.put(DataBaseSQLHelper.PAYMENTS_COLUMN_PAY_FEE,
						payFee);
				paymentsValues.put(DataBaseSQLHelper.PAYMENTS_COLUMN_PAY_DATE,
						paymentDate);
				paymentsValues.put(DataBaseSQLHelper.PAYMENTS_COLUMN_REMAIN,
						remain);

				dBase.insert(DataBaseSQLHelper.TABLE_PAYMENTS, null,
						paymentsValues);

				paymentDate = getNextPayDate(paymentDate);

				payPercent = remain * inputData.getPercent() / 1200;
				payFee = payment - payPercent;
				remain = remain - payFee;

			}

		}

		// dif
		if (inputData.getPaymentType() == 0) {
			double sumOfCredit = 0;
			double payment = 0;
			double payPercent = 0;
			double payFee = 0;

			switch (inputData.getCalcType()) {
			case InputData.BY_SUM:
				sumOfCredit = inputData.getSum();
				break;

			case InputData.BY_PAY:
				sumOfCredit = (1200 * inputData.getPeriod() * inputData
						.getSum())
						/ (inputData.getPercent() * inputData.getPeriod() + 1200);
				break;
			case InputData.BY_PROFIT:
				payment = inputData.getSum() * 0.4;
				sumOfCredit = payment
						/ ((inputData.getPercent() / 1200 * Math.pow(
								(1 + inputData.getPercent() / 1200),
								inputData.getPeriod())) / (Math.pow(
								(1 + inputData.getPercent() / 1200),
								inputData.getPeriod()) - 1));
				break;
			}

			ContentValues values = new ContentValues();
			values.put(DataBaseSQLHelper.INPUT_DATA_COLUMN_SUM_OF_LOAN,
					sumOfCredit);
			String[] whereArgs = { id + "" };
			dBase.update(DataBaseSQLHelper.TABLE_INPUT_DATA, values,
					DataBaseSQLHelper.INPUT_DATA_COLUMN_ID + "=?", whereArgs);

			payFee = sumOfCredit / inputData.getPeriod();
			payPercent = sumOfCredit * inputData.getPercent() / 1200;
			payment = payFee + payPercent;
			String payDate = getNextPayDate(inputData.getBeginDate());
			double remain = sumOfCredit - payFee;

			ContentValues cValues = new ContentValues();
			for (int payId = 1; payId <= inputData.getPeriod(); payId++) {
				cValues.put(DataBaseSQLHelper.PAYMENTS_COLUMN_ID_CALC, id);
				cValues.put(DataBaseSQLHelper.PAYMENTS_COLUMN_PAY_ID, payId);
				cValues.put(DataBaseSQLHelper.PAYMENTS_COLUMN_PAY, payment);
				cValues.put(DataBaseSQLHelper.PAYMENTS_COLUMN_PAY_FEE, payFee);
				cValues.put(DataBaseSQLHelper.PAYMENTS_COLUMN_PAY_PERCENT,
						payPercent);
				cValues.put(DataBaseSQLHelper.PAYMENTS_COLUMN_PAY_DATE, payDate);
				cValues.put(DataBaseSQLHelper.PAYMENTS_COLUMN_REMAIN, remain);

				dBase.insert(DataBaseSQLHelper.TABLE_PAYMENTS, null, cValues);

				payPercent = remain * inputData.getPercent() / 1200;
				payment = payFee + payPercent;
				remain -= payFee;
				payDate = getNextPayDate(payDate);
			}

		}

	}

	private String getNextPayDate(String currentDay) {
		SimpleDateFormat smplDateFrmt = new SimpleDateFormat("dd.MM.yyyy");
		Date begDate = null;
		try {
			begDate = smplDateFrmt.parse(currentDay);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(begDate);

		calendar.add(Calendar.MONTH, 1);
		return smplDateFrmt.format(calendar.getTime());
	}
}
