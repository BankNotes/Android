package calculator.dbase;

public class InputData {
	private double sum;
	private double percent;
	private String beginDate;
	private int period;
	private int isAnnuityPayment;
	private int calcType;
	private String name;
	private boolean isYearPeriod;


	public static final int BY_SUM = 0;
	public static final int BY_PAY = 1;
	public static final int BY_PROFIT = 2;

	public InputData(double inputSum, double percent, String beginDate,
			int period, boolean isYearPeriod, int calcType, int payType,
			String name) {
		setSum(inputSum);
		setPercent(percent);
		setBeginDate(beginDate);
		setPaymentType(payType);
		setName(name);
		this.setYearPeriod(isYearPeriod);
		setPeriod(period, isYearPeriod);
		setCalcType(calcType);

	}

	public double getSum() {
		return sum;
	}

	public void setSum(double sum) {
		this.sum = sum;
	}

	public double getPercent() {
		return percent;
	}

	public void setPercent(double percent) {
		this.percent = percent;
	}

	public String getBeginDate() {
		return beginDate;
	}

	public void setBeginDate(String beginDate) {
		this.beginDate = beginDate;
	}

	public int getPeriod() {
		return period;
	}

	public void setPeriod(int period, boolean isYearPeriod) {
		if (isYearPeriod) {
			this.period = 12 * period;
		} else {
			this.period = period;
		}
	}

	public int getPaymentType() {
		return isAnnuityPayment;
	}

	public void setPaymentType(int payType) {
		this.isAnnuityPayment = payType;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getCalcType() {
		return calcType;
	}

	public void setCalcType(int calcType) {
		this.calcType = calcType;
	}

	public boolean isYearPeriod() {
		return isYearPeriod;
	}

	public void setYearPeriod(boolean isYearPeriod) {
		this.isYearPeriod = isYearPeriod;
	}


}
