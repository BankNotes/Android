/**
 * 
 */
package calculator.ru.activity.showsheduler;

/**
 * @author skobelev
 * 
 */
public class RowOfSheduller {
	private int payId;
	private String payDate;
	private double payment;
	private double payPercent;
	private double payFee;
	private double remain;

	public RowOfSheduller(int payId, String payDate, double payment,
			double payPercent, double payFee, double remain) {
		this.payId = payId;
		this.payDate= payDate;
		this.payment = payment;
		this.payPercent=payPercent;
		this.payFee=payFee;
		this.remain=remain;
	}

	public int getPayId() {
		return payId;
	}

	public void setPayId(int payId) {
		this.payId = payId;
	}

	public String getPayDate() {
		return payDate;
	}

	public void setPayDate(String payDate) {
		this.payDate = payDate;
	}

	public double getPayment() {
		return payment;
	}

	public void setPayment(double payment) {
		this.payment = payment;
	}

	public double getPayFee() {
		return payFee;
	}

	public void setPayFee(double payFee) {
		this.payFee = payFee;
	}

	public double getPayPercent() {
		return payPercent;
	}

	public void setPayPercent(double payPercent) {
		this.payPercent = payPercent;
	}

	public double getRemain() {
		return remain;
	}

	public void setRemain(double remain) {
		this.remain = remain;
	}
}
