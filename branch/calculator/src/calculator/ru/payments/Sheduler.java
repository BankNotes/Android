package calculator.ru.payments;

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
	private int payType; // 0 - annuitet, 1 - differential
	private double[][] payments; // array of payments

	public Sheduler(double sumCred, double percent, int period,
			String beginDate, double comisPayment, int payType) {
		comisPayment = 0; // is not work
		this.sumCred = sumCred;
		this.percent = percent;
		this.period = period;
		this.beginDate = beginDate;
		this.payType = payType;
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

	// Days of pay
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

	public double getMaxSumCredit(double paySum) {
		return paySum
				/ (percent / 1200 * Math.pow((1 + percent / 1200), period))
				/ (Math.pow((1 + percent / 1200), period) - 1);
	}

	// calculate annuitet payment
	private double getPaymentAnu() {
		if (payType == 0) {

			double k = (percent / 1200 * Math.pow((1 + percent / 1200), period))
					/ (Math.pow((1 + percent / 1200), period) - 1);
			paymentAnnu = k * sumCred;
		}
		return paymentAnnu;

	}

	// caluculate percent
	private double getIncrementPercent(double creditRemain, double percent) {
		return creditRemain * percent / 1200;
	}

	/**
	 * @return Returns Sum of all payments
	 */
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

	private void createShedulePayments(double creditSum, int period,
			double percent) {
		if (payType == 0) // для ануитета
		{
			double shedulePayment = getPaymentAnu();

			payments = new double[period][];
			for (int i = 0; i < period; i++) {
				payments[i] = new double[4];
			}
			// in all arrays
			// 0 - payment
			// 1 - payment percent
			// 2 - payment main
			// 3 - remain of overdraft

			// fist pay
			payments[0][0] = shedulePayment + comisPayment;

			payments[0][1] = sumCred * percent / 1200;
			payments[0][2] = shedulePayment - payments[0][1];
			payments[0][3] = creditSum - payments[0][2];

			for (int i = 1; i < period; i++) {
				payments[i][0] = shedulePayment;
				payments[i][1] = payments[i - 1][3] * percent / 1200;
				payments[i][2] = payments[i][0] - payments[i][1];
				payments[i][3] = payments[i - 1][3] - payments[i][2];
			}
		} else {
			double firstPay = creditSum / period
					+ getIncrementPercent(creditSum, percent);

			payments = new double[period][];
			for (int i = 0; i < period; i++) {
				payments[i] = new double[4];
			}

			payments[0][0] = firstPay;
			payments[0][1] = getIncrementPercent(creditSum, percent);
			payments[0][2] = creditSum / period;
			payments[0][3] = creditSum - payments[0][2];

			// other payments
			for (int i = 1; i < period; i++) {
				payments[i][0] = payments[0][2]
						+ getIncrementPercent(payments[i - 1][3], percent);
				payments[i][1] = getIncrementPercent(payments[i - 1][3],
						percent);
				payments[i][2] = creditSum / period;
				payments[i][3] = payments[i - 1][3] - payments[i][2];
			}
		}

	}

}
