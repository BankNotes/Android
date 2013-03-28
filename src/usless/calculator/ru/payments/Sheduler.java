package usless.calculator.ru.payments;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * @author skobelev
 * 
 */
public class Sheduler {
	private double percent; // % interesr rate
	private double sumCred; // ammount sum
	private int period; // number of payments
	private String beginDate; // date of ammount
	// private Date endDate; //
	private double paymentAnnu; // ammount payment annuitet
	private double comisPayment; // comiss pay
	private int isAnnuitet; // 0 - annuitet, 1 - differential
	private double[][] payments; // array of payments

	private final int PAYMENT = 0;
	private final int PAY_PERCENT = 1;
	private final int PAY_FEE = 2;
	private final int REMAIN = 3;

	public Sheduler(double sumCred, double percent, int period,
			String beginDate, double comisPayment, int isAnnuitet) {
		comisPayment = 0; // is not work
		this.sumCred = sumCred;
		this.percent = percent;
		this.period = period;
		this.beginDate = beginDate;
		this.isAnnuitet = isAnnuitet;
		this.comisPayment = comisPayment;

	}

	// Shedule of pay
	public double[][] getPayments() {
		createShedulePayments(sumCred, period, percent);
		return payments;

	}

	public String[][] getPaymentsS() {
		createShedulePayments(sumCred, period, percent);

		String[][] paymentsS = new String[period][5];

		String[] dd = getDateOfPay(beginDate, period);

		// Dates in 0 column
		for (int i = 0; i < period; i++) {
			paymentsS[i][0] = dd[i];
		}

		// Summs
		for (int i = 0; i < period; i++) {
			for (int j = 1; j < 5; j++) {

				paymentsS[i][j] = payments[i][j - 1] + "";
				paymentsS[i][j].replace("-", "");
			}
		}

		return paymentsS;
	}

	

	public double getMaxSumCredit(double paySum) {
		return paySum
				/ (percent / 1200 * Math.pow((1 + percent / 1200), period))
				/ (Math.pow((1 + percent / 1200), period) - 1);
	}

	

	public double getPayout() {
		double res = 0;
		for (int i = 0; i < period; i++) {
			res += payments[i][0];
		}
		return res;
	}

	public double getPayoutPercent() {
		double res = 0;
		for (int i = 0; i < period; i++) {
			res += payments[i][1];
		}
		return res;
	}

	public double getSumPrincipal() {
		double res = 0;
		for (int i = 0; i < period; i++) {
			res += payments[i][2];
		}
		return res;
	}
	
	private String[] getDateOfPay(String startDate, int numMonths) {
		String[] d = new String[numMonths];
		SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");

		Date dt = null;
		try {
			dt = sdf.parse(startDate);
		} catch (ParseException e) {
			e.printStackTrace();
		}

		Calendar c1 = Calendar.getInstance();

		c1.setTime(dt);

		for (int i = 0; i < period; i++) {
			c1.add(Calendar.MONTH, 1);
			d[i] = sdf.format(c1.getTime());
		}
		return d;
	}
	
	private double getPaymentAnu() {
		if (isAnnuitet==1) {

			double k = (percent / 1200 * Math.pow((1 + percent / 1200), period))
					/ (Math.pow((1 + percent / 1200), period) - 1);
			paymentAnnu = k * sumCred;
		}
		return paymentAnnu;

	}

	private double getIncrementPercent(double creditRemain, double percent) {
		return creditRemain * percent / 1200;
	}

	private void createShedulePayments(double creditSum, int period,
			double percent) {
		if (isAnnuitet==0) 
		{
			double shedulePayment = getPaymentAnu();

			payments = new double[period][];
			for (int i = 0; i < period; i++) {
				payments[i] = new double[4];
			}
			

			// fist pay
			payments[0][PAYMENT] = shedulePayment + comisPayment;
			payments[0][PAY_PERCENT] = sumCred * percent / 1200;
			payments[0][PAY_FEE] = shedulePayment - payments[0][PAY_PERCENT];
			payments[0][REMAIN] = creditSum - payments[0][PAY_FEE];

			for (int i = 1; i < period; i++) {
				payments[i][PAYMENT] = shedulePayment;
				payments[i][PAY_PERCENT] = payments[i - 1][REMAIN] * percent / 1200;
				payments[i][PAY_FEE] = payments[i][PAYMENT] - payments[i][PAY_PERCENT];
				payments[i][REMAIN] = payments[i - 1][REMAIN] - payments[i][PAY_FEE];
			}
			
		} else {
			double firstPay = creditSum / period
					+ getIncrementPercent(creditSum, percent);

			payments = new double[period][];
			for (int i = 0; i < period; i++) {
				payments[i] = new double[4];
			}

			payments[0][PAYMENT] = firstPay;
			payments[0][PAY_PERCENT] = getIncrementPercent(creditSum, percent);
			payments[0][PAY_FEE] = creditSum / period;
			payments[0][REMAIN] = creditSum - payments[0][PAY_FEE];

			// other payments
			for (int i = 1; i < period; i++) {
				payments[i][PAYMENT] = payments[0][PAY_FEE]
						+ getIncrementPercent(payments[i - 1][REMAIN], percent);
				payments[i][PAY_PERCENT] = getIncrementPercent(payments[i - 1][REMAIN],
						percent);
				payments[i][PAY_FEE] = creditSum / period;
				payments[i][REMAIN] = payments[i - 1][REMAIN] - payments[i][PAY_FEE];
			}
		}

	}

	

}
