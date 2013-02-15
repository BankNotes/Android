package calculator.ru.activity.comput;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import calculator.ru.payments.Payments;

public class LoanComputer extends Activity {

	private static final Uri IDATA_URI = Uri
			.parse("content://calculator.dbase.inputdatacontentprovider/input_data");
	

	private double inputSum, percent;
	private String beginDate;
	private int payType, idCalc, period;
	private ContentResolver resolver;

	public LoanComputer(double inputSum, int period, double percent,
			String beginDate, int payType, ContentResolver resolver) {
		this.inputSum = inputSum;
		this.period = period;
		this.percent = percent;
		this.beginDate = beginDate;
		this.payType = payType;
		this.resolver = resolver;

		ContentValues values = new ContentValues();

		values.put("inputSum", inputSum);
		values.put("percent", percent);
		values.put("period", period);
		values.put("payType", payType);
		values.put("beginDate", beginDate);

		this.resolver.insert(IDATA_URI, values);

		Cursor c = this.resolver.query(IDATA_URI, null, null, null, null);
		c.moveToLast();
		idCalc = c.getInt(0);
		c.close();
	}

	public void calcBySumOfLoan() {
		Payments p = new Payments(inputSum, percent, period, beginDate,
				payType, idCalc, resolver, this);

		p.calculate();
	}

	public void calcByPayment() {
		Payments p = new Payments(inputSum, payType, percent, beginDate,
				period, idCalc, resolver, this);
		p.calculate();
	}

	public void calcByProfit() {
		Payments p = new Payments(inputSum, percent, period, beginDate,
				payType, resolver, this);
		p.calculate();
	}

}
