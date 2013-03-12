package calculator.ru.activity.comput;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import calculator.ru.payments.Payments;

public class LoanComputer extends Activity {

	private static final Uri IDATA_URI = Uri
			.parse("content://calculator.dbase.inputdatacontentprovider/input_data");

	private double inputSum, percent;
	private String beginDate, nameCalculation;
	private int payType, idCalc=0, period;
	private ContentResolver resolver;
	private Context context;

	public LoanComputer(double inputSum, int period, double percent,
			String beginDate, int payType, ContentResolver resolver,
			Context context, String nameCalculation) {
		this.inputSum = inputSum;
		this.period = period;
		this.percent = percent;
		this.beginDate = beginDate;
		this.payType = payType;
		this.resolver = resolver;
		this.context = context;
		this.nameCalculation = nameCalculation;

		ContentValues values = new ContentValues();

		values.put("inputSum", inputSum);
		values.put("percent", percent);
		values.put("period", period);
		values.put("payType", payType);
		values.put("beginDate", beginDate);
		values.put("name", nameCalculation);

		this.resolver.insert(IDATA_URI, values);

		Cursor c = this.resolver.query(IDATA_URI, null, null, null, null);
		if (c != null) {
			c.moveToLast();
			idCalc = c.getInt(0);
		} 
		c.close();
	}

	public void calcBySumOfLoan() {
		Payments p = new Payments(inputSum, percent, period, beginDate,
				payType, idCalc, resolver, this.context);

		p.calculate();
	}

	public void calcByPayment() {
		Payments p = new Payments(inputSum, payType, percent, beginDate,
				period, idCalc, resolver, this.context);
		p.calculate();
	}

	public void calcByProfit() {
		Payments p = new Payments(inputSum, percent, period, beginDate,
				payType, resolver, this.context);
		p.calculate();
	}

}
