package calculator.ru.activity.item;

public class ItemOfCalc {

	int id, period;
	double sum, percent;
	String beginDate, endDate, creditType, calcType;
	boolean checked;

	public ItemOfCalc(int id, int period, double sum, double percent,
			String beginDate, String endDate, String creditType,
			String calcType, boolean checked) {
		this.id = id;
		this.period = period;
		this.sum = sum;
		this.percent = percent;
		this.beginDate = beginDate;
		this.endDate = endDate;
		this.creditType = creditType;
		this.calcType = calcType;
		this.checked = checked;
	}

	public int getId() {
		return id;
	}

	public String getSum() {
		return this.sum + "";
	}

	public double getDoubleSum() {
		return this.sum;
	}

	public String getPercent() {
		return this.percent + "";
	}

	public String getBeginDate() {

		return this.beginDate;
	}

	public String getEndDate() {
		return this.endDate;
	}

	public String getPeriod() {
		return this.period + "";
	}

	public String getCreditType() {
		// TODO Auto-generated method stub
		return this.creditType;
	}

	public String getCalcType() {
		// TODO Auto-generated method stub
		return this.calcType;
	}

	public boolean isChecked() {
		return checked;
	}

	public void setChecked(boolean check) {
		this.checked = check;
	}
}
