package calculator.ru.payments;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import calculator.dbase.InputDataContentProvider;
import calculator.ru.R;

public class Payments extends Activity {

	private String[][] paymentsTimeTable;
	private double sumOfLoan;
	private double maxSumPayment;
	private Sheduler sh;
	private int period;
	private int idCalc;
	private double percent;
	private String beginDate;
	private String endDate;
	private String paymentType;
	private String calcType;
	private String nameCalc; 
	
	private static final Uri IDATA_URI = Uri
			.parse("content://calculator.dbase.inputdatacontentprovider/input_data");
	private static final Uri PAYMENT_URI = Uri
			.parse("content://calculator.dbase.paymentscontentprovider/payments");
	private static final Uri TOTALS_URI = Uri
			.parse("content://calculator.dbase.totalscontentprovider/totals");
	private static final Uri LIST_URI = Uri
			.parse("content://calculator.dbase.listofloancontentprovider/list_of_loan");
	ContentResolver cResolver;

	private final int PAY_DATE = 0;
	private final int PAYMENT = 1;
	private final int PAY_PERCENT = 2;
	private final int PAY_FEE = 3;
	private final int REMAIN = 4;

	private String[] loanTypes;
	private Context context;
	
	public Payments(double ammountIncom, double percent, int period,
			String beginDate, int payType, ContentResolver cResolver, Context context) {
		this.beginDate = beginDate;
		this.percent=percent;
		this.maxSumPayment = 0.4 * ammountIncom;
		this.loanTypes = context.getResources().getStringArray(R.array.loanType);
		this.sumOfLoan = maxSumPayment
				/ ((percent / 1200 * Math.pow((1 + percent / 1200), period)) / (Math
						.pow((1 + percent / 1200), period) - 1));
		this.sh = new Sheduler(sumOfLoan, percent, period, beginDate, 0, payType);
		this.period = period;
		this.paymentsTimeTable = sh.getPaymentsS();
		this.cResolver = cResolver;
		this.context = context;
		this.paymentType = getPaymentType(payType);
		this.calcType=this.loanTypes[2]; // by profit

		
		Cursor c = cResolver.query(IDATA_URI, null, null, null, null);
		c.moveToLast();
		idCalc = c.getInt(0);
		nameCalc = c.getString(6);
		c.close();
		
		ContentValues inputValues = new ContentValues();


		inputValues.put(InputDataContentProvider.INPUTSUM, sumOfLoan);
		inputValues.put(InputDataContentProvider.PERCENT, percent);
		inputValues.put(InputDataContentProvider.PERIOD, period);
		inputValues.put(InputDataContentProvider.PAYTYPE, payType);
		inputValues.put(InputDataContentProvider.BEGINDATE, beginDate);
		inputValues.put(InputDataContentProvider.NAME, nameCalc);
		
		
		
		String[] whereArgs = {idCalc+""};
		
		cResolver.update(IDATA_URI, inputValues, "id=?", whereArgs);


	}

	public Payments(double creditSum, double percent, int period,
			String beginDate, int payType, int numCalc, ContentResolver cr, Context context) {
		this.sumOfLoan = creditSum;
		this.sh = new Sheduler(creditSum, percent, period, beginDate, 0,
				payType);
		this.paymentsTimeTable = sh.getPaymentsS();
		this.period = period;
		this.idCalc = numCalc;
		this.cResolver = cr;
		this.beginDate = beginDate;
		this.percent=percent;
		this.context = context;
		this.loanTypes = context.getResources().getStringArray(R.array.loanType);
//		this.payType=payType;
		this.paymentType = getPaymentType(payType);
		this.loanTypes = context.getResources().getStringArray(R.array.calcTypes);
		this.calcType= loanTypes[0]; // by sum of loan
		
		Cursor c = cResolver.query(IDATA_URI, null, null, null, null);
		c.moveToLast();
		nameCalc = c.getString(6);
		c.close();
		
	}

	public Payments(double paySum, int payType, double percent,
			String beginDate, int period, int numCalc, ContentResolver cr, Context context) {

		this.sumOfLoan = paySum
				/ ((percent / 1200 * Math.pow((1 + percent / 1200), period)) / (Math
						.pow((1 + percent / 1200), period) - 1));
		this.sh = new Sheduler(this.sumOfLoan, percent, period, beginDate, 0,
				payType);
		this.period = period;
		this.context = context;
		this.loanTypes = context.getResources().getStringArray(R.array.loanType);
		this.paymentsTimeTable = sh.getPaymentsS();
		this.cResolver = cr;
		this.idCalc = numCalc;
		this.beginDate = beginDate;
		this.percent=percent;
//		this.payType=payType;
		this.paymentType = getPaymentType(payType);
		this.calcType=this.loanTypes[1];  // by pay sum
		Cursor c = cResolver.query(IDATA_URI, null, null, null, null);
		c.moveToLast();
		nameCalc = c.getString(6);
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
			payDate = paymentsTimeTable[i][PAY_DATE];
			pay = paymentsTimeTable[i][PAYMENT];
			payPercent = paymentsTimeTable[i][PAY_PERCENT];
			payFee = paymentsTimeTable[i][PAY_FEE];
			remain = paymentsTimeTable[i][REMAIN];

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

		String[] projection = { "id_calc", "pay_id", "pay_date" };
		String where = "id_calc=?";
		String[] whereArgs = { idCalc + "" };
		String sortOrder = "id_calc, pay_id";

		Cursor cur = cResolver.query(PAYMENT_URI, projection, where, whereArgs,
				sortOrder);
		cur.moveToLast();
		this.endDate = cur.getString(2);
		cur.close();

		ContentValues listValues = new ContentValues();
		listValues.put("id_calc", this.idCalc);
		listValues.put("sum_of_loan", this.sumOfLoan);
		listValues.put("percent", this.percent);
		listValues.put("begin_date", this.beginDate);
		listValues.put("end_date", this.endDate);
		listValues.put("qty_payments", this.period);
		listValues.put("credit_type", this.paymentType);
		listValues.put("calc_type", this.calcType);
		listValues.put("name_calc", this.nameCalc);
		
		cResolver.insert(LIST_URI, listValues);
		listValues.clear();

	}

	private String getPaymentType(int payType){
		if(payType==1) {
			return context.getResources().getString(R.string.different);
		}
		return context.getResources().getString(R.string.anuitent);
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