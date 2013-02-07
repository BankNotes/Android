package calculator.ru.payments;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;

public class Payments extends Activity {

	private String[][] grafik;
	private double maxSumCredit = 0;
	private double maxSumPayment;
	private Sheduler sh;
	private int period;
	private int idCalc;
	private static final Uri IDATA_URI = Uri
			.parse("content://calculator.ru.inputdatacontentprovider/input_data");
	private static final Uri PAYMENT_URI = Uri
			.parse("content://calculator.ru.paymentscontentprovider/payments");
	private static final Uri TOTALS_URI = Uri
			.parse("content://calculator.ru.totaldatacontentprovider/totals");
	ContentResolver cResolver;

	public Payments(double creditSum, double percent, int period,
			String beginDate, int payType, int numCalc, ContentResolver cr) {

		this.sh = new Sheduler(creditSum, percent, period, beginDate, 0,
				payType);
		this.grafik = sh.getPaymentsS();
		this.period = period;
		this.idCalc = numCalc;
		this.cResolver = cr;
	}

	public Payments(double paySum, int payType, double percent,
			String beginDate, int period, int numCalc, ContentResolver cr) {

		this.maxSumCredit = paySum
				/ ((percent / 1200 * Math.pow((1 + percent / 1200), period)) / (Math
						.pow((1 + percent / 1200), period) - 1));
		this.sh = new Sheduler(this.maxSumCredit, percent, period, beginDate,
				0, payType);
		this.period = period;
		this.grafik = sh.getPaymentsS();
		this.cResolver = cr;
		this.idCalc = numCalc;

	}

	public Payments(double ammountIncom, double percent, int period,
			String beginDate, int payType, ContentResolver cr) {
		maxSumPayment = 0.4 * ammountIncom;
		maxSumCredit = maxSumPayment
				/ ((percent / 1200 * Math.pow((1 + percent / 1200), period)) / (Math
						.pow((1 + percent / 1200), period) - 1));
		sh = new Sheduler(maxSumCredit, percent, period, beginDate, 0, payType);
		this.period = period;
		grafik = sh.getPaymentsS();
		cResolver = cr;

		ContentValues values = new ContentValues();

		values.put("inputSum", maxSumCredit);
		values.put("percent", percent);
		values.put("period", period);
		values.put("payType", payType);
		values.put("beginDate", beginDate);

		cr.insert(IDATA_URI, values);

		Cursor c = cr.query(IDATA_URI, null, null, null, null);
		c.moveToLast();
		idCalc = c.getInt(0);
		c.close();

	}

	
	public void calculate() {
		ContentValues values = new ContentValues();
		int payId;
		String payDate;
		String pay;
		String payPercent;
		String payFee;
		String remain;
		for (int i = 0; i < period; i++) {
			payId = i + 1;
			payDate = grafik[i][0];
			pay = grafik[i][1];
			payPercent = grafik[i][2];
			payFee = grafik[i][3];
			remain = grafik[i][4];

			values.put("id_calc", idCalc);
			values.put("pay_id", payId);
			values.put("pay_date", payDate);
			values.put("pay", pay);
			values.put("pay_percent", payPercent);
			values.put("pay_fee", payFee);
			values.put("remain", remain);

			cResolver.insert(PAYMENT_URI, values);

			values.clear();

		}
		setSumOfPayments();
	}

	private void setSumOfPayments() {

		double sumPays = sh.getPayout();
		double sumPercentPays = sh.getPayoutPercent();
		double sumPayFee = sh.getSumPrincipal();

		ContentValues sumValues = new ContentValues();

		sumValues.put("id_calc", idCalc);
		sumValues.put("sumPays", sumPays);
		sumValues.put("sumPercentPays", sumPercentPays);
		sumValues.put("sumPayFee", sumPayFee);

		cResolver.insert(TOTALS_URI, sumValues);

		sumValues.clear();
	}
}